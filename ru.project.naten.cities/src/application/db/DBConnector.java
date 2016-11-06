package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {

	public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;

	public DBConnector() {
		conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\���������\\Documents\\SQLiteStudio\\words\\words2.db"); //jdbc:sqlite:C:\\Users\\���������\\Documents\\SQLiteStudio\\words\\words.db"
			System.out.println("���� ����������!");
			statmt = conn.createStatement();
			//TODO uncomment
			//cleanTable();
			//boolean ok = statmt.execute("USE words.words;");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * ��� ����� �� ������������ �����
	 * @param letter - �����
	 * @return 
	 */
	public String getWordByLetter(String letter){
		letter = letter.toUpperCase();
		String que = "SELECT * FROM cities_all WHERE name LIKE '" + letter + "%' AND used='false';";
		try {
			resSet = statmt.executeQuery(que);
			if (!resSet.next())
				return null;
			String city = resSet.getString(1);
			return city;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���� �� ������ ����� � ��
	 * @param word - �������� �����
	 * @return
	 */
	public boolean checkWord(String word){
		boolean s = false;
		word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
		String que = "SELECT * FROM cities_all WHERE name = '" + word + "' AND used='false';";
		try {
			resSet = statmt.executeQuery(que);
			if (resSet.next())
				s = true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * �������� ������ ����, ������� ���������� �� ������������ �����
	 * @param letter - �����, � ������� ���������� �����
	 * @return - ������ ���� �� �����
	 */
	public List<String> getWordsFromLetter(String letter){
		List<String> list = new ArrayList<>();
		letter = letter.toUpperCase();
		String que = "SELECT * FROM cities_all WHERE name LIKE '" + letter + "%' AND used='false';";
		try {
			resSet = statmt.executeQuery(que);
			while (resSet.next()){
				String city = resSet.getString(1);
				list.add(city);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ������� � �������� ��� ������� �������������.
	 */
	public void cleanTable() {
		String que = "UPDATE cities_all SET used='false';";
		try {
			statmt.executeUpdate(que);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �������� ����� � �� ��� ��������������.
	 * @param word �����, ������� ����� �������� ��� ��������������
	 */
	public void markUsedWord(String word) {
		String que = "UPDATE cities_all SET used='true' WHERE name='" + word + "';";
		try {
			statmt.executeUpdate(que);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �������� ���������� ���� �� ������������ �����
	 * @param s - �����, �� ������� �������� ���������� ���� �� ��
	 * @return - ���������� ���� � ��
	 */
	public int getCount(String s){
		String que = "SELECT COUNT(*) FROM cities_all WHERE name LIKE '" + s + "%' AND used='false';";
		int count = 0;
		try {
			resSet = statmt.executeQuery(que);
			//if (resSet.next())
				count = resSet.getInt(1);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
