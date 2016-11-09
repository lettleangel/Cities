package application;

public class TimeConverter {

	/**
	 * ������������ ����� �� ������� ������ � ������ ��:��
	 * @param time - ����������� �����
	 * @return - ������ ��:��
	 */
	public static String secToMin(int time){
		int min = time / 60;
		int sec = time % 60;
		return String.valueOf(min) + ":" + String.format("%02d", sec);
	}
}
