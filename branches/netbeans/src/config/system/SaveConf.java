/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config.system;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.logging.Level;
import webservices.PBECoder;

/**
 *
 * @author leng
 */
public class SaveConf {


    /**save configure to files.*/
    public void save(String _filename)
    {
        if((_filename==null)||(_filename.trim().length()<1))
        {
            System.out.println("error 1102090105::fail to get the output configure file name");
            return;
        }

        try{
            FileOutputStream output = new FileOutputStream(_filename.trim());

            PrintStream p = new PrintStream(output);
            String outstr;

            //##1##comment
            p.println("// =================== These are comments ===================");
            p.println("// [1] All parameters are surround by [ and ]; ");
            p.println("// [2] Parameters are separated by one or more space line; ");
            p.println("// [3] Each parameter can have one or more values; ");
            p.println("// [4] Each value is one line; ");
            p.println("// [5] Values belong to one specific parameter should be ");
            p.println("//     connected lines, and no space line between them.");
            p.println("// ==========================================================");
            p.println("");
            
            //##2##CONCEPT_LIB
            p.println("[CONCEPT_LIB]");
            outstr = env.Parameters.CONCEPT_LIB;
            
            if (outstr == null){
                p.println("");                
            }
            else
            {
                p.println(outstr);
            }
            p.println("");


            //##3## current / latest annotator's name
            p.println("[ANNOTATOR]");
            //String annotatorName = ResultEditor.WorkSpace.WorkSet.current_annotator_name;
            String annotatorName = resultEditor.annotator.Manager.getCurrentAnnotator();
            if ((annotatorName == null)||(annotatorName.trim().length() < 1))
                 p.println("");
            else
                p.println(annotatorName.trim());
            p.println("");

            //##3## current / latest annotator's name
            p.println("[ENABLE_DIFF_BUTTON]");            
            if ( env.Parameters.EnableDiffButton )
                 p.println("true");
            else
                p.println("false");
            p.println("");
            
            
            //##4## current / latest annotator's name
            p.println("[ANNOTATOR_ID]");
            //String annotatorID = ResultEditor.WorkSpace.WorkSet.current_annotator_id;
            String annotatorID = resultEditor.annotator.Manager.getCurrentAnnotatorID();
            if ((annotatorID == null)||(annotatorID.trim().length() < 1))
                p.println("");
            else
                p.println(annotatorID.trim());
            p.println("");


            //##5## LATEST_MENTION_ID
            outstr = String.valueOf( env.Parameters.getLatestUsedMentionID() );
            if (env.Parameters.getLatestUsedMentionID() < 10000)
            {
                outstr = "10000";
            }
            p.println("[LATEST_MENTION_ID]");
            p.println(outstr);
            p.println("");

            
            // flag to enable/disable the oracle seeker to find simlar annotations
            p.println("[MASK]");
            try{
                if((env.Parameters.Sysini.functions==null)||(env.Parameters.Sysini.functions.length!=6))
                {
                    env.Parameters.Sysini.functions = new char[6];
                    for(int i=0; i<6; i++)
                        env.Parameters.Sysini.functions[i] = '1';

                }

                for(int i=0; i<6; i++)
                    p.print(env.Parameters.Sysini.functions[i]);

                p.println();
                
            } catch(Exception ex){
            }
            p.println("");
            
            
            // output UMLS/UTS account
            p.println("[UMLS_USERNAME]");
            if( ( env.Parameters.umls_username == null ) 
                    || (env.Parameters.umls_username.trim().length() < 1 ) )
                p.println("");
            else
                p.println(env.Parameters.umls_username);
            
            p.println("");
            
            
            p.println("[UMLS_PASSWORD]");
            if( ( env.Parameters.umls_decryptedPassword == null ) 
                    || (env.Parameters.umls_decryptedPassword.trim().length() < 1 ) ) {
                p.println("NOPASSWORD");
            } else {
                // PBECoder encryptor = new PBECoder();
                //PBECoder.prepareForCONFIG(); // set encryption parameter
                
                //byte[] encryptData = PBECoder.encrypt(
                //        env.Parameters.umls_decryptedPassword.getBytes(), 
                //        env.Parameters.umls_encryptorPassword, 
                //        env.Parameters.umls_encryptorSalt);
                p.println( safe.AESCoder.encrypt( env.Parameters.umls_decryptedPassword )  );
                
            }
            
            p.println("");


            //##6## export latest used path of workspace to configure file
            p.println("[WORKSPACE_PATH]");
            if ( env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath == null )
                p.println("");
            else{
                String strPath = env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath;                
                p.println(strPath);                
            }
            p.println("");
            
            
            //##6## export latest used path of workspace to configure file
            p.println("[OracleFunction]");
            if ( env.Parameters.OracleStatus.sysvisible == true )
                p.println("true");
            else{                
                p.println("false");                
            }
            p.println("");
            
            p.println("[ENABLE_UMLS_FILTER]");
            if ( env.Parameters.UMLSSetting.UMLSfilterOn == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");


            p.println("[CPT]");
            if ( env.Parameters.UMLSSetting.CPT == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
            
            
            p.println("[HCPCS]");
            if ( env.Parameters.UMLSSetting.HCPCS == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
            
            
         p.println("[ICD10CM]");
            if ( env.Parameters.UMLSSetting.ICD10CM == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[ICD10PCS]");
            if ( env.Parameters.UMLSSetting.ICD10PCS == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[LNC]");
            if ( env.Parameters.UMLSSetting.LNC == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[MEDLINEPLUS]");
            if ( env.Parameters.UMLSSetting.MEDLINEPLUS == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[MeSH]");
            if ( env.Parameters.UMLSSetting.MeSH == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[MedDRA]");
            if ( env.Parameters.UMLSSetting.MedDRA == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[RXNORM]");
            if ( env.Parameters.UMLSSetting.RXNORM == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[SCTSPA]");
            if ( env.Parameters.UMLSSetting.SCTSPA == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[SCTUSX]");
            if ( env.Parameters.UMLSSetting.SCTUSX == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[SNOMEDCT]");
            if ( env.Parameters.UMLSSetting.SNOMEDCT == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
        
        
        p.println("[UMD]");
            if ( env.Parameters.UMLSSetting.UMD == true )
                p.println("true");
            else{                
                p.println("false");                
            }            
            p.println("");
       
            

            // ##end## save and close
            p.close();
            
        }
        catch(Exception ex)
        {
            log.LoggingToFile.log( Level.WARNING, "error 1102090106:: fail to save configure to disk."
                    + ex.toString() );
        }
    }
}
