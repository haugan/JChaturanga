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
        ChessBoardGrid grid = new ChessBoardGrid();

        menu.addObserver(grid);
        System.out.println("Observers of MenuTop: " + menu.countObservers());

        setTop(menu.getMenuBar());
        setCenter(grid);

        root.getChildren().add(this);
        gameScene = new Scene(root, Color.DIMGRAY);
    }

    public Scene getGameScene() {return gameScene;}

}
