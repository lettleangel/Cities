package ru.project.naten.cities;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/message.fxml"));
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
