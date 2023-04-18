import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowAllRecords extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Records";   
      String docType =
         "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      
      out.println(docType +
         "<html>\n" +
         "<head><title>" + title + "</title></head>\n" +
         "<body bgcolor = \"#f0f0f0\">\n" +
         "<h1 align = \"center\">" + title + "</h1>\n");
      Connection conn =null;
      Statement stmt = null;
      try {
         
         
         conn = DatabaseConnection.initializeDatabase();
         stmt = conn.createStatement();
         String sql;

         sql = "SELECT * FROM bank_accounts";
         ResultSet rs = stmt.executeQuery(sql);

         // Extract data from result set
         while(rs.next()){
            //Retrieve by column name
            int account_id  = rs.getInt("account_id");
            float balance = rs.getFloat("balance");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            String address = rs.getString("address");
            boolean status_flag  = rs.getBoolean("status");
            String status;
            if(status_flag)status="Active Account";else status="Not Active Account";

            //Display values
            out.print("Account_id: " + account_id+ "<br>" );
            out.print("First Name: " + firstname+ "<br>");
            out.println("Last Name: " + lastname+ "<br>");
            out.print("Balance: " + balance+ "<br>");
            out.println("Address: " + address+ "<br>");
            out.println("Status: " + status + "<br>---------------------------------<br><br>");

         }
         out.println("</body></html>");
         // Clean-up environmentout.
         rs.close();
         stmt.close();
         conn.close();
      } catch(SQLException se) {
         //Handle errors for JDBC
         se.printStackTrace();
      } catch(ClassNotFoundException e) {
         //Handle errors for Class.forName
         e.printStackTrace();
      } finally {
         //finally block used to close resources
         try {
            if(stmt!=null)
               stmt.close();
         } catch(SQLException se2) {
         } // nothing we can do
         try {
            if(conn!=null)
            conn.close();
         } catch(SQLException se) {
            se.printStackTrace();
         } //end finally try
      } //end try
    }


}
