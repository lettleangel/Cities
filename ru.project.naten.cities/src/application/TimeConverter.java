package application;

public class TimeConverter {

	/**
	 * Конвертируем время из формата секунд в формат мм:сс
	 * @param time - принимаемое время
	 * @return - строку мм:сс
	 */
	public static String secToMin(int time){
		int min = time / 60;
		int sec = time % 60;
		return String.valueOf(min) + ":" + String.format("%02d", sec);
	}
}
