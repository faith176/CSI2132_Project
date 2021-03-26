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
  private String news;
  
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
			,"oades097", "University917");
			//initialize variable that will hold the statement to be executed
			this.st = db.createStatement();
	  } catch(SQLException ex) {
			System.err.println("Error get information from database");
			ex.printStackTrace();
		}
  }
  
  public void AdminCase() throws SQLException {
	  
	  Scanner scanner = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("\n" + "Welcome Admin! What would you like to do? Type the corresponding number" + "\n");
	    System.out.println("1: Select");
	    System.out.println("2: Insert");
	    System.out.println("3: Select");
	    AdminTask = scanner.nextLine();
	    
	
	    switch(AdminTask) {
	    	case ("1"):
	    		AdminTask = "Select";
	    		System.out.println("Task selected is: " + AdminTask + "\n");  // Output user input
	    		
	    		System.out.println("Input column(s) to select: ");  // Output user input
	    		column = scanner.nextLine();
	    		
	    		System.out.println("Input table(s) to select: ");  // Output user input
	    		table = scanner.nextLine();
	    		
	    		System.out.println("Would you like a where condition, Type Y or N: ");  // Output user input
	    		whereCondition = scanner.nextLine();
	    		
	    		if (whereCondition == "Y") {
	    			System.out.println("Input where condition: ");  // Output user input
	    			whereCondition = scanner.nextLine();
	    		}
	    		else {
	    			whereCondition = "";
	    		}
	    		
	    		//calls on select function
	    		printResultSet(select());
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
			ResultSet rs = st.executeQuery(partialQuery);
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
			ResultSet rs = st.executeQuery(partialQuery);
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
