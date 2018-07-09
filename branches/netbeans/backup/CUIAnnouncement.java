package negex_fob;
/**
 *
 * @author Jianwei (Chris) Leng 
 *  * Nov 3th, 2009, VA office
 */

import java.io.*;
import java.sql.*;
import java.sql.Statement;

public class CUIAnnouncement{
	
    private static String classifiedCuiName;
    
    CUIAnnouncement(){}
    
    
    
    private  static Connection con;
    private static Object TYPE_SCROLL_INSENSITIVE;

    static{
        String dsn="jdbc:odbc:cuidata";
        String driver="sun.jdbc.odbc.JdbcOdbcDriver";
        try
        {
            //DIRECT LINK to cuidata.mdb
            //String strurl="jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=books.mdb";
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            //Connection conn=DriverManager.getConnection(strurl) ;

            //USE odbc LINK
            Class.forName(driver);
            con=DriverManager.getConnection(dsn);
        }catch(Exception e) {
            System.out.println("[XML::CUIAnnouncement::] can not link to the database.");
            System.out.println("[XML::CUIAnnouncement::] dsn : " + dsn);
            System.out.println(" Error in connection "+e);
            //break;
        }
        System.out.println("[XML::CUIAnnouncement::] Linked to ODBC-JDBC: " + dsn );
    }

    private String removeUnderscore(String _originalStatement){
        String returnStatement = _originalStatement.trim().replaceAll("_", " ");
        return returnStatement;
    }

    
    public String getAnnouncementCUIName(String _cuiKeyword){
        String classifiedCuiName = "";
        String CUI="";
        ResultSet rs=null;
        // get CUI
        if( (_cuiKeyword != null) && (_cuiKeyword.length() > 0) ){
            try{
                
                Statement sql = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String sqlstatement = "SELECT * FROM cui WHERE keyword='" + removeUnderscore(_cuiKeyword)+"'";// + " order by keyword";
                ///System.out.println( sqlstatement ); ///
                rs = sql.executeQuery( sqlstatement );
                //rs = sql.executeQuery("SELECT flightNum,FromCity,ArivCity,flightprice FROM FLIGHTS
                //PreparedStatement stmt=con.prepareStatement("SELECT * FROM cui"); // where keyword=chills order by keyword");
                //stmt.setString( 1, _cuiKeyword );
                //rs=stmt.executeQuery();
            
                //if find matched keyword in table cui, get CUI as string
                if (rs != null ) {
                    rs.first();
                    CUI = rs.getString("CUI");
                    ///System.out.println("find!!!"+CUI);
                }else{
                    System.out.println("[XML::CUINAME::] Can not find matached CUI by CUI keyword:[" + _cuiKeyword+"]" );
                    CUI = "";
                }
                rs.close();

            }catch(Exception e){
                System.out.println("error in selecting from table:"+e);
                CUI="";
            }

            // use CUI string to get CUI announcement name
            if(CUI != ""){                
                try{
                    Statement sql = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    String sqlstatement = "SELECT * FROM CUIAnnouncement where CUI='" + CUI +"'";
                    //System.out.println( sqlstatement );
                    rs = sql.executeQuery( sqlstatement );

                    //PreparedStatement stmt=con.prepareStatement(='?' order by keyword");
                    //stmt.setString( 1, "CUI" );
                    //rs=stmt.executeQuery();
            
                    if ( rs != null ) {
                        rs.first();
                        classifiedCuiName = rs.getString("AnnounceName");
                    }else{ classifiedCuiName = "";}

                    rs.close();
            
                }catch(Exception e){
                    System.out.println("error in selecting from table:"+e);
                }
            } else{
                classifiedCuiName = "";
            }
        }else{ classifiedCuiName = ""; }
    
        return classifiedCuiName;
    }
}


