package preAnnotate;

import java.io.File;

/**
 * Inherited FileFilter class to facilitate reuse when
 * multiple file filter selections are required. 
 */
public class ExtensionFileFilter
    extends javax.swing.filechooser.FileFilter {
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private java.util.List<String> extensions;
    private String description;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public ExtensionFileFilter(String[] exts, String desc) {
        if (exts != null) {
            extensions = new java.util.ArrayList<String>();

            for (String ext : exts) {

                // Clean array of extensions to remove "."
                // and transform to lowercase.
                extensions.add(
                    ext.replace(".", "").trim().toLowerCase()
                );
            }
        } // No else need; null extensions handled below.

        // Using inline if syntax, use input from desc or use
        // a default value.
        // Wrap with an if statement to default as well as
        // avoid NullPointerException when using trim().
        description = (desc != null) ? desc.trim() : "Custom File List";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Overriden FileFilter Methods">
    // Handles which files are allowed by filter.
    @Override
    public boolean accept(File f) {

        // Allow directories to be seen.
        if (f.isDirectory()) return true;

        // exit if no extensions exist.
        if (extensions == null) return false;

        // Allows files with extensions specified to be seen.
        for (String ext : extensions) {
            if (f.getName().toLowerCase().endsWith("." + ext))
                return true;
        }

        // Otherwise file is not shown.
        return false;
    }

    // 'Files of Type' description
    @Override
    public String getDescription() {
        return description;
    }
    //</editor-fold>
}