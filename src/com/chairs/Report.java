package com.chairs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Report
 */
@WebServlet("/Report")
public class Report extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Report() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] floors = { "First", "Second", "Third", "Fourth" };
		response.getWriter().append("Report\n\n");
		for (String floor : floors) {
			response.getWriter().append("\nFloor: " + floor + "\n");
			try {
				response.getWriter().append(getData(floor));
			} catch (SQLException e) {
			}
		}
	}

	private String getData(String floor) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionPool.getInstance().getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String q = "select floor, room, chair from floorplan where floor=?";
		PreparedStatement st = conn.prepareStatement(q);
		st.setString(1, floor);
		
//		System.out.println(q);
		ResultSet rs = st.executeQuery();
		HashMap<String, HashSet<String>> hm = new HashMap<>();
		while (rs.next()) {
			String chair = rs.getString("chair");
			String room = rs.getString("room");
			if (hm.containsKey(room)) {
				hm.get(room).add(chair);
			} else {
				HashSet<String> hs = new HashSet<>();
				hs.add(chair);
				hm.put(room, hs);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String key : hm.keySet()) {
			HashSet<String> hs = hm.get(key);
			sb.append(key).append(" ").append(hs.toString().replace('[', ' ').replace(']', ' ')).append("\n");
		}
//		System.out.print("Result = " + sb.toString());
		conn.close();
		return sb.toString();
	}
}
