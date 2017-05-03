package gui;

import gui.menu.MenuTop;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MainGUIBorderPane extends BorderPane {

    private Scene gameScene;

    public MainGUIBorderPane() {
        Group root = new Group();
        MenuTop topMenu = new MenuTop();
        ChessBoardFlowPane chessBoard = new ChessBoardFlowPane();
        this.setTop(topMenu.getMenuBar());
        this.setCenter(chessBoard);
        root.getChildren().add(this);
        gameScene = new Scene(root, Color.DIMGRAY);
    }

    public Scene getGameScene() {return gameScene;}

}
