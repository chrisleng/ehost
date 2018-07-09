/*
 * The contents of this file are subject to the GNU GPL v3 (the "License"); 
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at URL http://www.gnu.org/licenses/gpl.html
 *
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 *
 * The Original Code is eHOST.
 *
 * The Initial Developer of the Original Code is University of Utah.  
 * Copyright (C) 2009 - 2012.  All Rights Reserved.
 *
 * eHOST was developed by the Division of Epidemiology at the 
 * University of Utah. Current information about eHOST can be located at 
 * http://http://code.google.com/p/ehost/
 * 
 * Categories:
 * Scientific/Engineering
 * 
 * Intended Audience:
 * Science/Research
 * 
 * User Interface:
 * Java Swing, Java AWT, Custom Components.
 * 
 * Programming Language
 * Java
 *
 * Contributor(s):
 *   Jianwei "Chris" Leng <Chris.Leng@utah.edu> (Original Author), 2009-2012
 *   Kyle Anderson added the Verifier Function, 2010 
 *   Annotation Admin Team added the Sync functions, 2011-2012
 *   
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import log.LogCleaner;

/**
 * The entrance class to load the GUI of eHOST and display the splash dialog
 * before the main frame finishing loading resource.
 * 
 * @author Jianwei "Chris" Leng
 */
public class eHOST {

	/** Initial works, before finishing loading the GUI. */
	private static void initial() {

		// check whether current OS is a Mac OS
		// and switchpool.iniMSP.isUnixOS = true if its a Mac OS / Unix;
		// we use '/' as the path separator
		env.Parameters.isUnixOS = commons.OS.isMacOS();

		// log begin
		String text = "#eHOST# Reading configure file ...";
		//log.LoggingToFile.log(Level.INFO, text);

		// display the splash window for this software
		userInterface.splashWindow.SplashController.showtext(text);

		// load eHOST system configure information
		config.system.SysConf.loadSystemConfigure();

		// set the flag, after loading configure information
		env.Parameters.isFirstTimeLoadingConfigureFile = false;

		// set the latest mention id to the module if XML output
		int latest_used_metion_id = env.Parameters.getLatestUsedMentionID();
		algorithmNegex.XMLMaker
				.set_mention_id_startpoint(latest_used_metion_id);
	}

	/**
	 * Main method of eHOST project. It's the entrance that used to start to
	 * create and display an instance of the primary GUI from class "GUI.java".
	 * */
	public static void main(String[] args) {

		String text = null;

		// start the splash window
		userInterface.splashWindow.SplashController.start();

		try {
                        LogCleaner deleteLocker = new LogCleaner();
                        deleteLocker.doit();
                                
			// init the log system
			//Logger logger = Logger.getLogger("eHOST");
			//log.LoggingToFile.setLogingProperties(logger, Level.ALL);
			//log.LoggingToFile.setLogger(logger);

			text = "#eHOST# Initializing ...";
			//log.LoggingToFile.log(Level.INFO, text);
			userInterface.splashWindow.SplashController.showtext(text);

			// read configure settings and load values into memory
			initial();

			// show the first sentence on the splash window
			text = "#eHOST# Launching the main GUI ...";
			//log.LoggingToFile.log(Level.INFO, text);                        
			userInterface.splashWindow.SplashController.showtext(text);

			// start loading the main GUI in a new thread
			new Thread() {
				@Override
				public void run() {

					// show dialog - main GUI window of eHOST project
					userInterface.GUI gui = new userInterface.GUI();
					gui.setVisible(true);
					resultEditor.loadingNotes.ShowNotes.setGUIHandler(gui);
                                        
				}
			}.start();

		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
