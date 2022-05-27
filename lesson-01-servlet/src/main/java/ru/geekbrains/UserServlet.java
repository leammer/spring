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

@WebServlet(urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

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
		PrintWriter wr = resp.getWriter();
		Long id;
		if (req.getPathInfo() != null || (id= getIdFromInfo(req.getPathInfo()))!=-1) {
			User user = userRepository.findById(id);
			if (user != null) {
				wr.println("<p>User name: " + user.getUsername() + "</p>");

			} else {
				wr.println("<p>There is no such user</p>");
			}
		} else {

			wr.println("<h1>List of users</h1>");

			wr.println("<table>");
			wr.println("<tr>");
			wr.println("<th>Id</th>");
			wr.println("<th>Username</th>");
			wr.println("</tr>");

			for (User user : userRepository.findAll()) {
				wr.println("<tr>");
				wr.println("<td>" + user.getId() + "</td>");
				wr.println("<td>" + user.getUsername() + "</td>");
				wr.println("</tr>");
			}

			wr.println("</table>");
		}
	}

	private static Long getIdFromInfo(String str) {
		try {
			Long id = Long.parseLong(str.substring(1));
			return id;
		} catch (NullPointerException | NumberFormatException e) {
			return (long) -1;
		}
	}
}
