package gui.main;

import gui.board.ChessBoardGrid;
import gui.menu.MenuTop;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MainGUI extends BorderPane {

    private Scene gameScene;

    public MainGUI() {
        Group root = new Group();
        MenuTop menu = new MenuTop();
        StatusBar statusBar = new StatusBar(1);
        ChessBoardGrid grid = new ChessBoardGrid();

        menu.addObserver(grid);

        setTop(menu.getMenuBar());
        setCenter(grid);
        setBottom(statusBar);

        root.getChildren().add(this);
        gameScene = new Scene(root, Color.DIMGRAY);
    }

    public Scene getGameScene() {return gameScene;}

}
