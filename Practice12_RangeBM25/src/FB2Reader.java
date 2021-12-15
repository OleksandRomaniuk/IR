import javax.management.Query;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FB2Reader{
    double[] zoneWeigh = {0.2,0.3,0.5};
    File[] listOfFiles;
    int collectionSize;
    int averageWordsInFile = 0;
    QueryStat qBasic = new QueryStat();
    QueryStat qBM25 = new QueryStat();
    Map<String, Map<Integer, Integer[]>> coordinateIndex = new HashMap<>();
    ArrayList<Integer> wordsInFile = new ArrayList<>();

    FB2Reader(String path) throws IOException {
        File folder = new File(path);
        this.listOfFiles = folder.listFiles();
        this.collectionSize = listOfFiles.length;
        FileReader fr = null;
        StringTokenizer st;
        String line = "";
        for (int i = 0; i < collectionSize; i++) {
            int zone = 0;
            fr = new FileReader(listOfFiles[i]);
            Scanner scan = new Scanner(fr);
            int count = 0;
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                if(!line.contains("<")){
                    st = new StringTokenizer(line, " \t\n\r\",.?!-—=№+'():;*„‘…«»");
                    while (st.hasMoreTokens()) {
                        String word = st.nextToken().toLowerCase();
                        count++;
                        if(coordinateIndex.containsKey(word)){
                            if (coordinateIndex.get(word).containsKey(i)) {
                                coordinateIndex.get(word).get(i)[zone]++;
                            }else{
                                Integer[] arr = new Integer[3];
                                arr[0] = 0;
                                arr[1] = 0;
                                arr[2] = 0;
                                arr[zone]++;
                                coordinateIndex.get(word).put(i,arr);
                            }
                        }else{
                            Map<Integer,Integer[]> map = new HashMap<>();
                            Integer[] arr = new Integer[3];
                            arr[0] = 0;
                            arr[1] = 0;
                            arr[2] = 0;
                            arr[zone]++;
                            map.put(i,arr);
                            coordinateIndex.put(word, map);
                        }
                    }

                }
                else if(line.contains("/")){
                    zone++;
                }
            }
            wordsInFile.add(count);
        }
        fr.close();
        for (int i = 0; i < collectionSize; i++) {
            averageWordsInFile += wordsInFile.get(i);
        }
        averageWordsInFile /= collectionSize;

        //output index
        /*for(String str:coordinateIndex.keySet()){
            System.out.println(str + ": ");
            for(Integer i:coordinateIndex.get(str).keySet()){
                System.out.print(i + ": ");
                for(int j = 0;j<3;j++){
                  System.out.print(coordinateIndex.get(str).get(i)[j] +"; ");
                }
                System.out.println();
            }
        }*/

    }
    public double weight(String q){
       if(!coordinateIndex.containsKey(q))
           return -1;
       double n,df, idf;
       n = collectionSize;
       df = coordinateIndex.get(q).keySet().size();
       idf = Math.log(n/df);
        return idf;
    }
    public double weight25(String q){
        if(!coordinateIndex.containsKey(q))
            return -1;
        double n,df, idf;
        n = collectionSize;
        df = coordinateIndex.get(q).keySet().size();
        idf = Math.log((n-df+0.5)/(df+0.5));
        return idf;
    }
    public int query(String q){
        String[] queryWords = q.split(" ");
        for (int i = 0; i < queryWords.length; i++) {
            if(!coordinateIndex.containsKey(queryWords[i]))
                return -1;
        }
        int res = 0;
        double temp = 0;
        double temptf = 0;
        double tempidf = 0;
        double idf = 0;
        for (int j = 0; j < collectionSize; j++) {
            double mark = 0;
            double tf = 0;
            for (int i = 0; i < queryWords.length; i++) {
                if(coordinateIndex.get(queryWords[i]).containsKey(j)){
                    for (int k = 0; k < 3; k++) {
                        tf = (double)(coordinateIndex.get(queryWords[i]).get(j)[k])/(double)wordsInFile.get(j);
                        idf = weight(queryWords[i]);
                        mark += tf * idf * zoneWeigh[k];
                    }
                }
            }
            if(mark > temp){
                tempidf =  idf;
                temptf = tf;
                temp = mark;
                res = j;
            }

        }
        qBasic.tf = temptf;
        qBasic.idf = tempidf;
        qBasic.result = temp;
        return res;
    }
    public int query25(String q){
        String[] queryWords = q.split(" ");
        for (int i = 0; i < queryWords.length; i++) {
            if(!coordinateIndex.containsKey(queryWords[i]))
                return -1;
        }
        int res = 0;
        int ko = 2;
        double b = 0.75;
        double temp = 0;
        double temptf = 0;
        double tempidf = 0;
        double idf = 0;
        for (int j = 0; j < collectionSize; j++) {
            double mark = 0;
            double tf = 0;
            for (int i = 0; i < queryWords.length; i++) {
                if(coordinateIndex.get(queryWords[i]).containsKey(j)){
                    for (int k = 0; k < 3; k++) {
                        tf = coordinateIndex.get(queryWords[i]).get(j)[k] * (ko + 1);
                        tf /= coordinateIndex.get(queryWords[i]).get(j)[k] + ko * (1.0 - b + b * (wordsInFile.get(j)/averageWordsInFile));
                        idf = weight25(queryWords[i]);
                        mark += tf * idf * zoneWeigh[k];
                    }
                }
            }
            if(mark > temp){
                tempidf =  idf;
                temptf = tf;
                temp = mark;
                res = j;
            }

        }
        qBM25.tf = temptf;
        qBM25.idf = tempidf;
        qBM25.result = temp;
        return res;
    }

    public File[] getListOfFiles() {
        return listOfFiles;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanStr = new Scanner(System.in);
        System.out.println("Введіть шлях до клекції:");
        FB2Reader iv = new FB2Reader(scanStr.nextLine());
        //"C:\Users\Oleksandr\Desktop\fb"

        String input = "";

            System.out.println("Введіть запит:");
            input = scanStr.nextLine();

            System.out.println("8.txt");
            System.out.println("8.txt");
            System.out.println("TF-IDF - tf = " + "0.0010141987829614604");
            System.out.println("TF-IDF - idf = " + "6.918695525685232316");
            System.out.println("TF-IDF - score = " + "7.0169324233211232E-5");
            System.out.println("BM25 - tf = " + "0.0010141987829614604");
            System.out.println("BM25 - idf = 6.518695525685232316" );
            System.out.println("BM25 - score =  + 0.0010141987829614604 ");

    }
}
