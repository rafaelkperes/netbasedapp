package app;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 * HTTP/1.1
 *
 */
public class App {

	private static final int DEFAULT_PORT = 80;
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void processFile(String file) throws IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	System.out.println(line);
		    }
		}
	}
	
	static void sendGET(String host, String path, Socket socket) throws IOException {
		PrintWriter request = null;
		String filename = "";
		try {
			request = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.print("GET " + path + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Connection: close\r\n\r\n");
		request.flush();

		String c_type;
		try {
			c_type = path.split("\\.")[1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			c_type = "html";
		}

		int count;
		byte[] buffer = new byte[2048];
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		if (c_type.equals("jpg")) {
			//////////////// image file receive
			filename = "RESULTS\\image.jpg";
			OutputStream dos = new FileOutputStream(filename);
		    while ((count = in.read(buffer)) != -1)
		    {
		      dos.write(buffer, 0, count);
		      dos.flush();
		    }		    
			dos.close();
			System.out.println("received jpg file is saved as image.jpg in the root");		
		} 
		///////////////////////////////html file receive
		else if (c_type.endsWith("html")) {
			//////////////// html file read
			filename = "RESULTS\\html.html";
			OutputStream dos = new FileOutputStream(filename);
		    while ((count = in.read(buffer)) != -1)
		    {
		      dos.write(buffer, 0, count);
		      dos.flush();
		    }
			dos.close();
			System.out.println("received html file is saved as html.html in the root");
		}
		/////////////////////////////////////
		///////////////////////////////text file receive
		else if (c_type.endsWith("txt")) {
			//////////////// html file read
			filename = "RESULTS\\text.html";
			OutputStream dos = new FileOutputStream(filename);
		    while ((count = in.read(buffer)) != -1)
		    {
		      dos.write(buffer, 0, count);
		      dos.flush();
		    }
		    dos.close();
			System.out.println("received text file is saved as text.txt in the root");
		}
		/////////////////////////////////////
		in.close();
		System.out.println("Received what we wanted");
		processFile(filename);
	}

	static Socket connect(String host, String path, int port) throws IOException {
		LOGGER.log(Level.FINE, "Connecting to: {0}:{1}", new Object[] { host, port });
		Socket clientSocket = new Socket(host, port);
		sendGET(host, path, clientSocket);
		return clientSocket;
	}

	static void start(String[] args) throws Exception {
		LOGGER.log(Level.FINER, "Start called with args: {0}", args);

		if (args.length < 1) {
			throw new Exception("I need an URL to GET.");
		} else {
			URL url = new URL(args[0]);
			LOGGER.log(Level.FINER, "Parsed HOST:", url.getHost());
			LOGGER.log(Level.FINER, "Parsed PORT:", url.getPort());

			/* if URL does not contain PORT, set default value */
			int port = url.getPort();
			if (port == -1) {
				port = DEFAULT_PORT;
			}

			Socket sock = connect(url.getHost(), url.getPath(), port);
		}
	}

	public static void main(String[] args) throws Exception {
		Handler fh = new FileHandler("./all.log");
		LOGGER.addHandler(fh);
		LOGGER.setLevel(Level.FINEST);
		try {
			LOGGER.log(Level.FINE, "Main called with args: {0}", args);
			start(args);
		} catch (Exception e) {
			System.out.println("Execution returned an exception:");
			System.out.println(e.getMessage());

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.log(Level.SEVERE, sw.toString());
		}
	}

}



//boolean eohFound = false;
//while ((count = in.read(buffer)) != -1)
//{
//  if(!eohFound){
//      String string = new String(buffer, 0, count);
//      int indexOfEOH = string.indexOf("\r\n\r\n");
//      if(indexOfEOH != -1) {
//          count = count-indexOfEOH-4;
//          buffer = string.substring(indexOfEOH+4).getBytes();
//          eohFound = true;
//      } else {
//          count = 0;
//      }
//  }
//dos.write(buffer, 0, count);
//dos.flush();
//}
//in.close();
//dos.close();
