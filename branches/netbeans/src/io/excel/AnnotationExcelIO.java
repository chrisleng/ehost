/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.excel;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import report.iaaReport.genHtml.PreLoadDocumentContents;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;

/**
 *
 * @author Chris
 */
public class AnnotationExcelIO {

    public void save() {

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        try {

            int rowcount = 0;
            File project = env.Parameters.WorkSpace.CurrentProject;

            if (project == null) {
                return;
            }

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("Annotation_by_att");

            //
            XSSFRow row0 = sheet1.createRow(0);
            row0.createCell((short) 0).setCellValue("Project Name");
            row0.createCell((short) 1).setCellValue("Annotation ID");
            row0.createCell((short) 2).setCellValue("Annotation Text");
            row0.createCell((short) 3).setCellValue("Annotation Class");
            row0.createCell((short) 4).setCellValue("Full Span");
            row0.createCell((short) 5).setCellValue("Span Start");
            row0.createCell((short) 6).setCellValue("Span End");
            row0.createCell((short) 7).setCellValue("Created Date");
            row0.createCell((short) 8).setCellValue("Annotator Name");
            row0.createCell((short) 9).setCellValue("Annotator ID");
            row0.createCell((short) 10).setCellValue("Attributes");
            row0.createCell((short) 11).setCellValue("Relaionships");
            row0.createCell((short) 11).setCellValue("Contents");

            for (Article article : depot.getDepot()) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                // go through all annotation for each article
                for (Annotation annotation : article.annotations) {
                    if (annotation == null) {
                        continue;
                    }

                    rowcount++;
                    XSSFRow row1 = sheet1.createRow(rowcount);
                    // project name
                    row1.createCell((short) 0).setCellValue( project.getName() );  
                    // annotation id
                    row1.createCell((short) 1).setCellValue( annotation.mentionid );  
                    // current annotator name                   
                    row1.createCell((short) 2).setCellValue( annotation.annotationText );                                        
                    // class
                    row1.createCell((short) 3).setCellValue( annotation.annotationclass );
                    // span info
                    row1.createCell((short) 4).setCellValue( annotation.getSpansInText() );
                    row1.createCell((short) 5).setCellValue( annotation.spanset.getMinimumStart() );
                    row1.createCell((short) 6).setCellValue( annotation.spanset.getMaximumEnd() );
                    // create time
                    row1.createCell((short) 7).setCellValue( annotation.creationDate );
                    // annotator
                    row1.createCell((short) 8).setCellValue( annotation.getAnnotator() );
                    row1.createCell((short) 9).setCellValue( annotation.annotatorid );
                    row1.createCell((short) 10).setCellValue( annotation.getAttributeString() );
                    row1.createCell((short) 11).setCellValue( annotation.getComplexRelationshipString() );
                    
                    String textcontent = PreLoadDocumentContents.getSurroundText( annotation, article );
                    row1.createCell((short) 12).setCellValue( textcontent );
                }

            }

            // BUILD THE FILE NAME
            String filename = project.getAbsolutePath() + File.separatorChar
                    + "AnnotationsInExcel.xlsx";
            System.out.println("Annotation results are saved on MS EXCEL Format:" + filename);

            // OUTPUT THE FILE TO DISK
            FileOutputStream fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close(); // CLOSE

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
