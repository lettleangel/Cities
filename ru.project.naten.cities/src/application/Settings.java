package application;

public class Settings {

	private boolean checkTime;
	private boolean alert;
	private int alertTimeSlider;
	private int gameTimeSlider;
	private String language;
	
	private static Settings set = new Settings();
	
	private Settings(){
		checkTime = true;
		alert = true;
		alertTimeSlider = 50;
		gameTimeSlider = 300;
		language = "En";
	}
	
	public static Settings getInstance() {
		return set;
	}
	
	/**
	 * @return the checkTime
	 */
	public boolean getCheckTime() {
		return checkTime;
	}
	
	/**
	 * @param checkTime the checkTime to set
	 */
	public void setCheckTime(boolean checkTime) {
		this.checkTime = checkTime;
	}
	
	/**
	 * @return the alert
	 */
	public boolean getAlert() {
		return alert;
	}
	
	/**
	 * @param alert the alert to set
	 */
	public void setAlert(boolean alert) {
		this.alert = alert;
	}
	
	/**
	 * @return the alertTimeSlider
	 */
	public int getAlertTimeSlider() {
		return alertTimeSlider;
	}
	
	/**
	 * @param alertTimeSlider the alertTimeSlider to set
	 */
	public void setAlertTimeSlider(int alertTimeSlider) {
		this.alertTimeSlider = alertTimeSlider;
	}
	
	/**
	 * @return the gameTimeSlider
	 */
	public int getGameTimeSlider() {
		return gameTimeSlider;
	}
	
	/**
	 * @param gameTimeSlider the gameTimeSlider to set
	 */
	public void setGameTimeSlider(int gameTimeSlider) {
		this.gameTimeSlider = gameTimeSlider;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
