/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config.system;

import config.Block;
import java.io.File;
import java.math.BigInteger;
import java.util.Vector;
import java.util.logging.Level;
import webservices.PBECoder;

/**
 *
 * @author leng
 */
public class ParameterGather {

    
    /**Load parameters into memory: In each of these splitted blocks, 
     * parametername indicates the name of
     * a parameters, and all strings stored in the vector named "values" are
     * its values defined in repetitive formats. 
     */
    public void load(Vector<Block> _blocks)
    {

        if((_blocks==null)||(_blocks.size()<1))
            return;

        try{
            for(Block block: _blocks){
             
                if(block==null)
                    continue;

                //System.out.println("-"+block.parameterName+"/"+block.values.toString() );

                if(block.parameterName.compareTo("WORKSPACE_PATH")==0)
                {
                    getLatestUsedWorkspace(block);
                }

                //Annotator Name
                if(block.parameterName.compareTo("ANNOTATOR")==0)
                {
                    getLatestUsedAnnotator(block);
                }

                //Annotator ID
                if(block.parameterName.compareTo("ANNOTATOR_ID")==0)
                {
                    getLatestUsedAnnotatorID(block);
                }

                //Annotator ID
                if(block.parameterName.compareTo("MASK")==0)
                {
                    getMask(block);
                }
                
                if(block.parameterName.compareTo("ENABLE_DIFF_BUTTON")==0)
                {
                    getDIffButtonStatus(block);
                }
                
                if(block.parameterName.compareTo("OracleFunction")==0){
                    getOracleVisibleStatus(block);
                }
                
                if(block.parameterName.compareTo("UMLS_USERNAME") == 0 ){
                    getUMLSUsername( block );
                }
                
                if(block.parameterName.compareTo("UMLS_PASSWORD") == 0 ){
                    getUMLSPassword( block );
                }
                
                if(block.parameterName.compareTo("ENABLE_UMLS_FILTER") == 0 ){
                    getUMLSFilter( block );
                }
                
                if(block.parameterName.compareTo("CPT") == 0 ){
                    getUMLS_CPT( block );
                }
                
                if(block.parameterName.compareTo("HCPCS") == 0 ){
                    getUMLS_HCPCS( block );
                }                                
        
                if(block.parameterName.compareTo("ICD10CM") == 0 ){
                    getUMLS_ICD10CM( block );
                }
                
                if(block.parameterName.compareTo("ICD10PCS") == 0 ){
                    getUMLS_ICD10PCS( block );
                }
                if(block.parameterName.compareTo("LNC") == 0 ){
                    getUMLS_LNC( block );
                }
                if(block.parameterName.compareTo("MEDLINEPLUS") == 0 ){
                    getUMLS_MEDLINEPLUS( block );
                }
                if(block.parameterName.compareTo("MeSH") == 0 ){
                    getUMLS_MeSH( block );
                }
                if(block.parameterName.compareTo("MedDRA") == 0 ){
                    getUMLS_MedDRA( block );
                }
                if(block.parameterName.compareTo("RXNORM") == 0 ){
                    getUMLS_RXNORM( block );
                }
                if(block.parameterName.compareTo("SCTSPA") == 0 ){
                    getUMLS_SCTSPA( block );
                }
                if(block.parameterName.compareTo("SCTUSX") == 0 ){
                    getUMLS_SCTUSX( block );
                }
                if(block.parameterName.compareTo("SNOMEDCT") == 0 ){
                    getUMLS_SNOMEDCT( block );
                }
                if(block.parameterName.compareTo("UMD") == 0 ){
                    getUMLS_UMD( block );
                }
                
       

            }
        }catch(Exception ex){
             
        }
    }


    private static void getMask(Block _block){

        try{
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

            env.Parameters.Sysini.functions = new char[6];
            for(int i=0; i<6; i++)
            {
                env.Parameters.Sysini.functions[i]=0;
            }
            env.Parameters.Sysini.functions[1]=1;           
            env.Parameters.Sysini.functions[5]=1;


           // check validity
            String strPath = _block.values.get(0);
            if((strPath!=null)&&(strPath.trim().length()==6))
            {
                for(int i=0; i<6; i++)
                {
                    env.Parameters.Sysini.functions[i]=strPath.charAt(i);
            }
            }
            
               
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1101141245:: fail to get mark to enable or disable functions" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }
    
     private static void getDIffButtonStatus(Block _block){

        try{
            env.Parameters.EnableDiffButton = false;
            
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

           // check validity
            String enabled = _block.values.get(0);;
            if(enabled==null)
                env.Parameters.EnableDiffButton = false;
            else{
                if( enabled.trim().toLowerCase().compareTo("true") == 0 )
                    env.Parameters.EnableDiffButton = true;
                else
                    env.Parameters.EnableDiffButton = false;
            }
            
               

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1101141245:: fail to get mark to enable or disable functions" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }
     
     
             
    private static void getOracleVisibleStatus(Block _block){

        try{
            env.Parameters.OracleStatus.sysvisible = true;
            
            return;
            
            /*
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

           // check validity
            String enabled = _block.values.get(0);;
            if(enabled==null)
                env.Parameters.EnableDiffButton = false;
            else{
                if( enabled.trim().toLowerCase().compareTo("true") == 0 )
                    env.Parameters.OracleStatus.sysvisible = true;
                else if( enabled.trim().toLowerCase().compareTo("false") == 0 )
                    env.Parameters.OracleStatus.sysvisible = false;
                 
            }
            */
               

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1101141245:: fail to get mark to enable or disable functions" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }


    private static void getLatestUsedAnnotatorID(Block _block){
        try{
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

            // check validity
            String id = _block.values.get(0);

            if( (id==null)||(id.trim().length()<1))
            {
                resultEditor.annotator.Manager.setCurrentAnnotatorID(null);
            }
            else
            {
                resultEditor.annotator.Manager.setCurrentAnnotatorID(id.trim());
            }

        }
        catch(Exception ex)
        {
            log.LoggingToFile.log( Level.SEVERE,"error 1101141245:: fail to get latest used path" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }

    private static void getLatestUsedAnnotator(Block _block){

        try{
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

            // check validity
            String strPath = _block.values.get(0);                
            
            if( (strPath==null)||(strPath.trim().length()<1))
            {
                resultEditor.annotator.Manager.setCurrentAnnotator(null);
            }
            else
            {
                resultEditor.annotator.Manager.setCurrentAnnotator(strPath.trim());
            }
            

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1101141245:: fail to get latest used path" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }

    /**extra path of latest used workspace by saved string.
     *    get absolute path of latest used workspace,
     * [1]set parameter=null, if no any saved path;
     * [2]if latest used workspace found, replace all '=' back to space;
     * [3]set parameter=null if any errors occurred.
     */
    private static void getLatestUsedWorkspace(Block _block){

        try{
            if(_block==null)
                return;

            if((_block.values==null)||(_block.values.size()!=1))
                return;

            
                     
            

            // check validity
            String strPath = _block.values.get(0);;
            if(strPath==null)
                env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath = null;
            

            
            strPath = strPath.trim();

            // check validity again
            if(strPath.trim().length()<1)
                env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath = null;

            // check the path is existing and is a folder;
            if(!verifyWorkspacePath(strPath)){
                env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath = null;
            }

            // evententlly, set the paramter
            env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath = strPath;

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1101141245:: fail to get latest used path" +
                    " of workspace!"
                    + ex.getMessage() );
        }
    }

    /**This method is used to verify the workspace.*/
    private static boolean verifyWorkspacePath(String path){
        try{

            File f = new File(path);
            if (f==null)
                return false;
            if (!f.exists())
                return false;
            if (f.isFile())
                return false;

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1101141247:: fail to verify the latest " +
                    "used path of workspace:: "
                    + ex.getMessage() );
            return false;
        }
        return true;
    }

    private void getUMLSUsername(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String username = block.values.get(0);                
            
            if( (username==null)||(username.trim().length()<1))
            {
                env.Parameters.umls_username = null;
            }
            else
            {
                env.Parameters.umls_username = username.trim();
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }

    private void getUMLSFilter(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.UMLSfilterOn = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.UMLSfilterOn = true;
                else
                    env.Parameters.UMLSSetting.UMLSfilterOn = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_CPT(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.CPT = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.CPT = true;
                else
                    env.Parameters.UMLSSetting.CPT = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_HCPCS(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.HCPCS = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.HCPCS = true;
                else
                    env.Parameters.UMLSSetting.HCPCS = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    
    private void getUMLS_ICD10CM(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.ICD10CM = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.ICD10CM = true;
                else
                    env.Parameters.UMLSSetting.ICD10CM = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_ICD10PCS(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.ICD10PCS = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.ICD10PCS = true;
                else
                    env.Parameters.UMLSSetting.ICD10PCS = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_LNC(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.LNC = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.LNC = true;
                else
                    env.Parameters.UMLSSetting.LNC = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_MEDLINEPLUS(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.MEDLINEPLUS = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.MEDLINEPLUS = true;
                else
                    env.Parameters.UMLSSetting.MEDLINEPLUS = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_MeSH(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.MeSH = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.MeSH = true;
                else
                    env.Parameters.UMLSSetting.MeSH = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_MedDRA(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.MedDRA = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.MedDRA = true;
                else
                    env.Parameters.UMLSSetting.MedDRA = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    
    private void getUMLS_RXNORM(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.RXNORM = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.RXNORM = true;
                else
                    env.Parameters.UMLSSetting.RXNORM = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_SCTSPA(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.SCTSPA = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.SCTSPA = true;
                else
                    env.Parameters.UMLSSetting.SCTSPA = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_SCTUSX(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.SCTUSX = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.SCTUSX = true;
                else
                    env.Parameters.UMLSSetting.SCTUSX = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_SNOMEDCT(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.SNOMEDCT = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.SNOMEDCT = true;
                else
                    env.Parameters.UMLSSetting.SNOMEDCT = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    private void getUMLS_UMD(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String value = block.values.get(0);                
            
            if( (value==null)||(value.trim().length()<1))
            {
                env.Parameters.UMLSSetting.UMD = false;
            }
            else
            {
                if(value.trim().toLowerCase().compareTo("true") == 0)
                    env.Parameters.UMLSSetting.UMD = true;
                else
                    env.Parameters.UMLSSetting.UMD = false;
                
            }
            

        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
    
    
    
    
    private void getUMLSPassword(Block block) {
        try{
            if(block==null)
                return;

            if((block.values==null)||(block.values.size()!=1))
                return;

            // check validity
            String encryptedpassword = block.values.get(0);                
            
            if( (encryptedpassword==null)||(encryptedpassword.trim().length()<1) 
                    ||(encryptedpassword.trim().compareTo( "NOPASSWORD" ) == 0 )  )
            {
                env.Parameters.umls_decryptedPassword = null;
            }
            else
            {
                PBECoder decryptor = new PBECoder();
                decryptor.prepareForCONFIG();
                
                // string to bigint
                //BigInteger bigInt = new BigInteger(encryptedpassword, 16);                                                                        
                // bigint to byte
                // bigInt.toByteArray()
                
                // Arrays.toString(bigInt.toByteArray())                                                                                     
                
                //PBECoder.prepareForCONFIG();
                //byte[] decryptData = PBECoder.decrypt(
                //        bigInt.toByteArray(), 
                //        env.Parameters.umls_encryptorPassword, 
                //        env.Parameters.umls_encryptorSalt);
                env.Parameters.umls_decryptedPassword = safe.AESCoder.decrypt(encryptedpassword);//new String(decryptData);
                
                
                
                
                // System.out.println( env.Parameters.umls_decryptedPassword );
            }
            
        }catch(Exception ex){
            System.out.println("error: fail to decrypt the umls password!!");
            //ex.printStackTrace();
        }
    }

}
