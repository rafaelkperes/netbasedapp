import java.util.*;
import java.io.*;
import java.net.URLDecoder;

public class UserProcessor {

	public static void printQuery(InputStream inStream) throws IOException {
		String inBuffer = "";
		String requestMethod = System.getenv("REQUEST_METHOD");
		System.out.println("The method you used for user request is: " + requestMethod);

		if (methGet()) {
			inBuffer = System.getenv("QUERY_STRING");
		} else {
			DataInput br = new DataInputStream(inStream);
			String line;
			try {
				while ((line = br.readLine()) != null) {
					inBuffer = inBuffer + line;
				}
			} catch (IOException ignored) {
			}
		}

		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = inBuffer.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "ISO-8859-1"),
					URLDecoder.decode(pair.substring(idx + 1), "ISO-8859-1"));

		}
		System.out.println(
				"Full name of user you requested: " + query_pairs.get("firstName") + " " + query_pairs.get("lastName"));
	}

	public static void readPhoneBook() throws IOException {
		String line;
		BufferedReader in;

		in = new BufferedReader(new FileReader("phonebook.txt"));
		line = in.readLine();

		while (line != null) {
			
			line = in.readLine();
			System.out.println(line);
		}
		
	}

	/*
	 * public static String searchNumber(String lastName, String firstName){
	 * String userNumber = null; if((lastName + ","+firstName) == "dds") return
	 * userNumber; }
	 */

	public static boolean methGet() {
		String requestMethod = System.getenv("REQUEST_METHOD");
		boolean returnVal = false;

		if (requestMethod != null) {
			if (requestMethod.equals("GET") || requestMethod.equals("get")) {
				returnVal = true;
			}
		}

		return returnVal;
	}

	public static String Header() {

		return "Content-type: text/html\n\n";
	}

	public static String HtmlTop(String Title) {
		String Top;
		Top = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<title>\n" + Title + "\n</title>\n</head>\n<body>\n";

		return Top;
	}

	public static String HtmlBot() {

		return "</body>\n</html>\n";
	}

	public static void main(String[] args) throws Throwable, IOException {

		System.out.println(Header());
		System.out.println(HtmlTop("User search result"));
		printQuery(System.in);
/*		readPhoneBook();
*/		System.out.println(HtmlBot());
	}
}
