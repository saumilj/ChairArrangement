package com.chairs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
	public void parseJsonAndInsert(String json) throws SQLException {
		JSONObject obj = new JSONObject(json);
		String floor = obj.getString("floor");

		Connection conn = null;
		try {
			conn = ConnectionPool.getInstance().getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		deleteFloorData(floor, conn);

		JSONArray arr = obj.getJSONArray("arr");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject roomChair = arr.getJSONObject(i);
			Iterator<String> iterator = roomChair.keys();
			while (iterator.hasNext()) {
				String chair = iterator.next();
				String room = roomChair.getString(chair);

				insert(floor, room, chair, conn);
			}
		}
		
		conn.close();
		ConnectionPool.closePool();
	}

	private void deleteFloorData(String floor, Connection conn) throws SQLException {
		String q = "delete from floorplan where floor=?";
		PreparedStatement st = conn.prepareStatement(q);
		st.setString(1, floor);
		st.executeUpdate();
	}

	public void insert(String floor, String room, String chair, Connection conn) throws SQLException {
		String q = "insert into floorplan (floor, room, chair) values (?,?,?)";
		PreparedStatement st = conn.prepareStatement(q);
		st.setString(1, floor);
		st.setString(2, room);
		st.setString(3, chair);
		st.executeUpdate();
	}
}
