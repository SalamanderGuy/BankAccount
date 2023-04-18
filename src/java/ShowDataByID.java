//ShowDataByID
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ShowDataByID"})
public class ShowDataByID extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //response with an html page 
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
      
      
      try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection  
            Connection con = DatabaseConnection.initializeDatabase();
            //Create a sql statement 
            PreparedStatement st = con.prepareStatement("SELECT * FROM bank_accounts WHERE account_id=? ");
            //store id from the ShowDataByID.html file
            st.setInt(1, Integer.valueOf(request.getParameter("id")));
            
                
            ResultSet rs = st.executeQuery();
            
            //handle error if id doesnt exist 
             if(!rs.isBeforeFirst()) // if cursor is at the default position
             {  
                response.sendRedirect("UnknownAccountError.html");
             }
             else
             {
                // Position the cursor   
                while (rs.next())
                {   
                    //store values 
                    int account_id  = rs.getInt("account_id");
                    float balance = rs.getFloat("balance");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String address = rs.getString("address");
                    boolean status_flag  = rs.getBoolean("status");
                    String status;
                    //Print appropriate message for account status
                    if(status_flag)status="Active Account";else status="Not Active Account";

                    //Display values
                    out.print("Account_id: " + account_id+ "<br>" );
                    out.print("First Name: " + firstname+ "<br>");
                    out.println("Last Name: " + lastname+ "<br>");
                    out.print("Balance: " + balance+ "<br>");
                    out.println("Address: " + address+ "<br>");
                    out.println("Status: " + status + "<br>---------------------------------<br><br>");
                }
             }
            // Close all the connections  
            st.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
                    out.println("SQLException caught: " +ex.getMessage());


        }
    }

}
