package umls;

// DO NOT ORGANIZE FOLLOWING IMPORTS OR DELETE ANY ONE OF THEM //
import UtsMetathesaurusContent.*;
import UtsMetathesaurusFinder.*;
import UtsSecurity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * UMLA searching functions, such as getting the CUI code and the label based on
 * the given term.
 *
 * @author  Jianwei Chris Leng, University of Utah & VA Hostipal SLC
 * @since   JDK 1.6
 * @since   Aug 6,2012
 */
public class GetCUI {

    /**service address*/
    private String serviceName = "http://umlsks.nlm.nih.gov";
        
    private UtsWsSecurityController securityService = null;

    /**
     * Get the Single Use Ticket.
     *
     * Do not be confused by the "serviceName" variable value above,
     * "https://umlsks.nlm.nih.gov" This is simply a generic service name to
     * access Terminology Services APIs at the National Library of Medicine, and
     * has nothing to do with the legacy UMLSKS Knowledge Sources Server.
     */
    public String getGrantingTicket() throws Exception {

        

            //initialize the security service
        try{
            securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
        }catch(Exception ex){
            throw new Exception("error1212181359:: [UMLS] ---- > no internet connection.");                
        }

            //String username = $your UTS username$;
            //String password = $your password$;

            boolean error = false;
            //UtsWsSecurityController securityService = null;;
            //try{
            //    //initialize the security service
            //    // securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
            //    securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
            //    
            //}catch(Exception ex){
            //    error = true;
            //    log.LoggingToFile.log(Level.SEVERE, "[UMLS] ---- > " + ex.getMessage() );
            //}
            String username = env.Parameters.umls_username;
            String encryptedpassword = env.Parameters.umls_encryptorPassword;
            String password = env.Parameters.umls_decryptedPassword;
            if( ( username == null ) || (encryptedpassword == null) || ( username.trim().length() < 1 )
                    || ( encryptedpassword.length() < 1 )
                    || ( password == null) || ( password.trim().length() < 1 )
                    )
            {
                throw new Exception("error1212181357:: [UMLS] ---- > wrong password or user name.");                
            }
            
            
            if (securityService == null) {
                error = true;
            }

            if (error) {
                //log.LoggingToFile.log(Level.SEVERE, "[UMLS] ---- > fail to open the file.");
                throw new Exception("error1212181356:: [UMLS] ---- > fail to open the file.");                
            }
try {
            //use the ticketGrantingTicket to generate a Single Use Ticket with each call
            String ticketGrantingTicket = securityService.getProxyGrantTicket(
                    username, 
                    password
                    );

            return ticketGrantingTicket;

        } catch (Exception e) {
            //e.printStackTrace();
            //e.printStackTrace();
            throw new Exception("error1212181355:: fail to get the single tickets");

        }

    }
    
    
    //Pass the Single-Use ticket as a parameter with each call to the API.

    /**
     * Main method for unit test.
     */
    /*
    public static void main(String args[]) {


        GetCUI getcui = new GetCUI();

        // init and handshake
        //String singleUseTicket = getcui.getProxyTicket();
        //String logs = "UMLS: single user ticket : result = [" + singleUseTicket + "]";
        //System.out.println( logs );
        //log.LoggingToFile.log(Level.SEVERE, logs);

        getcui.findConcepts(null, "disease");




        try {
            // Runtime properties
            //String username = $your UTS username$;
            //String password = $your password$;
            String umlsRelease = "2011AB";
            String serviceName = "http://umlsks.nlm.nih.gov";


            UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();
            UtsWsSecurityController securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

            //get the Proxy Grant Ticket - this is good for 8 hours and is needed to generate single use tickets.
            String ticketGrantingTicket = securityService.getProxyGrantTicket(
                    env.Parameters.umls_username, 
                    env.Parameters.umls_decryptedPassword
                    );

            //build some ConceptDTOs and retrieve UI and Default Preferred Name

            //use the Proxy Grant Ticket to get a Single Use Ticket
            String singleUseTicket1 = securityService.getProxyTicket(ticketGrantingTicket, serviceName);
            ConceptDTO result1 = utsContentService.getConcept(singleUseTicket1, umlsRelease, "C0018787");

            System.out.println(result1.getUi());
            System.out.println(result1.getDefaultPreferredName());

            //use the Proxy Grant Ticket to get another Single Use Ticket
            String singleUseTicket2 = securityService.getProxyTicket(ticketGrantingTicket, serviceName);
            ConceptDTO result2 = utsContentService.getConcept(singleUseTicket2, umlsRelease, "C0014591");
            System.out.println(result2.getUi());
            System.out.println(result2.getDefaultPreferredName());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }*/

    public ConceptDTO getConcept(String ticketGrantingTicket, String cuiNumber) {
        try {
            UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();
            //create the reference variables
            UtsWsFinderController utsFinderService = null;
            //instantiate and handshake

            //utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
            //log.LoggingToFile.log(Level.SEVERE, "getting real ticket now based on = [" + singleUseTicket + "]");
            String realticket = securityService.getProxyTicket(ticketGrantingTicket, serviceName);


            ConceptDTO result2 = utsContentService.getConcept(realticket, "2012AB", cuiNumber);
            //System.out.println(result2.getUi());
            //System.out.println(result2.getDefaultPreferredName());
            
            //myUiLabels = utsFinderService.findConcepts(realticket, "2011AB", "atom", "lou gehrig disease", "words", myPsf);
            
            
            return result2;
            
        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("fail to get concept for " + cuiNumber);
            return null;
        }
    }
//    private UtsWsSecurityController utsSecurityService;

    /**
     * To a given word or words, check UMLS server to find possible matched
     * concepts and return results.
     *
     * @param	singleUseTicket The token that we used to login the server.
     * @return return found concepts(CUI and label).
     */
    public List<UiLabel> findConcepts(String ticketGrantingTicket, String terms) {


        List<UiLabel> myUiLabels = new ArrayList<UiLabel>();

        try {


            //create the reference variables
            UtsWsFinderController utsFinderService = null;
            //instantiate and handshake

            utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
            //utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

            try {

                //log.LoggingToFile.log(Level.SEVERE, "getting real ticket now based on = [" + singleUseTicket + "]");
                String singleTicket = securityService.getProxyTicket(ticketGrantingTicket, serviceName);

                //create a PSF object to be used in conjunction with UtsWsFinderController objects
                UtsMetathesaurusFinder.Psf psfMetathesaurusContent = new UtsMetathesaurusFinder.Psf();
                
                psfMetathesaurusContent.setPageLn(50);
                
                if( env.Parameters.UMLSSetting.UMLSfilterOn ) {
                //System.out.println("size=" + psfMetathesaurusContent.getIncludedSources().size() );
                    psfMetathesaurusContent.getIncludedSources().clear();
                   
                   if(env.Parameters.UMLSSetting.CPT)
                       psfMetathesaurusContent.getIncludedSources().add("CPT"); 
                   if(env.Parameters.UMLSSetting.HCPCS)
                       psfMetathesaurusContent.getIncludedSources().add("HCPCS"); 
                   if(env.Parameters.UMLSSetting.ICD10CM)
                       psfMetathesaurusContent.getIncludedSources().add("ICD10CM"); 
                   if(env.Parameters.UMLSSetting.ICD10PCS)
                       psfMetathesaurusContent.getIncludedSources().add("ICD10PCS"); 
                   if(env.Parameters.UMLSSetting.LNC)
                       psfMetathesaurusContent.getIncludedSources().add("LNC"); 
                   if(env.Parameters.UMLSSetting.MEDLINEPLUS)
                       psfMetathesaurusContent.getIncludedSources().add("MEDLINEPLUS"); 
                   if(env.Parameters.UMLSSetting.MeSH)
                       psfMetathesaurusContent.getIncludedSources().add("MeSH"); 
                   if(env.Parameters.UMLSSetting.MedDRA)
                       psfMetathesaurusContent.getIncludedSources().add("MedDRA"); 
                   if(env.Parameters.UMLSSetting.RXNORM)
                       psfMetathesaurusContent.getIncludedSources().add("RXNORM"); 
                   if(env.Parameters.UMLSSetting.SCTSPA)
                       psfMetathesaurusContent.getIncludedSources().add("SCTSPA"); 
                   if(env.Parameters.UMLSSetting.SCTUSX)
                       psfMetathesaurusContent.getIncludedSources().add("SCTUSX"); 
                   if(env.Parameters.UMLSSetting.SNOMEDCT)
                       psfMetathesaurusContent.getIncludedSources().add("SNOMEDCT"); 
                   if(env.Parameters.UMLSSetting.UMD)
                       psfMetathesaurusContent.getIncludedSources().add("UMD"); 

                // usage: findConcepts(String singleUseTicket, String release,
                //                     String target, String str,
                //                     String searchType, PSF psf )
                }
                myUiLabels = utsFinderService.findConcepts(
                        singleTicket, 
                        "2012AB", 
                        "atom", 
                        terms,
                        "words", 
                        psfMetathesaurusContent);

            } catch (Exception ex) {
                ex.printStackTrace();
                log.LoggingToFile.log(Level.SEVERE, ex.toString());
                return null;
            }

            //for (int i = 0; i < myUiLabels.size(); i++) {
            //    UiLabel myUiLabel = myUiLabels.get(i);
            //    String ui = myUiLabel.getUi();
            //    String label = myUiLabel.getLabel();            
            //    System.out.println("CUI: " + ui);
            //    System.out.println("Name: " + label);
            //
            //}


        } catch (Exception e) {
            log.LoggingToFile.log(Level.ALL, "Error!!!" + e.getMessage());
            //e.printStackTrace();
        }

        return myUiLabels;
    }
    
    
    
     private UtsWsSecurityController utsSecurityService;

    /**
     * To a given word or words, check UMLS server to find possible matched
     * concepts and return results.
     *
     * @param	singleUseTicket The token that we used to login the server.
     * @return return found concepts(CUI and label).
     */
    public List<UiLabelRootSource> findConceptss(String ticketGrantingTicket, String terms) {


        //List<UiLabel> myUiLabels = new ArrayList<UiLabel>();
        List<UiLabelRootSource> myUiLabelsRootSource = new ArrayList<UiLabelRootSource>();

        try {


            //create the reference variables
            UtsWsFinderController utsFinderService = null;
            //instantiate and handshake

            utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
            //utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

            try {

                //log.LoggingToFile.log(Level.SEVERE, "getting real ticket now based on = [" + singleUseTicket + "]");
                String singleTicket = securityService.getProxyTicket(ticketGrantingTicket, serviceName);

                //create a PSF object to be used in conjunction with UtsWsFinderController objects
                UtsMetathesaurusFinder.Psf psfMetathesaurusContent = new UtsMetathesaurusFinder.Psf();
                psfMetathesaurusContent.setPageLn(50);

                // usage: findConcepts(String singleUseTicket, String release,
                //                     String target, String str,
                //                     String searchType, PSF psf )
                
                
                //myUiLabels = utsFinderService.findConcepts(
                //        singleTicket,
                //        "2011AB",
                //        "atom",
                //        terms,
                //        "words",
                //        psfMetathesaurusContent);
                
                //singleTicket = securityService.getProxyTicket(ticketGrantingTicket, serviceName);
                
                //concept.setSourceSystem("SNOMED CT");
                
                myUiLabelsRootSource = utsFinderService.findCodes( 
                        singleTicket, 
                        "2011AB", 
                        "atom", 
                        terms, //"diabetic foot", 
                        "words", 
                        psfMetathesaurusContent);

                //for (int i = 0; i < myUiLabelsRootSource.size(); i++) {
                //    UiLabelRootSource myUiLabelRootSource = myUiLabelsRootSource.get(i);
                //    String ui = myUiLabelRootSource.getUi();
                //    String label = myUiLabelRootSource.getLabel();
                //    String source = myUiLabelRootSource.getRootSource();
                //}

            } catch (Exception ex) {
                ex.printStackTrace();
                log.LoggingToFile.log(Level.SEVERE, ex.toString());
                return null;
            }

            //for (int i = 0; i < myUiLabels.size(); i++) {
            //    UiLabel myUiLabel = myUiLabels.get(i);
            //    String ui = myUiLabel.getUi();
            //    String label = myUiLabel.getLabel();            
            //    System.out.println("CUI: " + ui);
            //    System.out.println("Name: " + label);
            //
            //}


        } catch (Exception e) {
            log.LoggingToFile.log(Level.ALL, "Error!!!" + e.getMessage());
            //e.printStackTrace();
        }

        return myUiLabelsRootSource;
    }
    
    /*
    public String getConceptID(String uid, String dictionary) {
        //create the reference variables
        UtsWsFinderController utsFinderService = null;
        //instantiate and handshake

        utsFinderService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
        //utsSecurityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

        try {

            //log.LoggingToFile.log(Level.SEVERE, "getting real ticket now based on = [" + singleUseTicket + "]");
            String singleTicket = securityService.getProxyTicket(getProxyTicket(), serviceName);

            //create the reference variables
            UtsWsFinderController utsFinderService = null;
            //instantiate and handshake

            utsContentService = (new UtsWsFinderControllerImplService()).getUtsWsFinderControllerImplPort();
            
            AtomDTO myAtom = utsContentService.getDefaultPreferredAtom(singleTicket, "2011AB",
                    uid, //"126952004", 
                    dictionary//"SNOMEDCT"
                    );
            if (myAtom == null) {
                return null;
            }

            return myAtom.getConcept().getUi();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/
}
