/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.util.Vector;

/**
 * structure of annotation in pins extractor
 *
 * @author  Jianwei ‘Chris’ Leng, @ Sun Feb 11 16:33:27 MDT 2010
 *          Division of Epidemiology, School of Medicine, University of Utah
 * 
 */
public class PINAnnotationAttribute{
    String id;
    String annotationtext;
    String annotationclass;
    String INDEX_knowtator_mention_annotation;

    Vector<String> INDEXES_knowtator_slot_mention = new Vector<String>();
}
