package application.game;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.db.DBConnector;

/**
 * ���������� ��� ������� � � ����� ����� ������� ������
 * @author ���������
 *
 */
public class Motion {

	private DBConnector conn;

	private String previous;

	public Motion(DBConnector conn) {
		this.conn = conn;
	}

	public int check = 0;

	/**
	 * ���������� ���. 
	 * ���� ��� = 0 - ����� ������ �����
	 * ���� ��� = 1 - ����� ������ ����� (���� ���������)
	 * ���� ��� = -1 - ���� ���������, ���� ������
	 * @param move - �������� ���
	 * @return
	 */
	public int move(int move){		
		if (move == 1){
			move = 0;
		}

		else if (move == 0){
			move = 1;
		}
		return move;
	}
	/**
	 * ��������� ��������� �� �� ����� �����.
	 * ���� ������ ����� ����� ��������� � ��������� ������ ����������� �����, �� ���������� true 
	 * @param s1 - ���������� �����
	 * @param s2 - ������������ �����
	 * @return
	 */
	public boolean letter(String s1, String s2){
		if (s1 == null)
			return true;
		String str1 = s1.toLowerCase();
		String str2 = s2.toLowerCase();
		char last = str1.charAt(s1.length()-1);
		if ((last == '�') || (last =='�')||(last == '�')){
			last = str1.charAt(s1.length()-2);
		}
		char first = str2.charAt(0);
		if (last == first)
			return true;
		else return false;
	}

	/**
	 * ��� ��������
	 * 1) ���������
	 * 2) ���� �� ��������� �� ������� �� ����
	 * 3) ���� ��������� - ��������� ����� � ����������� ������
	 * @return
	 */
	public boolean peopleMove(String newWord){
		if (letter(previous, newWord) != true) {
			return false;
		} 
		else {
			previous = newWord;
			return true;
		}
	}

	//���� � ��� ���������� �����
	public String computerMoveEn(){
		String str = previous;
		char s = str.charAt(previous.length()-1);
		String st = conn.getWordByLetter(Character.toString(s));
		return st;
	}

	//���� � ��� ������� �����
	public String computerMoveRu(){
		String str = previous;
		char s = str.charAt(previous.length()-1);
		String st;
		if ((s == '�') || (s =='�')||(s == '�')){
			s = str.charAt(previous.length()-2);
		}
		st = conn.getWordByLetter(Character.toString(s));
		return st;
	}


	/**
	 * ��������� ���������� � ���������� ����� ������� �����
	 * @param word
	 * @return
	 */
	public boolean wordEnglish(String word){
		boolean s = false;
		Pattern p = Pattern.compile("[�-ߨ�-��0-9]");
		Matcher m = p.matcher(word);
		while (m.find()){
			s = true;
		}
		return s;
	}
	/**
	 * ��������� ���������� � ���������� ����� ������� �����
	 * @param word
	 * @return
	 */
	public boolean wordRussian(String word){
		boolean s = false;
		Pattern p = Pattern.compile("[A-Za-z0-9]"); // �������� �����, ������������� ������� �� ���������� ����� � �����
		Matcher m = p.matcher(word);
		while (m.find()){ //��������� ���� �� ��������� ������� � ������������ �����
			s = true;
		}
		return s;
	}

	/**
	 * ����� �������� ����� �� ������
	 * ���� ������ ������ ���� - ����� ��������,
	 * ���������� �����, ����� ��� �������� � null ���� ����� �� �������
	 * @param word - ����������� �����
	 * @return
	 */
	public String checkWord(String word){
		List<String> list = conn.getWordsFromLetter(Character.toString(word.charAt(0)));
		int index = list.indexOf(word);
		if (index >= 0){
			check = 0;
			return list.get(index);
		}
		
		//String wordLow = word.toLowerCase();
		for (String correctWord : list){
			if (correctWord.equalsIgnoreCase(word)){
				return correctWord;
			}
		}
		
		if(word.length() >= 7){
			for (String correctWord : list) {
				if(word.length() == correctWord.length()){
					char[] dbword = correctWord.toCharArray();
					char[] newword = word.toCharArray();
					int errors = 0;
					for(int i = 0; i < correctWord.length(); i++)
						if(dbword[i] != newword[i]){
							errors++;
						}
					if (errors <= 2){
						check = 0;
						return correctWord;
					}
				}
			}
		}
		check++;
		if(check >= 3){
			return null;
		}
		else 
			return Integer.toString(3 - check);
	}
	
	/**
	 * ������ ���������� ������� 
	 */
	public void clearCheck(){
		check = 0;
	}
	
	/**
	 * ���������� ��������� ����� ����
	 * @return
	 */
	public String lastWord(String word){
		String str1 = word.toLowerCase();
		int count = 1;
		char last = str1.charAt(word.length()-count);
		if ((last == '�') || (last =='�')||(last == '�')){
			count++;
			last = str1.charAt(word.length()-count);
		}
		if (conn.getCount(Character.toString(last)) == 0){
			count++;
			last = str1.charAt(word.length()-count);
		}
		return Character.toString(last);
	}
	
	/**
	 * �������� ��������� ����� �����
	 * @return
	 */
	public String getLastWord(){
		return lastWord(previous);
	}
}
