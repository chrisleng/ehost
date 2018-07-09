package preAnnotate.outDated;

import preAnnotate.readers.DictionaryExtractor;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Event;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import java.awt.Point;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Choice;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.TextArea;

/** This class represents a GUI that will allow a user to extract dictionary entries
 * from .annotation or .con files.
 *
 * @author Kyle
 *
 */
public class DictionaryGUI implements ActionListener, PropertyChangeListener
{
	//The three states that this GUI can be in.
	private enum State
	{
		INITIAL,
		PROCESSING,
		DONE
	}
	//The State of the GUI
	State state = State.INITIAL;  //  @jve:decl-index=0:

	//If a computation is currently taking place
	boolean computing = false;

	//The main Frame
	private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="146,6"

	//The main content pane
	private JPanel jContentPane = null;

	//Menu components
	private JMenuBar jJMenuBar = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem exitMenuItem = null;
	private JMenuItem aboutMenuItem = null;
	private JMenuItem cutMenuItem = null;
	private JMenuItem copyMenuItem = null;
	private JMenuItem pasteMenuItem = null;
	private JMenuItem saveMenuItem = null;

	//About dialog components
	private JDialog aboutDialog = null;
	private JPanel aboutContentPane = null;
	private JTextArea aboutText =null;

	//Main content pane components
	private JLabel SelectExtensions = null;
	private Choice extensionChoice = null;
	private JLabel sourcePath = null;
	private JTextField sourceText = null;
	private JLabel destination = null;
	private JTextField destinationText = null;
	private JButton create = null;
	private JButton sourceBrowser = null;
	private JButton destinationBrowse = null;

	//Used to pass the result of the computation from the worker thread to the GUI
	private String result;

	//worker is used to do the asynchronous call to the DictionaryExtractor
	public Task worker;

	private TextArea output = null;

	/**
	 * This class allows the GUI to make an asynchronous call to DictionaryExtractor, when
	 * the call is done then the GUI is notified and the output for the GUI is updated.
	 * @author Kyle
	 *
	 */
	public class Task extends SwingWorker<String, Void>
	{
		/**
		 * This is where the main work for the program is done.  It is done asynchronously so the
		 * GUI will not freeze.
		 */
		@Override
	    public String doInBackground()
    	{
			//This is a property that the GUI is linked too
			//When this value changes the GUI can take action
			setProgress(0);
			computing = true;

			//Extract source/destination file names
			String source = getSourceText().getText();
			String destination = getDestinationText().getText();

			//Disable gui components for computation.
			disableGUI();

			//Extract annotations
			DictionaryExtractor extractor = new DictionaryExtractor(source, destination, extensionChoice.getSelectedItem());
			result = extractor.addToDictionary();

			//Change progress property so GUI is aware that the computation is done.
			setProgress(100);
	        return result;
	    }
	}
	/**
	 * This method is responsible for keeping the state of all of the components consistent with user
	 * input and the overall state of the GUI.
	 */
	private void UpdateComponents()
	{
		switch(state)
		{
			case INITIAL:
			{
				//If we have all of the required information for a conversion from .pins to annotations then enable
				//the button.
				if(!getDestinationText().getText().equals("") && !computing)
					getCreate().setEnabled(true);

				//If required information is missing then disable the convert button.
				if(getDestinationText().getText().equals("")||getSourceText().getText().equals(""))
					getCreate().setEnabled(false);
				break;
			}
			case PROCESSING:
			{
				disableGUI();
				break;
			}
			case DONE:
			{
				computing = false;
	        	//Enable all gui components
	        	enableGUI();

				//reset source
	        	getSourceText().setText("");

	        	//output the result of the computation
	        	output.setText(result);

	        	//Go back to normal updating
	        	state = State.INITIAL;
	        	break;
			}
		}
	}
	/**
	 * This class will disable all of the components that the user could use to change
	 * information that shouldn't be changed during a computation.
	 */
	private void disableGUI()
	{
		getSourceText().setEnabled(false);
		getDestinationText().setEnabled(false);
		getExtensionChoice().setEnabled(false);
		getSourceBrowser().setEnabled(false);
		getDestinationBrowse().setEnabled(false);
		getCreate().setEnabled(false);
	}

	/**
	 * This class will enabled all of the components so the GUI is usable again.
	 */
	private void enableGUI()
	{
		getSourceText().setEnabled(true);
		getDestinationText().setEnabled(true);
		getExtensionChoice().setEnabled(true);
		getSourceBrowser().setEnabled(true);
		getDestinationBrowse().setEnabled(true);
	}

	/**
	 * This function is called when the Create/Add to dictionary button is pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		state = State.PROCESSING;
		UpdateComponents();
		output.setText("Processing...");
		//Initialize the worker thread.
		worker = new Task();
		//This allows the worker object to notify the GUI when it has completed the computation.
		worker.addPropertyChangeListener(this);
		//begin the asynchronous call.
		worker.execute();
	}
	/**
	 * This method is called in the main thread whenever a bound property changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		//When the progress of the computation changes, enable all components
		if ("progress" == evt.getPropertyName())
        {
			state = State.DONE;
			UpdateComponents();
        }

	}


	// ******** EVERYTHING BEYOND THIS POINT IS CODE AUTOMATICALLY GENERATED FROM VISUAL EDITOR AND
	//CONSISTS MAINLY OF COMPONENT INITIALIZATION****
	/**
	 * This method initializes the main frame for the GUI.
	 *
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame()
	{
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(525, 318);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Dictionary Creator");
			jFrame.setResizable(false);
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane, which is the main contentPane for the program.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null)
		{
			destination = new JLabel();
			destination.setBounds(new Rectangle(15, 91, 76, 16));
			destination.setText("Destination:");
			sourcePath = new JLabel();
			sourcePath.setBounds(new Rectangle(36, 52, 50, 16));
			sourcePath.setText("Source:");
			SelectExtensions = new JLabel();
			SelectExtensions.setBounds(new Rectangle(13, 18, 155, 16));
			SelectExtensions.setText("Select Source Extensions:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(SelectExtensions, null);
			jContentPane.add(getExtensionChoice(), null);
			jContentPane.add(sourcePath, null);
			jContentPane.add(getSourceText(), null);
			jContentPane.add(destination, null);
			jContentPane.add(getDestinationText(), null);
			jContentPane.add(getCreate(), null);
			getCreate().setActionCommand("Create");
	        getCreate().addActionListener(this);
			jContentPane.add(getSourceBrowser(), null);
			jContentPane.add(getDestinationBrowse(), null);
			jContentPane.add(getOutput(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
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
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			//Close window when exit is selected
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			//Open about content pane when About menu item is selected
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
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
	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
			aboutDialog.setSize(new Dimension(535, 276));
			aboutDialog.setResizable(false);
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutText(),null);

		}
		return aboutContentPane;
	}
	/** This method initializes JTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getAboutText()
	{
		if(aboutText == null)
		{

			aboutText = new JTextArea();
			aboutText.setEditable(false);
			//Instructions for the program
			aboutText.setText("This application will create a dictionary from .con or .annotation" +
					"\nfiles.  Two files will be created: One .preannotate file and one" +
					"\n.conflicts file.  The .preannotate file will contain the dictionary" +
					"\n,and the .conflicts file will contain all of the words that have" +
					"\nmultiple classes associated with it." +
					"\nInstructions:" +
						"\n\n     First Pick whether you want the words to be extracted from" +
						"\n     .annotation files .con files or both." +
						"\n\n     Then choose the soure file/folder for the .annotation or .con" +
						"\n     files." +
						"\n\n     Then Select the destination file. If it already exists, make sure" +
						"\n     that the .conflicts file is in the same folder.");
		}
		return aboutText;

	}
	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}

	/**
	 * This method initializes extensionChoice
	 *
	 * @return java.awt.Choice
	 */
	private Choice getExtensionChoice() {
		if (extensionChoice == null) {
			extensionChoice = new Choice();
			extensionChoice.add(".con");
			extensionChoice.add(".annotations");
			extensionChoice.add("both");
			extensionChoice.setBounds(new Rectangle(171, 14, 100, 21));
		}
		return extensionChoice;
	}

	/**
	 * This method initializes sourceText
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getSourceText() {
		if (sourceText == null) {
			sourceText = new JTextField();
			sourceText.setBounds(new Rectangle(97, 51, 297, 20));
			//Add a listener and update all components whenever the source text changes
			sourceText.addCaretListener(new javax.swing.event.CaretListener()
			{
				public void caretUpdate(javax.swing.event.CaretEvent e)
				{
					UpdateComponents();
				}
			});

		}
		return sourceText;
	}

	/**
	 * This method initializes destinationText
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getDestinationText() {
		if (destinationText == null) {
			destinationText = new JTextField();
			destinationText.setBounds(new Rectangle(95, 87, 299, 20));
			//Add a listener and update components whenever the destination text changes.
			destinationText.addCaretListener(new javax.swing.event.CaretListener()
			{
				public void caretUpdate(javax.swing.event.CaretEvent e)
				{
					UpdateComponents();
				}
			});
		}
		return destinationText;
	}

	/**
	 * This method initializes create
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getCreate() {
		if (create == null) {
			create = new JButton();
			create.setBounds(new Rectangle(149, 134, 182, 22));
			create.setEnabled(false);
			create.setText("Create/Add to dictionary");
		}
		return create;
	}

	/**
	 * This method initializes sourceBrowser
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSourceBrowser() {
		if (sourceBrowser == null) {
			sourceBrowser = new JButton();
			sourceBrowser.setBounds(new Rectangle(396, 52, 92, 17));
			sourceBrowser.setText("Browse...");
			//Add an action listener and set sourceText when a file is selected.
			sourceBrowser.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JFileChooser aFile = new JFileChooser();
					aFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					aFile.showOpenDialog(getJContentPane());
					File selFile = aFile.getSelectedFile();
					//If the user selected a path then put the pathname in the source textBox.
					if(selFile != null)
						getSourceText().setText(selFile.getAbsolutePath());

				}
			});
		}
		return sourceBrowser;
	}

	/**
	 * This method initializes destinationBrowse
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getDestinationBrowse() {
		if (destinationBrowse == null) {
			destinationBrowse = new JButton();
			destinationBrowse.setBounds(new Rectangle(397, 88, 89, 17));
			destinationBrowse.setText("Browse...");
			//Add a listener and change destination text when a file is chosen.
			destinationBrowse.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					JFileChooser aFile = new JFileChooser();
					aFile.showOpenDialog(getJContentPane());
					File selFile = aFile.getSelectedFile();
					//If the user selected a file then put it in the destination textBox.
					if(selFile != null)
						getDestinationText().setText(selFile.getAbsolutePath());
				}
			});
		}
		return destinationBrowse;
	}

	/**
	 * This method initializes output
	 *
	 * @return java.awt.TextArea
	 */
	private TextArea getOutput() {
		if (output == null) {
			output = new TextArea();
			output.setEditable(false);
			output.setBackground(Color.WHITE);
			output.setBounds(new Rectangle(3, 170, 517, 98));
		}
		return output;
	}
	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DictionaryGUI application = new DictionaryGUI();
				application.getJFrame().setVisible(true);
			}
		});
	}


}
