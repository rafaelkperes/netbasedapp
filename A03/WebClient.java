import java.io.*;
import java.net.*;

public class WebClient {

	public static void main(String[] args) throws IOException, URISyntaxException {

		String urlStr = "https://www.google.de/"; // some URL
		URI uri = new URI( urlStr); 
		String host = uri.getHost( ); 
		String path = uri.getRawPath( ); 
		if (path == null || path.length( ) == 0) {
		    path = "/";
		} 
		 
		String query = uri.getRawQuery( ); 
		if (query != null && query.length( ) > 0) {
		    path += "?" + query;
		} 
		
		String protocol = uri.getScheme( ); 
		int port = uri.getPort( ); 
		if (port == -1) {
		    if (protocol.equals("http")) { 
		        port = 80; // http port 
		    }
		    else if (protocol.equals("https")) {
		        port = 443; // https port 
		    }
		    else {
		        return;
		    }
		}
		
		try (Socket webSocket = new Socket(InetAddress.getByName(host), port);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(webSocket.getOutputStream(), "UTF8"));
				BufferedReader in = new BufferedReader(new InputStreamReader(webSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
				String userInput = stdIn.readLine();
				
				out.write( "GET /" + path + " HTTP/1.0\r\n" + 
	                       "Host: " + host + "\r\n" + 
	                       "Accept: text/plain, text/html, text/*\r\n" );
				out.flush();
				String hostResponse;
				File file = new File("httpresponse.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				while((hostResponse = in.readLine())!= null){
					System.out.println(hostResponse);
					bw.write(hostResponse);
					bw.close();					
				}
				in.close();

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + host);
			System.exit(1);
		}
	}

}
