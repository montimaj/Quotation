package ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class FileTypeFilter extends FileFilter {

    private String mExtension;
    private String mDescription;

    FileTypeFilter(String extension, String description) {
        mExtension = extension;
        mDescription = description;
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(mExtension);
    }

    public String getDescription() {
        return mDescription + String.format(" (*%s)", mExtension);
    }
}
