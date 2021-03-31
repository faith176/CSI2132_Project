import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 

public class Admin {
  private String column;
  private String table;
  private String whereCondition;
  private String partialQuery;
  private String AdminTask;

  //customer variables
  private String customer_sin;
  private String customer_firstName;
  private String customer_middleName;
  private String customer_lastName;
  private String customer_address;
  private String customer_date_of_registration;
  private String customer_phone;
  
  private Connection db;
  private Statement st; 

  public Admin() {
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
		,"elu032", "Qw300114727oP!");

		//initialize variable that will hold the statement to be executed
		this.st = db.createStatement();

	  } catch(SQLException ex) {
			System.err.println("Error get information from database");
			ex.printStackTrace();
		}
  }
  
  public void AdminCase() throws SQLException {
	  Scanner scanner = new Scanner(System.in);  // Create a Scanner object
	  
	  while (AdminTask != "0") {
		System.out.println("\n" + "Welcome Admin! What would you like to do? Type the corresponding number:" + "\n");
	    System.out.println("1: Select");
	    System.out.println("2: Insert");
	    System.out.println("3: Delete");
		System.out.println("4: Create a new Employee Account");
		System.out.println("5: Create a new Customer Account");
		System.out.println("0: Exit");

	    AdminTask = scanner.nextLine();
	    try {
	    switch(AdminTask) {
	    	case ("1"):
	    		AdminTask = "Select";
				getInfo();
				printResultSet(select());
				break;
			case ("2"):
				AdminTask = "Insert";
				getInfo();
				System.out.println("Query has been executed");
				break;
			case ("3"):
				AdminTask = "Delete";
				getInfo();
				System.out.println("Query has been executed");
				break;
			case ("4"):
				System.out.println("Employee account has been created" + "\n");
				break;
			case ("5"):
				getInfo();
				createCustomerAccount();
				System.out.println("Customer account has been created" + "\n");
				break;
			case ("0"):
				System.out.println("--- Logging out of admin...");
				return;
			default:
				System.out.println("--- Please enter a valid number.");
		}   	
	    } catch (SQLException e) {
	    		System.out.println("--- An SQL Exception occured. Check that you spelled everything correctly.");
	    }
	    	System.out.println("\n\nWould you like to do anything else? Type the corresponding number:" + "\n");
		}
	}

	//gets relevant info to perform a select, insert, or delete
	public void getInfo() throws SQLException {
		Scanner scanner2 = new Scanner(System.in);
		System.out.println("Task selected is: " + AdminTask + "\n"); 
	    		
	    	System.out.println("Input column(s) to " + AdminTask + ": "); 
	    	column = scanner2.nextLine();
	    		
	    	System.out.println("Input table(s) to " + AdminTask + ": ");  
	    	table = scanner2.nextLine();
	    		
	    	System.out.println("Would you like a where condition, Type Y or N: ");  
	    	whereCondition = scanner2.nextLine();
	    		
	    	if (whereCondition.equals("Y")) {
	    		System.out.println("Input where condition: "); 
	    		whereCondition = scanner2.nextLine();
	    	}
	    	else {
	    		whereCondition = "";
	    	}
	}
  
  public ResultSet select() throws SQLException {
			//initialize variable that will hold the statement to be executed
			st = db.createStatement(); 
					//Defines the sql query and executes it, saving the results into variable rs
			partialQuery = ("SELECT " + column + " FROM " + table);
			if (whereCondition != "") {
				partialQuery = ("SELECT " + column + " FROM " + table + " WHERE " + whereCondition );
			}
			else {
				partialQuery = ("SELECT " + column + " FROM " + table);
			}
			
			ResultSet rs = st.executeQuery(partialQuery);
			return rs;
		}
  

  public void insert() throws SQLException {
			//initialize variable that will hold the statement to be executed
			st = db.createStatement(); 
					//Defines the sql query and executes it, saving the results into variable rs
			partialQuery = ("SELECT " + column + " FROM " + table);
			if (whereCondition != "") {
				partialQuery = ("INSERT " + column + " FROM " + table + " WHERE " + whereCondition );
			}
			else {
				partialQuery = ("INSERT " + column + " FROM " + table);
			}
			st.executeUpdate(partialQuery);
  }

  
  public void delete() throws SQLException {
			//initialize variable that will hold the statement to be executed
			st = db.createStatement(); 
					//Defines the sql query and executes it, saving the results into variable rs
			partialQuery = ("SELECT " + column + " FROM " + table);
			if (whereCondition != "") {
				partialQuery = ("DELETE " + column + " FROM " + table + " WHERE " + whereCondition );
			}
			else {
				partialQuery = ("DELETE " + column + " FROM " + table);
			}
			st.executeUpdate(partialQuery);
  }

  public void createCustomerAccount() throws SQLException {
	Scanner scannerx = new Scanner(System.in);
	System.out.println("Please Enter Your SIN number:" + "\n");
	this.customer_sin = scannerx.nextLine();
	System.out.println("Please Enter Your first name:" + "\n");
	this.customer_firstName = scannerx.nextLine();
	System.out.println("Please Enter Your middle name:" + "\n");
	this.customer_middleName = scannerx.nextLine();
	System.out.println("Please Enter Your last name:" + "\n");
	this.customer_lastName = scannerx.nextLine();
	System.out.println("Please Enter Your address:" + "\n");
	this.customer_address = scannerx.nextLine();
	//sets the value of to current time
	this.customer_date_of_registration = String.valueOf(java.time.LocalDate.now());
	System.out.println("Please Enter Your phone number:" + "\n");
	this.customer_phone = scannerx.nextLine();
	
	st = db.createStatement(); 
	partialQuery = ("INSERT INTO customer VALUES ("+ customer_sin + ", '" + customer_firstName + "', '" + customer_middleName + "', '" + customer_lastName + "', '" + customer_address + "', '" + customer_date_of_registration +"', " + customer_phone + ")" );
	st.executeUpdate(partialQuery);
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