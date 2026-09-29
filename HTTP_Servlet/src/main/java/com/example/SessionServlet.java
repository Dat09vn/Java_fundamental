package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SessionServlet", urlPatterns = "/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 0;
        }
        visitCount++;
        session.setAttribute("visitCount", visitCount);

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Session Management Example</h1>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<p>Visit count: " + visitCount + "</p>");
        out.println("<p>Refresh this page to see the counter increase.</p>");
        out.println("</body></html>");
    }
}
