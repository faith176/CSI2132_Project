import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 

public class Employee {
    private String sin;
    private String accountAlready;
    private String partialQuery;

    private Connection db;
    private Statement st; 

    public Employee(String sin) {
        this.sin = sin;
        //try catch statement checks to see if the appropriate library has been added
		try {
            Class.forName("org.postgresql.Driver");
          } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL Driver" + "Include in your library path!");
            e.printStackTrace();
  }
      //attempts to connect to the database, needs password and username
    try {
          //DATABASE CONNECTION NOT WORKING NEED HELP 
          this.db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_b03_g30"
          ,"oades097", "University917");
          //initialize variable that will hold the statement to be executed
          this.st = db.createStatement();
    } catch(SQLException ex) {
          System.err.println("Error get information from database");
          ex.printStackTrace();
      }
    }

    public void EmployeeCase() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "Do you already have an Account, Y or N. If you type N, you will be asked to make a new account" + "\n");
		accountAlready = scanner.nextLine();

		if (accountAlready.equals("Y")) {
            System.out.println("Enter Employee SIN to login: ");
            //sets the sin number as the one the user inputted, can retrieve data for this sin now
            this.sin = scanner.nextLine();
		    logIn();
        }
	    else {
            createEmployeeAccount();
		}
    }

    //Will use SQL to get all the data from the database and add it to this class
    public void logIn() throws SQLException {
    	Statement st = db.createStatement();
    	//ResultSet rs = st.executeQuery("SELECT * FROM employee WHERE sin = " + sin + ";");
    	//System.out.println(rs);
    	
    	//st = db.createStatement(); 
        //partialQuery = ("SELECT * FROM employee WHERE sin = " + sin);
        //ResultSet rs = st.executeQuery(partialQuery);
        printResultSet(rs);
    	
    	
    }

    //Will create a new employee
    public void createEmployeeAccount() throws SQLException {
    	System.out.println("SHSHS");
    }


    public void createRentings() throws SQLException {

    }

    public void convertBookings() throws SQLException {

    }


    //Probably Won't use this
    public String getSin() throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT sin FROM employee WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        return rs.getString(1);
    }

    public void setSin (BigInteger newSin) throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("UPDATE sin = " + newSin + " FROM employee WHERE sin = " + sin);
        st.executeQuery(partialQuery);
    }

    //function that prints the results of a select query in a readable manner
		final private static void printResultSet(ResultSet rs) throws SQLException {
		    // Prepare metadata object and get the number of columns.
		    ResultSetMetaData rsmd = rs.getMetaData();
		    int columnsNumber = rsmd.getColumnCount();

		    // Print column names (a header).
		    for (int i = 1; i <= columnsNumber; i++) {
		        if (i > 1) System.out.print(" | ");
		        System.out.print(rsmd.getColumnName(i));
		    }
		    System.out.println("");

		    while (rs.next()) {
		        for (int i = 1; i <= columnsNumber; i++) {
		            if (i > 1) System.out.print(" | ");
		            System.out.print(rs.getString(i));
		        }
		        System.out.println("");
		    }
		}
}