#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "cache.h"
#include "csapp.h"

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400


//Helper functions
node*  new_node(char* tag, char* buffer);
void insert_last(cache* ch, node* n);
void evict(cache* ch);

/* Creates a new cache for the server */
cache* new_cache(){
  cache* new = (cache*)malloc(sizeof(cache));
  if (new == NULL){
    printf("Malloc failed\n");
    exit(1);
  }

  //initialise a dummy node
  node* dummy = (node*)malloc(sizeof(node));
  if (dummy == NULL){
    printf("Malloc failed\n");
    exit(1);
  }

  dummy->size = 0;
  dummy->tag = NULL;
  dummy->buf = NULL;
  dummy->next = NULL;

  //assign dummy node to the new empty cache
  new->start = dummy;
  new->end  = dummy;
  new->cache_size = 0;
  new->readcnt = 0;
  Sem_init(&new->mutex, 0, 1);
  Sem_init(&new->w, 0, 1);

  return new;
}

/* Creates a new node where we can store an object */
node* new_node(char* tag, char* buffer){
  node* new = (node*)malloc(sizeof(node));
  if (new == NULL){
    printf("Malloc failed\n");
    exit(1);
  }

  new->tag = tag;
  new->size = strlen(buffer);
  new->buf = buffer;
  new->next = NULL;

  return new;
}

/* Searches for the object in the cache. If an object is found then,
   it removes it from the existing location and places it at the end of the
   cache (LRU) and returns the buffer stored in this object's node. If no
   match found, returns NULL */
char* find(cache* ch, char* tg){
  node* curr = ch->start;
  node* prev = NULL;

  // begin read-write lock if first thread
  //(first readers-writers lock solution)
  P(&ch->mutex);
  ch->readcnt++;
  if(ch->readcnt == 1)
    P(&ch->w);
  V(&ch->mutex);

  while(curr->size != 0){    //check for dummy/last node
    if (strcmp(tg, curr->tag) == 0){
      if(prev == NULL)  //if match found at the start of the list
        ch->start = curr->next;
      else   //if match in the middle of list
        prev->next = curr->next;

      //add object to end of list and end read-write lock if last thread
      P(&ch->mutex);
      //insert this node at the end (only 1 thread manipulates
      //the list at a time)
      insert_last(ch, curr);
      ch->readcnt--;
      if(ch->readcnt == 0)
        V(&ch->w);
      V(&ch->mutex);

      return curr->buf;
    }
    prev = curr;
    curr = curr->next;
  }

  //no match found, end read-write lock if last thread
  P(&ch->mutex);
  ch->readcnt--;
  if(ch->readcnt == 0)
    V(&ch->w);
  V(&ch->mutex);

  return NULL;
}

void insert_last(cache* ch, node* n){
  node* curr = ch->end;

  node* dummy = (node*)malloc(sizeof(node));   //new dummy node
  dummy->size = 0;
  dummy->tag = NULL;
  dummy->buf = NULL;
  dummy->next = NULL;

  //old dummy node copies all contents of the node n
  curr->size = n->size;
  curr->tag = n->tag;
  curr->buf = n->buf;
  curr->next = dummy;

  ch->end = dummy;
  //free node n (no longer needed)
  free(n);
}

/* Removes a node from the front of the cache */
void evict(cache* ch){
  node* curr = ch->start;
  ch->start = curr->next;
  ch->cache_size = ch->cache_size - curr->size;

  //free the evicted object and its contents
  free(curr->tag);
  free(curr->buf);
  free(curr);
}

/* Adds a new object to the end of cache, by either direct adding (if
   enough free space) or adding after eviction (if not enough free space)*/
void add_to_cache(cache* ch, char* buf, char* tag){
  //create node
  node* n = new_node(tag, buf);

  //check if cache has sufficient free space to add the object without
  //eviction

  //begin read-write lock
  P(&ch->w);
  if(ch->cache_size + n->size <= MAX_CACHE_SIZE){
    insert_last(ch, n);
    ch->cache_size = ch->cache_size + n->size;
    //end read-write lock
    V(&ch->w);
    return;
  }

  //if not then evict objects from the front (LRU) until sufficient
  //space is made
  while(ch->cache_size + n->size > MAX_CACHE_SIZE)
    evict(ch);
  insert_last(ch, n);
  ch->cache_size = ch->cache_size + n->size;
  //end read-write lock
  V(&ch->w);
  return;
}


/* Frees the current cache and all its associated nodes and buffers */
void free_cache(cache* ch){
  node* tmp;
  node* curr = ch->start;

  //free all nodes
  while(curr != NULL){
    tmp = curr;
    curr = curr->next;
    //free the current node
    free(tmp->tag);
    free(tmp->buf);
    free(tmp);
  }

  //free the struct cache
  free(ch);
}





