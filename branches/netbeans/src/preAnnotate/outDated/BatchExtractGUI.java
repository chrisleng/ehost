package preAnnotate.outDated;

import preAnnotate.outDated.BatchFolderExtract;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.Dimension;
import javax.swing.JProgressBar;
import java.awt.TextArea;
import preAnnotate.format.Annotator;

/**
 * This class represents a graphical user interface that allows a user to
 * extract annotations from .pins files. The user can specify a source,
 * destination, and the annotator from which to extract annotations.
 *
 * @author Kyle
 *
 */
public class BatchExtractGUI extends JFrame implements ActionListener, PropertyChangeListener
{
    // Member variables

    // The allowable filter choices
    private String[] filterChoices = new String[]
    {
        "None", "Consensus", "Let me choose"
    };
    private String[] mainChoices = new String[]
    {
        "Create Annotations",
        "Create XML from Existing Annotations",
        "Create Annotations and XML"
    };
    // This variable will save the last directory where a source file/folder was
    // chosen in the source file browser.
    private String lastPath = null;
    private String xmlDirectory = null;

    // The states for the GUI
    private enum State
    {

        INITIAL, MAINPROCESSING, FILTERPROCESSING, WAITINGFORFILTERCHOICE, DONE
    }
    // If a computation is currently taking place
    boolean computing = false;
    private State state = State.INITIAL; // @jve:decl-index=0:
    // Not really sure.. JFrame requires this
    private static final long serialVersionUID = 1L;
    private JFrame jFrame = null; //@jve:decl-index=0:visual-constraint="10,296"
    // Main content Panel
    private JPanel jContentPane = null;
    // Components for the MenuBar
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    // Main GUI components
    private JLabel annotationOptionsLabel = null;
    private JLabel mainChoiceLabel = null;
    private Choice mainChoice = null;
    private JLabel Source = null;
    private JTextField SourcePath = null;
    private JLabel Destination = null;
    private JTextField DestinationPath = null;
    private JCheckBox overWrite = null;
    private JLabel overWriteLabel = null;
    private JLabel appendLabel = null;
    private JLabel Filter = null;
    private JCheckBox append = null;
    private JButton Convert = null;
    public String result = ""; // @jve:decl-index=0:
    private Choice filterChoice = null;
    private JProgressBar progressBar = null;
    private TextArea output = null;
    // Buttons to allow file browsing
    private JButton BrowseSource = null;
    private JButton BrowseDestination = null;
    // Objects that are used to make asynchronous calls to BatchFolderExtract
    private MainProcessing processor;
    private GetFilters filterProcessor;
    // Variables required to extract annotations and use filters.
    private BatchFolderExtract theBatch; // @jve:decl-index=0:
    private ArrayList<Annotator> batchAnnotators;
    private ArrayList<String> batchClasses; // @jve:decl-index=0:
    // About dialog stuff
    private JDialog aboutDialog = null; // @jve:decl-index=0:visual-constraint=
    // "12,10"
    private JPanel aboutContentPane = null;
    private JLabel aboutVersionLabel = null;
    private JTextArea jTextArea = null;
    //Main GUI filter choices
    private JLabel classFilter = null;
    private Choice classFilterChoice = null;
    private JLabel filtersLabel = null;
    //Main GUI xml components
    private JLabel xmlDestinationLabel = null;
    private JLabel xmlOptionsLabel = null;
    private JTextField xmlDirectory1 = null;
    private JButton xmlDestinationBrowser = null;
    private JLabel XMLInput = null;
    private JTextField inputFile = null;
    private JButton xmlInputBrowser = null;

    /**
     * This class allows the GUI to make an asynchronous call to
     * BatchFolderExtract to get the annotators, when the call is done then the
     * GUI is notified and the output for the GUI is updated.
     *
     * @author Kyle
     *
     */
    private class GetFilters extends SwingWorker<String, Void>
    {

        /**
         * This is where the annotators are processed. It is done asynchronously
         * so the GUI will not freeze.
         */
        @Override
        public String doInBackground()
        {
            result = "Processing";

            // This is a property that the GUI is linked too
            // When this value changes the GUI can take action
            setProgress(0);
            computing = true;

            // Extract source/destination files
            String source = getSourcePath().getText();
            String destination = getDestinationPath().getText();

            // If the destination file already has a .annotations extension
            // remove it so
            // the program doesn't add another one.
            if (destination.length() >= 12)
            {
                if (destination.substring(destination.length() - 12).equals(".annotations"))
                {
                    destination = destination.substring(0, destination.length() - 12);
                }
            }

            // Disable gui components for computation
            disableGUI();

            // Extract annotators if choice is "Let me choose".
//            theBatch = new BatchFolderExtract(source, destination);
            if (getFilterChoice().getSelectedItem().equals(filterChoices[2]))
            {
                batchAnnotators = theBatch.getHumanAnnotators();
            }
            // Extract classes if choice is "Let me choose"
            if (getClassFilterChoice().getSelectedItem().equals(filterChoices[2]))
            {
                batchClasses = theBatch.getAllClasses();
            }

            // Change progress property so GUI is aware that the computation is
            // done.
            setProgress(100);
            result = "Filters found! Choose one to continue.";
            return result;
        }
    }

    /**
     * This class allows the GUI to make an asynchronous call to
     * BatchFolderExtract to extract the annotations, when the call is done then
     * the GUI is notified and the output for the GUI is updated. This is only
     * used for the "none" and "consensus" filters.
     *
     * @author Kyle
     *
     */
    private class MainProcessing extends SwingWorker<String, Void>
    {

        /**
         * This is where the main work for the program is done. It is done
         * asynchronously so the GUI will not freeze.
         */
        @Override
        public String doInBackground()
        {
            // Clear out the result, disable the gui, and set some variables.
            result = "";
            disableGUI();
            computing = true;
            if (getMainChoice().getSelectedItem().equals(mainChoices[0]))
            {
                extractAnnotations(1);
            }
            else if (getMainChoice().getSelectedItem().equals(mainChoices[1]))
            {
                extractXML(1, 0);
            }
            else if (getMainChoice().getSelectedItem().equals(mainChoices[2]))
            {
                extractAnnotations(2);
                extractXML(2, 50);
            }

            // Make sure the progress gets set to 100 so the GUI updates
            // correctly.
            setProgress(100);
            return result;

        }

        /**
         * This method will return the destination file with the file extension removed.
         * @return - the path name entered by the user.
         */
        private String getActualDestination()
        {
            String destination = getDestinationPath().getText();

            // If the destination file already has a .annotations extension
            // remove it so
            // the program doesn't add another one.
            if (destination.length() >= 12)
            {
                if (destination.substring(destination.length() - 12).equals(".annotations"))
                {
                    destination = destination.substring(0, destination.length() - 12);
                }
            }
            return destination;
        }

        private boolean initializeAnnotation()
        {
            // Extract the append values from the check boxes
            boolean toAppend;
            if (getOverWrite().isSelected())
            {
                toAppend = false;
            }
            else
            {
                toAppend = true;
            }

            // Extract source/destination files
            String source = getSourcePath().getText();
            String destination = getActualDestination();

            // Create a new BatchFolderExtract object if it is null.
            if (theBatch == null)
            {
//                theBatch = new BatchFolderExtract(source, destination);
            }
            boolean goodToGo = true;
            // Clear the destination file if the user wants to.
            if (!toAppend)
            {
               // goodToGo = theBatch.clearDestinationFile();
            }
            else
            {
                if (!new File(destination).exists())
                {
                    goodToGo = false;
                }
            }
            if (!goodToGo)
            {
                result += "[ERROR]Problem with source or destination file. Check your path names and try again.\n";
            }
            return goodToGo;
        }

        /**
         * This method will extract the XML from a .annotations file.
         * @param progressDenom - The number of processes
         * @param alreadyComplete - The current position of the progress bar
         */
        private void extractXML(float progressDenom, int alreadyComplete)
        {

            // Extract output directory name
            String output = getXmlDirectory1().getText();
            // Extract input file name
            String input = "";
            if (mainChoice.getSelectedItem().equals(mainChoices[2]))
            {
                input = getActualDestination() + ".annotations";
            }
            else
            {
                input = getInputFile().getText();
            }
            if (new File(output).isDirectory() && new File(input).isFile())
            {
                /*
                result += "************Start Of XML Extraction*************\n";


                // Create the extractor
                AnnotationsToXML extractor = new AnnotationsToXML(input, output);
                // While the xml extractor has more files... extract them.
                while (extractor.hasNext())
                {
                    // Add the processing results to the result string.
                    result += extractor.extractNext();
                    System.out.println(((int) (alreadyComplete + ((float) ((extractor.getCompletePercentage()) * 99) / progressDenom))));
                    setProgress((int) (alreadyComplete + ((float) ((extractor.getCompletePercentage()) * 99) / progressDenom)));
                }
                result += "************End Of XML Extraction*************\n";
                result += extractor.getEndSummary();
                 *
                 */
            }
            else
            {
                result += "[ERROR]Invalid destination or input file for XML. Check your path names and try again.\n";
            }
        }

        /**
         * This method will extract the annotations from a .pins file.
         * @param progressDenom - the number of processes in this computation
         */
        private void extractAnnotations(float progressDenom)
        {

            //only extract if initialized properly
            if (initializeAnnotation())
            {
                // If the user selected 'None' then set chosen annotators to
                // null.
                if (getFilterChoice().getSelectedItem().equals(filterChoices[0]))
                {
                    theBatch.setChosenAnnotators(null);
                } // If the user selected 'Consensus' then set chosen annotators
                // to the set annotators.
                else
                {
                    if (getFilterChoice().getSelectedItem().equals(filterChoices[1]))
                    {
                        theBatch.setChosenAnnotators(theBatch.getSetAnnotators());
                    } // Get the chosen filter annotator choices if any.
                    else
                    {
                        if (batchAnnotators != null)
                        {
                            ArrayList<Annotator> toSend = new ArrayList<Annotator>();
                            toSend.add(batchAnnotators.get(getFilterChoice().getSelectedIndex()));
                            theBatch.setChosenAnnotators(toSend);
                        }
                    }
                }

                // Check to see if the user chose "None".
                if (getClassFilterChoice().getSelectedItem().equals(filterChoices[0]))
                {
                    theBatch.setChosenClasses(null);
                } // If the user has chosen filter classes then set the chosen
                // classes.
                else
                {
                    if (batchClasses != null)
                    {
                        ArrayList<String> toSend = new ArrayList<String>();
                        toSend.add(batchClasses.get(getClassFilterChoice().getSelectedIndex()));
                        theBatch.setChosenClasses(toSend);
                    }
                }
                // If the selected source folder didn't contain any .pins file
                // notify the user.
                if (!theBatch.hasNext())
                {
                    result = "The source file you chose has no .pins files.\n";
                    return;
                }

                // Extract annotations file by file updating progress after each
                // file.
                result += "************Start Of Annotation Extraction*************\n";
                while (theBatch.hasNext())
                {
                    result += theBatch.extractNext();
                    System.out.println("Progess: " + (int) ((theBatch.getCompletePercentage() * 99) / progressDenom));
                    System.out.println(theBatch.getCompletePercentage() + " " + progressDenom);
                    setProgress((int) ((theBatch.getCompletePercentage() * 99) / progressDenom));
                }
                result += "************End of Annotation Extraction*************\n";
                // Get the end summary for the annotation extraction
                result += theBatch.getEndSummary();
            }
        }
    }

    /**
     * This method will reset the state of any component that we will change
     * throughout the computation. This does not include the output textbox, as
     * the user will want that to stay so they can view the output.
     */
    private void resetGUI()
    {
        // Make sure computing is false, and make progress bar invisible.
        computing = false;
        getProgressBar().setVisible(false);
        getProgressBar().setValue(0);

        // Clear out annotator filter choices and reset them to original
        // choices.
        getFilterChoice().removeAll();


        for (String choice : filterChoices)
        {
            getFilterChoice().add(choice);


        }

        // Remove all class filter choices and set them to original choices.
        getClassFilterChoice().removeAll();
        getClassFilterChoice().add(filterChoices[0]);
        getClassFilterChoice().add(filterChoices[2]);

        // Null out some member variables so they aren't used again in the next
        // batch.
        theBatch = null;
        batchClasses = null;
        batchAnnotators = null;


    }

    /**
     * This class will disable all of the components that the user could use to
     * change information that we don't want changed.
     */
    private void disableGUI()
    {
        getSourcePath().setEnabled(false);
        getDestinationPath().setEnabled(false);
        getAppend().setEnabled(false);
        getOverWrite().setEnabled(false);
        getBrowseSource().setEnabled(false);
        getBrowseDestination().setEnabled(false);
        getFilterChoice().setEnabled(false);
        getConvert().setEnabled(false);
        getClassFilterChoice().setEnabled(false);
        getInputFile().setEnabled(false);
        getXmlInputBrowser().setEnabled(false);
        getBrowseSource().setEnabled(false);
        getXmlDestinationBrowser().setEnabled(false);
        getXmlDirectory1().setEnabled(false);
        getMainChoice().setEnabled(false);


    }

    /**
     * This method will be called whenever a property that is linked to the GUI
     * is changed. Currently only the progress property is connected. When other
     * threads change this property this method is called in the main thread.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName() == "progress")
        {
            System.out.println("Caught property change");
            updateComponents();


        }
    }

    /**
     * Called when the Convert button is pressed. This function starts the
     * asynchronous calls to BatchFolderExtract.
     */
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        output.setText("Processing...");

        // (First Case)If we're waiting on the users choice when this button is
        // pressed then we can now spawn
        // a new thread to do the annotation extraction from the chosen
        // annotator
        // OR
        // (SecondCase)We're in the initial state and the user did not choose
        // Let me choose for either filter,
        // so we can just extract the annotations.


        if (state == State.WAITINGFORFILTERCHOICE
                || (!getFilterChoice().getSelectedItem().equals(filterChoices[2])
                && !getClassFilterChoice().getSelectedItem().equals(filterChoices[2]) && state == State.INITIAL))
        {
            // Use the processor to process the annotations
            processor = new MainProcessing();

            // This allows the worker object to notify the GUI when it has
            // completed the computation.
            processor.addPropertyChangeListener(this);

            // begin the asynchronous call.
            processor.execute();

            // Change state and update components with the new state
            state = State.MAINPROCESSING;
            updateComponents();


        }
        // If we're in the intial state and the user has selected
        // "Let me choose" for a filter then we need
        // to get all of the filter choices so they can choose.
        else if ((getFilterChoice().getSelectedItem().equals(filterChoices[2]) || getClassFilterChoice().getSelectedItem().equals(filterChoices[2]))
                && state == State.INITIAL)
        {
            state = State.FILTERPROCESSING;
            filterProcessor = new GetFilters();
            filterProcessor.addPropertyChangeListener(this);
            filterProcessor.execute();
            updateComponents();


        }


    }

    /**
     * This class will update the GUI based on it's current state and the
     * current user input. It should be called whenever the state of the GUI or
     * the state of an individual component has changed.
     */
    private void updateComponents()
    {
        switch (state)
        {
            // Do the initial updating, to keep all components in the right
            // state.
            case INITIAL:
            {
                initialUpdate();


                break;


            } // Update while processing the annotators
            case FILTERPROCESSING:
            {
                filterProcessingUpdate();


                break;


            } // Updating for main annotation extraction
            case MAINPROCESSING:
            {
                mainProcessingUpdate();


                break;


            } // Updates while waiting for user to choose filters
            case WAITINGFORFILTERCHOICE:
            {
                waitingForFilterChoiceUpdate();


                break;


            } // Update after mainProcessing is done
            case DONE:
            {
                doneUpdate();


                break;


            }
        }
    }

    /**
     * This should be called whenever the user selects a different process to perform.
     * This method will enable the appropriate components for the user's chosen
     * process.
     */
    private void choiceEnable()
    {
        //If the user only wants to extract annotations then enable the required
        //components and clear out some textBoxes.
        if (getMainChoice().getSelectedItem().equals(mainChoices[0]))
        {
            enableForAnnotations();
            getInputFile().setText("");
            getXmlDirectory1().setText("");


        }
        //If the user only wants to extract XML then only enable the required XML
        //extraction components and clear out some textBoxes.
        else if (getMainChoice().getSelectedItem().equals(mainChoices[1]))
        {
            enableForXML();
            getConvert().setText("Output XML");
            getSourcePath().setText("");
            getDestinationPath().setText("");
            getXmlDirectory1().setText(xmlDirectory);


        }
        //If the user wants to extract annotations and xml then enable all components
        //and clear out some textBoxes.
        else if (getMainChoice().getSelectedItem().equals(mainChoices[2]))
        {
            enableForBoth();
            getSourcePath().setText("");
            getXmlDirectory1().setText(xmlDirectory);


        }
        //If the user selected anything other than the process choices then disable
        //The gui, but keep the process choice component enabled.
        else
        {
            this.disableGUI();
            getMainChoice().setEnabled(true);


        }
    }

    /**
     * Enable all components necessary for both xml extraction and annotation extraction.
     */
    private void enableForBoth()
    {
        //Enable all components
        getXmlDirectory1().setEnabled(true);
        getXmlDestinationBrowser().setEnabled(true);
        getXmlInputBrowser().setEnabled(false);
        getSourcePath().setEnabled(true);
        getDestinationPath().setEnabled(true);
        getAppend().setEnabled(true);
        getOverWrite().setEnabled(true);
        getBrowseSource().setEnabled(true);
        getBrowseDestination().setEnabled(true);
        getFilterChoice().setEnabled(true);
        getClassFilterChoice().setEnabled(true);

        //The user does not need to select the input file for the XML because
        //It is just going to be the created annotations file.
        getInputFile().setText("Created annotations will be used.");
        getInputFile().setEnabled(false);



    }

    /**
     * Enable all necessary components for XML extraction.
     */
    private void enableForXML()
    {
        //Enable xml components
        getXmlDirectory1().setEnabled(true);
        getXmlDestinationBrowser().setEnabled(true);
        getXmlInputBrowser().setEnabled(true);
        getInputFile().setEnabled(true);
        getInputFile().setText("");

        //Disable annotation components
        getSourcePath().setEnabled(false);
        getDestinationPath().setEnabled(false);
        getAppend().setEnabled(false);
        getOverWrite().setEnabled(false);
        getBrowseSource().setEnabled(false);
        getBrowseDestination().setEnabled(false);
        getFilterChoice().setEnabled(false);
        getClassFilterChoice().setEnabled(false);


    }

    /**
     * Enable all necessary components for annotation extraction.
     */
    private void enableForAnnotations()
    {
        //Enable annotation components
        getSourcePath().setEnabled(true);
        getDestinationPath().setEnabled(true);
        getAppend().setEnabled(true);
        getOverWrite().setEnabled(true);
        getBrowseSource().setEnabled(true);
        getBrowseDestination().setEnabled(true);
        getFilterChoice().setEnabled(true);
        getClassFilterChoice().setEnabled(true);

        //Disable XML components
        getXmlDirectory1().setEnabled(false);
        getXmlDestinationBrowser().setEnabled(false);
        getXmlInputBrowser().setEnabled(false);
        getInputFile().setEnabled(false);


    }

    /**
     * Updates that keep the GUI in line while the user is filling out the information
     * for an annotation extraction
     * @return - true if all necessary information is filled out... false otherwise.
     */
    private boolean initialAnnotationUpdate()
    {
        // If the overWrite box is selected then disable the append checkbox.
        if (getOverWrite().isSelected())
        {
            getAppend().setEnabled(false);


        } // If the append box is selected make sure the overwrite checkbox is
        // disabled.
        else if (getAppend().isSelected())
        {
            getOverWrite().setEnabled(false);


        }
        // If neither is selected make sure they are both enabled.
        else if (!getAppend().isSelected() && !getOverWrite().isSelected())
        {
            getOverWrite().setEnabled(true);
            getAppend().setEnabled(true);


        } // If they user has selected "Let me choose" for either filter then
        // change the convert button text.
        if (getFilterChoice().getSelectedItem().equals(filterChoices[2])
                || getClassFilterChoice().getSelectedItem().equals(filterChoices[2]))
        {
            Convert.setText("Get Filter Choices");


        } // If "Let me choose" is not selected then the convert button should
        // say "Create Annotations"
        else
        {
            Convert.setText("Create Annotations");


        } // If we have all of the required information for a conversion from
        // .pins to annotations then enable
        // the button.
        if (!getSourcePath().getText().equals("") && !getDestinationPath().getText().equals("")
                && (getOverWrite().isSelected() || getAppend().isSelected()))
        {
            Convert.setEnabled(true);


            return true;


        } // If required information is missing then disable the convert button.
        else
        {
            Convert.setEnabled(false);


            return false;


        }
    }

    /**
     * This method will be called while the user is filling out the necessary information
     * for xml extraction.
     * @return - true if all necessary information is filled out, false otherwise.
     */
    private boolean initialXMLUpdate()
    {
        //If required information is filled out enable the button and return true.
        if (!getInputFile().getText().equals("") && !getXmlDirectory1().getText().equals(""))
        {
            getConvert().setEnabled(true);


            return true;


        } //If required information is not filled out then disable button and return false.
        else
        {
            getConvert().setEnabled(false);


            return false;


        }
    }

    /**
     * This method will be called while the user is filling out the necessary information for
     * both annotation and xml extraction.  When required informatin is filled out the
     * convert button will be enabled.
     */
    private void initialBothUpdate()
    {
        //If required information for both xml extraction and anotation extraction
        //are filled out then enable the button.
        if (initialXMLUpdate() && initialAnnotationUpdate())
        {
            getConvert().setEnabled(true);


        } //If required information is not filled out disable the button.
        else
        {
            getConvert().setEnabled(false);


        }
    }

    /**
     * This method will keep the components of the GUI in the correct state
     * based on user inputs.
     */
    private void initialUpdate()
    {
        //If only extract annotations then only update annotation components.
        if (getMainChoice().getSelectedItem().equals(mainChoices[0]))
        {
            initialAnnotationUpdate();


        } //If only extracting xml then only update xml components.
        else if (getMainChoice().getSelectedItem().equals(mainChoices[1]))
        {
            initialXMLUpdate();


        } //If extracting both xml and annotations then update components for both.
        else if (getMainChoice().getSelectedItem().equals(mainChoices[2]))
        {
            initialBothUpdate();


        }
    }

    /**
     * This process will disable the GUI for the filterProcessing and then put
     * the resulting filters in the filter choice components so that the user
     * can choose a filter.
     */
    private void filterProcessingUpdate()
    {
        // getProgressBar().setVisible(true);
        // TODO make filterProcessor make use of the Progress property.
        int progress = filterProcessor.getProgress();


        if (progress != 100)
        {
            getProgressBar().setValue(progress);
            computing = true;
            disableGUI();


        } // Annotators are processed so display results and move to next state.
        if (progress == 100)
        {

            // If the annotators were extracted(non-null) then put the choices
            // in the filterChoice Choice Component
            if (batchAnnotators != null)
            {
                // Remove all of the filter choices so we can add all of the
                // annotators.
                getFilterChoice().removeAll();


                for (Annotator annotator : batchAnnotators)
                {
                    getFilterChoice().add(annotator.getAnnotatorName() + " " + annotator.getAnnotatorID());


                } // Add a 'None' option
                getFilterChoice().add(filterChoices[0]);


            } // If the classes were extracted(non-null) then put the choices in
            // the classFilterChoice Choice Component
            if (batchClasses != null)
            {
                // Remove all of the filter choices so we can add all of the
                // classes.
                getClassFilterChoice().removeAll();


                for (String aClass : batchClasses)
                {
                    getClassFilterChoice().add(aClass);


                } // Add a 'None' option
                getClassFilterChoice().add(filterChoices[0]);


            }
            // Filter has been processed so now we're waiting on te user.
            state = State.WAITINGFORFILTERCHOICE;
            updateComponents();


        }
        getOutput().setText(result);


    }

    /**
     * This method will disable everything for the main processing, and change
     * the state when the progress of the process == 100.
     */
    private void mainProcessingUpdate()
    {
        //Make sure progress bar is visible
        getProgressBar().setVisible(true);

        //Get the value of the progress bar.


        int progress = processor.getProgress();

        //If the computation is not complete... update the progress bar and
        //display results


        if (progress != 100)
        {
            System.out.println("Updating GUI... not done.");
            getProgressBar().setValue(progress);
            computing = true;
            disableGUI();
            getOutput().setText(result);


        }

        //If computation is complete, move to 'DONE' state and update.
        if (progress == 100)
        {
            System.out.println("Updating GUI... done.");
            state = State.DONE;
            updateComponents();


        }
    }

    /**
     * This method will keep the enable the filterChoices if they have been
     * processed and will disable everything that the user does not need for
     * choosing filters.
     */
    private void waitingForFilterChoiceUpdate()
    {
        // Disable the GUI
        disableGUI();

        // set computing to false since it isn't computing right now.
        computing = false;

        // Enable the filterChoice, classFilterChoice component and the convert
        // button


        if (!getFilterChoice().getItem(0).equals(filterChoices[0]))
        {
            getFilterChoice().setEnabled(true);


        }
        if (!getClassFilterChoice().getItem(0).equals(filterChoices[0]))
        {
            getClassFilterChoice().setEnabled(true);


        }
        //Enable convert button and change text to reflect actual process.
        getConvert().setEnabled(true);
        getConvert().setText("Create Annotations");

        // output the result of the finished computation
        output.setText("Annotators found! Choose one to continue.");


    }

    /**
     * This method will put the GUI back in it's initial state.
     */
    private void doneUpdate()
    {
        state = State.INITIAL;
        //Reset components
        resetGUI();

        //Enable the components for last chosen user process
        getMainChoice().setEnabled(true);
        choiceEnable();

        // output the result of the computation
        output.setText(result);

        // GUI is now in the initial state.
        updateComponents();


    }

    // *****ANYTHING BEYOND THIS POINT IS CODE AUTOMATICALLY GENERATED FROM
    // VISUAL EDITOR AND MOSTLY JUST CONTAINS
    // COMPONENT INITIALIZATION******
    /**
     * This method initializes this object
     *
     */
    public BatchExtractGUI(
            String xmlOutput)
    {
        super();
        xmlDirectory = xmlOutput;
        initialize();


    }

    /**
     * This method initializes this window
     *
     */
    private void initialize()
    {
        this.setSize(new Dimension(249, 112));



    }

    /**
     * This method initializes SourcePath - This is where the source file/folder
     * for the .pins files will be entered.
     *
     * @return javax.swing.JTextField
     */
    private JTextField getSourcePath()
    {
        if (SourcePath == null)
        {
            SourcePath = new JTextField();
            SourcePath.setBounds(new Rectangle(109, 125, 294, 20));
            // Listen for changes to the source Path and update components when
            // a change is found.
            SourcePath.addCaretListener(new javax.swing.event.CaretListener()
            {

                public void caretUpdate(javax.swing.event.CaretEvent e)
                {
                    updateComponents();


                }
            });


        }
        return SourcePath;


    }

    /**
     * This method initializes DestinationPath
     *
     * @return javax.swing.JTextField
     */
    private JTextField getDestinationPath()
    {
        if (DestinationPath == null)
        {
            DestinationPath = new JTextField();
            DestinationPath.setBounds(new Rectangle(109, 157, 295, 20));
            // Listen for changes to the destination path and update components
            // when a change is found.
            DestinationPath.addCaretListener(new javax.swing.event.CaretListener()
            {

                public void caretUpdate(javax.swing.event.CaretEvent e)
                {
                    updateComponents();


                }
            });


        }
        return DestinationPath;


    }

    /**
     * This method initializes overWrite
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getOverWrite()
    {
        if (overWrite == null)
        {
            overWrite = new JCheckBox();
            overWrite.setBounds(new Rectangle(267, 177, 21, 21));
            // Listen for changes to the overWrite check box and update
            // components when a change is found.
            overWrite.addChangeListener(new javax.swing.event.ChangeListener()
            {

                public void stateChanged(javax.swing.event.ChangeEvent e)
                {
                    updateComponents();


                }
            });


        }

        return overWrite;


    }

    /**
     * This method initializes append
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getAppend()
    {
        if (append == null)
        {
            append = new JCheckBox();
            append.setBounds(new Rectangle(380, 178, 21, 21));
            // Listen for changes to the append checkbos and update components
            // when a change is found.
            append.addChangeListener(new javax.swing.event.ChangeListener()
            {

                public void stateChanged(javax.swing.event.ChangeEvent e)
                {
                    updateComponents();



                }
            });


        }
        return append;


    }

    /**
     * This method initializes the Convert Button
     *
     * @return javax.swing.JButton
     */
    private JButton getConvert()
    {
        if (Convert == null)
        {
            Convert = new JButton();
            Convert.setEnabled(false);
            Convert.setBounds(new Rectangle(180, 300, 150, 25));
            Convert.setText("Create Annotations");


        }
        return Convert;


    }

    /**
     * This method initializes BrowseSource
     *
     * @return javax.swing.JButton
     */
    private JButton getBrowseSource()
    {
        if (BrowseSource == null)
        {
            BrowseSource = new JButton();
            BrowseSource.setText("Browse...");
            // Listen for changes to the source fileBrowser and update
            // components when a change is found.
            BrowseSource.setBounds(new Rectangle(408, 124, 88, 21));
            BrowseSource.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    JFileChooser aFile;


                    if (lastPath == null)
                    {
                        aFile = new JFileChooser();


                    }
                    else
                    {
                        aFile = new JFileChooser(lastPath);


                    }
                    aFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    aFile.showOpenDialog(getJContentPane());
                    File selFile = aFile.getSelectedFile();
                    lastPath = aFile.getCurrentDirectory().getAbsolutePath();
                    // If the user selected a path then put the pathname in the
                    // source textBox.


                    if (selFile != null)
                    {
                        SourcePath.setText(selFile.getAbsolutePath());


                    }

                }
            });


        }
        return BrowseSource;


    }

    /**
     * This method initializes BrowseDestination
     *
     * @return javax.swing.JButton
     */
    private JButton getBrowseDestination()
    {
        if (BrowseDestination == null)
        {
            BrowseDestination = new JButton();
            BrowseDestination.setBounds(new Rectangle(408, 157, 88, 20));
            BrowseDestination.setText("Browse...");
            // Listen for changes to the destination fileBrowser and update
            // components when a change is found.
            BrowseDestination.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    JFileChooser aFile = new JFileChooser();
                    aFile.showOpenDialog(getJContentPane());
                    File selFile = aFile.getSelectedFile();
                    // If the user selected a file then put it in the
                    // destination textBox.


                    if (selFile != null)
                    {
                        DestinationPath.setText(selFile.getAbsolutePath());


                    }
                }
            });


        }
        return BrowseDestination;


    }

    /**
     * This method initializes jContentPane - This content pane contains the
     * main components for this GUI.
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            XMLInput = new JLabel();
            XMLInput.setBounds(new Rectangle(47, 244, 63, 16));
            XMLInput.setFont(new java.awt.Font("Calibri", 1, 14));
            XMLInput.setText("Input File:");
            mainChoiceLabel = new JLabel();
            mainChoiceLabel.setBounds(new Rectangle(91, 8, 60, 16));
            mainChoiceLabel.setFont(new java.awt.Font("Calibri", 1, 14));
            mainChoiceLabel.setText("Process:");
            annotationOptionsLabel = new JLabel();
            annotationOptionsLabel.setBounds(new Rectangle(163, 48, 166, 23));
            annotationOptionsLabel.setFont(new java.awt.Font("Calibri", 1, 18));
            annotationOptionsLabel.setText("Annotations Options");
            xmlOptionsLabel = new JLabel();
            xmlOptionsLabel.setBounds(new Rectangle(195, 211, 103, 22));
            xmlOptionsLabel.setFont(new java.awt.Font("Calibri", 1, 18));
            xmlOptionsLabel.setText("XML Options");
            filtersLabel = new JLabel();
            filtersLabel.setBounds(new Rectangle(18, 77, 53, 25));
            filtersLabel.setFont(new java.awt.Font("Calibri", 1, 16));
            filtersLabel.setText("Filters");
            xmlDestinationLabel = new JLabel();
            xmlDestinationLabel.setBounds(new Rectangle(2, 271, 112, 16));
            xmlDestinationLabel.setFont(new java.awt.Font("Calibri", 1, 14));
            xmlDestinationLabel.setText("Output Directory:");
            classFilter = new JLabel();
            classFilter.setBounds(new Rectangle(83, 85, 40, 16));
            classFilter.setFont(new java.awt.Font("Calibri", 1, 14));
            classFilter.setText("Class:");
            appendLabel = new JLabel();
            appendLabel.setBounds(new Rectangle(322, 180, 55, 16));
            appendLabel.setFont(new java.awt.Font("Calibri", 1, 14));
            appendLabel.setText("Append");
            overWriteLabel = new JLabel();
            overWriteLabel.setBounds(new Rectangle(153, 181, 112, 16));
            overWriteLabel.setFont(new java.awt.Font("Calibri", 1, 14));
            overWriteLabel.setText("Overwrite/Create");
            Destination = new JLabel();
            Destination.setBounds(new Rectangle(29, 159, 74, 16));
            Destination.setFont(new java.awt.Font("Calibri", 1, 14));
            Destination.setText("Destination:");
            Filter = new JLabel();
            Filter.setBounds(new Rectangle(251, 86, 73, 16));
            Filter.setFont(new java.awt.Font("Calibri", 1, 14));
            Filter.setText("Annotator:");
            Source = new JLabel();
            Source.setBounds(new Rectangle(57, 126, 45, 16));
            Source.setFont(new java.awt.Font("Calibri", 1, 14));
            Source.setText("Source:");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(Source, null);
            jContentPane.add(getSourcePath(), null);
            jContentPane.add(Destination, null);
            jContentPane.add(Filter, null);
            jContentPane.add(getDestinationPath(), null);
            jContentPane.add(getOverWrite(), null);
            jContentPane.add(overWriteLabel, null);
            jContentPane.add(appendLabel, null);
            jContentPane.add(getAppend(), null);
            jContentPane.add(getConvert(), null);
            jContentPane.add(getFilterChoice(), null);
            Convert.setActionCommand("Convert");
            Convert.addActionListener(this);
            jContentPane.add(getBrowseDestination(), null);
            jContentPane.add(classFilter, null);
            jContentPane.add(getClassFilterChoice(), null);
            jContentPane.add(getProgressBar(), null);
            jContentPane.add(getOutput(), null);
            jContentPane.add(xmlDestinationLabel, null);
            jContentPane.add(filtersLabel, null);
            jContentPane.add(xmlOptionsLabel, null);
            jContentPane.add(getXmlDirectory1(), null);
            jContentPane.add(getXmlDestinationBrowser(), null);
            jContentPane.add(annotationOptionsLabel, null);
            jContentPane.add(mainChoiceLabel, null);
            jContentPane.add(getMainChoice(), null);
            jContentPane.add(getBrowseSource(), null);
            jContentPane.add(XMLInput, null);
            jContentPane.add(getInputFile(), null);
            jContentPane.add(getXmlInputBrowser(), null);


        }
        return jContentPane;


    }

    /**
     * This method initializes extensionChoice
     *
     * @return java.awt.Choice
     */
    private Choice getFilterChoice()
    {
        if (filterChoice == null)
        {
            filterChoice = new Choice();
            filterChoice.add(filterChoices[0]);
            filterChoice.add(filterChoices[1]);
            filterChoice.add(filterChoices[2]);
            filterChoice.setBounds(new Rectangle(329, 82, 112, 21));
            // Listen for changes to the filter choice and update components
            // when a change is found.
            filterChoice.addItemListener(new java.awt.event.ItemListener()
            {

                @Override
                public void itemStateChanged(ItemEvent arg0)
                {
                    updateComponents();



                }
            });



        }
        return filterChoice;


    }

    /**
     * This method initializes jFrame
     *
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame()
    {
        if (jFrame == null)
        {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(526, 497);
            jFrame.setResizable(false);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Annotations Extractor");


        }
        return jFrame;


    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar()
    {
        if (jJMenuBar == null)
        {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getHelpMenu());


        }
        return jJMenuBar;


    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu()
    {
        if (fileMenu == null)
        {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getExitMenuItem());


        }
        return fileMenu;


    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu()
    {
        if (helpMenu == null)
        {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.add(getAboutMenuItem());


        }
        return helpMenu;


    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem()
    {
        if (exitMenuItem == null)
        {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    System.exit(0);


                }
            });


        }
        return exitMenuItem;


    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem()
    {
        if (aboutMenuItem == null)
        {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            // Listen for changes to the About menu item and open the
            // aboutDialog when the About item is pressed.
            aboutMenuItem.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    // aboutDialog.setBounds(20,20,535,276);
                    aboutDialog.setSize(535, 276);
                    aboutDialog.setVisible(true);


                }
            });


        }
        return aboutMenuItem;


    }

    /**
     * This method initializes aboutDialog
     *
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog()
    {
        if (aboutDialog == null)
        {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("About");
            aboutDialog.setSize(new Dimension(535, 276));
            aboutDialog.setContentPane(getAboutContentPane());


        }
        return aboutDialog;


    }

    /**
     * This method initializes the JTextArea that will contain the instructions
     * for how to use this program.
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJTextArea()
    {
        if (jTextArea == null)
        {
            if (jTextArea == null)
            {
                jTextArea = new JTextArea();
                jTextArea.setBounds(new Rectangle(6, 40, 517, 203));
                jTextArea.setText("This program allows you to extract the annotations from a .pins file.\n"
                        + "Instructions:"
                        + "\n\tFirst, select the source file/folder where your .pins files are located.\n"
                        + "\tAny .pins file in the folder you select will be extracted and put into the\n"
                        + "\tdestination file of your choosing.\n\n"
                        + "\tNext, select the destination file for the annotations that will be extracted\n"
                        + "\tfrom all of your .pins files.\n\n"
                        + "\tThen, select whether you want annotations extracted from the .pins files to be\n"
                        + "\tappended to the annotations files that you selected for your destination or if\n"
                        + "\tyou want that file to be created or overwritten.");
                jTextArea.setEditable(false);


            }
        }
        return jTextArea;


    }

    /**
     * This method initializes aboutContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane()
    {
        if (aboutContentPane == null)
        {
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(null);
            aboutContentPane.add(getAboutVersionLabel(), null);
            aboutContentPane.add(getJTextArea(), null);


        }
        return aboutContentPane;


    }

    /**
     * This method initializes aboutVersionLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel()
    {
        if (aboutVersionLabel == null)
        {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel.setText("Version 1.0");
            aboutVersionLabel.setBounds(new Rectangle(211, 2, 87, 32));
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);


        }
        return aboutVersionLabel;


    }

    /**
     * This method initializes classFilterChoice
     *
     * @return java.awt.Choice
     */
    private Choice getClassFilterChoice()
    {
        if (classFilterChoice == null)
        {
            classFilterChoice = new Choice();
            classFilterChoice.setBounds(new Rectangle(129, 84, 112, 21));
            classFilterChoice.addItem(filterChoices[0]);
            classFilterChoice.addItem(filterChoices[2]);
            classFilterChoice.addItemListener(new java.awt.event.ItemListener()
            {

                @Override
                public void itemStateChanged(ItemEvent arg0)
                {
                    updateComponents();


                }
            });


        }
        return classFilterChoice;


    }

    /**
     * This method initializes progressBar
     *
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getProgressBar()
    {
        if (progressBar == null)
        {
            progressBar = new JProgressBar();
            progressBar.setBounds(new Rectangle(3, 437, 515, 10));
            progressBar.setVisible(false);


        }
        return progressBar;


    }

    /**
     * This method initializes output
     *
     * @return java.awt.TextArea
     */
    private TextArea getOutput()
    {
        if (output == null)
        {
            output = new TextArea();
            output.setEditable(false);
            output.setBackground(Color.WHITE);
            output.setBounds(new Rectangle(4, 332, 513, 102));


        }
        return output;


    }

    /**
     * This method initializes xmlDirectory1
     *
     * @return javax.swing.JTextField
     */
    private JTextField getXmlDirectory1()
    {
        if (xmlDirectory1 == null)
        {
            xmlDirectory1 = new JTextField();
            xmlDirectory1.setBounds(new Rectangle(116, 272, 290, 20));
            xmlDirectory1.setText(xmlDirectory);
            xmlDirectory1.addCaretListener(new javax.swing.event.CaretListener()
            {

                public void caretUpdate(javax.swing.event.CaretEvent e)
                {
                    updateComponents();


                }
            });


        }
        return xmlDirectory1;


    }

    /**
     * This method initializes xmlDestinationBrowser
     *
     * @return javax.swing.JButton
     */
    private JButton getXmlDestinationBrowser()
    {
        if (xmlDestinationBrowser == null)
        {
            xmlDestinationBrowser = new JButton();
            xmlDestinationBrowser.setBounds(new Rectangle(408, 272, 88, 19));
            xmlDestinationBrowser.setText("Browse...");
            xmlDestinationBrowser.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    JFileChooser aFile = new JFileChooser();
                    aFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    aFile.showOpenDialog(getJContentPane());
                    File selFile = aFile.getSelectedFile();
                    // If the user selected a file then put it in the
                    // destination textBox.


                    if (selFile != null)
                    {
                        getXmlDirectory1().setText(selFile.getAbsolutePath());


                    }
                }
            });


        }
        return xmlDestinationBrowser;


    }

    /**
     * This method initializes mainChoice
     *
     * @return java.awt.Choice
     */
    private Choice getMainChoice()
    {
        if (mainChoice == null)
        {
            mainChoice = new Choice();
            mainChoice.setBounds(new Rectangle(152, 6, 217, 21));
            mainChoice.addItemListener(new java.awt.event.ItemListener()
            {

                public void itemStateChanged(java.awt.event.ItemEvent e)
                {
                    choiceEnable();


                }
            });
            mainChoice.addItem("");


            for (String choice : mainChoices)
            {
                mainChoice.addItem(choice);


            }
        }
        return mainChoice;


    }

    /**
     * This method initializes inputFile
     *
     * @return javax.swing.JTextField
     */
    private JTextField getInputFile()
    {
        if (inputFile == null)
        {
            inputFile = new JTextField();
            inputFile.setBounds(new Rectangle(117, 242, 290, 20));
            inputFile.addCaretListener(new javax.swing.event.CaretListener()
            {

                public void caretUpdate(javax.swing.event.CaretEvent e)
                {
                    updateComponents();


                }
            });


        }
        return inputFile;


    }

    /**
     * This method initializes xmlInputBrowser
     *
     * @return javax.swing.JButton
     */
    private JButton getXmlInputBrowser()
    {
        if (xmlInputBrowser == null)
        {
            xmlInputBrowser = new JButton();
            xmlInputBrowser.setBounds(new Rectangle(409, 241, 88, 20));
            xmlInputBrowser.setText("Browse...");
            xmlInputBrowser.addActionListener(new java.awt.event.ActionListener()
            {

                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    JFileChooser aFile = new JFileChooser();
                    aFile.showOpenDialog(getJContentPane());
                    File selFile = aFile.getSelectedFile();
                    // If the user selected a file then put it in the
                    // destination textBox.


                    if (selFile != null)
                    {
                        getInputFile().setText(selFile.getAbsolutePath());


                    }
                }
            });


        }

        return xmlInputBrowser;


    }

    /**
     * Launches this application
     */
    public static void main(String[] args)
    {
        final String xmlOutput = ".\\xmloutput";
        SwingUtilities.invokeLater(new Runnable()
        {

            public void run()
            {
                BatchExtractGUI application = new BatchExtractGUI(xmlOutput);
                application.getJFrame().setVisible(true);
                application.disableGUI();
                application.getMainChoice().setEnabled(true);


            }
        });

    }
} // @jve:decl-index=0:visual-constraint="560,51"

