package ru.geekbrains;

import ru.geekbrains.persist.User;
import ru.geekbrains.persist.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\/(\\d+)");

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
        userRepository.insert(new User("User 1"));
        userRepository.insert(new User("User 2"));
        userRepository.insert(new User("User 3"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            req.setAttribute("users", userRepository.findAll());
            getServletContext().getRequestDispatcher("/user.jsp").forward(req, resp);
        }
//        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
//            PrintWriter wr = resp.getWriter();
//            wr.println("<table>");
//            wr.println("<tr>");
//            wr.println("<th>Id</th>");
//            wr.println("<th>Username</th>");
//            wr.println("</tr>");
//
//            for (User user : userRepository.findAll()) {
//                wr.println("<tr>");
//                wr.println("<td><a href='" + req.getContextPath() + "/user/" + user.getId() + "'>" + user.getId() + "</a></td>");
//                wr.println("<td>" + user.getUsername() + "</td>");
//                wr.println("</tr>");
//            }
//
//            wr.println("</table>");
//        } else {
//            Matcher matcher = PARAM_PATTERN.matcher(req.getPathInfo());
//            if (matcher.matches()) {
//                long id = Long.parseLong(matcher.group(1));
//                User user = this.userRepository.findById(id);
//                if (user == null) {
//                    resp.getWriter().println("User not found");
//                    resp.setStatus(404);
//                    return;
//                }
//                resp.getWriter().println("<p>Id: " + user.getId() + "</p>");
//                resp.getWriter().println("<p>Username: " + user.getUsername() + "</p>");
//            } else {
//                resp.getWriter().println("Bad parameter value");
//                resp.setStatus(400);
//            }
//        }
    }
}
