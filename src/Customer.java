import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Customer {
    protected String sin;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String address;
    protected String date_of_registration;

    private String view_type;
    private String price;
    private String occupants;
    private String arrival_date;
    private String departure_date;
    private String hotel_id;
    private String parent_brand;
    private String room_num;
    private Boolean foundRoom;
    private Boolean quit;

    private String partialQuery2;
    private String partialQuery3;

    private String accountAlready;
    private String partialQuery;
    private String customerTask = "23";

    //important database variables
    private Connection db;
    private Statement st;
    private Statement yu;

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
          this.db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_b03_g30"
          ,"elu032", "Qw300114727oP!");
          //initialize variable that will hold the statement to be executed
          this.st = db.createStatement();
          this.yu = db.createStatement();
    } catch(SQLException ex) {
          System.err.println("Error get information from database");
          ex.printStackTrace();
      }
    }

    public void CustomerCase() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "Do you already have an Account, Y or N. If you type N, you will be asked to make a new account:" + "\n");
		accountAlready = scanner.nextLine();

		if (accountAlready.equals("Y")) {
            System.out.println("Enter Customer SIN to login: ");
            //sets the sin number as the one the user inputted, can retrieve data for this sin now
            this.sin = scanner.nextLine();
        }
	    else if (accountAlready.equals("N")) {
            System.out.println("Please Enter Your SIN number:" + "\n");
            this.sin = scanner.nextLine();
            System.out.println("Please Enter Your first name:" + "\n");
            this.firstName = scanner.nextLine();
            System.out.println("Please Enter Your middle name:" + "\n");
            this.middleName = scanner.nextLine();
            System.out.println("Please Enter Your last name:" + "\n");
            this.lastName = scanner.nextLine();
            System.out.println("Please Enter Your address:" + "\n");
            this.address = scanner.nextLine();
            //sets the value of to current time
            this.date_of_registration = String.valueOf(java.time.LocalDate.now());
            createCustomerAccount();
		}

        getandPrintCustomerInfo();
        System.out.println("\n" + "--- You are now logged in.");
        loggedInTask();
    }

    public void loggedInTask() throws SQLException {
        while (!customerTask.equals("0")) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n" + "What would you like to do?");
            System.out.println("1: View current Bookings");
            System.out.println("2: View current Rentings");
            System.out.println("3: Create A New Booking");
            System.out.println("4: Request For A New Renting");
            System.out.println("0: Exit\n");
    
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
                    printResultSet(currentBookings());
                    break;
                case ("4"):
                    requestForRenting();
                    printResultSet(currentRentings());
                    break;
                case ("0"):
                	System.out.println("\n" +"--- Logging out of customer...");
                	return;
                default:
                	System.out.println("\n" + "--- Please enter a valid number.");
                    break;
            }
        }
        
    }

    public void getandPrintCustomerInfo() throws SQLException {
        this.firstName = getFirstName();
            this.middleName = getMiddleName();
            this.lastName = getLastName();
            this.address = getAddress();
            this.date_of_registration = getDateOfRegistration();

            System.out.println("\n" + "Customer Info Is: ");
            System.out.println(firstName);
            System.out.println(middleName);
            System.out.println(lastName);
            System.out.println(address);
            System.out.println(date_of_registration);
    }

    //Will create a new customer account
    public void createCustomerAccount() throws SQLException {
        st = db.createStatement(); 
        partialQuery = ("INSERT INTO customer VALUES ("+ sin + ", '" + firstName + "', '" + middleName + "', '" + lastName + "', '" + address + "', '" + date_of_registration +"')" );
        st.executeUpdate(partialQuery);
    }

    //FOR BOOKING
    public void createBooking() throws SQLException {
        Scanner scanner2 = new Scanner(System.in);
        ResultSet rs;
        foundRoom = false;
        quit= false;
        while (foundRoom == false && quit == false) {
        System.out.println("Let's create a booking, we will now check for your preferences" + "\n");

        //gets the users specified brand
        System.out.println("Which brand would you like to stay with" + "\n");
        st = db.createStatement(); 
        partialQuery = ("SELECT pbname FROM parent_brand");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        parent_brand = scanner2.nextLine();

        //gives the user the avaliable locations of hotels from their specified brand
        System.out.println("\n"+"Which location do you prefer of the availiable choices. Please input the hotel_id of your choice" + "\n");
        st = db.createStatement(); 
        partialQuery = ("SELECT hotel_id, physical_address FROM parent_brand,hotel WHERE parent_brand.pbname = hotel.pbname AND parent_brand.pbname = '" + parent_brand + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        hotel_id = scanner2.nextLine();

        //gets the view the user wants
        System.out.println("\n"+"These are the avaliable views in your hotel, input you choice");
        partialQuery = ("SELECT DISTINCT view_type FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        view_type = scanner2.nextLine();

        //gets the amount of occupants
        System.out.println("\n" +"How many occupants? These are the maximum capacities avaliable in the rooms in the hotel");
        partialQuery = ("SELECT DISTINCT capacity FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "' AND room.view_type = '" + view_type + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        occupants = scanner2.nextLine();

        System.out.println("\n"+"These are the possible rooms that fit your preferences please pick one. Input the room number" + "\n");
        partialQuery = ("SELECT room_num, extension_capabilities, other_amenities FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "' AND room.view_type = '" + view_type+ "' AND room.capacity >= " + occupants);
        rs = st.executeQuery(partialQuery);
        if (rs.isBeforeFirst() == true) {
            printResultSet(rs);
            room_num = scanner2.nextLine();

            System.out.println("What Arrival Date, use YYYY-MM-DD format");
            arrival_date = scanner2.nextLine();

            System.out.println("What Departure Date, use YYYY-MM-DD format");
            departure_date = scanner2.nextLine();

            System.out.println("Checking room avaliability");
            if (overlapsWithExisting() == false) {
                System.out.println("The room is avaliable, creating your booking now");
                foundRoom = true;
                st = db.createStatement();
                partialQuery = ("INSERT INTO booking VALUES (" + "(SELECT (COUNT(booking.booking_id) + 1) FROM booking)" + ", '" + view_type+ "', " + occupants + ", '" + arrival_date + "', '" + departure_date + "', " + betweenDates(arrival_date, departure_date) + ", " + room_num + ", " + hotel_id + ", " + sin + ")");
                st.executeUpdate(partialQuery);
                System.out.println("Congrats your booking has been accepted" + "\n");
            }
            else {
                System.out.println("\n"+ "Room not avaliable please start again");
                System.out.println("Would you like to try again, type Y or N" + "\n");
                foundRoom = false;
                if (scanner2.nextLine().equals("N")) {
                    quit = true;
                }
            }
        }
        else {
            System.out.println("\n" + "No rooms are good enough for your preferences, try being less picky this time");
            System.out.println("Would you like to try again, type Y or N");
            if (scanner2.nextLine().equals("N")) {
                quit = true;
            }
            foundRoom = false;
        }
        }
    }


    // FOR RENTING
    public void requestForRenting() throws SQLException {
        Scanner scanner3 = new Scanner(System.in);
        ResultSet rs;
        foundRoom = false;
        quit= false;
        while (foundRoom == false && quit == false) {
        System.out.println("Let's create a renting, we will now check for your preferences" + "\n");

        //gets the users specified brand
        System.out.println("Which brand would you like to stay with" + "\n");
        st = db.createStatement(); 
        partialQuery = ("SELECT pbname FROM parent_brand");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        parent_brand = scanner3.nextLine();

        //gives the user the avaliable locations of hotels from their specified brand
        System.out.println("\n"+"Which location are you currently requesting the renting from? Please input the hotel_id of your choice" + "\n");
        st = db.createStatement(); 
        partialQuery = ("SELECT hotel_id, physical_address FROM parent_brand,hotel WHERE parent_brand.pbname = hotel.pbname AND parent_brand.pbname = '" + parent_brand + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        hotel_id = scanner3.nextLine();

        //gets the view the user wants
        System.out.println("\n"+"These are the avaliable views in your hotel, input you choice");
        partialQuery = ("SELECT DISTINCT view_type FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        view_type = scanner3.nextLine();

        //gets the amount of occupants
        System.out.println("\n" +"How many occupants? These are the maximum capacities avaliable in the rooms");
        partialQuery = ("SELECT DISTINCT capacity FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "' AND room.view_type = '" + view_type + "'");
        rs = st.executeQuery(partialQuery);
        printResultSet(rs);
        occupants = scanner3.nextLine();

        System.out.println("\n"+"These are the possible rooms that fit your preferences please pick one. Input the room number" + "\n");
        partialQuery = ("SELECT room_num, price, extension_capabilities, other_amenities FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "' AND room.view_type = '" + view_type+ "' AND room.capacity >= " + occupants);
        rs = st.executeQuery(partialQuery);
        if (rs.isBeforeFirst() == true) {
            printResultSet(rs);
            room_num = scanner3.nextLine();

            System.out.println("\n" + "Since you made the request today, this is your arrival date: " + String.valueOf(java.time.LocalDate.now()) + "\n");
            arrival_date = String.valueOf(java.time.LocalDate.now());

            System.out.println("What Departure Date, use YYYY-MM-DD format");
            departure_date = scanner3.nextLine();

            //Gets the price for the room
            partialQuery = ("SELECT price FROM parent_brand,hotel,room WHERE parent_brand.pbname = hotel.pbname AND hotel.hotel_id = room.hotel_id AND parent_brand.pbname = '"  + parent_brand + "' AND hotel.hotel_id = '" + hotel_id + "' AND room.room_num = " + room_num + "");
            rs = st.executeQuery(partialQuery);
            while (rs.next()){
                price = rs.getString(1);
                }

            System.out.println("\n" + "Checking room avaliability");
            if (overlapsWithExisting() == false) {
                System.out.println("The room is avaliable, creating your renting now");
                foundRoom = true;
                st = db.createStatement();
                partialQuery = ("INSERT INTO renting VALUES (" + "(SELECT (COUNT(renting.renting_id) + 1) FROM renting)" + ", " + price + ", " + "false, '" + arrival_date + "', '" + departure_date + "', " + betweenDates(arrival_date, departure_date) + ", " + room_num + ", " + hotel_id + ", " + sin + ")");
                st.executeUpdate(partialQuery);
                System.out.println("\n" + "Once an employee accepts your renting request you will have access to your room and in renting information paid_for will equal true");
                System.out.println("Please check again later for your renting information to update" + "\n");
            }
            else {
                System.out.println("\n"+ "Room not avaliable please start again");
                System.out.println("Would you like to try again, type Y or N" + "\n");
                foundRoom = false;
                if (scanner3.nextLine().equals("N")) {
                    quit = true;
                }
            }
        }
        else {
            System.out.println("\n" + "No rooms are good enough for your preferences, try being less picky this time");
            System.out.println("Would you like to try again, type Y or N" + "\n");
            if (scanner3.nextLine().equals("N")) {
                quit = true;
            }
            foundRoom = false;
        }
        }
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

    //Gets the amount of days between two dates
    public String betweenDates(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(date1, formatter);
        LocalDate localDate2 = LocalDate.parse(date2, formatter);
        //System.out.println(String.valueOf(ChronoUnit.DAYS.between(localDate1, localDate2)));
        return (String.valueOf(ChronoUnit.DAYS.between(localDate1, localDate2)));
    }

    //checks if booking dates chosen overlap with ones already in the system
    public boolean overlapsWithExisting() throws SQLException {
        st = db.createStatement(); 
        yu = db.createStatement();
        partialQuery2 = ("SELECT arrival_date FROM booking WHERE booking.room_num = '"+ room_num+"' AND booking.hotel_id = '" + hotel_id+"'");
        partialQuery3 = ("SELECT departure_date FROM booking WHERE booking.room_num = '"+ room_num+"' AND booking.hotel_id = '" + hotel_id+"'");
        ResultSet rs = st.executeQuery(partialQuery2);
        ResultSet rd = yu.executeQuery(partialQuery3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrival = LocalDate.parse(arrival_date, formatter);
        LocalDate departure = LocalDate.parse(departure_date, formatter);

        LocalDate arrivalTemp = LocalDate.parse(arrival_date, formatter);
        LocalDate departureTemp = LocalDate.parse(departure_date, formatter);
        
        while (rd.next()) {
            departureTemp = LocalDate.parse(rd.getString(1), formatter);
            if(!(arrival.isAfter(departureTemp))) {
                return true;
            }
        }
        while (rs.next()){
            arrivalTemp = LocalDate.parse(rs.getString(1), formatter);
            if((departure.isBefore(arrivalTemp))) {
                return true;
            }
        }
        return false;
    }


    //
    //Getters and Setters
    //

    public String getSin() throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT sin FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        return rs.getString(1);
    }

    public void setSin (BigInteger newSin) throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("UPDATE sin = " + newSin + " FROM customer WHERE sin = " + sin);
        st.executeQuery(partialQuery);
    }

    public String getFirstName () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT first_name FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
            firstName = rs.getString(1);
            }
        return firstName;
    }

    public String getMiddleName () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT middle_name FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
            middleName = rs.getString(1);
            }
        return middleName;
    }

    public String getLastName () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT last_name FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
            lastName = rs.getString(1);
            }
        return lastName;
    }

    public String getAddress () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT address FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
            address = rs.getString(1);
            }
        return address;
    }

    public String getDateOfRegistration () throws SQLException {
        //initialize variable that will hold the statement to be executed
		st = db.createStatement(); 
        partialQuery = ("SELECT date_of_registration FROM customer WHERE sin = " + sin);
        ResultSet rs = st.executeQuery(partialQuery);
        while (rs.next()){
            date_of_registration = rs.getString(1);
            }
        return date_of_registration;
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