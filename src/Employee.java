import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 

public class Employee {


    private String accountAlready;

    private Connection db;
    private Statement st; 

    public Employee() {
        //try catch statement checks to see if the appropriate library has been added
		try {
            Class.forName("org.postgresql.Driver");
          } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL Driver" + "Include in your library path!");
            e.printStackTrace();
  }
      //attempts to connect to the database, needs password and username
    try {
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
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("\n" + "Do you already have an Account, Y or N. If you type N, you will be asked to make a new account" + "\n");
		accountAlready = scanner.nextLine();

		if (accountAlready == "Y") {
		createEmployeeAccount();
        }
	    else {
			//function retrives all relavant information of the employee from the database
			logIn();
		}
    }

    
}
