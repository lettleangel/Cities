package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SettingsController {

	@FXML
	public Slider gameTimeSlider; //����������� ����� ����
	
	@FXML
	public Slider alertTimeSlider; // ����������� ����� ����������� ��������� �� �������
	
	@FXML
	public Label gameTimeNum; // ���������� ����� ���� (label)
	
	@FXML
	public Label alertTimeNum; // ���������� ����� ����������� ��������� (label)
	
	@FXML
	public CheckBox alert; // ����� ����������� ���������
	
	@FXML
	public CheckBox checkWord; // ����� �������� ���� �� ������
	
	@FXML
	public VBox alertTimePanel; 
	/**
	 * �������� �� slider-��� � �������� ��������� �������� � label
	 */
	@FXML
	public void initialize(){
		gameTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				gameTimeNum.setText(Integer.toString(newValue.intValue()));
				if(!alertTimePanel.isDisabled())
					alertTimeSlider.setMax(newValue.intValue());
				Settings.getInstance().setGameTimeSlider(newValue.intValue());
				
				if(newValue.intValue() == 601){
					gameTimeNum.setText("inf");
					Settings.getInstance().setGameTimeSlider(-1);
					
				}
			}
		});
		
		alertTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				alertTimeNum.setText(Integer.toString(newValue.intValue()));
				Settings.getInstance().setAlertTimeSlider(newValue.intValue());
			}
		});
		
		gameTimeSlider.setValue(Settings.getInstance().getGameTimeSlider());
		alertTimeSlider.setValue(Settings.getInstance().getAlertTimeSlider());
		checkWord.setSelected(Settings.getInstance().getCheckTime());
		alert.setSelected(Settings.getInstance().getAlert());
		alertTimePanel.setDisable(!alert.isSelected());
	}
	
	/**
	 * ����� ����������� ���������
	 */
	@FXML
	public void alertChoise(){
		Settings.getInstance().setAlert(alert.isSelected());
		alertTimePanel.setDisable(!alert.isSelected());
		
	}
	
	/**
	 * ����� �������� ���� �� ������
	 */
	@FXML
	public void checkWordsChoise(){
		Settings.getInstance().setCheckTime(checkWord.isSelected());
	}
}
