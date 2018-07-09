/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices;

import gov.va.vinci.annotationAdmin.integration.model.AnalyteAssignment;

import java.awt.event.ActionEvent;
import java.util.EventObject;


/**
 *
 * @author yarden
 */
public class AssignmentEvent extends ActionEvent {
    
    public static final String SWITCH_TO_ANALYTE = "SwitchToAnalyte";
    
    private AnalyteAssignment analyteAssignment;
    
    public AssignmentEvent(Object source, String cmd, AnalyteAssignment analyteAssignment) {
        super(source, 0, cmd);
        
        this.analyteAssignment = analyteAssignment;
    }
    
    public AnalyteAssignment getAnalyte() {
        return analyteAssignment;
    }
}
