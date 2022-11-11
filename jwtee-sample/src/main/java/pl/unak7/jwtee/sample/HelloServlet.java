package pl.unak7.jwtee.sample;

import pl.unak7.jwtee.JWTSessionManager;
import pl.unak7.jwtee.JWTSessionManagerBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    @Inject
    JWTSessionManager sessionManager;

    private String message;

    public void init() {
        message = "Hello World!";
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        sessionManager.put(key, value);
        response.sendRedirect("ListServlet");
    }

    public void destroy() {
    }
}
