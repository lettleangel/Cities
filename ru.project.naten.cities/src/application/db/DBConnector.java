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
	
	private static final String PATH_RU = "C:\\Users\\Анастасия\\Documents\\SQLiteStudio\\words\\words2.db";
	private static final String PATH_EN = "";
	private Range range = Range.ALL;
	// Обозначение языка
	public enum Lang {
		RU,
		EN
	}
	
	public enum Range {
		ALL,
		CIS
	}
	

	/**
	 * бд со всеми городами
	 */
	public DBConnector(Lang lang) {
		conn = null;
		try {
			String chosenDB = null;
			if(lang == Lang.RU) // если у нас города на русском
				chosenDB = PATH_RU;
			else // Если у нас города на английском
				chosenDB = PATH_EN;
			
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + chosenDB);
			System.out.println("База Подключена!");
			statmt = conn.createStatement();
			//TODO uncomment
			//cleanTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * все слова на определенную букву
	 * @param letter - буква
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
	 * Есть ли данное слово в бд
	 * @param word - передаем слово
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
	 * получаем список слов, которое начинается на определенную букву
	 * @param letter - буква, с которой начинаются слова
	 * @return - список слов на букву
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
	 * Очищает в таблицах все отметки использования.
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
	 * Отмечает слово в бд как использованное.
	 * @param word Слово, которое нужно отметить как использованное
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
	 * Получаем количество слов на определенную букву
	 * @param s - буква, на которую получаем количество слов из бд
	 * @return - количество слов в бд
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
	
	/**
	 * устанавливает значение выборки
	 * @param range
	 */
	public void setRange(Range range){
		this.range = range;
	}
}
