/************* UDP CLIENT CODE ****************/

#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

typedef struct {
  char filename[64];
  char host[64];
  int port;
} FileRequest;

int main() {
  /* Server Initialize */
	int listenfd = 0, connfd = 0, n = 0;
	struct sockaddr_in serv_addr_tcp;

	char recvBuff[1025];
	time_t ticks;

	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	memset(&serv_addr_tcp, '0', sizeof(serv_addr_tcp));

	serv_addr_tcp.sin_family = AF_INET;
  serv_addr_tcp.sin_port = htons(2000);
	serv_addr_tcp.sin_addr.s_addr = htonl(INADDR_ANY);	

	bind(listenfd, (struct sockaddr*) &serv_addr_tcp, sizeof(serv_addr_tcp));

	if (listen(listenfd, 10)) {
    perror("listen failed");
    return 1;
	}
  /* ---- */

  /* Create UDP socket */
  int clientSocket, portNum, nBytes;
  struct sockaddr_in serverAddr;
  socklen_t addr_size;
  clientSocket = socket(PF_INET, SOCK_DGRAM, 0);

  /* Destination address for UDP (server) */
  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(3000);
  serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);

  /* Initialize size variable to be used later on */
  addr_size = sizeof serverAddr;
  
  /* Fill request parameters */
  FileRequest request;
  strcpy(request.host, "127.0.0.2");
  request.port = 2000;

  /* User input filename */
  printf("Filename to read from server:\n");
  fgets(request.filename, 64, stdin);
  printf("You typed: %s", request.filename);

  /* Send UDP request to server */
  char buffer[sizeof(FileRequest)];
  memcpy(buffer, &request, sizeof(FileRequest));
  sendto(clientSocket, buffer, sizeof(FileRequest),
    0, (struct sockaddr *) &serverAddr, addr_size);

  /* Wait for server connection */
	connfd = accept(listenfd, (struct sockaddr*) NULL, NULL);
  /* Read file from server */
	read(connfd, recvBuff, sizeof(recvBuff)-1);
	printf("server: %s\n",recvBuff);
	close(connfd);
  /* ---- */

	/* Read into file */
	FILE *fp;
	fp = fopen("received_file.txt","w");
	fprintf(fp, "%s", recvBuff);
	/* ---- */

  return 0;
}
