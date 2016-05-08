package address.storage;

import address.events.EventManager;
import address.events.FileOpeningExceptionEvent;
import address.events.OpenFileEvent;
import address.events.SaveEvent;
import address.model.Person;
import address.preferences.PreferencesManager;
import address.util.XmlHelper;
import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;



public class StorageManager {

    public StorageManager(){
        EventManager.getInstance().registerHandler(this);
    }


    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     */
    public void loadPersonDataFromFile(File file, ObservableList<Person> personData) throws Exception {
        List<Person> data  = XmlHelper.getDataFromFile(file);

        personData.clear();
        personData.addAll(data);

        // Save the file path to the registry.
        PreferencesManager.getInstance().setPersonFilePath(file);
    }



    @Subscribe
    private void handleOpenFileEvent(OpenFileEvent ofe) {
        try {
            loadPersonDataFromFile(ofe.file, ofe.personData);
        } catch (Exception e) {
            EventManager.getInstance().post(new FileOpeningExceptionEvent(e,ofe.file));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + ofe.file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void savePersonDataToFile(File file, ObservableList<Person> personData) {
        try {
            XmlHelper.saveToFile(file, personData);

            // Save the file path to the registry.
            PreferencesManager.getInstance().setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }


    @Subscribe
    private void handleSaveEvent(SaveEvent se){
        savePersonDataToFile(se.file, se.personData);
    }

    public List<Person> getPersonDataFromFile(File file)  {
        try {
            return file == null ? null : XmlHelper.getDataFromFile(file);
        } catch (Exception e) {
            EventManager.getInstance().post(new FileOpeningExceptionEvent(e, file));
            return Collections.emptyList();
        }
    }
}
