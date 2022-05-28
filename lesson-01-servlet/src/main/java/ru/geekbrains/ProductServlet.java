package ru.geekbrains;

import ru.geekbrains.persist.Product;
import ru.geekbrains.persist.ProductRepository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/product")
public class ProductServlet extends HttpServlet {

	private ProductRepository productRepository;

	@Override
	public void init() throws ServletException {
		this.productRepository = new ProductRepository();

		InputStream is = getClass().getClassLoader().getResourceAsStream("products.txt");
		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				String s[] = line.split("\\s");
				productRepository.insert(new Product(s[0], Float.parseFloat(s[1])));

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter wr = resp.getWriter();
		wr.println("<h1>Product list</h1>");

		wr.println("<table>");
		wr.println("<tr>");
		wr.println("<th>Id</th>");
		wr.println("<th>Title</th>");
		wr.println("<th>Cost</th>");
		wr.println("</tr>");

		for (Product product : productRepository.findAll()) {
			wr.println("<tr>");
			wr.println("<td>" + product.getId() + "</td>");
			wr.println("<td>" + product.getTitle() + "</td>");
			wr.println("<td>" + product.getCost() + "</td>");
			wr.println("</tr>");
		}

		wr.println("</table>");
	}
}
