package app;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.IOUtils;

public class App {

    private static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private static String getFileExtension(String path) {
        try {
            String ending = path.split("\\.")[1];
            return ending;
        } catch (PatternSyntaxException | IndexOutOfBoundsException e) {
            return "UNDEFINED";
        }
    }

    public static void sendHTML(String path, Socket skt) throws FileNotFoundException,
            IOException {
        path = "." + path;
        File file = new File(path);
        Date date = new Date();

        String data = "HTTP/1.1 200 OK\r\n"
                + "Date: " + getServerTime() + "\r\n";

        PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new FileReader(file));

        out.print(data);
        out.print("\r\n");

        String line;
        while ((line = br.readLine()) != null) {
            // process the line.
            out.print(line);
            out.print("\r\n");
        }
        
        System.out.println("Response: 200 OK");
        out.close();
    }

    public static void sendJPG(String path, Socket skt) throws FileNotFoundException,
            IOException {
        path = "." + path;
        File file = new File(path);
        String response_header = "HTTP/1.1 200 OK\r\n"
                + "Connection: keep-alive\r\n"
                + "Date: " + getServerTime() + "\r\n"
                + "Connection: Keep-Alive\r\n"
                + "Content-Type: image/jpeg\r\n"
                + "Content-Length: " + file.length() + "\r\n"
                + "Last-Modified: Fri, 10 Feb 2012 14:31:06 GMT\r\n"
                + "\r\n";

        System.out.println(response_header);

        byte[] bytes = new byte[16 * 1024];
        InputStream in = new FileInputStream(file);
        OutputStream out = skt.getOutputStream();

        int count;
        InputStream in_response = IOUtils.toInputStream(response_header, "UTF-8");
        count = in_response.read(bytes);
        out.write(bytes, 0, count);
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }
        
        System.out.println("Response: 200 OK");
        out.close();
        in.close();
    }

    public static void sendSimpleHeader(Socket skt, String error) throws IOException {
        System.out.println("Response: " + error);
        
        String response_header = "HTTP/1.1" + error + "\r\n";

        byte[] bytes = new byte[16 * 1024];
        OutputStream out = skt.getOutputStream();
        int count;
        InputStream in_response = IOUtils.toInputStream(response_header, "UTF-8");
        count = in_response.read(bytes);
        out.write(bytes, 0, count);

        out.close();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket srvr = new ServerSocket(1234);

        try {
            while (true) {
                Socket skt = srvr.accept();
                System.out.print("Server has connected!\n");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                skt.getInputStream()));
                int len;
                String request = in.readLine();
                System.out.println(request);
                System.out.println("Finished reading request.");

                try {
                    String path = request.split("\\s")[1];
                    System.out.println(path);
                    String extension = getFileExtension(path);
                    System.out.println(extension);

                    switch (extension) {
                        case "jpg":
                            sendJPG(path, skt);
                            break;
                        case "html":
                            sendHTML(path, skt);
                            break;
                        default:
                            if (path.endsWith("/")) {
                                sendHTML(path + "index.html", skt);
                            } else {
                                sendHTML(path, skt);
                            }
                    }
                } catch (PatternSyntaxException | IndexOutOfBoundsException e) {
                    /* Request Header is invalid */
                    sendSimpleHeader(skt, "400 Bad Request");
                } catch (FileNotFoundException e) {
                    /* Path is invalid */
                    sendSimpleHeader(skt, "404 Not Found");
                } catch (Exception e) {
                    /* Error while processing */
                    sendSimpleHeader(skt, "500 Internal Server Error");
                }
                skt.close();
            }
        } catch (Exception e) {
            System.out.print("Whoops! It didn't work!\n");
        } finally {
            srvr.close();
        }

    }

}
