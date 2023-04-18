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

@WebServlet(urlPatterns = {"/Transfer"})
public class Transfer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection
            Connection con = DatabaseConnection.initializeDatabase();
            //Create 2 sql statements
            //first statements is used to get the balance/status and check for valid id.. for the first account
            PreparedStatement st1 = con.prepareStatement("SELECT balance,status FROM bank_accounts WHERE account_id= ?");
            //second statements is used to get the status and check for valid id.. for the second account
            PreparedStatement st2 = con.prepareStatement("SELECT status FROM bank_accounts WHERE account_id= ?");   
            //store entered values from the Transfer.html file
            int id1 = Integer.valueOf(request.getParameter("id1"));
            int id2 = Integer.valueOf(request.getParameter("id2"));
            float amount = Float.valueOf(request.getParameter("amount"));
            st1.setInt(1, id1);
            st2.setInt(1, id2);
            //Execute queries.. 
            ResultSet rs1 = st1.executeQuery();
            ResultSet rs2 = st2.executeQuery();
            PrintWriter out = response.getWriter();
            
            
                      
            
            float balance= 0;    
            boolean status1=false;
            //if first account is valid
            if(rs1.next())            
            { 
                balance = rs1.getFloat("balance");
                status1 = rs1.getBoolean("status");
                //if second account is valid
                if(rs2.next())
                {
                    boolean status2  = rs2.getBoolean("status");
            
                    //if one of the accounts is inactive return error message
                    if(!status1 || !status2)
                    {    
                        out.println("<html><body><b>Inactive Account"+ "</b></body></html>"
                        + "</b><div><a href=\"index.html\">Return to Homepage </a></div></body></html>");
                    }
                    else
                    {   
                        //if current balance is less than the netered amount The transfer is not possible 
                        if(balance < amount)
                        {    
                            out.println("<html><body><b>Insufficient Balance !"+ "</b></body></html>"
                            + "</b><div><a href=\"index.html\">Return to Homepage </a></div></body></html>");
                        }     
                        else
                        {
                            //we create the update statements to complete the transfer
                            PreparedStatement st3 = con.prepareStatement("UPDATE bank_accounts SET balance = balance - ? WHERE account_id = ?");           
                            PreparedStatement st4 = con.prepareStatement("UPDATE bank_accounts SET balance = balance + ? WHERE account_id = ?");
                            st3.setFloat(1, amount);
                            st3.setInt(2, id1);
                            st4.setFloat(1, amount);
                            st4.setInt(2, id2);
                            st3.executeUpdate();
                            st4.executeUpdate();
                            out.println("<html><body><b>Successful Transfer!"+ "</b></body></html>"
                            + "</b><div><a href=\"index.html\">Return to Homepage </a></div></body></html>");
                            st3.close();
                            st4.close();
                        }
                    }
                }
            }
            else
            {
                out.println("<html><body><b>First account doesnt exist!"+ "</b></body></html>"
                + "</b><div><a href=\"index.html\">Return to Homepage </a></div></body></html>");
            }
            
            
  
            st1.close();
            st2.close();
            
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
        catch (NumberFormatException e) 
        {
            PrintWriter out = response.getWriter();
            out.println("<html><body><b>ID number must be an integer and amount a float!"
                    + "</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");

        }
        
    }

}
