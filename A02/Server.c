/************* UDP SERVER CODE *****************/

#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
  char filename[64];
  char host[64];
  int port;
} FileRequest;

int sendFileToClient(char buffer[]);

int sendFileToClient(char buffer[]) {
	printf("server: %s\n",buffer);
	return 1;
}

int tcp_client(FileRequest request) {
  printf("Filename: %s \n", request.filename);
  printf("Host: %s \n", request.host);
  printf("Port: %d \n", request.port);

	sleep(1);
  int sockfd = 0, n = 0;
  char sendBuff[1024];
  struct sockaddr_in serv_addr;

  if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
  {
    printf("\nError: Could not create socket \n");
    return 1;
  }

  memset(&serv_addr, '0', sizeof(serv_addr));

  serv_addr.sin_family = AF_INET;
  serv_addr.sin_port = htons(request.port);

  if (inet_pton(AF_INET, request.host, &serv_addr.sin_addr) <= 0)
  {
    printf("\nError: inet_pton return an error \n");
    return 1;
  }

  if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
  {
    printf("\nError: Connect Failed \n");
    return 1;
  }

  /* file operation */
  FILE *f = fopen(request.filename, "rb");
  fseek(f, 0, SEEK_END);
  long fsize = ftell(f);
  fseek(f, 0, SEEK_SET);  //same as rewind(f);

  char *string = malloc(fsize + 1);
  fread(string, fsize, 1, f);
  fclose(f);

  string[fsize] = 0;
  /* ---- */

  write(sockfd, string, strlen(string));
  close(sockfd);
  return 0;
}

int main() {
  int udpSocket, nBytes;
  int sent_udp;
  char buffer[1024];
  struct sockaddr_in serverAddr_udp, clientAddr_udp;
  struct sockaddr_storage serverStorage;
  socklen_t addr_size, client_addr_size;
  int i;

  /* Create UDP socket */
  udpSocket = socket(PF_INET, SOCK_DGRAM, 0);

  /* Configure settings in address struct */
  serverAddr_udp.sin_family = AF_INET;
  serverAddr_udp.sin_port = htons(3000);
  serverAddr_udp.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(serverAddr_udp.sin_zero, '\0', sizeof serverAddr_udp.sin_zero);

  /* Bind socket with address struct */
  bind(udpSocket, (struct sockaddr *) &serverAddr_udp, sizeof(serverAddr_udp));

  /* Initialize size variable to be used later on */
  addr_size = sizeof serverStorage;

  while(1) {
    /* Receive request from client */
    nBytes = recvfrom(udpSocket, buffer, 1024, 0,
      (struct sockaddr *) &serverStorage, &addr_size);
    FileRequest request;
    memcpy(&request, buffer, sizeof(FileRequest));

    tcp_client(request);
  }

  return 0;
}

