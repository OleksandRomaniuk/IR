package UsingCollections;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class PhraselSearch {

    private DictionaryPdf dictionaryPdf;
    private TreeMap<String, ArrayList<File>> biword;

    public PhraselSearch(DictionaryPdf dict){
        dictionaryPdf = dict;
        if (dictionaryPdf.getBiwordsIndex() == null)
            dictionaryPdf.makeBiwordIndex();
        biword = dictionaryPdf.getBiwordsIndex();
    }

    public ArrayList<File> search(String query){
        ArrayList<File> result = new ArrayList<File>();
        query = query.toLowerCase();
        String[] queryW = query.trim().split("\\W+");
        if (queryW.length == 1){
            if (dictionaryPdf.getInvertedIndex() == null)
                dictionaryPdf.makeInvertedIndex();
            TreeMap<String, ArrayList<File>> inverted = dictionaryPdf.getInvertedIndex();
            return(inverted.get(query));
        }
        String word = new StringBuilder().
                append(queryW[0].toLowerCase()).append(" ").
                append(queryW[1].toLowerCase()).toString();
        result = biword.get(word);
        for (int i = 1; i < queryW.length - 1; i++){
            word = new StringBuilder().
                    append(queryW[i].toLowerCase()).append(" ").
                    append(queryW[i + 1].toLowerCase()).toString();
            ArrayList<File> next = biword.get(word);
            if (next == null || result == null)
                return new ArrayList<File>();
            result = intersection(result, next);
        }
        return result;
    }

    public ArrayList<File> intersection(ArrayList<File> lexem0, ArrayList<File> lexem1) {
        ArrayList<File> res = new ArrayList<File>();
        for (File f : lexem0)
            if (lexem1.contains(f))
                res.add(f);
        return res;
    }

}
