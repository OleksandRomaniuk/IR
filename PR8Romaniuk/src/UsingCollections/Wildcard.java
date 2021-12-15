package UsingCollections;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class Wildcard {

    DictionaryPdf dictionaryPdf;
    TreeMap<String, ArrayList<String>> permuterm;
    TreeMap<String, ArrayList<String>> threeGram;
    BooleanSearchSet bs;

    public Wildcard(DictionaryPdf dict)
    {
        dictionaryPdf = dict;
        if (dictionaryPdf.getPermutermIndex() == null)
            dictionaryPdf.makePermutermIndex();
        permuterm = dictionaryPdf.getPermutermIndex();
        if (dictionaryPdf.getThreeGramIndex() == null)
            dictionaryPdf.makeThreeGramIndex();
        threeGram = dictionaryPdf.getThreeGramIndex();
        bs = new BooleanSearchSet(dictionaryPdf);
    }

    public ArrayList<File> wildcard(String s) throws IllegalQueryException {
        s = s.toLowerCase();
        ArrayList<String> res = new ArrayList<String>();
        if (!s.contains("*"))
            return bs.search(s);
        String grams[] = s.split("\\*");
        //wildcard at the end
        if (s.endsWith("*")){
            String copy[] = new String[grams.length + 1];
            for (int i = 0; i < grams.length; i++)
                copy[i] = grams[i];
            copy[grams.length] = "";
            grams = copy;
        }
        //one wildcard
        if(grams.length == 2)
            res = search(grams[0], grams[1]);
        //more wildcards
        else if(grams.length > 2){
            res = search(grams[0], grams[grams.length - 1]);
            //check the middle
            ArrayList<String> copy = new ArrayList<String>();
            String regex = s.replaceAll("\\*", ".*");
            for (String r : res) {
                if (r.matches(regex))
                    copy.add(r);
            }
            res.clear();
            res.addAll(copy);
        }
        //boolean search
        if (res.isEmpty())
            return new ArrayList<File>();
        String query = res.get(0);
        for (int i = 1; i < res.size(); i++)
            query = query + " OR " + res.get(i);
        return bs.search(query);
    }

    private ArrayList<String> search(String gram, String gram1) {
        ArrayList<String> res = new ArrayList<String>();
        //use3grams
        if (gram.length() > 1 && gram1.length() > 1){
            String[] threegrams = new String[gram.length() + gram.length() - 2];
            String cgram = "$" + gram;
            String cgram1 = gram1 + "$";

            //3grams of query
            for (int i = 0; i < threegrams.length; i++){
                if (cgram.length() < 3)
                    cgram = cgram1;
                threegrams[i] = cgram.substring(0, 3);
                cgram = cgram.substring(1);
            }

            //search in 3grams
            res = threeGram.get(threegrams[0]);
            for (int i = 1; i < threegrams.length; i++){
                ArrayList<String> lexem0 = threeGram.get(threegrams[i]);
                res = intersection(res, lexem0);
            }

            //check if matches query
            for (int i = 0; i < res.size(); i++){
                if (!res.get(i).startsWith(gram) || !res.get(i).endsWith(gram1))
                    res.remove(res.get(i));
            }
        }
        //use permuterm index
        else{
            //convert
            String query = gram + "*" + gram1 + "$";
            for (int i = 0; i <= gram.length(); i++)
                query = query.substring(1) + query.charAt(0);
            query = query.substring(0, query.length() - 1);
            for (String key : permuterm.keySet()){
                if(!key.startsWith(gram))
                    continue;
                else{
                    ArrayList<String> permut = permuterm.get(key);
                    for (int i = 0; i < permut.size(); i++)
                        if (permut.get(i).startsWith(query)){
                            res.add(key);
                            break;
                        }
                }
            }
        }
        return res;
    }

    private ArrayList<String> intersection(ArrayList<String> lexem1, ArrayList<String> lexem0) {
        ArrayList<String> res = new ArrayList<String>();
        for (String s : lexem0)
            if (lexem1.contains(s))
                res.add(s);
        return res;
    }
}
