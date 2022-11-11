package pl.unak7.jwtee.sample;

import com.auth0.jwt.algorithms.Algorithm;
import pl.unak7.jwtee.JWTSessionManager;
import pl.unak7.jwtee.JWTSessionManagerBean;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "ListServlet", value = "/ListServlet")
public class ListServlet extends HttpServlet {

    @Inject
    JWTSessionManager sessionManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        request.getSession();

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Dictionary</h1>");
        out.println("<a href=\"index.jsp\">Add to dictionary</a>");
        out.println("<table><tr><th>No.</th><th>Key</th><th>Value</th></tr>");
        int i = 1;
        Map<String, String> attributes = sessionManager.getMap();
        for(String key : attributes.keySet()) {
            out.println("<tr><th>" + i + "</th><th>" + key + "</th><th>" + attributes.get(key) + "</th></tr>");
        }
        out.println("</table></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}