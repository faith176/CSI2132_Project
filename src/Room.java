import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 


//this class will check room openness. It is a helper for employee.
public class Room {
    private int[] start;
    private int[] end;
    private String room_num;
    private String hotel_id;
    private int[] arrival;
    private int[] departure;
    
    private Connection db;
    private Statement st; 
  
    //get start and end dates of stay in specific room
    //compare them to booked and rented start/end dates in that room
        //order rooms by arrival date, make sure ends don't overlap

    public Room(String start, String end, String room_num, String hotel_id){
        this.start = dateToInt(start);
        this.end = dateToInt(end);
        this.room_num = room_num;
        this.hotel_id = hotel_id;
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
			  ,"elu032", "Qw300114727oP!");
			  //initialize variable that will hold the statement to be executed
			  this.st = db.createStatement();
		} catch(SQLException ex) {
			  System.err.println("Error get information from database");
			  ex.printStackTrace();
		  }
    }

    public boolean roomFree (String start, String end, String room_num, String hotel_id){
        //YYYY-MM-DD
        Room room = new Room(start, end, room_num, hotel_id);
        try{
        //check booking
            ResultSet rsb = select("SELECT * FROM booking WHERE room_num = " + this.room_num + " AND hotel_id = " + this.hotel_id + " ORDER BY arrival_date ASC;");
            while (rsb.next()) {
                arrival = dateToInt(rsb.getString(4));	//int[] {yyyy, mm, dd}
                departure = dateToInt(rsb.getString(5));
                if (!room.compare(arrival, departure)) {	//compare to the dates in question
                	return false;
                }
            }

        //check renting
            ResultSet rsr = select("SELECT * FROM renting WHERE room_num = " + this.room_num + " AND hotel_id = " + this.hotel_id + " ORDER BY arrival_date ASC;");
            while (rsr.next()) {
                arrival = dateToInt(rsr.getString(4));	//int[] {yyyy, mm, dd}
                departure = dateToInt(rsr.getString(5));
                if (!room.compare(arrival, departure)) {	//compare to the dates in question
                	return false;
                }
             }

        } catch (SQLException e){
            System.out.println("--- Error get information from database");
        }
        //if no overlap
        return true;

    }  

    public ResultSet select(String query) throws SQLException {
        //initialize variable that will hold the statement to be executed
        st = db.createStatement(); 
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
    
    public static int[] dateToInt(String date) {
    	String[] tmp1 = date.split("-");	
    	int day = Integer.parseInt(tmp1[2]);
    	int mon = Integer.parseInt(tmp1[1]);
    	int yea = Integer.parseInt(tmp1[0]);
    	int[] ans = new int[] {yea, mon, day};
    	return ans;
    }
    
    
    public boolean compare (int[] arrival, int[] departure) {
    	if (arrival[0] != departure[0]) {	//when the years are different
        	if ((arrival[0] + 2) <= departure[0] && this.start[0] > arrival[0] && this.start[0] < departure[0]) {
        		return false;
        	}
        	if ((arrival[0] + 2) <= departure[0] && this.end[0] > arrival[0] && this.end[0] < departure[0]) {
        		return false;
        	}
        	if (arrival[0] == this.start[0] && arrival[1] < this.start[1]) {
        		return false;
        	}
        	if (arrival[0] == this.end[0] && arrival[1] < this.end[1]) {
        		return false;
        	}
        	if (departure[0] == this.start[0] && departure[1] < this.start[1]) {
        		return false;
        	}
        	if (departure[0] == this.end[0] && departure[1] < this.end[1]) {
        		return false;
        	} else {
        		return true;
        	}
        }
        if (arrival[0] == departure[0]) {	//when the years are the same
        	if ((arrival[1] + 2) <= departure[1] && this.start[1] > arrival[1] && this.start[1] < departure[1]) {
        		return false;
        	}
        	if ((arrival[1] + 2) <= departure[1] && this.end[1] > arrival[1] && this.end[1] < departure[1]) {
        		return false;
        	}
        	//edge days
        	if (arrival[1] != departure[1]) {
        		if (arrival[1] == this.start[1] && arrival[2] < this.start[2]) {
            		return false;
            	}
            	if (arrival[1] == this.end[1] && arrival[2] < this.end[2]) {
            		return false;
            	}
            	if (departure[1] == this.start[1] && departure[2] < this.start[2]) {
            		return false;
            	}
            	if (departure[1] == this.end[1] && departure[2] < this.end[2]) {
            		return false;
            	} else {
            		return true;
            	}
        	}
        	if (arrival[1] == departure[1]) {
        		if (arrival[2] < this.start[2] && departure[2] > this.start[2]) {
        			return false;
        		}
        		if (arrival[2] < this.end[2] && departure[2] > this.end[2]) {
        			return false;
        		}
        	}
        }
        
        return true; //if we didn't catch anything, it's probably fine :D
    }
}
