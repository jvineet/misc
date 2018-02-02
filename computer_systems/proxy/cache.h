/* The cache is imlemented as a queue of object nodes that hold object buffer
   and tag. The new objects are added to the end of queue and evicted from the
   front of queue. If an object is used from cache, it is removed from
   the current location and added to the end of the cache list in accordance
   with LRU policy */

#include "csapp.h"

//each  node holds an object
typedef struct node {
  char* tag;          /*unique tag for each object, same as request URL*/
  int size;           /*size of object*/
  char* buf;          /*holds contents of the object*/
  struct node* next;  /*points  to the  next node*/
} node;

//defnes a proxy cache
typedef struct {
  node* start;         //points to the start of the cache (node list)
  node* end;           //points to end of cache (node list)
  int cache_size;      //stores the current cache size
  sem_t mutex;         /*controls access to certain sections that are
                         accessed by multiple read threads */
  sem_t w;             /*controls access to the critical sections that
                         access the shared object*/
  int readcnt;         //counts number of readers currently reading together
} cache;

/*creates a new cache*/
cache* new_cache(void);

/*serches for an object in the cache that has some tag 'tag' */
char* find(cache* ch, char* tag);

/* adds an object to the cache, also evicts old objects if required  */
void add_to_cache(cache* ch, char* buf, char* tag);

/* frees the cache and its associated buffers and tags*/
void free_cache(cache* ch);
