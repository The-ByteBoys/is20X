package servlets;

import enums.User;
import models.UserModel;
import tools.PasswordEncrypt;
import tools.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(name = "RegisterUser", urlPatterns = {"/userregistration"})
public class RegisterUser extends AbstractAppServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Register User");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

//        req.setContentType("text/html");
//        PrintWriter out = response.getWriter();

        out.print("<h1>Registering user...</h1>");

        String fname=req.getParameter("userFname");
        String lname=req.getParameter("userLname");
        String email=req.getParameter("userEmail");
        String pass=req.getParameter("userPass");

        UserModel newUser = new UserModel();

        newUser.set(User.FNAME, fname);
        newUser.set(User.LNAME, lname);
        newUser.set(User.EMAIL, email);
        newUser.set(User.PASSWORD, pass);

        try {
            int newID = PasswordEncrypt.opprettBruker(newUser);
            out.print("User added with id: "+newID);
        }
        catch(Exception e){
            e.printStackTrace();
            out.print("Exception! "+e);
        }


        /*try{
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            //    Connection con= DriverManager.getConnection(
            //            "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");

            //    PreparedStatement ps=con.prepareStatement(
            //            "insert into registeruser values(?,?)");

            ps.setString(1,n);
            ps.setString(2,p);

            int i=ps.executeUpdate();
            if(i>0)
                out.print("You are successfully registered...");

        }catch (Exception e2) {System.out.println(e2);}

        out.close();*/
    }

    /**
     * Returns a short description of the servlet.
     *
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

