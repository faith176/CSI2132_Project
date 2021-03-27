import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; 


//this class will check room avaliability. It is a helper for employee.
public class Room {
    private String start;
    private String end;
    private String room_num;
    private String hotel_id;
    
    private Connection db;
    private Statement st; 
  
    //get start and end dates of stay in specific room
    //compare them to booked and rented start/end dates in that room
        //order rooms by arrival date, make sure ends don't overlap

    public Room(String start, String end, String room_num, String hotel_id){
        this.start = start;
        this.end = end;
        this.room_num = room_num;
        this.hotel_id = hotel_id;
    }

    public boolean roomFree (String start, String end, String room_num, String hotel_id){
        //YYYY-MM-DD
        Room room = new Room(start, end, room_num, hotel_id);

        //check booking
            //SELECT * FROM booking WHERE room_num = this.room_num AND hotel_id = this.hotel_id ORDER BY arrival_date ASC;
            //make sure the arrival date of an existing entry doesn't overlap the departure date of the new one
            //select("SELECT * FROM booking WHERE room_num = " + this.room_num + " AND hotel_id = " + this.hotel_id + " ORDER BY arrival_date ASC;");

            //SELECT * FROM booking WHERE room_num = this.room_num AND hotel_id = this.hotel_id ORDER BY departure_date ASC;
            //make sure the departure date of an existing entry doesn't overlap the arrival date of the new one
    

        //check renting
            //SELECT * FROM renting WHERE room_num = this.room_num AND hotel_id = this.hotel_id ORDER BY arrival_date ASC;
            //make sure the arrival date of an existing entry doesn't overlap the departure date of the new one
            
            //SELECT * FROM renting WHERE room_num = this.room_num AND hotel_id = this.hotel_id ORDER BY departure_date ASC;
            //make sure the departure date of an existing entry doesn't overlap the arrival date of the new one


        //if no overlap
        return true;

    }  

    public ResultSet select(String query) throws SQLException {
        //initialize variable that will hold the statement to be executed
        st = db.createStatement(); 
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
}
