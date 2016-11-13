package app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

/**
 * HTTP/1.1
 *
 */
public class App {

    private static final int DEFAULT_PORT = 80;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    static void sendGET(String host, String path, Socket socket) throws IOException, Exception {
        String filename = "";

        /* throws IOException */
        PrintWriter request = new PrintWriter(socket.getOutputStream());

        /* write and send GET request */
        String requestheader = "GET " + path + " HTTP/1.1\r\n" + "Host: "
                + host + "\r\n" + "Connection: close\r\n\r\n";
        request.print(requestheader);
        request.flush();
        LOGGER.log(Level.FINE, "GET request sent", requestheader);

        byte[] buffer = new byte[2048];
        DataInputStream in = new DataInputStream(socket.getInputStream());
        OutputStream dos;
        boolean gotEOH = false;

        /* read response */
        String response = "";
        ByteArrayOutputStream byteContent = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(buffer)) != -1) {
            LOGGER.log(Level.FINEST, "response buffer read", new String(buffer));

            byteContent.write(buffer, 0, len);
            String bytetostr = new String(buffer);
            response = response.concat(bytetostr);
        }
        LOGGER.log(Level.FINE, "GET response received", response);

        int delimiter = response.indexOf("\r\n\r\n");
        String header = response.substring(0, delimiter);
        byte[] content = Arrays.copyOfRange(byteContent.toByteArray(),
                delimiter + 4, byteContent.toByteArray().length);
        LOGGER.log(Level.FINER, "GET response header", header);
        LOGGER.log(Level.FINER, "GET response content", content);

        String status = header.split(" |\n")[1] + " " + header.split(" |\n")[2];
        LOGGER.log(Level.FINER, "GET response status", status);
        System.out.println("GET RESPONSE STATUS: " + status);

        if (status.startsWith("2")) {
            /* 2XX STATUS CODE */
            String contentType;
            int cttindex;
            if ((cttindex = header.indexOf("Content-Type: ")) != -1) {
                contentType = header.substring(cttindex);
                contentType = contentType.split(" ")[1];
            } else {
                contentType = "";
            }

            filename = "RESULTS/response";
            switch (contentType) {
                case "text/plain":
                    filename = filename.concat(".txt");
                    break;
                case "image/png":
                    filename = filename.concat(".txt");
                    break;
                case "image/jpeg":
                    filename = filename.concat(".jpg");
                    break;
                case "image/bmp":
                    filename = filename.concat(".bmp");
                    break;
                case "image/gif":
                    filename = filename.concat(".gif");
                    break;
                case "text/html":
                    filename = filename.concat(".html");
                    break;
                default:
                    try {
                        String ending = path.split("\\.")[1];
                        filename = filename.concat("." + ending);
                    } catch(PatternSyntaxException | IndexOutOfBoundsException e) {
                        filename = filename.concat(".unknown");
                    }
             }
        } else if (status.startsWith("3")) {
            filename = "RESULTS/redirect.html";
        } else {
            throw new Exception("Request returned error code " + status);
        }

        dos = new FileOutputStream(filename);
        dos.write(content);
        dos.close();

        in.close();
    }

    static Socket connect(String host, String path, int port)
            throws IOException, Exception {
        LOGGER.log(Level.FINE, "Connecting to: {0}:{1}", new Object[]{host, port});
        Socket clientSocket = new Socket(host, port);
        if ("".equals(path)) {
            path = "/";
        }
        sendGET(host, path, clientSocket);
        return clientSocket;
    }

    static void start(String[] args) throws Exception {
        LOGGER.log(Level.FINER, "Start called with args: {0}", args);

        URL url = null;
        if (args.length < 1) {
            System.out.println("Type the URL:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            url = new URL(br.readLine());
        } else {
            url = new URL(args[0]);
        }
        LOGGER.log(Level.FINER, "Parsed HOST:", url.getHost());
        LOGGER.log(Level.FINER, "Parsed PORT:", url.getPort());

        /* if URL does not contain PORT, set default value */
        int port = url.getPort();
        if (port == -1) {
            port = DEFAULT_PORT;
        }

        Socket sock = connect(url.getHost(), url.getPath(), port);
    }

    public static void main(String[] args) throws Exception {
        Handler fh = new FileHandler("./RESULTS/all.log");
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
