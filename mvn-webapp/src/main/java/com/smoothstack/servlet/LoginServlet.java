package com.smoothstack.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.smoothstack.dto.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(user));
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		User sampleUser = new User();
		sampleUser.setName("1");
		sampleUser.setPassword("1");
		if (!user.getName().equals(sampleUser.getName()) || !user.getPassword().equals(sampleUser.getPassword())) {
			response.setStatus(401);
			request.setAttribute("errorMessage", "Name or password is incorrect. Please try again.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/success");
		}
	}
}
