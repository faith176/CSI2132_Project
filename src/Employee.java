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
    private String first_name;
    private String middle_name;
    private String last_name;
    private String address;
    private String salary;
    private String manager_sin;
    private String accountAlready;
    private String partialQuery;
    private String todo;
    private String todo_entered;

    public Connection db;
    public Statement st; 
    
    String booking_id;
    String arrival_date;
    String departure_date;
    String duration_of_stay;
    String room_num;
    String hotel_id;;
    String renting_id;
    String balance; // numeric
    boolean paid_for;

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
          this.db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_b03_g30"
          ,"elu032", "Qw300114727oP!");
          //initialize variable that will hold the statement to be executed
          this.st = db.createStatement();
          
    } catch(SQLException ex) {
          System.err.println("Error get information from database");
          ex.printStackTrace();
      }
    }

    public void EmployeeCase() throws SQLException {
    	todo = "what";
    	while (todo.equals("what")) {
    		Scanner scanner = new Scanner(System.in);
            System.out.println("\n" + "Welcome Employee, what would you like to do? Type the corresponding number:");
            System.out.println("1: Login");
            System.out.println("2: Create employee account");
            System.out.println("0: Exit");
    		todo_entered = scanner.nextLine();

    		switch(todo_entered) {
		    	case ("1"):
		    		todo_entered = "Login";
		    		System.out.println("Enter Employee SIN to login: ");
		    		// TODO error if not 9 digits
		    		//sets the sin number as the one the user inputted, can retrieve data for this sin now
		    		this.sin = scanner.nextLine();
		    		logIn();
					break;
				case ("2"):
					todo_entered = "Create";
					createEmployeeAccount();
					break;
				case ("0"):
					todo_entered = "Exit";
					System.out.println("--- Returning to main page...");
					return;
				default:
					System.out.println("--- Please enter a valid number.");
    		}
				
    	}
        
    }

    //Will use SQL to get all the data from the database and add it to this class
    public void logIn() throws SQLException {
    	// checks if the the sin entered is correct to an employee sin
    	st = db.createStatement(); 
        partialQuery = ("SELECT * FROM employee WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        //printResultSet(rs);
        
        // login unsuccessful
        // TODO make this work
        if (rs.next()) {
        	Scanner scanner = new Scanner(System.in);
            System.out.println("Login unsuccessful. Would you like to return to the home page or try again?");
            System.out.println("1: Return");
            System.out.println("2: Try again");
            todo_entered = scanner.nextLine();
            todo = "0";
            while (todo.equals("0")) {
            	switch(todo_entered) {
    				case ("1"):
    					// go back
    					todo = "good";
    					System.out.println("--- Returning to main page...");
    					break;
    				case ("2"):
    					todo = "good";
    					EmployeeCase();
    					break;
    				default:
    					System.out.println("Please enter a valid number.");
    			}
            }
        }
        
        // login successful
        // 102938162
        else {
        	System.out.println("--- Login successful...");
        	Scanner scanner = new Scanner(System.in);
            System.out.println("What would you like to do? Type the corresponding number: ");
            System.out.println("1: Convert a booking to a renting");
            System.out.println("2: Renting without a booking");
            System.out.println("0: Exit");
            todo_entered = scanner.nextLine();
            todo = "0";
            while (todo.equals("0")) {
            	switch(todo_entered) {
    				case ("1"):
    					todo = "Convert";
    					convertBookings();
    					break;
    				case ("2"):
    					todo = "Rent";
    					createRentings();
    					break;
    				case ("0"):
    					todo = "Exit";
    					System.out.println("--- Returning to main page...");
    					break;
    				default:
    					System.out.println("Please enter a valid number.");
    			}
            }
            
        }
    }

    //Will create a new employee
    public void createEmployeeAccount() throws SQLException {
    	boolean flag = false;
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("\n" + "--- employee account creation" + "\n\n");
        System.out.println("\n" + "Enter your SIN: " + "\n");
		sin = scanner.nextLine();
		System.out.println("\n" + "Enter your first name: " + "\n");
		first_name = scanner.nextLine();
		System.out.println("\n" + "Enter your middle name: " + "\n");
		middle_name = scanner.nextLine();
		System.out.println("\n" + "Enter your last name: " + "\n");
		last_name = scanner.nextLine();
		System.out.println("\n" + "Enter your address: " + "\n");
		address = scanner.nextLine();
		System.out.println("\n" + "Enter your salary: " + "\n");
		salary = scanner.nextLine();
		System.out.println("\n" + "Enter your manager SIN: " + "\n");
		manager_sin = scanner.nextLine();
		st = db.createStatement(); 
        st.executeUpdate("INSERT INTO employee VALUES (" + sin + ",'" + first_name + "','" + middle_name + "','" 
        				+ last_name + "','" + address + "'," + salary + "," + manager_sin + ")");
        System.out.println("\n\n" + "--- employee account created" + "\n\n");
    }


    public void createRentings() throws SQLException {
    	
    	/*renting_id INTEGER, 
        balance NUMERIC(8,2), 
        paid_for BOOLEAN, 
        arrival_date DATE,
        departure_date DATE,
        duration_of_stay INTEGER, --JAVA OR A FUNCTION FILLS THIS VALUE !!!
        room_num INTEGER,
    	hotel_id INTEGER,
        sin INTEGER*/
    }

    // function to convert booking to renting
    public void convertBookings() throws SQLException {
    	Scanner scanner = new Scanner(System.in);
    	// retrieve the customer's booking id
    	System.out.println("Enter customer's booking id: ");
    	booking_id = scanner.nextLine();
    	// query to retrieve booking information to insert into renting
    	st = db.createStatement(); 
        String p = ("SELECT * FROM booking WHERE booking_id = " + booking_id);
        ResultSet rd = st.executeQuery(p);
        if (rd.next()) {
        	arrival_date = rd.getString(4); 
            departure_date = rd.getString(5);
            duration_of_stay = rd.getString(6);
            room_num = rd.getString(7);
            hotel_id = rd.getString(8);
            sin = rd.getString(9);
        }
        // query to get the room price
        st = db.createStatement(); 
        partialQuery = ("SELECT price FROM room WHERE room_num = " + room_num);
        ResultSet rs = st.executeQuery(partialQuery);
        String price = null;
        if (rs.next()) {
        	price = rs.getString(1);
        }
        // calculate the balance
        balance = Float.toString(Float.parseFloat(duration_of_stay)*Float.parseFloat(price)); // numeric
        // query to get the max renting id, the new id will be max + 1
        st = db.createStatement(); 
        partialQuery = ("SELECT MAX(renting_id) FROM renting");
        rs = st.executeQuery(partialQuery);
        if (rs.next()) {
        	int ridi = Integer.parseInt(rs.getString(1)) + 1;
            renting_id = Integer.toString(ridi);
        }
        // to determine paid_for
    	System.out.println("Has the customer paid for the room yet? (Y or N): ");
    	String pf = scanner.nextLine();
    	if (pf.toUpperCase().equals("Y")) {
    		paid_for = true;
    	}
    	else {
    		paid_for = false;
    	}   
    	// query to insert into renting 
    	st = db.createStatement(); 
        partialQuery = ("INSERT INTO renting VALUES (" + renting_id + "," + balance + "," + paid_for + ", '" + arrival_date + 
        		"','" + departure_date + "'," + duration_of_stay + "," + room_num + "," + hotel_id + "," + sin + ")");
        st.executeUpdate(partialQuery);
        System.out.println("\n--- Room renting was successful...");
        System.out.println("What would you like to do next? Type the corresponding number: ");
        System.out.println("1: Convert a booking to a renting");
        System.out.println("2: Renting without a booking");
        System.out.println("0: Exit");
        todo_entered = scanner.nextLine();
        todo = "0";
        while (todo.equals("0")) {
        	switch(todo_entered) {
				case ("1"):
					todo = "Convert";
					convertBookings();
					break;
				case ("2"):
					todo = "Rent";
					createRentings();
					break;
				case ("0"):
					todo = "Exit";
					System.out.println("--- Returning to main page...");
					break;
				default:
					System.out.println("Please enter a valid number.");
			}
        }
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