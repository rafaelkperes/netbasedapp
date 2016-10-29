/************* UDP CLIENT CODE *******************/

#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

void tcp_server(){
    int listenfd = 0, connfd = 0, n = 0;
    struct sockaddr_in serv_addr_tcp;

    char recvBuff[1025];
    time_t ticks;

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
	time_t ticks;

	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	memset(&serv_addr_tcp, '0', sizeof(serv_addr_tcp));
	//memset(recvBuff, '0', sizeof(recvBuff));

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

  /*Create UDP socket*/
  clientSocket = socket(PF_INET, SOCK_DGRAM, 0);

  /*Configure settings in address struct*/
  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(3000);
  serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);

  /*Initialize size variable to be used later on*/
  addr_size = sizeof serverAddr;


    printf("Type a sentence to send to server:\n");
    fgets(buffer,1024,stdin);
    printf("You typed: %s",buffer);

    nBytes = strlen(buffer) + 1;

    /*Send message to server*/
    sendto(clientSocket,buffer,nBytes,0,(struct sockaddr *)&serverAddr,addr_size);

    /*Receive message from server*/
                nBytes = recvfrom(clientSocket,buffer,1024,0,NULL, NULL);

    printf("Received from server: %s\n",buffer);



    //////////////////////////server listen//////////////////////
	connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);
	read(connfd, recvBuff, sizeof(recvBuff)-1);
	printf("server: %s\n",recvBuff);
	close(connfd);
	/////////////////////////////////////////////////////////////

	////////////read into file
	FILE *fp;
	fp=fopen("received_file.txt","w");
	fprintf(fp,"%s",recvBuff);
	//////////////////////////

  return 0;
}
