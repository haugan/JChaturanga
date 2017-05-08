package gui.menu;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MenuTop {

    private MenuBar menuBar;

    public MenuTop() {
        menuBar = new MenuBar();

        // FILE MENU
        Menu fileM = new Menu("_File");
        MenuItem newMI = new MenuItem("New");
        MenuItem saveMI = new MenuItem("Save PGN...");
        MenuItem loadMI = new MenuItem("Load PGN...");
        MenuItem exitMI = new MenuItem("Exit");
        newMI.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        saveMI.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        loadMI.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        exitMI.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        exitMI.setOnAction(actionEvent -> {
            System.out.println("Exiting application...");
            Platform.exit();}
        );
        fileM.setMnemonicParsing(true);
        fileM.getItems().addAll(newMI,
                                saveMI,
                                loadMI,
                                new SeparatorMenuItem(),
                                exitMI);

        // EDIT MENU
        Menu editM = new Menu("_Edit");
        MenuItem undoMI = new MenuItem("Undo move");
        MenuItem redoMI = new MenuItem("Redo move");
        MenuItem optionsMI = new MenuItem("Options...");
        undoMI.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
        redoMI.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        optionsMI.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        editM.setMnemonicParsing(true);
        editM.getItems().addAll(undoMI,
                                redoMI,
                                new SeparatorMenuItem(),
                                optionsMI);

        // TOOLS MENU
        Menu toolsM = new Menu("_Tools");
        CheckMenuItem hintsCMI = new CheckMenuItem("Show legal moves");
        CheckMenuItem coordsCMI = new CheckMenuItem("Show coordinates");
        hintsCMI.setSelected(false);
        hintsCMI.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN));
        coordsCMI.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        toolsM.setMnemonicParsing(true);
        toolsM.getItems().addAll(hintsCMI,
                                 coordsCMI);

        Menu screenshotM = new Menu("_Screenshot");
        MenuItem savePNGMI = new MenuItem("Save as PNG...");
        MenuItem saveGIFMI = new MenuItem("Save as GIF...");
        toolsM.setMnemonicParsing(true);
        screenshotM.getItems().addAll(savePNGMI,
                                      saveGIFMI);
        toolsM.getItems().add(screenshotM);

        // HELP MENU
        Menu helpM = new Menu("_Help");
        MenuItem aboutMI = new MenuItem("About");
        helpM.setMnemonicParsing(true);
        helpM.getItems().add(aboutMI);

        // ADD ALL SUB MENUS TO TOP MENU
        menuBar.getMenus().addAll(fileM,
                                  editM,
                                  toolsM,
                                  helpM);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

}
