import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DictCompress {
    int block_size = 4;
    SortedMap<String, ArrayList<Integer>> sortedMap;
    private ArrayList<String> compressed, decompressed;
    char key = 126;

    DictCompress(){
        compressed = new ArrayList<>();
        decompressed = new ArrayList<>();
    }

    DictCompress(int block_size){
        this.block_size = block_size;
        compressed = new ArrayList<>();
        decompressed = new ArrayList<>();
    }

    ArrayList<String> compressDict(HashMap<String, ArrayList<Integer>> dict, char key){
        this.key = key;
        sortedMap = new TreeMap();
        sortedMap.putAll(dict);
        String block = "";
        int sz = 0;
        for(Map.Entry<String,ArrayList<Integer>> entry : sortedMap.entrySet()) {
            block += entry.getKey()+ " ";
            sz++;
            if(sz >=block_size){
                addBlock(block);
                sz=0;
                block = "";
            }
        }
        return compressed;
    }

    void addBlock(String s){
        String[] bl = s.split(" ");
        int pref = findPref(bl);
        String res = bl[0].substring(0, pref)+"*"+bl[0].substring(pref);
        for (int i = 1; i < bl.length; i++) {
            res+=(bl[i].length()-pref);
            res+=key+bl[i].substring(pref);
        }
        compressed.add(res);
    }

    int findPref(String[] s) {
        int min_size = s[0].length();
        for (int j = 1; j < s.length; j++) {
            if(s[j].length()<min_size)min_size=s[j].length();
        }
        for (int i = 0; i < min_size; i++) {
            boolean same = false;
            for (int j = 1; j < s.length; j++) {
                if(s[0].charAt(i)!=s[j].charAt(i))same=!same;
            }
            if (same)return (i);
        }
        return 0;
    }

    ArrayList<String> decompress(ArrayList<String> dict, char key){
        this.key = key;
        ArrayList<String> res = new ArrayList<>();
        for (String s:
             dict) {
            String[] t = doBlock(s,key);
            for (int i = 0; i < t.length; i++) {
               decompressed.add(t[i]);
            }
        }
        return decompressed;
    }


    String[] doBlock(String s, char key){
        String pref = "";
        int iter = 0;
        for (; iter < s.length(); iter++) {
            if(s.charAt(iter)!='*')pref+=s.charAt(iter);
            else break;
        }
        iter++;
        String res = pref;
        while (iter<s.length()){
            if(Character.isDigit(s.charAt(iter))){
                String s2 ="";
                s2 += s.charAt(iter);
                iter++;
                if(Character.isDigit(s.charAt(iter))){
                    s2+=s.charAt(iter);//if sufix>9
                }
                int t = Integer.parseInt(s2);
                res+=" "+pref+s.substring(++iter,iter+=t);
            }
            else {
                res += s.charAt(iter);
                iter++;
            }
        }
        return res.split(" ");
    }


    void writeFile() throws IOException {//sort and writes result in file
        long n = System.currentTimeMillis();
        FileWriter filewriter = new FileWriter(new File(+n+".txt"));
        for(String s : compressed) {
            filewriter.write(s+"\n");
        }
        filewriter.close();
    }
}
