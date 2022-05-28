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

	private final String BAD_REQUEST_MESSAGE = "The request must contain the user number in a numeric value";
	private final String NOT_FOUND_MESSAGE = "There is no such user";

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

		if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
			printAllUsers(wr);
			return;
		}

		Long id = getIdFromInfo(req.getPathInfo());
		if (id == -1) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_REQUEST_MESSAGE);
			return;
		}

		User user = userRepository.findById(id);
		if (user == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_FOUND_MESSAGE);
			return;
		}
			
		wr.println("<p>User name: " + user.getUsername() + "</p>");
	}

	private void printAllUsers(PrintWriter wr) {
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

	private static Long getIdFromInfo(String str) {
		try {
			Long id = Long.parseLong(str.substring(1));
			return id;
		} catch (NullPointerException | NumberFormatException e) {
			return (long) -1;
		}
	}
}
