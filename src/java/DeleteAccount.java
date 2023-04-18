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

@WebServlet(urlPatterns = {"/DeleteAccount"})
public class DeleteAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection 
            Connection con = DatabaseConnection.initializeDatabase();           
            PreparedStatement st = con.prepareStatement("DELETE FROM bank_accounts WHERE account_id = ?");
            int id = Integer.valueOf(request.getParameter("id"));
            PrintWriter out = response.getWriter();
            st.setInt(1, id);
            
            
            //if the entered id is not valid the command st.executeUpdate() returns zero value
            if(st.executeUpdate()==0)
                response.sendRedirect("UnknownAccountError.html");
            else
            {    
                out.println("<html><body><b>Successfully Deleted"+ "</b></body></html>"
                + "</b><div><a href=\"index.html\">Return to Homepage </a></div></body></html>");
            }
            st.close();
            con.close();
        }
        catch (SQLException ex) {
           PrintWriter out = response.getWriter();
           if(ex.getErrorCode() == 1062) 
              out.println("<html><body><b>This ID already exists!"
              + "</b><div><a href=\"DeleteAccount.html\">Return </a></div></body></html>");
           else
               out.println("SQL exception caught: " +ex.getMessage());
        }
        catch (ClassNotFoundException ex)
        {
            PrintWriter out = response.getWriter();
            out.println("Class exception caught: " +ex.getMessage());
        }
        //catch when input for id is not an integer
        catch (NumberFormatException e) 
        {
            PrintWriter out = response.getWriter();
            out.println("<html><body><b>ID number must be an integer!"
                    + "</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");

        }
        
    }

}
