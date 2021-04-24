import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner; 

public class Employee {
    private String employee_sin;
	private String employee_parent_brand;
	private String employee_hotel_id;

	private String sin;
    private String partialQuery;
    private String todo;
    private String todo_entered;

    public Connection db;
    public Statement st; 
	public Statement yu;
	public Statement nr; 
	public Statement qw;
    
    String booking_id;
    String arrival_date;
    String departure_date;
    String duration_of_stay;
    String room_num;
    String hotel_id;;
    String renting_id;
    String balance; // numeric
    boolean paid_for;

	//variables for creating a renting
	String customer_sin;
	String customer_parent_brand;
	String customer_view_type;
	String customer_hotel_id;
	String customer_occupants;
	String customer_room_num;
	String customer_arrival_date;
	String customer_departure_date;
	String customer_price;

    public Employee(String username, String password) {

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
          ,username, password);
          //initialize variable that will hold the statement to be executed
          this.st = db.createStatement();
          
    } catch(SQLException ex) {
          System.err.println("Error from database, username and password may be incorrect");
          ex.printStackTrace();
      }
    }

    public void EmployeeCase() throws SQLException {
    	todo = "what";
    	Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "Welcome Employee, what would you like to do? Type the corresponding number:");
	    while (todo.equals("what")) {
	        System.out.println("1: Login");
            System.out.println("0: Exit");
    		todo_entered = scanner.nextLine();
        	try {
	    		switch(todo_entered) {
			    	case ("1"):
			    		todo_entered = "Login";
			    		System.out.println("Enter Employee SIN to login: ");
			    		//sets the sin number as the one the user inputted, can retrieve data for this sin now
			    		this.employee_sin = scanner.nextLine();
			    		logIn();
						break;
					case ("0"):
						todo_entered = "Exit";
						System.out.println("\n--- Returning to main page...");
						return;
					default:
						System.out.println("\n--- Please enter a valid number.");
	    		}
			} catch (SQLException e) {
				System.out.println("\n--- An SQL Exception occured. Check that you spelled everything correctly.");
			}
	    }
    }

    //Will use SQL to get all the data from the database and add it to this class
    public void logIn() throws SQLException {
    	// checks if the the sin entered is correct to an employee sin
    	st = db.createStatement(); 
        partialQuery = ("SELECT * FROM employee WHERE sin = " + employee_sin);
        ResultSet rs = st.executeQuery(partialQuery);
		getEmployeeInfo();
        
        // login unsuccessful
        if (!rs.next()) {
        	Scanner scanner = new Scanner(System.in);
            System.out.println("Login unsuccessful. Would you like to return to the home page or try again? \nContact the admin if you do not have an account.");
            System.out.println("1: Return");
            System.out.println("2: Try again");
            todo_entered = scanner.nextLine();
            todo = "0";
            while (todo.equals("0")) {
            	switch(todo_entered) {
    				case ("1"):
    					// go back
    					todo = "good";
    					System.out.println("\n--- Returning to main page...");
    					break;
    				case ("2"):
    					todo = "good";
    					EmployeeCase();
    					break;
    				default:
    					System.out.println("\nPlease enter a valid number.");
    			}
            }
        }
        
        // login successful
        // 102938162
        else {
        	System.out.println("\n--- You are now logged in as an employee...");
        	
			todo = "0";
			while (todo.equals("0")) {
        	Scanner scanner = new Scanner(System.in);
            System.out.println("\nWhat would you like to do? Type the corresponding number: ");
            System.out.println("1: Convert a booking to a renting");
            System.out.println("2: Renting without a booking");
            System.out.println("3: Update renting to paid");
            System.out.println("4: View avaliable rooms");
            System.out.println("0: Exit");
            todo_entered = scanner.nextLine();
            
            try {
            	switch(todo_entered) {
    				case ("1"):
						todo_entered = "Convert";
    					convertBookings();
    					break;
    				case ("2"):
						todo_entered = "Rent";
    					createRentings();
    					break;
    				case ("3"):
						todo_entered = "Update";
    					updatePaidFor();
    					break;
                    case ("4"):
						todo_entered = "View";
    					roomAvalibility();
    					break;
    				case ("0"):
						todo_entered = "Exit";
						todo= "Exit";
    					System.out.println("\n--- Returning to main page...");
    					break;
    				default:
    					System.out.println("\nPlease enter a valid number.");
    			}
            } catch (SQLException e) {
				System.out.println("\n--- An SQL Exception occured. Check that you spelled everything correctly.");
			}
            }
        }
    }


    // FOR RENTING
    public Boolean createRentings() throws SQLException {
        Scanner scanner3 = new Scanner(System.in);
        ResultSet rs;
        Boolean foundRoom = false;
        Boolean quit= false;
		
        while (foundRoom == false && quit == false) {
        System.out.println("Creating a new renting, please input the customer's preferences and information" + "\n");

		//need the brand of the employee
		System.out.println("The brand the customer will be staying with is " + employee_parent_brand + "\n");
		customer_parent_brand = employee_parent_brand;
		customer_hotel_id = employee_hotel_id;

		//gets customer sin
		System.out.println("Please Enter the customer's sin number" + "\n");
		customer_sin = scanner3.nextLine();

        //gets the view the user wants
        System.out.println("\n"+"These are the avaliable views in your hotel, input you choice");
        partialQuery = ("SELECT DISTINCT view_type FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + customer_parent_brand + "' AND hotel.hotel_id = '" + customer_hotel_id + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        customer_view_type = scanner3.nextLine();

        //gets the amount of occupants
        System.out.println("\n" +"How many occupants? These are the maximum capacities avaliable in the rooms");
        partialQuery = ("SELECT DISTINCT capacity FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + customer_parent_brand + "' AND hotel.hotel_id = '" + customer_hotel_id + "' AND room.view_type = '" + customer_view_type + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        customer_occupants = scanner3.nextLine();

        System.out.println("\n"+"These are the possible rooms that fit your preferences please pick one. Input the room number" + "\n");
        partialQuery = ("SELECT room_num, price, extension_capabilities, other_amenities FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + customer_parent_brand + "' AND hotel.hotel_id = '" + customer_hotel_id + "' AND room.view_type = '" + customer_view_type+ "' AND room.capacity >= " + customer_occupants);
        rs = st.executeQuery(partialQuery);
        if (rs.isBeforeFirst() == true) {
            printResultSet(rs);
            customer_room_num = scanner3.nextLine();

            System.out.println("\n" + "Since you made the request today, this is your arrival date: " + String.valueOf(java.time.LocalDate.now()) + "\n");
            customer_arrival_date = String.valueOf(java.time.LocalDate.now());

            System.out.println("What Departure Date, use YYYY-MM-DD format");
            customer_departure_date = scanner3.nextLine();

            //Gets the price for the room
            partialQuery = ("SELECT price FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + customer_parent_brand + "' AND hotel.hotel_id = '" + customer_hotel_id + "' AND room.room_num = " + customer_room_num + "");
            rs = st.executeQuery(partialQuery);
            while (rs.next()){
                customer_price = rs.getString(1);
                }

			//gets the total price of their stay
			String total_price = String.valueOf(Float.parseFloat(customer_price)*Integer.parseInt(betweenDates(customer_arrival_date, customer_departure_date)));

            System.out.println("\n" + "Checking room avaliability");
            if (overlapsWithExisting() == false) {
                System.out.println("The room is avaliable, creating your renting now");
                foundRoom = true;
                st = db.createStatement();
                partialQuery = ("INSERT INTO renting VALUES (" + "(SELECT (COUNT(renting.renting_id) + 1) FROM renting)" + ", " + total_price + ", " + "false, '" + customer_arrival_date + "', '" + customer_departure_date + "', " + betweenDates(customer_arrival_date, customer_departure_date) + ", " + customer_room_num + ", " + customer_hotel_id + ", " + customer_sin + ")");
                st.executeUpdate(partialQuery);
                System.out.println("\n" + "renting has been accepted");
            }
            else {
                System.out.println("\n"+ "Room not avaliable please start again");
                System.out.println("Would you like to try again, type Y or N" + "\n");
                foundRoom = false;
                if (scanner3.nextLine().toUpperCase().equals("N")) {
                    quit = true;
                }
            }
        }
        else {
            System.out.println("\n" + "No rooms match the customer's preferences, please try again");
            System.out.println("Would you like to try again, type Y or N" + "\n");
            if (scanner3.nextLine().toUpperCase().equals("N")) {
                quit = true;
            }
            foundRoom = false;
        }
        }
		return true;
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
        } else {
        	System.out.println("Booking id does not exist. Try again.\n");
        	return;
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
        // to determine paid_for is false; assuming that customers pay at the end of their stay
    	paid_for = false;
    	  
    	// query to insert into renting 
    	st = db.createStatement(); 
        partialQuery = ("INSERT INTO renting VALUES (" + renting_id + "," + balance + "," + paid_for + ", '" + arrival_date + 
        		"','" + departure_date + "'," + duration_of_stay + "," + room_num + "," + hotel_id + "," + sin + ")");
        st.executeUpdate(partialQuery);
        System.out.println("\n--- Room renting was successful...");
        
        // query to remove booking that has already been converted to a renting 
    	st = db.createStatement(); 
        partialQuery = ("DELETE FROM booking WHERE booking_id = " + booking_id);
        st.executeUpdate(partialQuery);
    }
    
    public void updatePaidFor() throws SQLException {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Please enter the renting id that you would like to update: ");
    	renting_id = scanner.nextLine();
    	st = db.createStatement(); 
        partialQuery = ("UPDATE renting SET paid_for = true WHERE renting_id = " + renting_id);
        st.executeUpdate(partialQuery);
        System.out.println("\n--- Update successful...");
    }

    public void roomAvalibility() throws SQLException {
        //print available rooms
    	System.out.println("\nAvailable rooms in the hotel: ");
    	st = db.createStatement(); 
        partialQuery = ("SELECT room_num, price FROM hotel, room WHERE occupied = false AND hotel.hotel_id = room.hotel_id AND hotel.hotel_id = '" + employee_hotel_id + "'");
        ResultSet rq = st.executeQuery(partialQuery);
        printResultSet(rq);
        System.out.println("");
        //Prints unavailable rooms
        System.out.println("Unavailable rooms in the hotel: ");
        st = db.createStatement(); 
        partialQuery = ("SELECT  renting_id, renting.room_num, price FROM hotel, room, renting WHERE occupied = true AND hotel.hotel_id = room.hotel_id AND renting.hotel_id = hotel.hotel_id AND renting.room_num = room.room_num AND hotel.hotel_id = '" + employee_hotel_id + "'");
        ResultSet rw = st.executeQuery(partialQuery);
        printResultSet(rw);
        System.out.println("");
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


	//gets the info from employee needed to create a renting
	public void getEmployeeInfo () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT pbname, works_for.hotel_id FROM works_for, belongs_to WHERE works_for.hotel_id = belongs_to.hotel_id AND sin = " + employee_sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
			employee_parent_brand = rs.getString(1);
            employee_hotel_id = rs.getString(2);
            }
    }

	//Gets the amount of days between two dates
    public String betweenDates(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(date1, formatter);
        LocalDate localDate2 = LocalDate.parse(date2, formatter);
        return (String.valueOf(ChronoUnit.DAYS.between(localDate1, localDate2)));
    }

	//checks if booking dates chosen overlap with ones already in the system
    public boolean overlapsWithExisting() throws SQLException {
		String partialQuery2;
		String partialQuery3;
		String partialQuery4;
		String partialQuery5;
        st = db.createStatement(); 
        yu = db.createStatement();
		nr = db.createStatement();
		qw = db.createStatement();
        partialQuery2 = ("SELECT arrival_date FROM booking WHERE booking.room_num = '"+ customer_room_num+"' AND booking.hotel_id = '" + customer_hotel_id+"'");
        partialQuery3 = ("SELECT departure_date FROM booking WHERE booking.room_num = '"+ customer_room_num+"' AND booking.hotel_id = '" + customer_hotel_id+"'");
		partialQuery4 = ("SELECT arrival_date FROM renting WHERE renting.room_num = '"+ customer_room_num+"' AND renting.hotel_id = '" + customer_hotel_id+"'");
		partialQuery5 = ("SELECT departure_date FROM renting WHERE renting.room_num = '"+ customer_room_num+"' AND renting.hotel_id = '" + customer_hotel_id+"'");
        ResultSet rs = st.executeQuery(partialQuery2);
        ResultSet rd = yu.executeQuery(partialQuery3);
		ResultSet rq = nr.executeQuery(partialQuery4);
        ResultSet rw = qw.executeQuery(partialQuery5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrival = LocalDate.parse(customer_arrival_date, formatter);
        LocalDate departure = LocalDate.parse(customer_departure_date, formatter);

        LocalDate arrivalTemp_booking = LocalDate.parse(customer_arrival_date, formatter);
        LocalDate departureTemp_booking = LocalDate.parse(customer_departure_date, formatter);
		LocalDate arrivalTemp_renting = LocalDate.parse(customer_arrival_date, formatter);
        LocalDate departureTemp_renting = LocalDate.parse(customer_departure_date, formatter);
        
        while (rd.next()) {
            departureTemp_booking = LocalDate.parse(rd.getString(1), formatter);
            if(!(arrival.isAfter(departureTemp_booking))) {
                return true;
            }
        }
        while (rs.next()){
            arrivalTemp_booking = LocalDate.parse(rs.getString(1), formatter);
            if((departure.isBefore(arrivalTemp_booking))) {
                return true;
            }
        }

		while (rw.next()) {
            departureTemp_renting = LocalDate.parse(rw.getString(1), formatter);
            if(!(arrival.isAfter(departureTemp_renting))) {
                return true;
            }
        }
        while (rq.next()){
            arrivalTemp_renting = LocalDate.parse(rq.getString(1), formatter);
            if((departure.isBefore(arrivalTemp_renting))) {
                return true;
            }
        }
        return false;
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