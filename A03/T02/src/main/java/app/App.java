package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String data = "danish speaking from server";
		while (true) {
			try {
				File file = new File("dummy.html");
				long length = file.length();
				byte[] bytes = new byte[16 * 1024];
				InputStream in = new FileInputStream(file);
				ServerSocket srvr = new ServerSocket(1234);
				Socket skt = srvr.accept();
				System.out.print("Server has connected!\n");
				OutputStream out = skt.getOutputStream();
				//PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
				System.out.print("Sending string: '" + data + "'\n");
				//out.print(data);
				int count;
		        while ((count = in.read(bytes)) > 0) {
		            out.write(bytes, 0, count);
		        }
				out.close();
				in.close();
				skt.close();
				srvr.close();
			} catch (Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}
		}
	}
}