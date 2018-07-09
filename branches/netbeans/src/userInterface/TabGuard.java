/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface;

/**
 *
 * @author Chris
 */
 /**watch and remember which tab is latest actived.*/
    public class TabGuard{

        // values of tab selector
        public enum tabs{
            creationAnnotation, resulteditor, dictionariesSetting,
            pinExtractor, configuration, converter, exit, 
            assignmentsScreen
        };

        private static tabs latestLoadedTab;

        public static tabs getLatestActivedTab(){
            return latestLoadedTab;
        }

        public static void justActivedTab(tabs _tab){
            latestLoadedTab = _tab;
        }
    }