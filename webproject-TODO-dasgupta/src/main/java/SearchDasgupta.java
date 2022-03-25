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

@WebServlet("/SearchDasgupta")
public class SearchDasgupta extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchDasgupta() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   String keyword = request.getParameter("keyword");
	   String keyword2 = request.getParameter("keyword2");
	   search(keyword, keyword2, response);
   }

   void search(String keyword1, String keyword2, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionDasgupta.getDBConnection(getServletContext());
         connection = DBConnectionDasgupta.connection;

         if (keyword1.isEmpty() && keyword2.isEmpty()) {
            String selectSQL = "SELECT * FROM myTableTODO";
            preparedStatement = connection.prepareStatement(selectSQL);
         }
         else if (keyword1.isEmpty()) {
             String selectSQL = "SELECT * FROM myTableTODO WHERE PHONE LIKE ?";
             String thePhoneNumber = "%" + keyword2 + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, thePhoneNumber);
         }
         else if (keyword2.isEmpty()) {
             String selectSQL = "SELECT * FROM myTableTODO WHERE EMAIL LIKE ?";
             String theEmail = "%" +keyword1 + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, theEmail);
         }
         else {
            String selectSQL = "SELECT * FROM myTableTODO WHERE EMAIL LIKE ? AND PHONE LIKE ?";
            String theEmail = "%" +keyword1 + "%";
            String thePhoneNumber = "%" +keyword2 + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, theEmail);
            preparedStatement.setString(2, thePhoneNumber);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String userName = rs.getString("myuser").trim();
            String email = rs.getString("email").trim();
            String phone = rs.getString("phone").trim();
            String address = rs.getString("address").trim();
            String todo = rs.getString("todo").trim();

           out.println("ID: " + id + ", ");
           out.println("User: " + userName + ", ");
           out.println("Email: " + email + ", ");
           out.println("Phone: " + phone + ", ");
           out.println("Address: " + address + ", ");
           out.println("TODO: " + todo + "<br>");
         }
         out.println("<a href=/webproject/search_dasgupta.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
