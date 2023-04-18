import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Efthimeros
 */
public class Deposit extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection
            Connection con = DatabaseConnection.initializeDatabase();
            //Create SQL statement
            PreparedStatement st = con.prepareStatement("UPDATE bank_accounts SET balance = balance + ? WHERE account_id = ? AND status = 1");
            //store all the values
            //request.getParameter("name") returns the the entered value from the Deposit.html file
            float balance = Float.valueOf(request.getParameter("balance"));
            int id = Integer.valueOf(request.getParameter("id"));
            st.setFloat(1, balance);
            st.setInt(2, id);
            
            
            PrintWriter out = response.getWriter();
                
                /*we make the executeUpdate() inside an if() because sql will return an empty string
                if id is wrong or status=0 which is not an error you can handle with a  try/catch */
                
                if(st.executeUpdate()== 1)
                    out.println("<html><body><b>Successful Deposit!" + "</b></body></html>");
                else //if the executeUpdate() returns 0 (empty line) line print error message
                    out.println("<html><body><b>Deposit Error! Incorrect ID or the Account is Deactivated!"+
                        "<div><a href=\"Deposit.html\">Try again with an existing bank account</a></div></body></html>");
                
            //close all connections
            st.close();
            con.close();
        }
        catch (SQLException ex) {
           PrintWriter out = response.getWriter();
           out.println("SQL exception caught: " +ex.getMessage());
        }
        catch (ClassNotFoundException ex)
        {
            PrintWriter out = response.getWriter();
            out.println("Class exception caught: " +ex.getMessage());
        }
        //catch when input for id is not an integer
        catch (NumberFormatException e)  //This exception is triggered when a wrong type of data is entered
        {
            PrintWriter out = response.getWriter();
            out.println("<html><body><b>ID number must be an integer!"
                    + "</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");
        }
        
    }
}
