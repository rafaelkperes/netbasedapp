#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

void tcp_server(){
    int listenfd = 0, connfd = 0, n = 0;
    struct sockaddr_in serv_addr_tcp;

    char recvBuff[1025];

    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    memset(&serv_addr_tcp, '0', sizeof(serv_addr_tcp));
    memset(recvBuff, '0', sizeof(recvBuff));

    serv_addr_tcp.sin_family = AF_INET;
    serv_addr_tcp.sin_addr.s_addr = htonl(INADDR_ANY); //INADDR_ANY
    serv_addr_tcp.sin_port = htons(2000);

    bind(listenfd, (struct sockaddr*)&serv_addr_tcp, sizeof(serv_addr_tcp));

    listen(listenfd, 10);

	connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);

	read(connfd, recvBuff, sizeof(recvBuff)-1);

	printf("server: %s\n",recvBuff);
	close(connfd);
}


int main(){

  /////////////////////server initialize/////////////////////////
	int listenfd = 0, connfd = 0, n = 0;
	struct sockaddr_in serv_addr_tcp;

	char recvBuff[1025];

	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	memset(&serv_addr_tcp, '0', sizeof(serv_addr_tcp));

	serv_addr_tcp.sin_family = AF_INET;
	serv_addr_tcp.sin_addr.s_addr = htonl(INADDR_ANY);
	serv_addr_tcp.sin_port = htons(2000);

	bind(listenfd, (struct sockaddr*)&serv_addr_tcp, sizeof(serv_addr_tcp));

	if(listen(listenfd, 10)){
	    perror("listen failed");
	    return 1;
	}
  /////////////////////////////////////////////////////////////

  int clientSocket, portNum, nBytes;
  char buffer[1024];
  struct sockaddr_in serverAddr;
  socklen_t addr_size;

  clientSocket = socket(PF_INET, SOCK_DGRAM, 0);

  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(3000);
  serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);

  addr_size = sizeof serverAddr;


	printf("Type the name of the file to be requested (e.g. \"sugar.txt\"):\n");
	fgets(buffer,1024,stdin);
	printf("File requested: \"%s\"",buffer);

	nBytes = strlen(buffer) + 1;

	/*Send message to server*/
	sendto(clientSocket,buffer,nBytes,0,(struct sockaddr *)&serverAddr,addr_size);

	/*Receive message from server*/
				nBytes = recvfrom(clientSocket,buffer,1024,0,NULL, NULL);


    //////////////////////////server listen//////////////////////
	connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);
	read(connfd, recvBuff, sizeof(recvBuff)-1);
	close(connfd);
	/////////////////////////////////////////////////////////////

	////////////read into file
	//printf("Received from server: %s\n",recvBuff);
	FILE *fp;
	fp=fopen("received_file.txt","w");
	fprintf(fp,"%s",recvBuff);
	printf("Received file over TCP, PORT 2000\n");
	printf("File saved as \"received_file.txt\"\n");
	//////////////////////////

  return 0;
}
