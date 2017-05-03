package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

public class ChessBoardFlowPane extends FlowPane {

    public ChessBoardFlowPane() {
        this.setAlignment(Pos.TOP_LEFT); // default alignment
        this.setMinSize(600,600); // no resize below this size
        this.setPrefWrapLength(600); // default horizontal size
        this.getChildren().addAll(); // add Squares
    }

}
