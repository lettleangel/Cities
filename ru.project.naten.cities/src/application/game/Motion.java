package application.game;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.db.DBConnector;

/**
 * Определяет ход игроков и с какой буквы следует ходить
 * @author Анастасия
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
	 * Определяем ход. 
	 * Если ход = 0 - ходит первый игрок
	 * Если ход = 1 - ходит второй игрок (либо компьютер)
	 * Если ход = -1 - игра завершена, либо ошибка
	 * @param move - передает ход
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
	 * Проверяем правильно ли мы пишем слово.
	 * Если первая буква слова совпадает с последней буквой предыдущего слова, то возвращаем true 
	 * @param s1 - предыдущее слово
	 * @param s2 - записываемое слово
	 * @return
	 */
	public boolean letter(String s1, String s2){
		if (s1 == null)
			return true;
		String str1 = s1.toLowerCase();
		String str2 = s2.toLowerCase();
		char last = str1.charAt(s1.length()-1);
		if ((last == 'ь') || (last =='ы')||(last == 'ъ')){
			last = str1.charAt(s1.length()-2);
		}
		char first = str2.charAt(0);
		if (last == first)
			return true;
		else return false;
	}

	/**
	 * Ход человека
	 * 1) проверяет
	 * 2) если не совпадает то сказать об этом
	 * 3) если совпадает - запомнить слово и переключить игрока
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

	//если у нас английское слово
	public String computerMoveEn(){
		String str = previous;
		char s = str.charAt(previous.length()-1);
		String st = conn.getWordByLetter(Character.toString(s));
		return st;
	}

	//если у нас русское слово
	public String computerMoveRu(){
		String str = previous;
		char s = str.charAt(previous.length()-1);
		String st;
		if ((s == 'ь') || (s =='ы')||(s == 'ъ')){
			s = str.charAt(previous.length()-2);
		}
		st = conn.getWordByLetter(Character.toString(s));
		return st;
	}


	/**
	 * Проверяет содержится в английском слове русские буквы
	 * @param word
	 * @return
	 */
	public boolean wordEnglish(String word){
		boolean s = false;
		Pattern p = Pattern.compile("[А-ЯЁа-яё0-9]");
		Matcher m = p.matcher(word);
		while (m.find()){
			s = true;
		}
		return s;
	}
	/**
	 * Проверяет содержится в английском слове русские буквы
	 * @param word
	 * @return
	 */
	public boolean wordRussian(String word){
		boolean s = false;
		Pattern p = Pattern.compile("[A-Za-z0-9]"); // странная штука, устанавливает паттерн на английские буквы и цифры
		Matcher m = p.matcher(word);
		while (m.find()){ //проверяет есть ли указанные символы в передаваемом слове
			s = true;
		}
		return s;
	}

	/**
	 * Метод проверки слова на ошибки
	 * если ошибок меньше двух - слово подходит,
	 * возвращаем слово, когда оно подходит и null если слово не подошло
	 * @param word - проверяемое слово
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
	 * Чистит количество попыток 
	 */
	public void clearCheck(){
		check = 0;
	}
	
	/**
	 * Определяет последнюю букву хода
	 * @return
	 */
	public String lastWord(String word){
		String str1 = word.toLowerCase();
		int count = 1;
		char last = str1.charAt(word.length()-count);
		if ((last == 'ь') || (last =='ы')||(last == 'ъ')){
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
	 * Получаем последнюю букву слова
	 * @return
	 */
	public String getLastWord(){
		return lastWord(previous);
	}
}
