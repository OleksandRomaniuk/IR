package UsingCollections;

import java.util.*;

public class RangeSearch {

    private DictionaryFb2 dictionaryFb2;
    private TreeMap<String, HashMap<Integer, Set<String>>> index;

    public RangeSearch(DictionaryFb2 dict){
        dictionaryFb2 = dict;
        if (dictionaryFb2.getInvertedIndex() == null)
            dictionaryFb2.makeInvertedIndex();
        index = dictionaryFb2.getInvertedIndex();
    }

    public Map<Integer, Double> search(String query) throws IllegalQueryException {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        query = query.toLowerCase();
        String[] queryW = query.trim().split("\\W+");
        if (queryW.length == 0)
            throw new IllegalQueryException(query);
        Map<Integer, Set<String>> res = index.get(queryW[0]);
        for (String w : queryW){
            res = intersection(res, index.get(w));
        }
        result = countRange(res);
        result = sortByValues(result);
        return result;
    }

    private Map<Integer, Double> sortByValues(Map<Integer, Double> res) {
        Map<Integer, Double> result = new LinkedHashMap<>();
        res.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        return result;
    }

    private Map<Integer, Double> countRange(Map<Integer, Set<String>> res) {
        Map<Integer, Double> result = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Set<String>> entry : res.entrySet()) {
            double range = 0;
            for (String s : entry.getValue())
                range += DictionaryFb2.ZONES.get(s);
            result.put(entry.getKey(), range);
        }
        return result;
    }

    private Map<Integer, Set<String>> intersection(Map<Integer,
            Set<String>> h1, Map<Integer, Set<String>> h2) {
        Map<Integer, Set<String>> res = new HashMap<Integer, Set<String>>();
        for (int i : h1.keySet())
            if (h2.containsKey(i)){
                Set<String> r = new HashSet<String>();
                for (String s : h1.get(i)){
                    if (h2.get(i).contains(s))
                        r.add(s);
                }
                res.put(i, r);
            }
        return res;
    }

}
