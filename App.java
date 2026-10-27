import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:exam.db";
        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            System.out.println("Database Connected Successfully!");

    
            stmt.execute("CREATE TABLE IF NOT EXISTS feedback (" +
                         "name TEXT, service TEXT, rating DOUBLE, comments TEXT)");

            
            System.out.print("Enter number of feedbacks: ");
            int n = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO feedback VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 0; i < n; i++) {
                System.out.println("\nFeedback " + (i + 1) + ":");
                System.out.print("Customer Name: ");
                String name = sc.nextLine();
                System.out.print("Service Name: ");
                String service = sc.nextLine();
                System.out.print("Rating (1 to 5): ");
                double rating = sc.nextDouble();
                sc.nextLine();
                System.out.print("Comments: ");
                String comments = sc.nextLine();

                ps.setString(1, name);
                ps.setString(2, service);
                ps.setDouble(3, rating);
                ps.setString(4, comments);
                ps.executeUpdate();
            }
            System.out.println("All feedbacks inserted successfully!\n");

            System.out.println("--- Average Rating Per Service ---");
            ResultSet rs1 = stmt.executeQuery("SELECT service, AVG(rating) FROM feedback GROUP BY service");
            while (rs1.next()) {
                System.out.println("Service: " + rs1.getString(1) + " | Average: " + rs1.getDouble(2));
            }
            System.out.println("\n--- Feedbacks Rated Below 3 ---");
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM feedback WHERE rating < 3");
            while (rs2.next()) {
                System.out.println(rs2.getString(1) + " | " + 
                                   rs2.getString(2) + " | " + 
                                   rs2.getDouble(3) + " | " + 
                                   rs2.getString(4));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
