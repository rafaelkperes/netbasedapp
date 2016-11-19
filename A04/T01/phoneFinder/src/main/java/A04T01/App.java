package A04T01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final String FILENAME = "phonebook.txt";

    public static String findPhone(File file,
            String firstName, String lastName) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            String fName = line.split(",")[0];
            String lName = line.split(",")[1];
            
            if(firstName.equals(fName)
                    && lastName.equals(lName)) {
                return line.split(",")[2];
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        File file = new File(FILENAME);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("First Name: ");
        String firstName = br.readLine();
        System.out.print("Last Name: ");
        String lastName = br.readLine();
        
        String phone = findPhone(file, firstName, lastName);
        if(phone != null) {
            System.out.println("Found: " + phone);
        } else {
            System.out.println("Not Found!");
        }
    }

}
