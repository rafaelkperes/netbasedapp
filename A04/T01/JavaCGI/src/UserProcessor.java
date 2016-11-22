import java.util.*;
import java.io.*;
import java.net.URLDecoder;

public class UserProcessor {
	
	//Standart input is read only once upon call of UserProcessor by maincgi.sh.
	//So we need save it in global var
	static String mainBuffer;

	//Return input for method GET(from QUERY_STRING environment variable) or POST(from standart input)
	public static String returnInput() throws IOException {
		String inBuffer = null;
		if (methGet()) {
			inBuffer = System.getenv("QUERY_STRING");
			return inBuffer;
		} else {
			return mainBuffer;
		}

	}
	
	//Save input stream into global variable mainBuffer
	public static void saveInStream(InputStream inStream) {
		String inBuffer = "";

		DataInput br = new DataInputStream(inStream);
		String line;
		try {
			while ((line = br.readLine()) != null) {
				inBuffer = inBuffer + line;
			}
		} catch (IOException ignored) {
		}
		mainBuffer = inBuffer;
	}

	//Parse our input into <key value> pairs
	public static Map<String, String> parseQuery() throws IOException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();

		String[] pairs = returnInput().split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}

		return query_pairs;
	}

	//Search for number using lastName and firstName
	public static String numberSearchInPhoneBook(String lastName, String firstName) {
		String searchLine = null;
		String userNumber = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader("phonebook.txt"));
			String line = in.readLine();

			while (line != null) {
				if (lastName.equals(line.substring(0, line.indexOf(",")))
						&& firstName.equals(line.substring(line.indexOf(",") + 1, line.lastIndexOf(",")))) {
					searchLine = line;
				}
				line = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Error while reading file line by line:" + e.getMessage());
		}
		userNumber = searchLine.substring(searchLine.lastIndexOf(",") + 1);

		return userNumber;
	}

	//Return true, if used GET method(using environment variable REQUEST_METHOD)
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

	//Return header, so server can generate appropriate output
	public static String Header() {

		return "Content-type: text/html\n\n";
	}

	//Return top part of html markup
	public static String HtmlTop(String Title) {
		String Top = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<title>\n" + Title + "\n</title>\n</head>\n<body>\n";		

		return Top;
	}

	//Return bottom part of html markup
	public static String HtmlBot() {

		return "</body>\n</html>\n";
	}

	public static void main(String[] args) throws Throwable, IOException {
		String requestMethod = System.getenv("REQUEST_METHOD");
		
		//Initialize global variable mainBuffer
		saveInStream(System.in);

		String lastName = parseQuery().get("lastName");
		String firstName = parseQuery().get("firstName");

		System.out.println(Header());
		System.out.println(HtmlTop("User phone number search result"));
		System.out.println("<p>The method used for request: " + requestMethod + "</p>");
		System.out.println("Phone number of " + lastName + " " + firstName + " is ");
		System.out.println(numberSearchInPhoneBook(lastName, firstName));
		System.out.println(HtmlBot());
	}
}
