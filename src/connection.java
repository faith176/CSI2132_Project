import java.sql.SQLException;
import java.util.Scanner;

public class Connection { 
		public static void main(String[] args) throws SQLException {

			Scanner scanner = new Scanner(System.in);
			System.out.println("\n" + "What Would You Like To Log In As? Type the number" + "\n");
			System.out.println("1: Admin");
	    	System.out.println("2: Employee");
	    	System.out.println("3: Customer" + "\n");
			String personType = scanner.nextLine();

			switch(personType) {
				case ("1"):
					personType = "Admin";
					Admin admin = new Admin();
					admin.AdminCase();
					break;
				case ("2"):
					personType = "Employee";
					Employee employee = new Employee("");
					employee.EmployeeCase();
					break;
				case ("3"):
					personType = "Customer";
					Customer customer = new Customer("");
					customer.CustomerCase();
					break;
					}
		}
	}