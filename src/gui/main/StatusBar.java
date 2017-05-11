package gui.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar extends HBox {

    private Label statusLbl;
    private static SimpleStringProperty msg;

    public StatusBar(final int spacing) {
        super(spacing);

        msg = new SimpleStringProperty();

        statusLbl = new Label();
        statusLbl.setStyle("-fx-background-color: WHITESMOKE; -fx-text-fill: BLACK;");
        statusLbl.textProperty().bind(msg);

        setStyle("-fx-background-color: WHITESMOKE;");
        setAlignment(Pos.CENTER);
        getChildren().addAll(statusLbl);
    }

    public static void setStatus(String text) {
        msg.set(text);
    }

}
