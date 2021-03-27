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
    //get start and end dates of stay in specific room
    //compare them to booked and rented start/end dates in that room
        //order rooms by arrival date, make sure ends don't overlap

    public boolean roomFree (String start, String end, String room_num, String hotel_id){
        //YYYY-MM-DD
        
        return true;

    }  
}
