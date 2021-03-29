import java.sql.SQLException;
import java.util.Scanner;

public class connection {
		public static void main(String[] args) throws SQLException {

			String personType = "1";
			Scanner scanner = new Scanner(System.in);
			while (personType != "0"){ 
				
				System.out.println("\n\n" + "What Would You Like To Log In As? Type the number:" + "\n");
				System.out.println("1: Admin");
				System.out.println("2: Employee");
				System.out.println("3: Customer");
				System.out.println("0: Exit" + "\n");
				personType = scanner.nextLine();

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
					case ("0"):
						System.out.println("Thank you for using our service. Goodbye!" + "\n");
						System.exit(0);
						break;
					default:
						System.out.println("--- Please enter a valid number.");
					}

			}
		}
	}