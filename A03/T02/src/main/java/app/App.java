package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

	public static void sendHTML(File file, Socket skt) throws IOException{
		String data = "HTTP/1.1 200 OK\nDate: Sat, 28 Nov 2009 04:36:25 GMT";
		PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
		//out.print(data);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		out.println(data);
		out.print("\n");
	    while ((line = br.readLine()) != null) {
	        // process the line.
	    	out.print(line);
	     }
		out.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) {
			try {
				File file = new File("dummy.html");
				long length = file.length();
				ServerSocket srvr = new ServerSocket(1234);
				Socket skt = srvr.accept();
				System.out.print("Server has connected!\n");
				sendHTML(file, skt);
				skt.close();
				srvr.close();
			} catch (Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}
		}
	}
}