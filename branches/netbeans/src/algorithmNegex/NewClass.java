package algorithmNegex;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chris
 */
class frequence{
        public String entry = null;
        public int fre = 0;
}

public class NewClass {
        private static ArrayList<frequence> freList = new ArrayList<frequence>();


        public void getFrequence(String _yourDocument){
                ArrayList results = new ArrayList();

                if(( _yourDocument == null) ||( _yourDocument.trim().length() < 1 ))
                        return;

                String doc = preProcess(_yourDocument);
                String[] terms = splitDocToSentence(doc);

                int size = terms.length;
                if (size < 1)
                        return;

                for(int i=0; i<size;i++){
                        String word = terms[i];
                        if(hasExist(word)){
                            countPlueOne(word);
                        }else{
                            countThisWord(word);
                        }

                }


                return;
        }


        public void clear(){
                freList.clear();
        }

        public void countThisWord(String _word){
                frequence o = new frequence();
                o.entry = _word.trim();
                o.fre = 1;
                freList.add(o);
        }

        private boolean hasExist(String _word){
                int size = freList.size();


                for(int i=0; i<size;i++){
                        frequence f = freList.get(i);
                        if( f.entry.compareTo( _word.trim() ) == 0 )
                                return true;
                        else
                                return false;
                }

                return false;
        }

        private void countPlueOne(String _word){
                int size = freList.size();


                for(int i=0; i<size;i++){
                        frequence f = freList.get(i);
                        if( f.entry.compareTo( _word.trim() ) == 0 ){
                                frequence o = new frequence();
                                o.entry = f.entry;
                                o.fre = f.fre + 1;
                                freList.set(i, o);
                                return;
                        }
                }
        }


        private String[] splitDocToSentence(String _yourDocument ){
                if(( _yourDocument == null) ||( _yourDocument.trim().length() < 1 ))
                        return null;

                String[] terms = _yourDocument.split(" ");

                return terms;
        }

        private String preProcess(String _yourDocument ){
                String processed = null;

                if(( _yourDocument == null) ||( _yourDocument.trim().length() < 1 ))
                        return null;

                processed = _yourDocument.replaceAll(". ", "  ");
                processed = processed.replaceAll(",", " ");
                processed = processed.replaceAll(";", " ");
                processed = processed.replaceAll("?", " ");
                processed = processed.replaceAll("[0123456789](.)[0123456789]", "000");

                return processed;
        }
}
