package ru.project.naten.cities;

public class GameMessage {

	private MessageType type;
	private String word;

	public GameMessage(String word, MessageType type){
		this.word = word;
		this.type = type;
	}
	
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	public enum MessageType {
		PLAYER_1,
		PLAYER_2,
		COMPUTER,
		SYSTEM
	}
}
