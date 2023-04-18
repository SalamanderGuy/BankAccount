import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Efthimeros
 */
public class Withdraw extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Initialize the database 
            //Call the function initializeDatabase() from the DatabaseConnection.java to get a connection
            Connection con = DatabaseConnection.initializeDatabase();
            
            //first we make a simple query (select balance..) to check the entered id and get the balance 
            PreparedStatement st1 = con.prepareStatement("SELECT balance FROM bank_accounts WHERE account_id = ?");
            //store id
            int id = Integer.valueOf(request.getParameter("id"));
            st1.setInt(1, id);
            //execute the query
            ResultSet rs = st1.executeQuery(); //If id is wrong the catch method will be triggered..
            PrintWriter out = response.getWriter();
            
            //if a valid id was entered the rs.next()== true
            if(rs.next())
            {
                //storing current account balance (id is already stored..)
                float current_balance=rs.getFloat("balance");  
                //update statement
                try (PreparedStatement st2 = con.prepareStatement("UPDATE bank_accounts SET balance = balance - ? WHERE account_id = ? AND status = 1")) {
                    //store withdraw amount from the Withdraw.html file
                    float withdraw_amount = Float.valueOf(request.getParameter("balance"));
                    //if account balance >= withdraw amount
                    if(withdraw_amount <= current_balance)
                    {
                        //set values
                        st2.setFloat(1, withdraw_amount);
                        st2.setInt(2, id);
                        /*we make the executeUpdate() inside an if() because sql will return an empty string
                          if status = 0 which is not an error you can handle with a  try/catch */
                        if(st2.executeUpdate()==1)
                        {
                            out.println("<html><body><b>Succesful Withdrawal !"+"</b></body></html>"
                            +"</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");
                            //close connections
                            st1.close();                   
                            st2.close();
                            con.close();
                        }
                        else
                        {
                            out.println("<html><body><b>Deactivated account !"+"</b></body></html>"
                            +"</b><div><a href=\"index.html\">Return to Homepage</a></div></body></html>");

                            st1.close();                   
                            
                            con.close();
                        }
                    }
                    else 
                    {
                        out.println("<html><body><b>Insufficient Balance !"+"</b></body></html>"
                        + "</b><div><a href=\"Withdraw.html\">Return </a></div></body></html>");
                        
                    }
                    st1.close();
                    con.close(); 
                }
                  
            }
            else //entered id is not valid
            {
                out.println("<html><body><b>Incorrect ID !"
                            + "<div><a href=\"Deposit.html\">Try again with an existing bank account</a></div>"
                            +"</b></body></html>");
                st1.close();
                con.close();
            }
               
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
            out.println("<html><body><b>ID number must be an integer!"
                    + "</b><div><a href=\"Withdraw.html\">Return </a></div></body></html>");

        }
        
    }
}
