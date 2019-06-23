package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<AnnoCount> loadAnni(){
		List<AnnoCount> result = new ArrayList<AnnoCount>();
		String sql = "SELECT  YEAR(DATETIME) as anno ,COUNT(*) as cnt " + 
				"FROM sighting " + 
				" WHERE country = \"us\" "+
				"GROUP BY YEAR(DATETIME) " +
				"ORDER BY YEAR(DATETIME) ASC ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
	
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					 	result.add(new AnnoCount(Year.of(res.getInt("anno")), res.getInt("cnt")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> loadStati(Year anno){
		List <String> result = new ArrayList<String>();
		String sql = "SELECT distinct state " + 
				"FROM sighting " + 
				"WHERE YEAR(DATETIME) = ? " + 
				"AND country = \"us\"  " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					 	result.add(res.getString("state"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public boolean esisteArco(Year anno, String s1, String s2) {
		int cont=0;
		String sql = "SELECT COUNT(*) as cnt " + 
				"FROM sighting AS s1, sighting AS s2 " + 
				"WHERE YEAR (s1.DATETIME) = ? " +
				"AND YEAR (s1.DATETIME) = YEAR(s2.DATETIME) " + 
				"AND s1.state= ? " + 
				"AND s2.state = ? " + 
				
				"AND s1.country = \"us\" and s2.country = \"us\" "+
				"AND s2.DATETIME > s1.datetime ";
		try {
			Connection conn = DBConnect.getConnection() ;
	
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);
			
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
					cont = res.getInt("cnt");
					if(cont>0) {
						conn.close();
						return true;
					}
						
					else {
						conn.close();
						return false;
					}
				}
			
			
			conn.close();
		
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		}
		return false;
	}
	
	

}
