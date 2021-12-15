import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by rokde on 22.01.2017.
 */
public class Reading {
    File[] list;
    String[][] dict = new  String[1000][2];
    String[][] dict2 = new String[1000][2];
    int id  = 0;
    public Reading(String directoryName){
        File dir = new File(directoryName);
        list = dir.listFiles(new ExtFilter("txt"));
    }

    void readF(){
        for (int i = 0; i < list.length; i++) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(list[i]), StandardCharsets.UTF_8))){
                String line;
                while ((line = reader.readLine()) != null) {
                    writeln(line,i);
                }
            } catch (IOException e) {
                // log error
            }
        }
    }

    void writeln(String line, int doc){
        line.toLowerCase();
        String tl = line.replaceAll("\\W", " ").toLowerCase();
        String[] mg = tl.split("\\s+");
        for (int i = 0; i < mg.length; i++, id++) {
            if (id+1>=dict.length) dict=resize(dict);
            dict[id][0]=mg[i];
            dict[id][1]= String.valueOf(doc);
        }
    }

    public static String[][] resize(String[][] source)
    {
        String[][] a =new String[(source.length * 3) / 2 + 1][2];
        for (int j=0;j<source.length;j++) {
            a[j][0] = source[j][0];
            a[j][1] = source[j][1];
        }
        return a;
    }




    void nDict(){
        Arrays.sort(dict);
        int calc2 = 0;
        for (int i = 0; i < dict.length; i++) {
            if(calc2>=dict2.length)dict2=resize(dict2);
            dict2[calc2][0] = dict[i][0];
            dict2[calc2][1] = "";
            int calc = 0;
            String cash = "";
            while (dict[i].equals(dict[i + 1])) {
                cash += " " + dict[i][1];
                i++;
                calc++;
                if(i>dict.length)break;
            }
            dict2[calc2][1] = String.valueOf(calc)+cash;
            calc2++;
        }
    }






    private static class ExtFilter implements FileFilter
    {
        String ext;

        ExtFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File pathname) {
            String extension = getExtension(pathname);
            return extension.equals(ext);
        }

        private String getExtension(File pathname) {
            String filename = pathname.getPath();
            int i = filename.lastIndexOf('.');
            if ( i>0 && i<filename.length()-1 ) {
                return filename.substring(i+1).toLowerCase();
            }
            return "";
        }
    }
}
