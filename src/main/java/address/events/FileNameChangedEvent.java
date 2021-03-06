package address.events;

import java.io.File;

/**
 * Indicates the name of the data file has been changed
 */
public class FileNameChangedEvent {

    /** The new file */
    public File file;

    public FileNameChangedEvent(File file){
        this.file = file;
    }
}
