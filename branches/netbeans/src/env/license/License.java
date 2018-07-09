/*
 * The contents of this file are subject to the GNU GPL v3 (the "License"); 
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at URL http://www.gnu.org/licenses/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
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
 * Programmer:
 *   Jianwei "Chris" Leng <Chris.Leng@utah.edu> (Original Author), 2009-2012
 * 
 */
package env.license;

/**
 * This class control and decide which function is available to user based on 
 * a fixed file "ehost.lice".
 * 
 * @author Jianwei "Chris" Leng
 * @since   JDK1.6
 */
public class License {
    
    /**The filename of */
    private String filename = "ehost.lice";
    
    /**Read file ehost.lice to know which function is enabled and which is 
     * disabled. */
    //public getLicense(){
        
    //}
    
    /**This is a default value to tell eHOST that which function is available 
     * and which one is disabled. It's usually being called while the file
     * "ehost.lice" is missing.
     */
    private void setDefalutValue(){
    }
}
