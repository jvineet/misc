/* Vineet Joshi (vineetj)
   This file implements a concurrent proxy that uses a cache to store objects
   that it gets from the server and evicts them according to LRU policy. The
   cache is implemented in a separate file cache .c. */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "csapp.h"
#include "cache.h"

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400

/* You won't lose style points for including these long lines in your code */
static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:10.0.3) Gecko/20120305 Firefox/10.0.3\r\n";
static const char *accept_hdr = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
static const char *accept_encoding_hdr = "Accept-Encoding: gzip, deflate\r\n";
static const char *connection_hdr = "Connection: close\r\n";
static const char *proxy_connection_hdr = "Proxy-Connection: close\r\n";

//Global variables
cache* obj_cache;     //pointer to the cache of the current server

// Functions declaration
void* doit(void* connfd);
void create_new_request(rio_t* rio_s, char* new_req, char* host);
void read_forward_response(rio_t* rio_c, int fd, char* tag);
void parse_request(char *uri, char *host, char *version, int* port);
void get_host(char* str, char* host);

//main routine for proxy server
int main(int argc, char **argv)
{
  int listenfd, port, clientlen;
  struct sockaddr_in clientaddr;
  pthread_t tid;

  //Ignore SIGPIPE signal from broken socket
  Signal(SIGPIPE, SIG_IGN);

  /* Check command line args */
  if (argc != 2) {
    fprintf(stderr, "usage: %s <port>\n", argv[0]);
    exit(1);
  }
  port = atoi(argv[1]);

  listenfd = Open_listenfd(port);
  obj_cache = new_cache();     //declare cache for the server

  while (1) {
    clientlen = sizeof(clientaddr);
    int* connfd = malloc(sizeof(int));
    if(connfd == NULL){
      printf("Malloc error\n");
      exit(1);
    }
    //establish connection with client
    *connfd = Accept(listenfd, (SA *)&clientaddr, (socklen_t *)&clientlen);
    Pthread_create(&tid, NULL, doit, connfd);
  }

  free_cache(obj_cache);
  return 0;
}

/* Thread routine for proxy operation */
void* doit(void* connfd)
{
  int fd = *((int*)connfd);
  Pthread_detach(pthread_self());

  //Ignore SIGPIPE signal from broken socket
  Signal(SIGPIPE, SIG_IGN);

  int port;
  char buf[MAXLINE], method[MAXLINE], uri[MAXLINE], version[MAXLINE];
  char host[MAXLINE], new_req[MAXLINE];
  char* tag;
  char* obj;
  rio_t rio_s, rio_c;

  /* Read request line */
  Rio_readinitb(&rio_s, fd);
  Rio_readlineb(&rio_s, buf, MAXLINE);
  sscanf(buf, "%s %s %s", method, uri, version);
  if (strcasecmp(method, "GET")) {
    printf("%s - 501: Proxy does not implement this method\n", method);
    free(connfd);
    Close(fd);
    return NULL;
  }

  // Get the tag from client request (URI) and store it in heap
  // to avoid it from being overwritten
  tag = (char*)malloc(strlen(uri)+1);   //1 addd for null terminated char
  if(tag == NULL){
    printf("Malloc Failed\n");
    free(connfd);
    close(fd);
    exit(1);
  }
  strcpy(tag, uri);

  /* Parse the request line */
  parse_request(uri, host, version, &port);

  //search if the response object has already been saved in the cache
  obj = find(obj_cache, tag);
  if(obj != NULL){
    //object found in cache
    Rio_writen(fd, obj, strlen(obj));
    free(connfd);
    Close(fd);
    return NULL;
  }

  /* Request server from the object if it was not found in cache */
  sprintf(new_req, "%s %s %s\r\n", method, uri, version);

  //create new request and change host if client specifies a dfferent one
  create_new_request(&rio_s, new_req, host);

  //check if we still have a valid host
  if(strlen(host) == 0){
    printf("Client Error: no host specified by the client\n");
    free(connfd);
    Close(fd);
    return NULL;
  }

  //connect to host server
  int clientfd = Open_clientfd_r(host, port);
  if (clientfd < 0){
    printf("Error: Could not establish commection to server\n");
    free(connfd);
    Close(fd);
    return NULL;
  }
  Rio_readinitb(&rio_c, clientfd);

  //pass the new request to the server
  Rio_writen(clientfd, new_req, strlen(new_req));

  //read the response from the server and forward to client
  read_forward_response(&rio_c, fd, tag);

  //close all descriptors before returning from thread
  Close(clientfd);
  free(connfd);
  Close(fd);
  return NULL;
}


//crates a new modified request that proxy sends to server
void create_new_request(rio_t* rio_s, char* new_req, char* host){

  char buf[MAXLINE], key[MAXLINE], value[MAXLINE], hst[MAXLINE];
  int host_flag = 0;

  // process request headers from client
  Rio_readlineb(rio_s, buf, MAXLINE);
  while (strcmp(buf, "\r\n") != 0) {
    sscanf(buf, "%s %s\r\n", key, value);

    //ignore headers that we have to attach from handout specification
    if(strcmp(key, "User-Agent:") != 0 && strcmp(key, "Accept-Encoding:") != 0
       && strcmp(key, "Accept:") != 0 && strcmp(key, "Connection:") != 0
       && strcmp(key, "Proxy-Connection:") != 0) {

      if(strcmp(key, "Host:") == 0){
        strcat(new_req, buf);
        get_host(value,hst);
        strcpy(host,hst);            //use host from client
        host_flag = 1;
      } else
        strcat(new_req, buf);
    }
    //next header
    Rio_readlineb(rio_s, buf, MAXLINE);
  }

  //add the remaining headers we ignored earlier
  strcat(new_req, user_agent_hdr);
  strcat(new_req, accept_hdr);
  strcat(new_req, accept_encoding_hdr);
  strcat(new_req, connection_hdr);
  strcat(new_req, proxy_connection_hdr);

  //check if host was added already, if not add host parsed from uri
  if(host_flag == 0){
    sprintf(buf, "Host: %s\r\n", host);
    strcat(new_req, buf);
  }

  //add empty line indicating end of headers
  strcat(new_req, "\r\n");
 }

//get the host from uri if mentioned
void get_host(char* str, char*host){
  int i = 0;
  while(*str != ':'){
    host[i] = *str;
    str = str + 1;
    i++;
  }
  host[i] = '\0';
}

//reads request from server and forwards it to client
void read_forward_response(rio_t* rio_c, int fd, char* tag){
  char buf[MAXLINE], resp[MAXLINE];
  int n;
  size_t size = 0;//MAX_CACHE_SIZE;  //in case response doesn't have size info
  char obj[MAX_OBJECT_SIZE];
  int max_obj_flag = 0;

  Rio_readlineb(rio_c, buf, MAXLINE);
  strcpy(resp, "");
  strcpy(obj, "");

  //add response line and headers
  while (strcmp(buf, "\r\n") != 0) {
    strcat(resp, buf);
    //next line
    Rio_readlineb(rio_c, buf, MAXLINE);
  }
  strcat(resp, buf);      //indicates end of headers
  Rio_writen(fd, resp, strlen(resp));

  strcat(obj, resp);      //add the headers in the response object

  size = strlen(obj);

  /* add remaining body */
  while((n = Rio_readnb(rio_c, buf, MAXLINE)) != 0){
    Rio_writen(fd, buf, n);
    size = size + n;
    //add to object buffer if size is less than max object size
    if(!max_obj_flag && (size < MAX_OBJECT_SIZE))
      strcat(obj, buf);
    else    //raise flag that obect size exceeded maximum
      max_obj_flag = 1;
  }

  //add to cache if object size less than the maximium object size
  if(!max_obj_flag ){
    //store the object buffer in a heap, so it will ot be overwritten after
    //this function returns
    char* buffer = (char*)malloc(strlen(obj)+1);
    if(buffer == NULL){
      printf("Malloc Failed\n");
      exit(1);
    }
    strcpy(buffer, obj);
    add_to_cache(obj_cache, buffer, tag);
  }
}


/*
 * parse_uri - parse URI into port, host and suffix, version into 1.0
 */
/* $begin parse_uri */
void parse_request(char *uri, char *host, char *version, int* port)
{
  char *ptr;

  //checks if the URI has a host and parses it
  if ((ptr = strstr(uri, "http://")) == NULL) {
    *host = '\0';
  } else {

    ptr = ptr + 7;    //points to the host

    int i = 0;
    while(*ptr != ':' && *ptr != '/'){
      host[i] = *ptr;
      ptr = ptr + 1;
      i++;
    }
    host[i] = '\0';    //null termination
  }

  //extract port
  if(*ptr == ':')
    *port = atoi(ptr + 1);
  else
    *port = 80;

  //extract new uri
  char* tmp = strchr(ptr,'/');      //new starting location for uri
  strcpy(uri, tmp);

  if ((ptr = strstr(version, "HTTP/1.")) == NULL){
    printf("Invalid version in the file");
    exit(1);
  }

  *(ptr+7) = '0';   //changes version to 1.0
}


