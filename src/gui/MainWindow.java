package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class MainWindow {

    private Scene gameScene;

    public MainWindow() {
        Group root = new Group();
        gameScene = new Scene(root, 600, 600, Color.WHITESMOKE);
    }

    public Scene getScene() {return this.gameScene;}

}
