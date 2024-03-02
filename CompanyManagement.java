import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

//Classes created 
/*
    CompanyManagement - The Main Class where Execution begins.
    Login Class which is needed to Login to the Application.
    AccountDetails - to Display the Account User Details.
    Performance - Displays the details of Performance of each user.
    EmployeeInformationAdmin - The person who is admin has overall control of the overall inoformation stored.

 */
        
/*
    The class CompanyManagement contains the main class, this is where the execution of the program begins
    The Database connection is created and then used to initialize objects of different classes created which
    use the Database connection 
 */
public class CompanyManagement{
    public static int DeptID = 0;
    public static void main(String[] args) {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/management";
            String user = "root";
            String password = "Kalpana1!";
            Connection connection = DriverManager.getConnection(url, user, password);
            if(connection == null){
                System.out.println("Connection not possible at the momment, Please try again later");
            }
            else{
                System.out.println("Welcome and Login");
                int EMPNO = 0;
                Login L = new Login(connection);
                EMPNO = L.LoginFunction();
                DeptID = L.getDeptID(EMPNO);
                if(DeptID == 105){
                    System.out.println("Main Menu");
                    System.out.println("To Logout Press 1 ");
                    System.out.println("Employee Information - 2");
                    System.out.println("Employee Finances - 3");
                    System.out.println("Add Employee Information - 4");
                    System.out.println("Enter your choice : ");
                    Scanner S = new Scanner(System.in);
                    int Answer = S.nextInt();
                    switch(Answer){
                        case 1:
                            //exit();
                            break;
                        case 2 :
                            Admin E = new Admin(connection);
                            E.Display();
                            break;
                        case 3 :
                            System.out.println("In the Making");
                            // Create Employee Finances class.
                            break;
                        case 4 :
                            Admin A = new Admin(connection);
                            A.AddDetails();
                            break;
                        default :
                            System.out.println("Enter a Valid Response Please");
                    }

                }
                else{
                    System.out.println("To Display Account Details press 1");
                    System.out.println("To Display your performance press 2");
                    Scanner S = new Scanner(System.in);
                    int Answer = S.nextInt();
                    switch(Answer){
                        case 1 : 
                            Employee E = new Employee(connection, EMPNO);
                            E.ShowProfileDetails();
                            break;
                        case 2 :
                            Employee E2 = new Employee(connection, EMPNO);
                            E2.PerformanceEval();
                            break;
                        default:
                            System.out.println("Please enter a Valid Response");
                        }
                }
                }
                // Close resources
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// The Login mechanism is made into a class because, while logging in the user is verified whether the user is an
// Admin or not, as it is done during the Login process, both the login() and getDeptID to verify the user are
// Kept in one class.
class Login{
    private String Username;
    private String Password;
    private Connection connection;
    public static int DeptID = 0;

    Login(Connection connection){
        this.connection = connection;
        this.Username = "";
        this.Password = "";
    }
    
    public int LoginFunction(){
        int count = 0;
        int EMPNO = 0;// Saving the Employee number to use as a reference to access other details.
        System.out.print("Username: ");
        Scanner S = new Scanner(System.in);
        String Username2 = S.nextLine();
        String query = "SELECT COUNT(*) FROM LOGINCRED WHERE Username =  ?";
        try{
            PreparedStatement St = connection.prepareStatement(query);
            St.setString(1, Username2);
            ResultSet Rs = St.executeQuery();
            while(Rs.next()){
                count = Rs.getInt(1);
            }
            if(count == 1){
                System.out.print("Enter Password: ");
                String Password = S.nextLine();
                String query2 = "SELECT Password FROM LOGINCRED WHERE Username = ?";
                PreparedStatement St2 = connection.prepareStatement(query2);
                St2.setString(1, Username2);
                Rs = St2.executeQuery();
                while(Rs.next()){
                    String PW = Rs.getString(1);
                    System.out.println(Rs.getString(1));
                    if(Password.contentEquals(PW)){
                        System.out.println("Login Successful");
                        System.out.println();
                        String query3 = "SELECT EMPNO FROM LOGINCRED WHERE Username = ?";
                        PreparedStatement St3 = connection.prepareStatement(query3);
                        St3.setString(1, Username2);
                        ResultSet Rs3 = St3.executeQuery();
                        while(Rs3.next()){
                            EMPNO = Rs3.getInt(1);
                        }
                        Rs3.close();
                        St3.close();
                    }
                    else{
                        System.out.println("This is where things are going wrong");
                    }
                }
                Rs.close();
                St.close();
                St2.close();
            }
            else{
                System.out.println("Account Not Found");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return EMPNO;
    }
    public int getDeptID(int EMPNO){
        System.out.println("Your Employee Number is " + EMPNO);
        String query5 = "SELECT WORKDEPT FROM EMPLOYEE WHERE EMPNO = ?";
        try{
            PreparedStatement St4 = connection.prepareStatement(query5);
            St4.setInt(1, EMPNO);
            ResultSet Rs5 = St4.executeQuery();
            while(Rs5.next()){
                DeptID = Rs5.getInt(1); 
            }
            St4.close();
            Rs5.close();
        }catch(Exception E){
            System.out.println(E.getMessage());
        }
        return DeptID;
    }
}

class Employee{
    private int EMPNO;
    private Connection connection = null;
    public static int DeptID = 0;

    Employee(Connection connection, int EMPNO){
        this.connection = connection;
        this.EMPNO = EMPNO;
    }

    public void ShowProfileDetails(){
        try{
            String query4 = "SELECT * FROM EMPLOYEE WHERE EMPNO = ?";
            PreparedStatement St = connection.prepareStatement(query4);
            St.setInt(1, EMPNO);
            ResultSet Rs = St.executeQuery();
            while(Rs.next()){
                System.out.println("Employee Number : " + Rs.getInt(1));
                System.out.println("Full Name : " + Rs.getString(2) + " " + Rs.getString(3) + " " + Rs.getString(4));
                System.out.println("Department ID : " + Rs.getInt(5));
                System.out.println("Gender : " + Rs.getString(12));
                System.out.println("Date of Birth : " + Rs.getDate(13));
            }
            DeptID = Rs.getInt(5);
            St.close();
            Rs.close();
        }
        catch(Exception E){
            System.out.println(E.getMessage());
        }

    }

    public void PerformanceEval(){
        try{
            String query5 = "SELECT * FROM EPERFORMANCE WHERE EMPNO = ?";
            PreparedStatement St = connection.prepareStatement(query5);
            St.setInt(1, EMPNO);
            ResultSet Rs = St.executeQuery();
            while(Rs.next()){
                System.out.println("Your Report Submission Score : " + Rs.getString(1));
                System.out.println("Your Productivity Score : " + Rs.getString(2));
            }
            Rs.close();
            St.close();
        }
        catch(Exception E){
            System.out.println(E.getMessage());
        }
    }
}



class Admin{
    private Connection connection = null;
    Admin(Connection connection){
        this.connection = connection;
    }
    Scanner S = new Scanner(System.in);
    
    public void Display(){
        try{
            String query = "SELECT * FROM EMPLOYEE";
            PreparedStatement St = connection.prepareStatement(query); 
            ResultSet Rs = St.executeQuery();
            while(Rs.next()){
                System.out.println(Rs.getInt(1) + " " + Rs.getString(2) + " " + Rs.getString(3) + " " +
                Rs.getString(4) + " " + Rs.getInt(5) + " " + Rs.getString(9) + " " + Rs.getString(10) + " " + 
                Rs.getString(11) + " " + Rs.getString(12) + " " + Rs.getDate(13));
            }
            St.close();
            Rs.close();
        }catch(Exception E){
            E.getMessage();
        }
    }
    public void AddDetails(){
        try{
            String query = "INSERT INTO EMPLOYEE (EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT, PHONENO, ADDRESS1, ADDRESS2, CITY, STATES, COUNTRY, SEX, BIRTHDATE)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement St = connection.prepareStatement(query);
            System.out.println("Adding New Details");
            System.out.print("Enter Employee ID: ");
            int Empno = S.nextInt();
            System.out.print("Enter First Name: ");
            String FirstName = S.next();
            System.out.print("Enter Middle Name Initial: ");
            String Midinitial = S.next();
            System.out.print("Enter Last Name: ");
            String Lastname = S.next();
            System.out.print("Enter Working Dept ID: ");
            int WorkDept = S.nextInt();
            System.out.print("Enter Phone Number:");
            int PhoneNo = S.nextInt();
            System.out.print("Enter Address 1 and 2, 'Address 1 is mandatory, But Address 2 can be left empty':");
            String Address1 = S.nextLine();
            String Address2 = S.nextLine();
            System.out.print("Enter Your City: ");
            String City = S.nextLine();
            System.out.print("Enter your State: ");
            String State = S.nextLine();
            System.out.print("Enter Country");
            String Country = S.nextLine();
            System.out.print("Enter your Gender");
            String Gender = S.nextLine();
            System.out.print("Enter Date of Birth in yyyy-MM-dd format:");
            // String date2 = S.nextLine();
            //SimpleDateFormat format = new  SimpleDateFormat("yyyy-mm-dd");
            //java.sql.Date d = date
            Scanner scanner = new Scanner(System.in);
            //System.out.println("Enter the date in yyyy-MM-dd format:");
            String dateString = scanner.nextLine();
        
        
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
        
            // String sql = "INSERT INTO mytable (date_column) VALUES (?)";
            //PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            // Execute the SQL statement
            //int rowsInserted = preparedStatement.executeUpdate();
            St.setInt(1,Empno);
            St.setString(2, FirstName);
            St.setString(3, Midinitial);
            St.setString(4, Lastname);
            St.setInt(5, WorkDept);
            St.setInt(6, PhoneNo);
            St.setString(7, Address1);
            St.setString(8, Address2);
            St.setString(9, City);
            St.setString(10, State);
            St.setString(11, Country);
            St.setString(12, Gender);
            St.setDate(13, sqlDate);

            int InsertionUpdation = St.executeUpdate();
            if(InsertionUpdation == 1){
                System.out.println("The Data has been inserted successfully");
            }
            else{
                System.out.println("Data could not be Inserted");
            }
        }catch(Exception E){
            System.out.println(E.getMessage());
        }
    }
}