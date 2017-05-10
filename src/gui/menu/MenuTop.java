package gui.menu;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Observable;

import static gui.menu.MenuChoices.HIGHLIGHT_LEGAL_MOVES;
import static gui.menu.MenuChoices.TOGGLE_TOOLTIP;

public class MenuTop extends Observable {

    private MenuBar menuBar;

    public MenuTop() {
        menuBar = new MenuBar();

        // FILE MENU
        Menu fileM = new Menu("File");
        MenuItem newMI = new MenuItem("New");
        MenuItem saveMI = new MenuItem("Save PGN...");
        MenuItem loadMI = new MenuItem("Load PGN...");
        MenuItem exitMI = new MenuItem("Exit");
        newMI.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        saveMI.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        loadMI.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        exitMI.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        exitMI.setOnAction(e -> {
            System.out.println("Exiting application..");
            Platform.exit();
        });
        fileM.setMnemonicParsing(true);
        fileM.getItems().addAll(newMI, saveMI, loadMI, new SeparatorMenuItem(), exitMI);

        // EDIT MENU
        Menu editM = new Menu("Edit");
        MenuItem undoMI = new MenuItem("Undo move");
        MenuItem redoMI = new MenuItem("Redo move");
        undoMI.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
        redoMI.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        editM.setMnemonicParsing(true);
        editM.getItems().addAll(undoMI, redoMI);

        // TOOLS MENU
        Menu toolsM = new Menu("Tools");
        toolsM.setMnemonicParsing(true);

        CheckMenuItem tooltipsCMI = new CheckMenuItem("Show tooltips");
        tooltipsCMI.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
        tooltipsCMI.setSelected(true);
        tooltipsCMI.setOnAction(e -> {
            System.out.println("Toggling tooltips..");
            setChanged();
            notifyObservers(TOGGLE_TOOLTIP);
            clearChanged();
        });

        CheckMenuItem highlightCMI = new CheckMenuItem("Highlight legal moves");
        highlightCMI.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        highlightCMI.setSelected(true);
        highlightCMI.setOnAction(e -> {
            System.out.println("Toggling highlighting..");
            setChanged();
            notifyObservers(HIGHLIGHT_LEGAL_MOVES);
            clearChanged();
        });

        toolsM.getItems().addAll(tooltipsCMI, highlightCMI);

        // HELP MENU
        Menu helpM = new Menu("Help");
        MenuItem aboutMI = new MenuItem("About");
        helpM.setMnemonicParsing(true);
        helpM.getItems().add(aboutMI);

        // ADD ALL SUB MENUS TO TOP MENU
        menuBar.getMenus().addAll(fileM, editM, toolsM, helpM);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

}
