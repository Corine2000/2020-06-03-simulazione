package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		
		String sql = "SELECT * FROM Players";
		//List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                  Integer id = res.getInt("PlayerID");
                  if(!idMap.containsKey(id)) {
				Player player = new Player(id, res.getString("Name"));
				
				idMap.put(id, player);
                  }
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getAllVertex(Map<Integer, Player> idMap, Double goals){
		List<Player> result = new ArrayList<>();
		String sql = "SELECT * "
				+ "FROM players "
				+ "WHERE players.PlayerID IN( "
				+ "									SELECT DISTINCT a.PlayerID "
				+ "									FROM actions a  "
				+ "									GROUP BY a.PlayerID "
				+ "									HAVING  AVG(a.Goals)>?)";
		
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goals);
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Player p = new Player(res.getInt("PlayerID"),res.getString("Name"));
				result.add(p);
				
				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					idMap.put(res.getInt("PlayerID"), p);
				}
			
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*public List<Adiacenza> getAllArchi(double x, Map<Integer, Player> idMap){
		List<Adiacenza> result = new ArrayList<>();
		String sql="SELECT a1.PlayerID AS id1, a2.PlayerID AS id2, (a1.TimePlayed -a2.TimePlayed) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID AND a1.PlayerID < a2.PlayerID  	AND "
				+ "      a1.TeamID <> a2.TeamID AND a1.`Starts`=a2.`Starts` AND a1.`Starts`=1 AND  "
				+ "      (a1.TimePlayed -a2.TimePlayed)<>0 "
				+ "      AND a1.PlayerID IN( "
				+ "									SELECT  a.PlayerID "
				+ "									FROM actions a  "
				+ "									GROUP BY a.PlayerID "
				+ "									HAVING  AVG(a.Goals)>?) "
				
				+ "							     AND a2.PlayerID IN( "
				+ "									SELECT DISTINCT a.PlayerID "
				+ "									FROM actions a  "
				+ "									GROUP BY a.PlayerID "
				+ "									HAVING  AVG(a.Goals)>?) "
				+ " GROUP BY a1.PlayerID, a2.PlayerID";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			st.setDouble(2, x);
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Player p1 = idMap.get(res.getInt("id1"));
				Player p2 = idMap.get(res.getInt("id1"));
				
				if(p1!= null && p2!=null) {
					Adiacenza a = new Adiacenza(p1, p2, res.getInt("peso"));
					
					result.add(a);
				}
				
			
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}*/
	
	public List<Adiacenza> getAllArchi( Map<Integer, Player> idMap){
		List<Adiacenza> result = new ArrayList<>();
		
		String sql = "SELECT a1.PlayerID AS id1, a2.PlayerID AS id2, (a1.TimePlayed -a2.TimePlayed) as peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID AND a1.PlayerID < a2.PlayerID  	AND  		"
				+ "      a1.TeamID <> a2.TeamID AND a1.`Starts`=a2.`Starts` AND a1.`Starts`=1 AND "
				+ "      (a1.TimePlayed -a2.TimePlayed)<>0	 "
				+ "GROUP BY  a1.PlayerID, a2.PlayerID ";
		//ma anche con diverso sui dicatori mi da lo stesso risultato
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Player p1 = idMap.get(res.getInt("id1"));
				Player p2 = idMap.get(res.getInt("id2"));
				
				if(p1!=null && p2!=null) {
				
				Adiacenza ad = new Adiacenza(p1, p2, res.getInt("peso"));
				result.add(ad);
				}
				
				}
			
			
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
