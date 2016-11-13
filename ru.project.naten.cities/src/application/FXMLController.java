package application;

import java.io.IOException;

import application.GameMessage.MessageType;
import application.db.DBConnector;
import application.db.DBConnector.Range;
import application.game.Motion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class FXMLController {

	private static final String PLAYER_1 = "Player 1";
	private static final String PLAYER_2 = "Player 2";

	//private int TIME = 300;

	//private int ALERT_TIME = 5;

	private DBConnector conn;

	@FXML
	private TextField actiontarget; //������ ���������, ��� �������� �����

	@FXML
	private RadioMenuItem radioButton1; //���������� ���� ������ ��������

	@FXML
	private RadioMenuItem radioButton2; //���������� ���� ������ ����������

	@FXML
	private RadioMenuItem radioButton3; // ���������� ���������� ����

	@FXML
	private RadioMenuItem radioButton4; // ���������� ������� ����

	@FXML
	private Label playerIndocator; // ��������� ���� �������

	@FXML
	private RadioMenuItem all_cities; //�������� ��� ������

	@FXML
	private RadioMenuItem cis_cities; //�������� ������ ���

	@FXML
	private ListView<GameMessage> listView; //���� ���������
	private ObservableList<GameMessage> list = FXCollections.observableArrayList();

	@FXML
	private Label time; // ���������� ����� 

	private Motion motion; // + ����� � ������

	private int move = 0; // ���������� ���� 

	private Thread timer; // ����� �������
	private Runnable runTimer; // ������������ ������


	public FXMLController() {
	}

	/**
	 * ���������� �������� ������ SEND.
	 * ��� ������� ������ Send ������������ ��������� � TextArea (��� �� ����� ������������ Enter)
	 * @param event
	 */
	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {

		String city = actiontarget.getText();

		// ��������, ����� �� ������ ������ �����
		if (city == null){
			return;
		}
		if(city.isEmpty() == true){
			return;
		}

		if(city.length() < 3){
			return;
		}



		//����� ������ ������
		if (radioButton1.isSelected() == true){
			boolean ok;
			actiontarget.clear();

			if(validateWord(city)==false)
				return;
			String word = motion.checkWord(city);
			if(word == null) {
				showWinners();
				return;
			} else if(word.length() == 1){
				playerIndocator.setText("Try again! You have " + (word) + " tries. Player " + (move+1));
				return;
			}
			else 
				city = word;
			//�������� �����
			ok = motion.peopleMove(city);



			//�������������� ���������� ������, �� ���� �� ���������� ���� (move) � � ������ ������������� ������������ label
			if (ok == true){
				motion.clearCheck();
				addMessage(city, move);
				move = motion.move(move);
				//��������, ��� ����� �������������
				conn.markUsedWord(city);

				if (timer.isAlive()) { // ���� ������ �������
					timer.interrupt(); // �������������
					timer = new Thread(runTimer); // ������ �����
				}
				timer.start(); // ��������� ������

				if (move == 0 )
					playerIndocator.setText(PLAYER_1);
				else 
					playerIndocator.setText(PLAYER_2);
			} else {
				if (move == 0 )
					playerIndocator.setText(PLAYER_1);
				else 
					playerIndocator.setText(PLAYER_2);
			}
		}

		//���� � �����������
		else if (radioButton2.isSelected() == true){
			boolean ok;
			if(move == 0){
				actiontarget.clear();

				if(validateWord(city)==false) {
					String word = motion.checkWord(city);
					if(word == null) {
						showWinners();
						return;
					} else if(word.length() == 1){
						playerIndocator.setText("Try again! You have " + (word) + " tries. Player " + (move+1));
						return;
					}
					else 
						city = word;
				}

				//�������� �����
				ok = motion.peopleMove(city);

				if (ok == true){
					motion.clearCheck();
					addMessage(city, move);
					move = motion.move(move);
					//��������, ��� ����� �������������
					conn.markUsedWord(city);
					//---CPU MOVE-----------------------------------------
					if(radioButton3.isSelected() == true)
						city = motion.computerMoveEn();
					else
						city = motion.computerMoveRu();

					if (city == null){	
						showWinners();
						return;
					}
					actiontarget.clear();

					//�������� �����
					ok = motion.peopleMove(city);

					if (ok == true){
						addMessage(city, move);
						move = motion.move(move);
						//��������, ��� ����� �������������
						conn.markUsedWord(city);
					}	
					playerIndocator.setText(PLAYER_1);
				}
			}
		}

		//������ ����� �� ������ � �������
		actiontarget.requestFocus();

		//������� �� ����������� ���������� ����
		char s = city.charAt(city.length()-1);
		if(conn.getWordByLetter(Character.toString(s)) == null){
			showWinners();
		}
	}

	/**
	 * ���������� ������� �� Enter ���� ������� �����
	 * @param event - � ���������� �������� enter � ����������
	 */
	@FXML
	protected void enterPressed(KeyEvent event){
		if (event.getCode().equals(KeyCode.ENTER)) {
			handleSubmitButtonAction(null);
		}
	}

	/**
	 * ������������� ������ �� ������
	 */
	@FXML
	public void initialize(){
		System.out.println("Init");
		actiontarget.requestFocus();

		conn = new DBConnector(DBConnector.Lang.RU); // ���� ���������� ���� ������ � ������� �����
		
		if (all_cities.isSelected() == true) // ���� ������� ��� ������
			conn.setRange(Range.ALL);
		else // ���� ������� ������ ������ ���
			conn.setRange(Range.CIS);
		
		motion = new Motion(conn);
		listView.setItems(list);
		listView.setCellFactory(new Callback<ListView<GameMessage>, ListCell<GameMessage>>() {
			@Override
			public ListCell<GameMessage> call(ListView<GameMessage> param) {
				return new MessageController();
			}

		});

		// ������ ��� (������������) ������
		runTimer = new Runnable() {
			int currentTime = 0;

			@Override
			public void run() {
				while (currentTime < Settings.getInstance().getGameTimeSlider()){
					Platform.runLater(() -> time.setText(TimeConverter.secToMin(Settings.getInstance().getGameTimeSlider()-currentTime))); // ���������� �������� ������� � ���������

					currentTime++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						currentTime = 0;
						return;
					}

					if (currentTime == Settings.getInstance().getAlertTimeSlider()){
						Platform.runLater(() -> {
							
							/*Alert alert = new Alert(AlertType.NONE);
							alert.setHeaderText("Warning!");
							String s = motion.getLastWord();
							alert.setContentText("With letter " + s + " we have " + conn.getCount(s) + " words");
							alert.show(); */
							
							String s = motion.getLastWord();
							
							Stage info = new Stage();
							info.initModality(Modality.NONE);
							info.initStyle(StageStyle.UNDECORATED);
							
							VBox box = new VBox();
							Label lb = new Label("With letter " + s + " we have " + conn.getCount(s) + " words");
							box.getStyleClass().add("time-info");
							box.getChildren().add(lb);
							
							Scene sc = new Scene(box);
							sc.getStylesheets().add("/application/application.css");
							info.setScene(sc);
							info.show();
							
							double x1 = time.localToScreen(time.getBoundsInLocal()).getMinX() + time.getWidth()/2.0 - info.getWidth()/2.0;
							double y1 = time.localToScreen(time.getBoundsInLocal()).getMinY() -5 - info.getHeight();
							info.setX(x1);
							info.setY(y1);
						});
					}
				}
				Platform.runLater(() -> showWinners());
			}
		};
		// ������ ����� ����� � ��������� ���� ��������������
		timer = new Thread(runTimer);
	}

	/**
	 * ������� ����. ���������� ����������, ������ ������ ��� ������� ������
	 * @param event
	 */
	@FXML
	public void reastartEd(ActionEvent event){
		motion = new Motion(conn);
		move = 0;
		conn.cleanTable();
		playerIndocator.setText(PLAYER_1);
		list.clear();
	}

	/**
	 * ���������� ����������
	 */
	public void showWinners(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Game Over");
		if ((move == 1)&&(radioButton2.isSelected() == true)){
			alert.setContentText("You Win");
		}
		else if((move == 0)&&(radioButton2.isSelected() == true)){
			alert.setContentText("Computer Win");
		}
		else {
			if (move == 1)
				alert.setContentText("Player 1 Win");
			else 
				alert.setContentText("Player 2 Win");
		}

		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				reastartEd(null);
			}
		});
		alert.show();
	}

	/**
	 * �������� ������������ ����. 
	 * ���� � ��� ����� �� ������ ���� ����, ���������� ��� ������� ������� (� ����������� �� ���������� ����� ����)
	 * �� ����� ��������
	 * @param word ����������� �����
	 * @return
	 */
	public boolean validateWord(String word) {		
		//���������, ����� ������������ ����� ���� �� ������ 3� ��������
		if(word.length() < 3)
			return false;

		//��������� �� ������� �������� ������� �������� � ����
		if(radioButton3.isSelected() == true)
			if (motion.wordEnglish(word) == true)
				return false;

		if(radioButton4.isSelected() == true)
			if (motion.wordRussian(word) == true)
				return false;

		//�������� ���� �� ����� ����� � ���� ������
		/*if (!conn.checkWord(word)){	
			return false;
		}*/

		return true;
	}

	/**
	 * ������� ��� ������� �� ������
	 * @param event
	 */
	@FXML
	public void surrender(ActionEvent event){
		showWinners();
	}

	public void addMessage(String word, int move){
		MessageType player = null;
		if(radioButton1.isSelected() == true){
			if(move == 0)
				player = MessageType.PLAYER_1;
			else 
				player = MessageType.PLAYER_2;
		}
		else if(radioButton2.isSelected() == true){
			if(move == 0)
				player = MessageType.PLAYER_1;
			else 
				player = MessageType.COMPUTER;
		}
		list.add(new GameMessage(word, player));
		listView.scrollTo(list.size()-1);
	}

	/**
	 * �������� ���� ��������
	 * @param event
	 */
	@FXML
	public void openProperties(ActionEvent event){
		Stage props = new Stage(StageStyle.DECORATED);
		props.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/settings.fxml"));
		loader.setController(new SettingsController());
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene sc = new Scene(root);
		props.setScene(sc);
		props.show();
		
		props.setMinHeight(props.getHeight());
		props.setMinWidth(props.getWidth());
		
	}
	
	
}
