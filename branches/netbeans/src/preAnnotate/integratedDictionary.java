/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * integratedDictionary.java
 *
 * Created on Jun 15, 2010, 1:10:41 PM
 */
package preAnnotate;

import resultEditor.annotations.Article;
import java.awt.Cursor;
import preAnnotate.format.Annotator;
import preAnnotate.readers.FileToAnnotations;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JFileChooser;
import preAnnotate.format.dictEntry;

/**
 * This class represents a JPanel that will be used as an interface to extract
 * dictionary entries.
 * @author Kyle
 */
public class integratedDictionary extends javax.swing.JPanel implements GUIInterface
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private boolean killInput = false;
    private Thread reading = null;
    //The list of readable extensions
    private static String[] readableFormats = new String[]
    {
        ".PREANNOTATE", ".XML", ".PINS", ".ANNOTATIONS"
    };
    private int changes = 0;

    //The List that will contain the input files.
    private ArrayList<Article> listElements;

    //This object will be used to extract classes, annotators, and annotations
    private FileToAnnotations reader;

    //Used to store extraction information
    private Set<dictEntry> newAnnotations = new TreeSet<dictEntry>();
    private Set<dictEntry> newConflicts = new TreeSet<dictEntry>();
    private Set<dictEntry> filteredByClass = new TreeSet<dictEntry>();
    private Set<dictEntry> filteredByAnnotator = new TreeSet<dictEntry>();
    private Set<dictEntry> filteredByBoth = new TreeSet<dictEntry>();
    private Set<String> classes = new TreeSet<String>();
    private int totalDictEntries = 0;
    private int totalDictConflicts = 0;
    public boolean stopThread = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /** Creates new form integratedDictionary */
    public integratedDictionary()
    {
        
        //ArrayList for holding list elements.
        listElements = new ArrayList<Article>();

        //Initialize the components
        initComponents();

        //Create a new reader to read dictionary entries
        reader = new FileToAnnotations(this);

        //Make the progess bar visible.
        jProgressBar1.setVisible(false);

        //Make cancel input button invisible.
        cancelInput.setVisible(false);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Add an annotation to the list of new annotations extracted during the last
     * dictionary extraction.
     *
     * @param annotation - the newly extracted annotation.
     */
    public void addAnnotation(dictEntry annotation)
    {
        newAnnotations.add(annotation);
        changeMade();
    }

    /**
     * Add a newly extracted conflict word.
     * @param conflict - the newly extracted conflict word.
     */
    public void addConflict(dictEntry conflict)
    {
        newConflicts.add(conflict);
        changeMade();
    }

    /**
     * Add an anotation that was filtered because of it's class.
     * @param theClass - the annotation filtered because of its' class.
     */
    public void addFilteredClass(dictEntry theClass)
    {
        filteredByClass.add(theClass);
        changeMade();
    }

    /**
     * Add a new entry that was filtered because it was from the wrong annotator.
     * @param theAnnotator - the annotation filtered because of the annotator
     */
    public void addFilteredAnnotator(dictEntry theAnnotator)
    {
        filteredByAnnotator.add(theAnnotator);
        changeMade();
    }

    /**
     * Add a new entry that was filtered because of its' class and annotator.
     * @param filtered - the entry that was filtered.
     */
    public void addDoubleFiltered(dictEntry filtered)
    {
        filteredByBoth.add(filtered);
        changeMade();
    }

    /**
     * Set the total number of entries in the dictionary.
     * @param value - the total number of entries.
     */
    public void setTotalEntries(int value)
    {
        totalDictEntries = value;
        changeMade();

    }

    /**
     * Set the total number of conflicts from the extraction.
     * @param value
     */
    public void setDictConflicts(int value)
    {
        totalDictConflicts = value;
        changeMade();
    }

    /**
     * Add an extracted class from the dictionary
     * @param aClass - the class.
     */
    public void addclasses(String aClass)
    {
        classes.add(aClass);
        changeMade();
    }

    /**
     * This method will add an ArrayList of classes to the jCombBox, so the user
     * can choose which class to filter by.
     *
     * @param classes - the classes the user can choose from.
     */
    public void setClasses(TreeSet<String> classes)
    {
        //Remove all old items.
        classList.setListData(new Vector());

        Vector<String> theClasses = new Vector<String>();
        //Add all classes as long as they aren't null.
        for (String aClass : classes)
        {
            if (aClass != null)
            {
                theClasses.add(aClass);
            }
        }
        classList.setListData(theClasses);

    }


    /**
     * This class will be used to display the available annotator choices to the user
     * so the user can select an annotator to filter by.
     * @param annotators - available annotator choices.
     */
    public void setAnnotators(TreeSet<Annotator> annotators)
    {
        Vector<Annotator> theAnnotators = new Vector<Annotator>();
        //Remove existing annotator choices.
        annotatorList.setListData(new Vector());

        //Add new options
        for (Object annotator : annotators)
        {
            if (annotator != null)
            {
                Annotator anAnnotator = (Annotator) annotator;
                theAnnotators.add(anAnnotator);
            }
        }
        annotatorList.setListData(theAnnotators);
    }

    /**
     * This method will disable the GUI for a computation.
     */
    public void disableGUI()
    {
        this.addFile.setEnabled(false);
        this.removeFile.setEnabled(false);
        this.browser.setEnabled(false);
        this.workingSet.setEnabled(false);
        this.overwrite.setEnabled(false);
        this.append.setEnabled(false);
        this.classList.setEnabled(false);
        this.annotatorList.setEnabled(false);
        this.destination.setEnabled(false);
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
    }

    /**
     * This method will reEnable the gui after a computation.
     */
    public void enableGUI()
    {
        this.addFile.setEnabled(true);
        this.create.setEnabled(true);
        this.browser.setEnabled(true);
        this.workingSet.setEnabled(true);
        this.overwrite.setEnabled(true);
        this.append.setEnabled(true);
        this.classList.setEnabled(true);
        this.annotatorList.setEnabled(true);
        this.destination.setEnabled(true);
        jButton1.setEnabled(true);
        jButton2.setEnabled(true);
    }

    /**
     * Reset anything with the GUI that was changed.
     */
    public void resetGUI()
    {
        this.create.setText("Create Dictionary");
    }
    /**
     * Call this after a thread is killed to reset variables and black out progress bar.
     */
    public void killThread()
    {
        stopThread = false;
        this.jProgressBar1.setVisible(false);
    }
    public boolean killInput()
    {
        return killInput;
    }
    public void setKillInput(boolean kill)
    {
        killInput = kill;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods - For Modifying GUI Components">
    /**
     * This method will redraw the detailed info about the dictionary extraction.
     */
    private void updateInfo()
    {
        //Strings for the labels that will show extraction information.
        final String addedString = "Number of Annotations Added:";
        final String conflictsString = "Number of New Conflicts:";
        final String classFilteredString = "Number of Annotations Filtered By Class:";
        final String annotatorFilteredString = "Number of Annotations Filtered By Annotator:";
        final String bothFilteredString = "Number of Annotations Filtered By Both:";
        final String totalEntriesString = "Number of Entries:";
        final String totalClassesString = "Number of Classes:";
        final String totalConflictsString = "Number of Conflicts:";

        //Update the information labels with current extraction information.
        added.setText("<html>" + addedString + " <font color = \"blue\">" + newAnnotations.size() + "</font></html>");
        conflicts.setText("<html>" + conflictsString + " <font color = \"blue\">" + newConflicts.size() + "</font></html>");
        classFiltered.setText("<html>" + classFilteredString + " <font color = \"blue\">" + filteredByClass.size() + "</font></html>");
        annotatorFiltered.setText("<html>" + annotatorFilteredString + " <font color = \"blue\">" + filteredByAnnotator.size() + "</font></html>");
        bothFiltered.setText("<html>" + bothFilteredString + " <font color = \"blue\">" + filteredByBoth.size() + "</font></html>");
        totalEntries.setText(totalEntriesString + " " + totalDictEntries);
        totalClasses.setText("<html>" + totalClassesString + " <font color = \"blue\">" + classes.size() + "</font></html>");
        totalConflicts.setText(totalConflictsString + " " + totalDictConflicts);
    }
    /**
     * This method should be called every time the dictionary writer updates a value
     * within this GUI that is keeping track of information from the write.
     */
    private void changeMade()
    {
        changes ++;
        if(changes == 100)
        {
            updateInfo();
            changes = 0;
        }
    }
    /**
     * This method will clear all of the information about the last process.
     */
    private void clearInfo()
    {
        newAnnotations.clear();
        newConflicts.clear();
        filteredByClass.clear();
        filteredByAnnotator.clear();
        filteredByBoth.clear();
        totalDictEntries = 0;
        classes.clear();
        totalDictConflicts = 0;
        updateInfo();

    }
    private void kill()
    {
        stopThread = true;
        this.create.setText("Create Dictionary");
        return;
    }

    /**
     * Set the class and annotator filters for the extraction.
     */
    private void setFilters()
    {
        extractClassFilters();
        extractAnnotatorFilters();
    }

    /**
     * Extract the chosen class filter and send it to the dictionary writer.
     */
    private void extractClassFilters()
    {
        //Extract the chosen filter class
        if (this.classList.getSelectedValues().length != 0)
        {
            ArrayList<String> toSend = new ArrayList<String>();
            for (int i = 0; i < classList.getSelectedValues().length; i++)
            {
                String toAdd = (String) classList.getSelectedValues()[i];
                toSend.add(toAdd);
            }
            reader.setChosenClasses(toSend);
        }
        //If the 'None' option is selected then set class filter to null.
        else
        {
            reader.setChosenClasses(null);
        }
    }

    /**
     * Extract the chosen annotator and send it to the dictionary writer.
     */
    private void extractAnnotatorFilters()
    {
        //Extract the chosen filter annotator
        if (this.annotatorList.getSelectedValues().length != 0)
        {
            ArrayList<Annotator> toSend = new ArrayList<Annotator>();
            for (int i = 0; i < annotatorList.getSelectedValues().length; i++)
            {
                Annotator toAdd = (Annotator) annotatorList.getSelectedValues()[i];
                toSend.add(toAdd);
            }
            reader.setChosenAnnotators(toSend);
        }
        //If the no options are selected then set chosenAnnotators to null
        else
        {
            reader.setChosenAnnotators(null);
        }
    }

    /**
     * Check all input variables for validity and display error messages if the
     * user has entered information wrong.
     * @return - true if everything is ready to go, false otherwise.
     */
    private boolean checkValid()
    {
        // Make sure the user entered a destination.
        if (this.destination.getText().equals(""))
        {
            displayMessage("You didn't enter a destination!");
            return false;
        }

        //Create a File object from the destination text
        String name = destination.getText();
        File destinationFile = new File(name);
        String extension = FileToAnnotations.getExtension(destinationFile);

        //Check to see if user already entered a .preannotate extension.. if not add one.
        if (!extension.toLowerCase().equals(".preannotate"))
        {
            name += ".preannotate";
        }

        //Make a new file with the .preannotate extension
        destinationFile = new File(name);

        //Make sure the file exists if the user is attempting to append... if not inform user.
        if (!(destinationFile.isFile()) && append.isSelected())
        {
            displayMessage("You are attempting to append to a file that does not exist!");
            return false;
        }
        //If the user didn't select append/create then inform user to select one.
        else if (!append.isSelected() && !overwrite.isSelected())
        {
            displayMessage("Choose to overwrite or append destination file!");
            return false;
        }
        //If the user doesn't have any input files then prompt user.
        else if (this.listElements.size() == 0 && !workingSet.isSelected())
        {
            displayMessage("You don't have any input files!");
            return false;
        }

        //Try to create the destination file... if it can't be created then prompt user
        try
        {
            if (!destinationFile.createNewFile() && !destinationFile.isFile())
            {
                displayMessage("Can't create destination file!");
                return false;
            }

        }
        //If any IOExceptions prompt user
        catch (IOException e)
        {
            displayMessage("Invalid Destination File!");
            return false;
        }
        return true;
    }
    /**
     * Set the GUI for extraction.
     */
    private void setForExtraction()
    {
        this.results.setText("");
        this.create.setText("Stop");
        clearInfo();
        disableGUI();
    }
    /**
     * Erase the selected files from the main list of files.
     */
    private void eraseSelection()
    {
        //Get the index of the first and last selected item.
        int start = jList1.getMinSelectionIndex();
        int stop = jList1.getMaxSelectionIndex();

        //Make sure something is selected.
        if (start != -1)
        {
            //Remove from the start index repeatedly until we've erased all the objects
            //between start and stop.
            for (int i = start; i <= stop; i++)
            {
                listElements.remove(start);

            }
        }
        //Redraw the file list.
        reDrawList();
    }

    /**
     * ReDraw the main list of files.
     */
    private void reDrawList()
    {
        //Transfer all list elements into an [] so the list can use them.
        Article[] elements = new Article[listElements.size()];
        for (int i = 0; i < listElements.size(); i++)
        {
            elements[i] = listElements.get(i);
        }
        //ReExtract annotations and classes.
        runClassExtraction(listElements);
        runAnnotatorExtraction(listElements);

        //Set list data.
        jList1.setListData(elements);
    }

    /**
     * Open dialog to allow user to choose a destination
     */
    private void openSingleFileDialog()
    {
        File directory = jFileChooser1.getCurrentDirectory();
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setCurrentDirectory(directory);
        jFileChooser1.setMultiSelectionEnabled(false);

        // Add file filter
        jFileChooser1.addChoosableFileFilter(new ExtensionFileFilter(
                new String[]
                {
                    ".preannotate"
                },
                "Dictionary files (*.preannotate)"));

        // Turn off 'All Files' capability of file chooser,
        // so only our custom filter is used.
        jFileChooser1.setAcceptAllFileFilterUsed(false);

        int result = jFileChooser1.showSaveDialog(this);

        if (jFileChooser1.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION)
        {
            destination.setText(jFileChooser1.getSelectedFile().getAbsolutePath());
        }
        jFileChooser1.resetChoosableFileFilters();
    }

    /**
     * Open file dialog to allow user to select input files... multiple selections are allowed.
     */
    private void openFileDialog()
    {
        jFileChooser1.setSelectedFile(new File(""));
        File[] f = new File[1];
        f[0] = new File("");
        jFileChooser1.setSelectedFiles(f);

        //Allow multiple file selection.
        jFileChooser1.setMultiSelectionEnabled(true);

        // Add second file filter
        jFileChooser1.addChoosableFileFilter(new ExtensionFileFilter(
                new String[]
                {
                    ".preannotate", ".pins", ".annotations", ".xml"
                },
                "Annotation Files (*.preannotate, *.pins, *.annotations, *.xml)"));

        // Turn off 'All Files' capability of file chooser,
        // so only our custom filter is used.
        jFileChooser1.setAcceptAllFileFilterUsed(false);

        //Store the result so we know if the user selected ok or not.
        int result = jFileChooser1.showOpenDialog(this);

        //If the user did not approve the selection then just return.
        if (result != JFileChooser.APPROVE_OPTION)
        {
            return;
        }
        //Otherwise get the list of selected files.
        File[] selFiles = jFileChooser1.getSelectedFiles();

        ArrayList<String> read = new ArrayList<String>();

        //For each file that the user selected try to read it.
        for (File selected : selFiles)
        {
            //Get the absolute path
            String name = selected.getAbsolutePath();
            String extension = "";
            //Get the extension
            if (name.lastIndexOf(".") != -1)
            {
                extension = name.substring(name.lastIndexOf("."));
            }

            //Assume we can't read it.
            boolean canRead = false;

            //Compare to all readable extensions to see if we can read it.
            for (String readable : readableFormats)
            {
                //If extensions match then we can read it.
                if (readable.toLowerCase().equals(extension.toLowerCase()))
                {
                    canRead = true;
                    break;
                }
            }

            //If we can read it then add it to the file list.
            if (canRead)
            {
                read.add(name);
            }
        }
        //Process information about the file.
        preprocessFiles(read);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Event Handlers">
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jDialog1 = new javax.swing.JDialog();
        jOptionPane1 = new javax.swing.JOptionPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        workingSet = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        classList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        annotatorList = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        destination = new javax.swing.JTextField();
        browser = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        overwrite = new javax.swing.JCheckBox();
        append = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        create = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        addFile = new javax.swing.JButton();
        removeFile = new javax.swing.JButton();
        cancelInput = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        added = new javax.swing.JLabel();
        conflicts = new javax.swing.JLabel();
        classFiltered = new javax.swing.JLabel();
        annotatorFiltered = new javax.swing.JLabel();
        bothFiltered = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        totalEntries = new javax.swing.JLabel();
        totalClasses = new javax.swing.JLabel();
        totalConflicts = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        infoList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        results = new javax.swing.JTextPane();

        jList3.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList3);

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jPanel18.setBackground(new java.awt.Color(41, 119, 167));
        jPanel18.setFont(new java.awt.Font("Calibri", 1, 13));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 13));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DICTIONARY SOURCES");
        jPanel18.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jPanel4.add(jPanel18);

        workingSet.setFont(new java.awt.Font("Calibri", 0, 13));
        workingSet.setText("Include Working Set");
        workingSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workingSetActionPerformed(evt);
            }
        });
        jPanel4.add(workingSet);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        jPanel3.setLayout(new java.awt.BorderLayout());

        classList.setFont(new java.awt.Font("Calibri", 0, 13));
        classList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "No Classes Extracted" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        classList.setToolTipText("<html>Hold Ctrl to select multiple class filters<br>By default no filters will be used.</html>");
        jScrollPane4.setViewportView(classList);

        jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jLabel2.setText("Class Filters");
        jPanel3.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jButton1.setFont(new java.awt.Font("Calibri", 0, 13));
        jButton1.setText("Clear");
        jButton1.setToolTipText("Clear selected classes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, java.awt.BorderLayout.PAGE_END);

        jPanel9.add(jPanel3);

        jPanel8.setLayout(new java.awt.BorderLayout());

        annotatorList.setFont(new java.awt.Font("Calibri", 0, 13));
        annotatorList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "No Annotators Extracted" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        annotatorList.setToolTipText("<html>Hold Ctrl to select multiple Annotator filters<br> By default no filters will be used</html>");
        jScrollPane6.setViewportView(annotatorList);

        jPanel8.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jLabel4.setText("Annotator Filters");
        jPanel8.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        jButton2.setFont(new java.awt.Font("Calibri", 0, 13));
        jButton2.setText("Clear");
        jButton2.setToolTipText("Clear selected annotators");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton2, java.awt.BorderLayout.PAGE_END);

        jPanel9.add(jPanel8);

        jPanel11.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setLayout(new java.awt.GridLayout(1, 3, 0, 5));

        jPanel20.setLayout(new java.awt.GridLayout(5, 1, 5, 5));

        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel25.setBackground(new java.awt.Color(41, 119, 167));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 13));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("OUTPUT OPTIONS");
        jPanel25.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel20.add(jPanel15);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 13));
        jLabel7.setText("Destination:");
        jPanel24.add(jLabel7, java.awt.BorderLayout.LINE_START);

        destination.setFont(new java.awt.Font("Calibri", 0, 13));
        jPanel24.add(destination, java.awt.BorderLayout.CENTER);

        browser.setFont(new java.awt.Font("Calibri", 0, 13));
        browser.setText("Browse...");
        browser.setToolTipText("Choose a destination file");
        browser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserActionPerformed(evt);
            }
        });
        jPanel24.add(browser, java.awt.BorderLayout.LINE_END);

        jPanel20.add(jPanel24);

        jPanel12.setLayout(new java.awt.GridLayout(1, 0));
        jPanel12.add(jPanel26);

        buttonGroup1.add(overwrite);
        overwrite.setFont(new java.awt.Font("Calibri", 0, 13));
        overwrite.setText("Overwrite/Create");
        overwrite.setToolTipText("<html>Overwrite the destination file, <br>Or create it if it does not exist. </html>");
        jPanel12.add(overwrite);

        buttonGroup1.add(append);
        append.setFont(new java.awt.Font("Calibri", 0, 13));
        append.setText("Append");
        append.setToolTipText("Append to destination file.");
        jPanel12.add(append);
        jPanel12.add(jPanel27);

        jPanel20.add(jPanel12);

        create.setFont(new java.awt.Font("Calibri", 0, 13));
        create.setText("Create Dictionary");
        create.setToolTipText("Perform extraction with selected options.");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });
        jPanel20.add(create);
        jPanel20.add(jProgressBar1);

        jPanel14.add(jPanel20);

        jPanel11.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel11, java.awt.BorderLayout.PAGE_END);

        jPanel22.setLayout(new java.awt.BorderLayout());

        jList1.setFont(new java.awt.Font("Calibri", 0, 13));
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel22.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel23.setLayout(new java.awt.GridLayout(2, 0));

        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        addFile.setFont(new java.awt.Font("Calibri", 0, 13));
        addFile.setText("Add Files");
        addFile.setToolTipText("<html>Add files to extract dictionary entries from.  <br>Reads from .xml, .preannotate, .pins, or .annotations files.</html>");
        addFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFileActionPerformed(evt);
            }
        });
        jPanel7.add(addFile);

        removeFile.setFont(new java.awt.Font("Calibri", 0, 13));
        removeFile.setText("Remove");
        removeFile.setToolTipText("Remove selected file(s) from extraction set.");
        removeFile.setEnabled(false);
        removeFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFileActionPerformed(evt);
            }
        });
        jPanel7.add(removeFile);

        cancelInput.setFont(new java.awt.Font("Calibri", 0, 13));
        cancelInput.setText("Cancel");
        cancelInput.setToolTipText("Stop reading files");
        cancelInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelInputActionPerformed(evt);
            }
        });
        jPanel7.add(cancelInput);
        jPanel7.add(jPanel6);

        jPanel23.add(jPanel7);

        jPanel13.setBackground(new java.awt.Color(41, 119, 167));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 13));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("FILTER OPTIONS");
        jPanel13.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel13);

        jPanel5.add(jPanel23, java.awt.BorderLayout.PAGE_END);

        jPanel22.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel22, java.awt.BorderLayout.CENTER);

        add(jPanel1);

        jTabbedPane1.setFont(new java.awt.Font("Calibri", 0, 13));

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel21.setFont(new java.awt.Font("Calibri", 0, 12));
        jPanel21.setLayout(new java.awt.GridLayout(12, 1, 0, 5));

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 14));
        jLabel10.setText("Extraction Summary:");
        jPanel21.add(jLabel10);

        added.setFont(new java.awt.Font("Calibri", 0, 13));
        added.setText("Number of Annotations Added:");
        added.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        added.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addedMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addedMouseEntered(evt);
            }
        });
        jPanel21.add(added);

        conflicts.setFont(new java.awt.Font("Calibri", 0, 13));
        conflicts.setText("Number of New Conflicts:");
        conflicts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        conflicts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                conflictsMouseClicked(evt);
            }
        });
        jPanel21.add(conflicts);

        classFiltered.setFont(new java.awt.Font("Calibri", 0, 13));
        classFiltered.setText("Number of Annotations Filtered By Class:");
        classFiltered.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        classFiltered.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                classFilteredMouseClicked(evt);
            }
        });
        jPanel21.add(classFiltered);

        annotatorFiltered.setFont(new java.awt.Font("Calibri", 0, 13));
        annotatorFiltered.setText("Number of Annotations Filtered By Annotator:");
        annotatorFiltered.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        annotatorFiltered.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annotatorFilteredMouseClicked(evt);
            }
        });
        jPanel21.add(annotatorFiltered);

        bothFiltered.setFont(new java.awt.Font("Calibri", 0, 13));
        bothFiltered.setText("Number of Annotations Filtered By Both:");
        bothFiltered.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bothFiltered.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bothFilteredMouseClicked(evt);
            }
        });
        jPanel21.add(bothFiltered);
        jPanel21.add(jLabel15);

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 14));
        jLabel11.setText("Dictionary Statistics:");
        jPanel21.add(jLabel11);

        totalEntries.setFont(new java.awt.Font("Calibri", 0, 13));
        totalEntries.setText("Number of Entries:");
        jPanel21.add(totalEntries);

        totalClasses.setFont(new java.awt.Font("Calibri", 0, 13));
        totalClasses.setText("Number of Classes:");
        totalClasses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        totalClasses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                totalClassesMouseClicked(evt);
            }
        });
        jPanel21.add(totalClasses);

        totalConflicts.setFont(new java.awt.Font("Calibri", 0, 13));
        totalConflicts.setText("Number of Conflicts:");
        jPanel21.add(totalConflicts);

        jPanel10.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        infoList.setFont(new java.awt.Font("Calibri", 0, 13));
        jScrollPane3.setViewportView(infoList);

        jPanel10.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Process Summary", jPanel10);

        jPanel2.setLayout(new java.awt.BorderLayout());

        results.setEditable(false);
        jScrollPane2.setViewportView(results);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Text Output", jPanel2);

        add(jTabbedPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void addFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addFileActionPerformed
    {//GEN-HEADEREND:event_addFileActionPerformed
        openFileDialog();
}//GEN-LAST:event_addFileActionPerformed

    private void removeFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeFileActionPerformed
    {//GEN-HEADEREND:event_removeFileActionPerformed
        eraseSelection();
    }//GEN-LAST:event_removeFileActionPerformed
    
    private void createActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_createActionPerformed
    {//GEN-HEADEREND:event_createActionPerformed
        //If the button is set as "Stop" then kill the thread and return.
        if (this.create.getText().equals("Stop"))
        {
            //Set variables to notify thread that it has been stopped and return.
            kill();
            return;
        }

        //Set annotator/classes filters for the extraction
        setFilters();

        //If valid information then do the extraction.
        if (checkValid())
        {
            //Set GUI up for extraction
            setForExtraction();

            //Clear the infoList from any previous info
            infoList.setListData(new Vector());
            //Run the extraction
            runProcess(destination.getText(), overwrite.isSelected(), listElements);
        }
    }//GEN-LAST:event_createActionPerformed

    private void workingSetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_workingSetActionPerformed
    {//GEN-HEADEREND:event_workingSetActionPerformed
        //Extract the working set if the user wants the working set.
        if (workingSet.isSelected())
        {
            listElements.addAll(reader.extractWorkingSet());
        }
        //Remove all articles that are from the working set if it is not selected.
        else
        {
            listElements.removeAll(reader.extractWorkingSet());
        }

        //reDraw the file list.
        reDrawList();
    }//GEN-LAST:event_workingSetActionPerformed

    private void classFilteredMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_classFilteredMouseClicked
    {//GEN-HEADEREND:event_classFilteredMouseClicked
        Vector v = new Vector();
        v.addAll(filteredByClass);
        infoList.setListData(v);
    }//GEN-LAST:event_classFilteredMouseClicked

    private void conflictsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_conflictsMouseClicked
    {//GEN-HEADEREND:event_conflictsMouseClicked
        Vector v = new Vector();
        v.addAll(newConflicts);
        infoList.setListData(v);
    }//GEN-LAST:event_conflictsMouseClicked

    private void annotatorFilteredMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_annotatorFilteredMouseClicked
    {//GEN-HEADEREND:event_annotatorFilteredMouseClicked
        Vector v = new Vector();
        v.addAll(filteredByAnnotator);
        infoList.setListData(v);
    }//GEN-LAST:event_annotatorFilteredMouseClicked

    private void bothFilteredMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_bothFilteredMouseClicked
    {//GEN-HEADEREND:event_bothFilteredMouseClicked
        Vector<dictEntry> v = new Vector<dictEntry>();
        v.addAll(filteredByBoth);
        infoList.setListData(v);
    }//GEN-LAST:event_bothFilteredMouseClicked

    private void totalClassesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_totalClassesMouseClicked
    {//GEN-HEADEREND:event_totalClassesMouseClicked
        Vector v = new Vector();
        v.addAll(classes);
        infoList.setListData(v);
    }//GEN-LAST:event_totalClassesMouseClicked

    private void addedMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_addedMouseClicked
    {//GEN-HEADEREND:event_addedMouseClicked
        Vector<dictEntry> v = new Vector<dictEntry>();
        v.addAll(newAnnotations);
        infoList.setListData(v);
    }//GEN-LAST:event_addedMouseClicked

    private void addedMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_addedMouseEntered
    {//GEN-HEADEREND:event_addedMouseEntered
        added.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_addedMouseEntered

    private void browserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browserActionPerformed
    {//GEN-HEADEREND:event_browserActionPerformed
        openSingleFileDialog();
    }//GEN-LAST:event_browserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        classList.clearSelection();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        annotatorList.clearSelection();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jList1MouseClicked
    {//GEN-HEADEREND:event_jList1MouseClicked
        
    }//GEN-LAST:event_jList1MouseClicked

    private void cancelInputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelInputActionPerformed
    {//GEN-HEADEREND:event_cancelInputActionPerformed
        killInput = true;
        cancelInput.setVisible(false);
    }//GEN-LAST:event_cancelInputActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jList1ValueChanged
    {//GEN-HEADEREND:event_jList1ValueChanged
        if(jList1.getSelectedIndex() >= 0)
            removeFile.setEnabled(true);
        else
            removeFile.setEnabled(false);
    }//GEN-LAST:event_jList1ValueChanged
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods - Run Seperate Threads for Lengthy Operations">
    /**
     * This function will asynchronously write the input files to the destination.
     * @param destination - the String representing the absolute path of the destination file.
     * @param overWrite - true to overwrite the destination file, false to append
     * @param fileNames - all absolute filenames of the input files
     */
    private void runProcess(final String destination, final boolean overWrite, final ArrayList<Article> articles)
    {
        //Create a new thread to do the writing.
        new Thread()
        {

            @Override
            public void run()
            {
                //Write the dictionary
                reader.createDict(destination, overWrite, articles);

                //enable gui after computation is done.
                resetGUI();
                enableGUI();
            }
        }.start();
    }

    /**
     * Extract all of the classes from all of the input files.
     * @param fileNames - the list of input files.
     */
    private void runClassExtraction(final ArrayList<Article> articles)
    {
        new Thread()
        {

            @Override
            public void run()
            {
                TreeSet<String> classes = reader.getAllClasses(articles);
                setClasses(classes);
                enableGUI();
            }
        }.start();
    }

    /**
     * extract all of the annotators from all of the input files.
     * @param fileNames - the input fileNames
     */
    private void runAnnotatorExtraction(final ArrayList<Article> articles)
    {
        new Thread()
        {

            @Override
            public void run()
            {
                TreeSet<Annotator> annotators = reader.getAllAnnotators(articles);
                setAnnotators(annotators);
                enableGUI();
            }
        }.start();
    }

    private void preprocessFiles(final ArrayList<String> files)
    {
        cancelInput.setVisible(true);
        reading = new Thread()
        {

            @Override
            public void run()
            {
                ArrayList<Article> temp = reader.readAll(files);
                if(temp!= null)
                    listElements.addAll(temp);
                reDrawList();
                cancelInput.setVisible(false);
            }
        };
        reading.start();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="GUIInterface Methods">
    /**
     * This method should be called whenever a message needs to be shown to the user.
     * @param error - the message to be shown.
     */
    public void displayMessage(String error)
    {
        jOptionPane1.showMessageDialog(this, error);
    }
    /**
     * This method will be used to display the output for outside processes.
     * @param text
     */
    public void output(String text)
    {
        this.results.setText(results.getText() + text);
    }

    /**
     * Set the percentage for the progress bar and label what the process is.
     *
     * @param percent - percent completion
     * @param process - current process name
     */
    public void setProgressPercentage(int percent, String process)
    {
        //If the percentage isn't within our range just return.
        if (percent > 100 || percent < 0)
        {
            return;
        }
        //If process is complete then make progress bar disappear.
        else if (percent == 100)
        {
            jProgressBar1.setVisible(false);
            updateInfo();
        }
        //Update progress bar value and text.
        else
        {
            jProgressBar1.setValue(percent);
            jProgressBar1.setVisible(true);
            jProgressBar1.setString(process);
            jProgressBar1.setStringPainted(true);
            jProgressBar1.revalidate();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GUI Components">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFile;
    private javax.swing.JLabel added;
    private javax.swing.JLabel annotatorFiltered;
    private javax.swing.JList annotatorList;
    private javax.swing.JCheckBox append;
    private javax.swing.JLabel bothFiltered;
    private javax.swing.JButton browser;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelInput;
    private javax.swing.JLabel classFiltered;
    private javax.swing.JList classList;
    private javax.swing.JLabel conflicts;
    private javax.swing.JButton create;
    private javax.swing.JTextField destination;
    private javax.swing.JList infoList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JList jList3;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox overwrite;
    private javax.swing.JButton removeFile;
    private javax.swing.JTextPane results;
    private javax.swing.JLabel totalClasses;
    private javax.swing.JLabel totalConflicts;
    private javax.swing.JLabel totalEntries;
    private javax.swing.JCheckBox workingSet;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
