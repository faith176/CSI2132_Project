import java.sql.SQLException;
import java.util.Scanner;

public class connection {
		public static void main(String[] args) throws SQLException {
			String personType = "1";
			String username ="";
			String password = "";
			Scanner scanner = new Scanner(System.in);

			//get user's credencials
			System.out.println("\n Please input database credencials for Uottawa PgAdmin4: ");
				System.out.println("Username: ");
				username= scanner.nextLine();
				System.out.println("Password: ");
				password = scanner.nextLine();

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
						Admin admin = new Admin(username, password);
						admin.AdminCase();
						break;
					case ("2"):
						personType = "Employee";
						Employee employee = new Employee(username, password);
						employee.EmployeeCase();
						break;
					case ("3"):
						personType = "Customer";
						Customer customer = new Customer(username, password);
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