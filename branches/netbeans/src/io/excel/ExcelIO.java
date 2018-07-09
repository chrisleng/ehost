package io.excel;

/**
 * Oct 15, 2014 
 * @author Chris
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;

 
public class ExcelIO {

    /*public void load() {
        try {
            File project = env.Parameters.WorkSpace.CurrentProject;
            FileInputStream fileOut = new FileInputStream(
                    project.getAbsolutePath() + File.separatorChar + "saved"
                    + File.separatorChar
                    + "PBMReviews.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(fileOut);
            XSSFSheet sheet1 = wb.getSheet("PBM_Review_Summary");
            if (sheet1 != null) {
                int totalrow = sheet1.getLastRowNum();
                for (int rownum = 1; rownum <= totalrow; rownum++) {
                    XSSFRow row = sheet1.getRow(rownum);
                    if (row != null) {
                        String filename = row.getCell(0).getStringCellValue();
                        
                        // ana
                        Depot.pbm_ana_confirmed = -1;
                        String ana = row.getCell(2).getStringCellValue();
                        if(ana.trim().toLowerCase().compareTo("yes")==0)
                            Depot.pbm_ana_confirmed = 1;
                        else if(ana.trim().toLowerCase().compareTo("no")==0)
                            Depot.pbm_ana_confirmed = 2;
                        else if(ana.trim().toLowerCase().compareTo("utd")==0)
                            Depot.pbm_ana_confirmed = 3;
                        
                        Depot.pbm_ana_confirmed_UTD = row.getCell(3).getStringCellValue();
                        
                        // event
                        Depot.pbm_event_due = -1;
                        String eve = row.getCell(4).getStringCellValue();
                        if(eve.trim().toLowerCase().compareTo("yes")==0)
                            Depot.pbm_event_due = 1;
                        else if(eve.trim().toLowerCase().compareTo("no")==0)
                            Depot.pbm_event_due = 2;
                        else if(eve.trim().toLowerCase().compareTo("utd")==0)
                            Depot.pbm_event_due = 3;
                        
                        Depot.pbm_event_due_UTD = row.getCell(5).getStringCellValue();
                        
                        
                        Depot.pbm_nameofVaccine = row.getCell(6).getStringCellValue();
                        Depot.pbm_dose = row.getCell(7).getStringCellValue();
                        Depot.pbm_route = row.getCell(8).getStringCellValue();
                        Depot.pbm_date = row.getCell(9).getStringCellValue();
                        
                        
                        Article article = Depot.getArticleByFilename(filename);
                        if(article !=null){
                            article.pbmNoteLevel.symptomNDate = row.getCell(10).getStringCellValue();
                            article.pbmNoteLevel.accuteCondition = row.getCell(11).getStringCellValue();
                            article.pbmNoteLevel.coMorbidCondition = row.getCell(12).getStringCellValue();
                            article.pbmNoteLevel.comments = row.getCell(13).getStringCellValue();
                        }
                        
                    }
                }
            }

            fileOut.close();
        } catch (Exception ex) {
            System.out.println(" == ERROR ==: " + ex.getLocalizedMessage());
        }
    }*/
    
    public void save() {
        try {
            File project = env.Parameters.WorkSpace.CurrentProject;

            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            File[] files = resultEditor.workSpace.WorkSet.getAllTextFile();

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("PBM_Review_Summary");

            XSSFRow row0 = sheet1.createRow(0);

            row0.createCell((short) 0).setCellValue("File Name with extension");
            row0.createCell((short) 1).setCellValue("File Name");
            row0.createCell((short) 2).setCellValue("Term");
            row0.createCell((short) 3).setCellValue("Span");
            //row0.createCell((short) 3).setCellValue("Span_End");
            row0.createCell((short) 4).setCellValue("Class");            
            

            int j = 1;

            for (File file : files) {
                if (file == null) {
                    continue;
                }

                String filename = file.getName();
                
                String fullfilename = "";
                try{
                    fullfilename = getFileNameNoEx(filename);
                }catch(Exception ex){
                }
                String filepath = file.getAbsolutePath();

                Article article = depot.getArticleByFilename(filename);
                if (article != null) {
                    
                    if(article.annotations ==null)
                        continue;
                    
                    for(Annotation annotation : article.annotations){
                        if(annotation ==null)
                            continue;
                        XSSFRow row = sheet1.createRow(j);                    
                        row.createCell((short) 0).setCellValue(fullfilename);                        
                        row.createCell((short) 1).setCellValue(filename);                        
                        row.createCell((short) 2).setCellValue(annotation.annotationText );
                        row.createCell((short) 3).setCellValue(String.valueOf( annotation.getSpansInText() ));
                        //row.createCell((short) 3).setCellValue(String.valueOf( annotation.spanend ));
                        row.createCell((short) 4).setCellValue( annotation.annotationclass );
                        
                        int attributeStartPoint = 5;
                        // start to add attribute: their name and value
                        if((annotation.attributes!= null)&&(!annotation.attributes.isEmpty())){
                            for( AnnotationAttributeDef aad : annotation.attributes ){
                                if(aad==null)
                                    continue;
                                row.createCell((short) attributeStartPoint).setCellValue( aad.name );
                                attributeStartPoint++;
                                row.createCell((short) attributeStartPoint).setCellValue( aad.value );
                                attributeStartPoint++;
                            }
                        }
                        
                        // row id changed (+1)
                        j++;
                        
                    }
                }

                //row0.createCell((short) 28).setCellValue("11 total mg (string)");            
                //row0.createCell((short) 30).setCellValue("11 is adjusted?");
                //row0.createCell((short) 31).setCellValue("11 total mg (adjusted)");
                //XSSFRow row1 = sheet1.createRow(j);
                //row1.createCell((short) 0).setCellValue(filename);
                //row1.createCell((short) 1).setCellValue(filepath);

                //row1.createCell((short) 2).setCellValue("");

                //j++;

            }

            FileOutputStream fileOut = new FileOutputStream(
                    project.getAbsolutePath() + File.separatorChar + "saved"
                    + File.separatorChar
                    + "Annotations.xlsx");
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
}
