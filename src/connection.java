import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class connection {
		public static void main(String[] args) throws SQLException {

			Scanner scanner = new Scanner(System.in); 

			System.out.println("\n" + "What Would You Like To Log In As? Type the number" + "\n");
			System.out.println("1: Admin");
	    	System.out.println("2: Employee");
	    	System.out.println("3: Customer");
			String personType = scanner.nextLine();

			switch(personType) {
				case ("1"):
					personType = "Admin";
					Admin admin = new Admin();
					admin.AdminCase();
				case ("2"):
					personType = "Employee";
					Employee employee = new Employee();
					employee.EmployeeCase();
					}
			}
		}



