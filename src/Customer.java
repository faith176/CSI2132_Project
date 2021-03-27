import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 

public class Customer {
    private String sin;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String date_of_registration;

    private String accountAlready;
    private String partialQuery;
    private String customerTask;

    private Connection db;
    private Statement st;

    public Customer(String sin) {
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

    public void CustomerCase() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "Do you already have an Account, Y or N. If you type N, you will be asked to make a new account" + "\n");
		accountAlready = scanner.nextLine();

		if (accountAlready.equals("Y")) {
            System.out.println("Enter Customer SIN to login: ");
            //sets the sin number as the one the user inputted, can retrieve data for this sin now
            this.sin = scanner.nextLine();
        }
	    else if (accountAlready.equals("N")) {
            System.out.println("Please Enter Your SIN number to log in:" + "\n");
            this.sin = scanner.nextLine();
            System.out.println("Please Enter Your first name:" + "\n");
            this.firstName = scanner.nextLine();
            System.out.println("Please Enter Your middle name:" + "\n");
            this.middleName = scanner.nextLine();
            System.out.println("Please Enter Your last name:" + "\n");
            this.lastName = scanner.nextLine();
            System.out.println("Please Enter Your address:" + "\n");
            this.sin = scanner.nextLine();
            //sets the value of to current time
            this.date_of_registration = String.valueOf(java.time.LocalDate.now());
            createCustomerAccount();
		}
        System.out.println("\n" + "You are now logged in");
        loggedInTask();
    }

    public void loggedInTask() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        //default value
        customerTask = "1";
        while (!customerTask.equals("0")) {
            System.out.println("\n" + "What would you like to do?");
            System.out.println("0: To go back");
            System.out.println("1: View current Bookings");
            System.out.println("2: View current Rentings");
            System.out.println("3: Create A New Booking" + "\n");
    
            customerTask = scanner.nextLine();
            switch(customerTask) {
                case ("1"):
                    printResultSet(currentBookings());
                    break;
                case ("2"):
                    printResultSet(currentRentings());
                    break;
                case ("3"):
                    createBooking();
                    break;
            }
        }
    }

    //Will create a new customer account
    public void createCustomerAccount() throws SQLException {
        st = db.createStatement(); 
        partialQuery = ("INSERT INTO customer VALUES (" + sin + ", " + firstName + ", " + middleName + ", " + lastName + ", " + address+ ", " + date_of_registration +")" );
        st.executeQuery(partialQuery);
    }

    public boolean createBooking() {
        Scanner scanner2 = new Scanner(System.in);
        System.out.println("Let's create a booking, we will now check for your preferences" + "\n");

        System.out.println("Please Enter Your first name:" + "\n");
        return true;
    }


    public ResultSet currentBookings() throws SQLException {
        st = db.createStatement(); 
        partialQuery = ("SELECT * FROM booking WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        return rs;
        
    }

    public ResultSet currentRentings() throws SQLException {
        st = db.createStatement(); 
        partialQuery = ("SELECT * FROM renting WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        return rs;
    }

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