package UsingCollections;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class SearchClose {

    private DictionaryPdf dictionaryPdf;
    private TreeMap<String, TreeMap<File, ArrayList<Integer>>> coordinate;

    public SearchClose(DictionaryPdf dict){
        dictionaryPdf = dict;
        if (dictionaryPdf.getCoordinateIndex() == null)
            dictionaryPdf.makeCoordinateIndex();
        coordinate = dict.getCoordinateIndex();
    }

    public TreeMap<File, ArrayList<Integer>> search(String query) throws IllegalQueryException {
        TreeMap<File, ArrayList<Integer>> result = new TreeMap<File, ArrayList<Integer>>();
        //split query
        query = query.toLowerCase();
        if (!isValid(query))
            throw new IllegalQueryException(query);
        String[] qW = query.trim().split("\\W+");
        result = coordinate.get(qW[0]);
        for(int i = 1; i < qW.length - 1; i+=2){
            TreeMap<File, ArrayList<Integer>> lexem1 = coordinate.get(qW[i + 1]);
            int clos = Integer.valueOf(qW[i]);
            result = intersection(result, lexem1, clos);
        }
        return result;
    }

    private TreeMap<File, ArrayList<Integer>> intersection(TreeMap<File, ArrayList<Integer>> result, TreeMap<File, ArrayList<Integer>> lexem1, int clos) {
        TreeMap<File, ArrayList<Integer>>res = new TreeMap<File, ArrayList<Integer>>();
        for (File f : result.keySet())
            if (lexem1.containsKey(f)){
                ArrayList<Integer> coor = intersectC(result.get(f), lexem1.get(f), clos);
                if (!coor.isEmpty())
                    res.put(f, coor);
            }
        return res;
    }

    private ArrayList<Integer> intersectC(ArrayList<Integer> integers, ArrayList<Integer> integers1, int clos) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        for (int i1 : integers){
            for (int i2 : integers1){
                if (Math.abs(i2 - i1) <= clos)
                    //save the second coordinate
                    if (!res.contains(i2))
                        res.add(i2);
                if (i2 - i1 > clos)
                    break;
            }
        }
        return res;
    }

    private boolean isValid(String query) {
        String[] check = query.split("\\s+");
        if (check.length % 2 == 0)
            return false;
        for (int i = 1; i < check.length; i+=2){
            if (!check[i].startsWith("/") || !check[i].substring(1).matches("\\d+"))
                return false;
        }
        return true;
    }
}
