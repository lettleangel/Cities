package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class MessageController extends ListCell<GameMessage> {

	@FXML
	private Label message;
	
	@FXML
	private Label player; // записывает кто ходит
	
	@FXML
	private VBox vbox;

	@Override
	public void updateItem(GameMessage item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("message.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			
			message.setText(item.getWord());
			
			switch(item.getType()){
			case COMPUTER: 
				player.setText("Computer");
				break;
			
			case PLAYER_1: 
				player.setText("Player 1");
				break;
			
			case PLAYER_2: 
				player.setText("Player 2");
				break;
				
			case SYSTEM: 
				//player.setText("Computer");
				break;
			
			}
			
			setGraphic(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
