package UsingCollections;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class BooleanSearchSet {

    private DictionaryPdf dictionaryPdf;
    private TreeMap<String, ArrayList<File>> inverted;
    private final static String AND_SEARCH = "and";
    private final static String OR_SEARCH = "or";
    private final static String NOT_SEARCH = "-";

    private ArrayList<File> result;

    public BooleanSearchSet(DictionaryPdf dict){
        dictionaryPdf = dict;
        result = new ArrayList<File>();
        if (dictionaryPdf.getInvertedIndex() == null)
            dictionaryPdf.makeInvertedIndex();
        inverted = dictionaryPdf.getInvertedIndex();
    }

    private boolean isWord(String s) {
        if (s.equals(AND_SEARCH) || s.equals(OR_SEARCH) || s.equals("-"))
            return false;
        return true;
    }

    public ArrayList<File> search(String query) throws IllegalQueryException{
        //query without brackets
        query = query.toLowerCase();
        String que[] = query.trim().split("\\s+");
        if (que.length % 2 == 0)
            throw new IllegalQueryException(query);
        ArrayList<File> res;
        boolean not1 = false;
        if (que[0].startsWith(NOT_SEARCH)) {
            not1 = true;
            que[0] = que[0].substring(1);
        }
        if (inverted.containsKey(que[0]))
            res = inverted.get(que[0]);
        else
            return new ArrayList<>();
        if (not1)
            res = not(res);
        for (int i = 1; i < que.length - 1; i+=2){
            if (isWord(que[i]))
                throw new IllegalQueryException(query);
            else {
                //find list for next word
                ArrayList<File> lexem1;
                boolean not2 = false;
                if (que[i+1].startsWith(NOT_SEARCH)) {
                    not2 = true;
                    que[i+1] = que[i+1].substring(1);
                }
                if (inverted.containsKey(que[i+1]))
                    lexem1 = inverted.get(que[i+1]);
                else
                    return new ArrayList<>();
                if (not2)
                    lexem1 = not(lexem1);
                //find the result
                if (que[i].equals(AND_SEARCH))
                    res = intersection(res, lexem1);
                if (que[i].equals(OR_SEARCH))
                    res = union(res, lexem1);
            }
        }
        return res;
    }

    public ArrayList<File> union(ArrayList<File> lexem0, ArrayList<File> lexem1) {
        ArrayList<File> res = new ArrayList<File>();
        res.addAll(lexem0);
        for (int i = 0; i < lexem1.size(); i++)
            if (!res.contains(lexem1.get(i)))
                res.add(lexem1.get(i));
        return res;
    }

    public ArrayList<File> intersection(ArrayList<File> lexem0, ArrayList<File> lexem1) {
        ArrayList<File> res = new ArrayList<File>();
        for (File f : lexem0)
            if (lexem1.contains(f))
                res.add(f);
        return res;
    }

    public ArrayList<File> not(ArrayList<File> lexem){
        ArrayList<File> res = new ArrayList<File>();
        for (File f : dictionaryPdf.getCollection())
            if (!lexem.contains(f))
                res.add(f);
        return res;
    }
}