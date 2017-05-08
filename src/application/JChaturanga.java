package application;

import engine.board.Board;
import gui.main.MainGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class JChaturanga extends Application {

    public static void main(String[] args) {
        Board b = Board.initializeBoard();
        System.out.println(b); // print ASCII board
        launch(args); // launch JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ASD3000: application.JChaturanga");
        primaryStage.setScene(new MainGUI().getGameScene());
        primaryStage.show();
    }

}
