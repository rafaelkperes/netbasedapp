package app;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String data = "danish speaking from server";
		while (true) {
			try {
				ServerSocket srvr = new ServerSocket(1234);
				Socket skt = srvr.accept();
				System.out.print("Server has connected!\n");
				PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
				System.out.print("Sending string: '" + data + "'\n");
				out.print(data);
				out.close();
				skt.close();
				srvr.close();
			} catch (Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}
		}
	}
}