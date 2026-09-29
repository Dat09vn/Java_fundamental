package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "FormServlet", urlPatterns = "/form")
public class FormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>GET / POST Form Example</h1>");
        out.println("<form method='post' action='form'>");
        out.println("<label>Name: <input type='text' name='name' /></label><br/><br/>");
        out.println("<label>Email: <input type='email' name='email' /></label><br/><br/>");
        out.println("<button type='submit'>Submit</button>");
        out.println("</form>");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        if (name != null || email != null) {
            out.println("<p>GET request parameters:</p>");
            out.println("<p>Name: " + (name == null ? "" : name) + "</p>");
            out.println("<p>Email: " + (email == null ? "" : email) + "</p>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>POST Request Received</h1>");
        out.println("<p>Name: " + name + "</p>");
        out.println("<p>Email: " + email + "</p>");

        out.println("<p>All parameters:</p>");
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            out.println("<p>" + entry.getKey() + " = " + String.join(", ", entry.getValue()) + "</p>");
        }

        out.println("<a href='form'>Back to form</a>");
        out.println("</body></html>");
    }
}
