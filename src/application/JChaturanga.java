package application;

import engine.board.Board;
import gui.main.MainGUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JChaturanga extends Application {

    public static Board board;

    public static void main(String[] args) {
        //board = Board.initializeBoard(); // TODO: find usages of this board (if any)
        //System.out.println(board); // print ASCII board
        launch(args); // run JavaFX application thread
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ASD3000: JChaturanga 0.8");
        primaryStage.getIcons().add(new Image("/res/img/appIcon.png"));
        primaryStage.setScene(new MainGUI().getGameScene());
        primaryStage.show();
    }

}
