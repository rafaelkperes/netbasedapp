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

import org.apache.commons.io.IOUtils;

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
	
	public static void sendJPG(File file, Socket skt) throws IOException{
		String response_header = "HTTP/1.1 200 OK\nLast-Modified: Fri, 10 Feb 2012 14:31:06 GMT\nContent-Type: image/jpeg\nConnection: Keep-Alive";
		byte[] bytes = new byte[16 * 1024];
		InputStream in = new FileInputStream(file);
		OutputStream out = skt.getOutputStream();
		int count;
        response_header += "HTTP/1.1 200 OK\r\n";
        response_header += "Date: " + "\r\n";
        response_header += "Content-Type: image/png\r\n";
        response_header += "Content-Length: " + "\r\n";
        response_header += "Connection: keep-alive\r\n";
        response_header += "\r\n";
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }
		out.close();
		in.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) {
			try {
				File file = new File("dummy.jpg");
				long length = file.length();
				ServerSocket srvr = new ServerSocket(1234);
				Socket skt = srvr.accept();
				System.out.print("Server has connected!\n");
				//sendHTML(file, skt);
				sendJPG(file, skt);
				skt.close();
				srvr.close();
			} catch (Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}
		}
	}
}