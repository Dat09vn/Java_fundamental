# Servlet API Example

This is a minimal Java Servlet example using the Servlet API.

## Project structure

- `pom.xml` - Maven build file
- `src/main/java/com/example/HelloServlet.java` - servlet class
- `src/main/webapp/index.html` - landing page

## Run

1. Start the example locally with Jetty:
   ```bash
   mvn jetty:run
   ```
2. Open the app in your browser:
   - `http://localhost:8080/servlet-demo/`
   - `http://localhost:8080/servlet-demo/hello`

If you want to package it as a WAR file instead:
   ```bash
   mvn package
   ```

## What the servlet demonstrates

- `HttpServlet` subclassing
- `@WebServlet` mapping
- reading request URI and method
- writing HTML response using `PrintWriter`
- handling `GET` and `POST` form submissions
- reading request parameters with `request.getParameter(...)`
- session management using `HttpSession`
- request logging via a servlet `Filter`
- session lifecycle tracking via a servlet `Listener`
