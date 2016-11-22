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
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));

		}
		System.out.println(
				"Full name of user you requested: " + query_pairs.get("firstName") + " " + query_pairs.get("lastName"));
	}

	public static boolean methGet() {
		String RequestMethod = System.getenv("REQUEST_METHOD");
		boolean returnVal = false;

		if (RequestMethod != null) {
			if (RequestMethod.equals("GET") || RequestMethod.equals("get")) {
				returnVal = true;
			}
		}

		return returnVal;
	}

	public static boolean methPost() {
		String RequestMethod = System.getenv("REQUEST_METHOD");
		boolean returnVal = false;

		if (RequestMethod != null) {
			if (RequestMethod.equals("POST") || RequestMethod.equals("post")) {
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
		Top = "<html>\n<head>\n<title>\n" + Title + "\n</title>\n</head>\n<body>\n";

		return Top;
	}

	public static String HtmlBot() {

		return "</body>\n</html>\n";
	}

	public static void main(String[] args) throws Throwable, IOException {

		System.out.println(Header());
		System.out.println(HtmlTop("User search result"));
		printQuery(System.in);
		System.out.println("");
		System.out.println(HtmlBot());
	}
}
