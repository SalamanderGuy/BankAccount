import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/CreateAccount"})
public class CreateAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection
            Connection con = DatabaseConnection.initializeDatabase();
            //Create a new SQL statement
            PreparedStatement st = con.prepareStatement("INSERT INTO bank_accounts(account_id,firstname, lastname,address,balance,status) VALUES(?, ?, ?, ?, 0, 1)");
            //store all the values
            //request.getParameter("name") returns the the entered value from the CreateAccount.html file
            int id = Integer.valueOf(request.getParameter("id"));
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String address = request.getParameter("address");    
            
            PrintWriter out = response.getWriter();
            
            //store an empty string
            String empty= "";  
            //Checking for empty strings.
            if(empty.equals(firstname) || empty.equals(lastname) || empty.equals(address) )
            {
                out.println("<html><body><b>You must fill all the values!"
                  + "</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");
            }
            else
            {
                // sets the data to st pointer
                st.setInt(1, id);
                st.setString(2, firstname);
                st.setString(3, lastname);
                st.setString(4, address);

                st.executeUpdate();
                out.println("<html><body><b>Successfully Created"
                        + "</b></body></html>");
            }
            //close connections
            st.close();
            con.close();
        }
        catch (SQLException ex) 
        {
           PrintWriter out = response.getWriter();
           //this is the error code when an already existing primary key is inserted
           if(ex.getErrorCode() == 1062) 
              out.println("<html><body><b>This ID already exists!"
              + "</b><div><a href=\"CreateAccount.html\">Return </a></div></body></html>");
           else
               out.println("SQL exception caught: " +ex.getMessage());
        }
        catch (ClassNotFoundException ex)
        {
            PrintWriter out = response.getWriter();
            out.println("Class exception caught: " +ex.getMessage());
        }
        //catch when input for id is not an integer
        catch (NumberFormatException e) //This exception is triggered when a wrong type of data is entered
        {
            PrintWriter out = response.getWriter();
            out.println("<html><body><b>ID number must be an integer!"
                    + "</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");

        }
        
    }

}
