package A04T02;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class PhoneBookApplet extends JApplet {

    private static final String DEFAULT_PHONEBOOK_URI = "http://netappsvm.informatik.uni-stuttgart.de/~group07/phonebook.txt";
    private String phonebookURI;

    private final String[] columnNames = {
        "First Name",
        "Last Name",
        "Phone Number"};
    private final int columnCnt = 3;

    private JTable table;
    private Object[][] data;

    public PhoneBookApplet() throws HeadlessException {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() {
        this.phonebookURI = getParameter ("url");
        /*if(this.phonebookURI == null || phonebookURI.isEmpty()) {
            this.phonebookURI = DEFAULT_PHONEBOOK_URI;
        }*/
        //Execute a job on the event-dispatching thread
        try {            
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Load phone book data from server.
                    try {
                        loadPhoneBookData();
                    } catch (Exception e) {
                        data = new Object[1][columnCnt];
                        data[0][0] = e.getMessage();
                        data[0][1] = " ";
                        data[0][2] = " ";
                    }
                    // Create the table (GUI) with the phone book data.
                    createGUI();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }

    void createGUI() {
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        setContentPane(scrollPane);
    }

    void loadPhoneBookData() throws Exception {
        // Set data list.
        List<String[]> dataList = new ArrayList<>();

        // Load phonebooks.txt
        URL obj = new URL(phonebookURI);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        if (200 != responseCode) {
            throw new Exception("Unexpected code returned: " + responseCode);
        }

        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        
        String lineStr = null;
        while((lineStr = in.readLine()) != null) {
            dataList.add(lineStr.split(","));
        }
        
        // Convert to array.
        data = new Object[dataList.size()][columnCnt];
        int i = 0;
        for (String[] line : dataList) {
            for (int j = 0; j < columnCnt; j++) {
                data[i][j] = line[j];
            }
            i++;
        }

    }
}
