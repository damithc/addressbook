package address.controller;

import java.io.File;

import address.events.EventManager;
import address.events.LoadDataRequestEvent;
import address.events.SaveRequestEvent;
import address.model.ModelManager;
import address.preferences.PreferencesManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 */
public class RootLayoutController {

    private MainController mainController;
    private ModelManager modelManager;

    public void setConnections(MainController mainController, ModelManager modelManager) {
        this.mainController = mainController;
        this.modelManager = modelManager;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        modelManager.getPersonData().clear();
        PreferencesManager.getInstance().setPersonFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = getFileChooser();

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainController.getPrimaryStage());

        if (file != null) {
            EventManager.getInstance().post(new LoadDataRequestEvent(file, modelManager.getPersonData()));
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File personFile = PreferencesManager.getInstance().getPersonFilePath();
        if (personFile != null) {
            EventManager.getInstance().post(new SaveRequestEvent(personFile, modelManager.getPersonData()));
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = getFileChooser();

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainController.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            EventManager.getInstance().post(new SaveRequestEvent(file, modelManager.getPersonData()));
        }
    }

    /**
     * @return a file chooser for choosing xml files. The initial folder is set to the same folder that the
     *     current data file is located (if any).
     */
    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File currentFile = PreferencesManager.getInstance().getPersonFilePath();
        if(currentFile != null) {
            fileChooser.setInitialDirectory(currentFile.getParentFile());
        }
        return fileChooser;
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("AddressApp");
    	alert.setHeaderText("About");
    	alert.setContentText("Some code adapted from http://code.makery.ch");

    	alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        mainController.showBirthdayStatistics();
    }


}