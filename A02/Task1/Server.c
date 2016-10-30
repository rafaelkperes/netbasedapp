#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <stdlib.h>

int tcp_client(char buffer[]){
	sleep(1);
    int sockfd = 0, n = 0;
    char sendBuff[1024];

    struct sockaddr_in serv_addr;

    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        printf("\n Error : Could not create socket \n");
        return 1;
    }

    memset(&serv_addr, '0', sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(2000);

    if(inet_pton(AF_INET, "127.0.0.2", &serv_addr.sin_addr)<=0)
    {
        printf("\n inet_pton error occured\n");
        return 1;
    }

    if( connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
    {
       printf("\n Errorrr : Connect Failed \n");
       return 1;
    }

    ///////////file operation
    char filename[strlen(buffer)];
    strcpy(filename, buffer);
    strtok(filename, "\n");
    FILE *f = fopen(filename, "rb");
    fseek(f, 0, SEEK_END);
    long fsize = ftell(f);
    fseek(f, 0, SEEK_SET);

    char *string = malloc(fsize + 1);
    fread(string, fsize, 1, f);
    fclose(f);

    string[fsize] = 0;
    /////////////////////////
    write(sockfd, string, strlen(string));
    printf("File sent.\n");
    close(sockfd);
    return 0;
}

int main(){
  int udpSocket, nBytes;
  int sent_udp;
  char buffer[1024];
  struct sockaddr_in serverAddr_udp, clientAddr_udp;
  struct sockaddr_storage serverStorage;
  socklen_t addr_size, client_addr_size;
  int i;

  udpSocket = socket(PF_INET, SOCK_DGRAM, 0);

  serverAddr_udp.sin_family = AF_INET;
  serverAddr_udp.sin_port = htons(3000);
  serverAddr_udp.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(serverAddr_udp.sin_zero, '\0', sizeof serverAddr_udp.sin_zero);

  bind(udpSocket, (struct sockaddr *) &serverAddr_udp, sizeof(serverAddr_udp));

  addr_size = sizeof serverStorage;

  while(1){
	printf("UDP Server Running on PORT: 3000 ...\n");
	nBytes = recvfrom(udpSocket,buffer,1024,0,(struct sockaddr *)&serverStorage, &addr_size);

	sendto(udpSocket,buffer,nBytes,0,(struct sockaddr *)&serverStorage,addr_size);

	tcp_client(buffer);
  }

  return 0;
}

