/*
 * mm.c
 *
 * Vineet Joshi (vineetj)
 *
 * I implemented a segregated list with 40 buckets to store the free blocks.
 * The buckets are indexed based on the increasing size of the block. Each
 * stores a range of 1000 sizes. Sizes beyond 40000 are all stored in the final
 * bucket as such large allocations will be very rare. All the free blocks are
 * singly linked to one another. The segregation list is itself stored at the
 * front of the heap after the padding. All the chains in the seg list
 * terminate to the address stored at heap_list(second word in the prologue) so
 * that the entire list remains within the heap. The blocks are coalesed after
 * 'free operation' and 'extend heap' operation if they have adjacent free
 * blocks and then added to the seg list. The new blocks that are merged are
 * removed from the seg list prior to merge. The insertion of blocks to the
 * segregation list follows the LIFO policy. The blocks are also split if the
 * block size is larger than the required alocation size. Since the blocks are
 * singly linked we only need to store one pointer within the free block. So
 * the minimum block size is 16 bytes. The block pointer always points to the
 * header of the block. All pointer arithmetics are done w.r.t the header.
 * However, malloc, reacclo and calloc, still return void pointers to the first
 * free byte tn the block. All the sizes are referred to in word size for the
 * blocks and most pointer arithmetic is done for uint32_t*
 *
 */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include "contracts.h"

#include "mm.h"
#include "memlib.h"


// Create aliases for driver tests
// DO NOT CHANGE THE FOLLOWING!
#ifdef DRIVER
#define malloc mm_malloc
#define free mm_free
#define realloc mm_realloc
#define calloc mm_calloc
#endif

/*
 *  Logging Functions
 *  -----------------
 *  - dbg_printf acts like printf, but will not be run in a release build.
 *  - checkheap acts like mm_checkheap, but prints the line it failed on and
 *    exits if it fails.
 */

#ifndef NDEBUG
#define dbg_printf(...) printf(__VA_ARGS__)
#define checkheap(verbose) do {if (mm_checkheap(verbose)) {  \
                             printf("Checkheap failed on line %d\n", __LINE__);\
                             exit(-1);  \
                        }}while(0)
#else
#define dbg_printf(...)
#define checkheap(...)
#endif

/*Basic Constants*/
#define WSIZE 4 /* Word and header/footer size (bytes) */
#define DSIZE 8
#define CHUNKSIZE (1<<9) /* Extend heap by this amount (bytes) */
#define LISTSIZE 40      /* Size of segregated lists */
#define DIV 40000      /*any block size greater than this goes to last bucket*/


// Global variables
uint32_t* heap_listp; /*points to the second word in the prologue, also
                        terminal blocks in the seg list point to its
                        destination address*/
uint32_t** seg_list; /*Pointer to Segregated List for storing free blocks
                       that is stored in the beginning of the heap*/


/*
 *  Helper functions
 *  ----------------
 */

static uint32_t* extend_heap(size_t words);
static uint32_t* coalesce(uint32_t* bp);
static void place(uint32_t* bp, size_t asize);
static void split_block(uint32_t* bp, size_t words);
static uint32_t* find_fit(size_t words);
static int get_ind(size_t size);
static void add_to_list(uint32_t* bp);
static void delete_from_list(uint32_t* bp);
static uint32_t* extend_right(uint32_t* bp);

// Align p to a multiple of w bytes
static inline void* align(const void const* p, unsigned char w) {
    return (void*)(((uintptr_t)(p) + (w-1)) & ~(w-1));
}

// Check if the given pointer is 8-byte aligned
static inline int aligned(const void const* p) {
    return align(p, 8) == p;
}

// Returns whether the pointer is in the heap.
static int in_heap(const void* p) {
  return p <= mem_heap_hi() && p >= mem_heap_lo();
}

// Returns the maximum of two integers
static inline uint32_t max(uint32_t a, uint32_t b){
  if (a>b)
    return a;
  return b;
}

/*
 *  Block Functions
 *  ---------------
 *  TODO: Add your comment describing block functions here.
 *  The functions below act similar to the macros in the book, but calculate
 *  size in multiples of 4 bytes.
 */

// Returns the pointer to the next free node in seg list
static uint32_t* next_free_block(uint32_t* bp){
  REQUIRES(bp != NULL);
  REQUIRES(in_heap(bp));

  uint32_t* ref = bp+1;
  uint32_t* ret = *(uint32_t**)(ref);

  ASSERT(in_heap(ret));

  return ret;
}

// Sets pointer to the next free node in seg list
static void set_next_free(uint32_t* bp, uint32_t* set){
  REQUIRES(bp != NULL);
  REQUIRES(in_heap(bp));
  REQUIRES(in_heap(set));
  REQUIRES(set != NULL);

  uint32_t* ref = bp+1;
  *(uint32_t**)(ref) = set;
}

// Return the size of the given block in multiples of the word size
static inline unsigned int block_size(const uint32_t* block) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));

    return (block[0] & 0x3FFFFFFF);
}

// Return true if the block is free, false otherwise
static inline int block_free(const uint32_t* block) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));

    return !(block[0] & 0x40000000);
}

// Mark the given block as free(0)/alloced(1) by marking the header and footer.
static inline void block_mark(uint32_t* block, int asg) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));

    unsigned int foot = block_size(block) - 1;
    block[0] = !asg ? block[0] & (int) 0xBFFFFFFF : block[0] | 0x40000000;
    block[foot] = block[0];
}

// Return a pointer to the memory malloc should return
static inline uint32_t* block_mem(uint32_t* const block) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));
    REQUIRES(aligned(block + 1));

    if(in_heap(block))
      return block + 1;
    else
      return NULL;
}

// Return the header to the previous block
static inline uint32_t* block_prev(uint32_t* const block) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));

    return block - block_size(block - 1);
}

// Return the header to the next block
static inline uint32_t* block_next(uint32_t* const block) {
    REQUIRES(block != NULL);
    REQUIRES(in_heap(block));

    return block + block_size(block);
}

// Write a word at address p
static inline void put(uint32_t* const p, uint32_t val){
  REQUIRES(p != NULL);
  REQUIRES(in_heap(p));

  *p = val;
}

// Read a word at address p
static inline uint32_t get(uint32_t* const p){
  REQUIRES(p != NULL);
  REQUIRES(in_heap(p));

  return *p;
}

//Pack a size and allocated bit into a word (size in multiples of word)
static inline uint32_t pack(uint32_t size, uint32_t alloc){
  REQUIRES(alloc == 1 || alloc == 0);

  return size | (alloc << 30);
}

// Returns a pointer to the footer of a block
static inline uint32_t* footer(uint32_t* const block){
  REQUIRES(block != NULL);
  REQUIRES(in_heap(block));

  return block + block_size(block) - 1;
}

/*
 *  Malloc Implementation
 *  ---------------------
 *  The following functions deal with the user-facing malloc implementation.
 */

/*
 * Initialize: return -1 on error, 0 on success.
 */
int mm_init(void) {
  /* Create the initial empty heap */
  uint32_t* bp;

  //the seglist is stored in the beginning of the heap so
  //we allocate the array of list pointers in the beginning of the
  //heap after padding
  if ((long)(heap_listp = mem_sbrk((2*LISTSIZE+4)*WSIZE)) == -1)
    return -1;

  ASSERT(aligned(heap_listp));
  put(heap_listp, 0); /* Alignment padding */
  put(heap_listp + 2*LISTSIZE+1, pack(DSIZE/WSIZE, 1)); /* Prologue header */
  put(heap_listp + 2*LISTSIZE+2, pack(DSIZE/WSIZE, 1)); /* Prologue footer */
  put(heap_listp + 2*LISTSIZE+3, pack(0, 1)); /* Epilogue header */

  seg_list  = (uint32_t**)(heap_listp + 1); /* Points seg list to its start
                                             location in the heap */
  heap_listp += 2*LISTSIZE+2; //set heap_listp to 2nd word of prologue
  ASSERT(aligned(heap_listp));

  for(int i=0; i<LISTSIZE; i++) /* Initializing the segregated list */
    seg_list[i] = heap_listp;  //determines the end of the block list

  //Extend the empty heap with a free block of CHUNKSIZE bytes */
  if ((bp = extend_heap(CHUNKSIZE/WSIZE)) == NULL)
    return -1;

  ASSERT(block_free(bp));
  checkheap(1);
  return 0;
}

/* Extends the heap upwards by either the request size or the default size,
   whichever greater, and adds the new free block to the seg list */
static uint32_t* extend_heap(size_t words)
{
  checkheap(1);

  dbg_printf("%d",100);
  char *bp;
  size_t byte_size;

  // Old epilogue will be new header
  /* Allocate an even number of words to maintain alignment */
  byte_size = (words % 2) ? (words+1) * WSIZE : words * WSIZE;
  if ((long)(bp = mem_sbrk(byte_size)) == -1)
    return NULL;

  uint32_t* bp2 = (uint32_t*)(bp);
  uint32_t* hdr = bp2-1;

  ASSERT(in_heap(hdr));
  /* Initialize free block header/footer and the epilogue header */
  // The old epilogue footer becomes the new  block header
  put(hdr, pack(byte_size/WSIZE, 0));         /* Free block header */
  put(footer(hdr), pack(byte_size/WSIZE, 0)); /* Free block footer */
  put(footer(hdr)+1, pack(0, 1));             /* New epilogue header */

  /* Coalesce if the previous block was free */
  ASSERT(block_free(hdr));
  hdr = coalesce(hdr);
  add_to_list(hdr);

  checkheap(1);
  return hdr;
}

/* Merges the current free blocks to its left and right neighbor blocks in the
   heap if either of them is free and returns a pointer to this larger sized
   merged bock*/
static uint32_t* coalesce(uint32_t *bp)
{
  ASSERT(in_heap(bp));
  ASSERT(in_heap(block_next(bp)));
  ASSERT(in_heap(block_prev(bp)));
  ASSERT(block_free(bp));
  checkheap(1);

  size_t prev_alloc = !block_free(block_prev(bp));
  size_t next_alloc = !block_free(block_next(bp));
  size_t size = block_size(bp);

  if (prev_alloc && next_alloc) { // Case 1 no neighbor free
    return bp;
  }

  else if (prev_alloc && !next_alloc) { // Case 2 successor free
    delete_from_list(block_next(bp));

    size += block_size(block_next(bp));
    put(bp, pack(size, 0));
    put(footer(bp), pack(size,0));
  }

  else if (!prev_alloc && next_alloc) { // Case 3 predecessor free
    delete_from_list(block_prev(bp));

    size += block_size(block_prev(bp));
    bp = block_prev(bp);
    put(bp, pack(size, 0));
    put(footer(bp), pack(size, 0));
  }

  else { // Case 4  both predecessor and successor free
    delete_from_list(block_prev(bp));
    delete_from_list(block_next(bp));

    size += block_size(block_prev(bp)) + block_size(block_next(bp));
    bp = block_prev(bp);
    put(bp, pack(size, 0));
    put(footer(bp), pack(size, 0));
  }

  ASSERT(in_heap(bp));
  checkheap(1);
  return bp;
}


/*
 * malloc
 */
void* malloc (size_t size) {
  checkheap(1);  // Let's make sure the heap is ok!

  size_t asize; /* Adjusted block size */
  size_t extendsize; /* Amount to extend heap if no fit */
  uint32_t* bp;

  /* Ignore spurious requests */
  if (size == 0)
    return NULL;

  /* Adjust block size to include overhead and alignment reqs. */
  if (size <= DSIZE)
    asize = 2*DSIZE;
  else
    asize = DSIZE * ((size + (DSIZE) + (DSIZE-1)) / DSIZE);

  size_t words = asize/WSIZE;

  /* Search the heap for a fit and returns a block after removing it from
   the seg list, if no such block exists then returns NULL */
  if ((bp = find_fit(words)) != NULL) {
    place(bp, words);
    return (void*)(block_mem(bp));
  }

  /* No fit found. Get more memory and place the block */
  extendsize = max(asize,CHUNKSIZE);
  if ((bp = extend_heap(extendsize/WSIZE)) == NULL)
    return NULL;
  delete_from_list(bp);      //removed from seg list to be allocated
  place(bp, words);

  checkheap(1);    //check heap again after malloc
  return (void*)(block_mem(bp));
}

// Allocates the free block to the user and marks it as allocated
static void place(uint32_t* bp, size_t words){
  REQUIRES(block_free(bp));
  checkheap(1);

  if(block_size(bp) > (words+5))
    split_block(bp, words); /*splits the block and adds the new free list
                              node to the free list*/
  block_mark(bp, 1);
  checkheap(1);
}

// Splits the free block into two if it has a larger size than required and
// stores the unused free block int othe heap
static void split_block(uint32_t* bp, size_t words){
  REQUIRES(block_free(bp));
  checkheap(1);

  size_t size = block_size(bp) - words;
  //splits and marks the free blocks that will be allocated
  put(bp, pack(words, 0));
  put(footer(bp), pack(words,0));
  //marks the unused free block
  put(block_next(bp), pack(size, 0));
  put(footer(block_next(bp)), pack(size,0));

  ASSERT(block_free(bp));
  add_to_list(block_next(bp));
  checkheap(1);
}

//finds a required free block for allocation and deletes it from Seglist
static uint32_t* find_fit(size_t words){
  checkheap(1);

  uint32_t* list;
  uint32_t* prev;

  // Finds the list of appropriate free block based on size in bytes
  // and delets it from the seg list
  int ind = get_ind(WSIZE*words);
  ASSERT(ind < LISTSIZE);
  //first searches in the right size bucket and then searches on progressively
  //sized buckets until match is found
  for(int i=ind; i<LISTSIZE; i++){
    list = seg_list[i];
    prev = NULL;
    while(list != heap_listp){  //check if we reach end of seg list
      ASSERT(in_heap(list));
      if(block_size(list) >= words && block_free(list)){  //checks for match
        //removes the block if match is found
        if(prev != NULL)
          set_next_free(prev, next_free_block(list));
        else
          seg_list[i] = next_free_block(list);
        //returns this block pointer
        ASSERT(block_free(list));

        checkheap(1);
        return list;
      }
      prev = list;
      list = next_free_block(list);
    }
  }
  checkheap(1);
  return NULL;
}


//deletes a blook from the seg list
static void delete_from_list(uint32_t* bp){
  REQUIRES(block_free(bp));
  checkheap(1);

  uint32_t* list;
  uint32_t* prev;

  int ind = get_ind(WSIZE*block_size(bp));
  list = seg_list[ind];
  prev = NULL;
  while(list != heap_listp){
    ASSERT(in_heap(list));
    if(list == bp){                  //checks for match
      //removes the block if match is found
      if(prev != NULL)
        set_next_free(prev, next_free_block(list));
      else
        seg_list[ind] = next_free_block(list);

      checkheap(1);
      return;
    }
    prev = list;
    list = next_free_block(list);
  }
  checkheap(1);
}


/* Returns the index where a free block should be stored in segregated list
based on its size */
static int get_ind(size_t size){
  int ref = size*LISTSIZE/DIV;

  if(ref < LISTSIZE-1)
    return ref;
  else
    return  LISTSIZE-1;
}

// Adds anew free block to the bucket in seg list based on size in block
static void add_to_list(uint32_t* bp){
  REQUIRES(block_free(bp));
  checkheap(1);

  size_t size = block_size(bp);
  int ind = get_ind(WSIZE*size);
  // Add according to LIFO
  set_next_free(bp, seg_list[ind]);
  seg_list[ind] = bp;
  REQUIRES(in_heap(bp));
  checkheap(1);
}

/*
 * free
 */
void free (void *ptr) {
  checkheap(1);

  if(ptr == NULL)
    return;

  uint32_t* bp = (uint32_t*)ptr - 1;
  ASSERT(!block_free(bp));   //checks if block has been initialized

  // mark block as free
  block_mark(bp, 0);

  ASSERT(block_free(bp));
  //coalesce and add to seglist
  bp = coalesce(bp);
  add_to_list(bp);

  checkheap(1);
}


/*
 * realloc - you may want to look at mm-naive.c
 */
void* realloc(void *oldptr, size_t bytesize) {
  checkheap(1);

  size_t oldsize;
  size_t newsize;
  size_t asize;

  /* If bytesize == 0 then this is just free, and we return NULL. */
  if(bytesize == 0){
    free(oldptr);
    return NULL;
  }

  /* If oldptr is NULL, then this is just malloc. */
  if(oldptr == NULL)
    return malloc(bytesize);

  uint32_t* old = (uint32_t*)oldptr - 1;
  ASSERT(!block_free(old));

  //extend byte sizet to a multiple of DOUBLESIZE
  if (bytesize <= DSIZE)
    asize = 2*DSIZE;
  else
    asize = DSIZE * ((bytesize + (DSIZE) + (DSIZE-1)) / DSIZE);

  newsize = asize/WSIZE;
  oldsize = block_size(old);

  //If the new size s less than or equal to old size mark the block
  //as free and treat it as the new block so it could split if new size
  //is too small. This way it will retain old contents
  if(newsize <= oldsize){
    block_mark(old, 0);
    place(old, newsize);
    return oldptr;
  }

  //If the adjacent block is free and the sum of thea and the previous block
  //is large enough to meet requirements then we justextend the old block to
  //right. It retains old contents and list search is not required
  uint32_t* next = block_next(old);
  if(block_free(next) && newsize <= oldsize+block_size(next)){
    block_mark(old, 0);
    old = extend_right(old);
    place(old, newsize);
    return oldptr;
  }

  // We need to look for other places to store the new size
  void* newptr = malloc(bytesize);

  /* If realloc() fails the original block is left untouched  */
  if(!newptr) {
    return NULL;
  }

  /* Copy the old data. */
  ASSERT(WSIZE*oldsize < asize);
  memcpy(newptr, oldptr, WSIZE*oldsize);

  free(oldptr);    //free the old block

  checkheap(1);
  return newptr;
}

//extends a block to the right if the successor is free
static uint32_t* extend_right(uint32_t* bp){
  REQUIRES(in_heap(bp));

  delete_from_list(block_next(bp));
  size_t size = block_size(bp);

  size += block_size(block_next(bp));
  put(bp, pack(size, 0));
  put(footer(bp), pack(size,0));

  ASSERT(in_heap(bp));
  return bp;
}


/*
 * calloc - you may want to look at mm-naive.c
 */
void* calloc (size_t nmemb, size_t size) {
  checkheap(1);

  size_t bytesize = nmemb * size;
  //allocate space in the heap
  void *newptr = malloc(bytesize);

  if(!newptr) {
    return NULL;
  }
  //initialize the bytes in the returned block to 0
  memset(newptr, 0, bytesize);

  checkheap(1);
  return newptr;
}

// Returns 0 if no errors were found, otherwise returns the error
int mm_checkheap(int verbose) {
  verbose = verbose;
  uint32_t* heap_front = heap_listp+1;
  uint32_t* heap_end = (uint32_t*)((char*)mem_heap_hi()-3);

  uint32_t* pt = heap_front;

  //check if each block is well defined
  while(pt != heap_end){
    if(*pt != *footer(pt)){
      dbg_printf("Error: header and footer are different");
      return 1;
    }

    if((pt + block_size(pt)-1) != footer(pt)){
      dbg_printf("Error: block size different than stored in header");
      return 2;
    }

    if(block_free(pt) && (block_free(block_prev(pt))
                          || block_free(block_next(pt)))){
      dbg_printf("Error: blocks not coalesed");
      return 3;
    }
    pt = block_next(pt);
  }

  uint32_t* list;
  //check seg list to see if all the blocks in it are empty
  for(int i=0; i<LISTSIZE; i++){
    list = seg_list[i];
    while(list != heap_listp){  //check for end of list
      if(!block_free(list)){
        dbg_printf("Error: blocks not free in Seg List");
        return 4;
      }

      if(!in_heap(list)){
        dbg_printf("Error: block out of heap");
        return 5;
      }
    }
  }
  //no errors were found
  return 0;
}