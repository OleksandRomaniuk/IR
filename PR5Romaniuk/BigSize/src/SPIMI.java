import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SPIMI {
    File[] list;
    int block_size = 1000;
    HashMap<String, ArrayList<Integer>> d1 = new HashMap<String, ArrayList<Integer>>();

    SPIMI(File[] list){
        this.list=list;
        readF();
    }


    void readF(){//reads the collection
        for (int i = 0; i < list.length; i++) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(list[i]), StandardCharsets.UTF_8))){
                String line;
                while ((line = reader.readLine()) != null) {
                    tokenizer(line, i);
                }
            } catch (IOException e) {
                // log error
            }
        }
        if(d1.size()>0) try {//wrie collection if smt left
            writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void tokenizer(String line, int doc) throws IOException {//write unsorted dictionary
        String newLine = line.replaceAll("[^A-Za-zа-яА-Я`]", " ").toLowerCase();
        String[] mg = newLine.split("\\s+");
        for (int i = 0; i < mg.length; i++) {
            if(mg[i].equals(""))continue;
            SAlgoritm(mg[i], doc);
        }
    }



    void SAlgoritm(String w, int doc) throws IOException {

        check(w,doc);

        if(d1.size()>block_size)
            writeFile();


    }

    void check(String w, int d){//check if word in dictionary and add word or id
        if(d1.containsKey(w)){
            if(!d1.get(w).contains(d)) {
                d1.get(w).add(d);
            }
        }
        else {
            ArrayList<Integer> l = new ArrayList<>();
            l.add(d);
            d1.put(w,l);
        }
    }

    void writeFile() throws IOException {//sort and writes result in file
        CompressIndex t = new CompressIndex(d1);
        t.encodeInd();
        t.writeFile();

        DictCompress t1 = new DictCompress(4);
        t1.compressDict(d1,'~');
        t1.writeFile();

        SortedMap<String, ArrayList<Integer>> sortedMap = new TreeMap();
        sortedMap.putAll(d1);
        long n = System.currentTimeMillis();
        FileWriter filewriter = new FileWriter(new File("C:\\blocks\\"+n+".txt"));
        for(Map.Entry<String,ArrayList<Integer>> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            String s = key;
            for (Integer i:
                 value) {
                s+=","+i;
            }
            filewriter.write(s+"\n");
        }
        filewriter.close();
        d1.clear();
    }
}


