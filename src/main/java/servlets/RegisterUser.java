package servlets;

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

@WebServlet(name = "RequestUserFromDB", urlPatterns = {"/userregistration"})
public class RegisterUser extends UserRepository {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

response.setContentType("text/html");
PrintWriter out = response.getWriter();

String n=request.getParameter("userName");
String p=request.getParameter("userPass");

try{
Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection con= DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:xe","system","oracle");

    PreparedStatement ps=con.prepareStatement(
            "insert into registeruser values(?,?)");

ps.setString(1,n);
ps.setString(2,p);

int i=ps.executeUpdate();
    if(i>0)
    out.print("You are successfully registered...");

}catch (Exception e2) {System.out.println(e2);}
    out.close(); }
}
