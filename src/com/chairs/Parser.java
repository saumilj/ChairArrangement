package com.chairs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
	public void parseJson(String json) throws SQLException {
		
		Connection conn	= null;
		JSONObject obj 	= new JSONObject(json);
		String floor 	= obj.getString("floor");

		try {
			conn = ConnectionPool.getInstance().getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Delete old entry in DB for the corresponding floor		
		deleteFloorData(floor, conn);

		/* Once older entries are deleted, insert updated entries in DB
		 * Note:Based on requirements, older entries can be persisted in the 
		 * Database
		 */
		JSONArray arr = obj.getJSONArray("arr");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject roomChair = arr.getJSONObject(i);
			Iterator<String> iterator = roomChair.keys();
			while (iterator.hasNext()) {
				String chair = iterator.next();
				String room	 = roomChair.getString(chair);
				insertFloorData(floor, room, chair, conn);
			}
		}
		
		conn.close();
		ConnectionPool.closePool();
	}

	private void deleteFloorData(String floor, Connection conn) throws SQLException {
		String query = "delete from floorplan where floor=?";
		PreparedStatement st = conn.prepareStatement(query);
		st.setString(1, floor);
		st.executeUpdate();
	}

	private void insertFloorData(String floor, String room, String chair, Connection conn) throws SQLException {
		String query = "insert into floorplan (floor, room, chair) values (?,?,?)";
		PreparedStatement st = conn.prepareStatement(query);
		st.setString(1, floor);
		st.setString(2, room);
		st.setString(3, chair);
		st.executeUpdate();
	}
}
