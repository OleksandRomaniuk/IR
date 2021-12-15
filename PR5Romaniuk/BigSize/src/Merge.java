import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Merge {
    File[] files;
    String dir;

    Merge(String dir){
        this.dir=dir;
        files = new Collection(dir).getCollection();
        mergeCollection();
    }

    void mergeCollection(){
        while (files.length>1){
            for (int i = 0; i < files.length; i++) {
                if(i==files.length-1)break;
                mergeFiles(files[i],files[++i]);
            }
            files=new Collection(dir).getCollection();
        }
    }


    void mergeFiles(File inputFile1, File inputFile2) {
        try {

            long n = System.currentTimeMillis();

            File outputFile = new File(dir+"\\"+n + ".txt");
            outputFile.createNewFile();

            FileWriter filewriter = new FileWriter(new File(dir+"\\"+n+".txt"));

            Scanner readerL = new Scanner(inputFile1);
            Scanner readerR = new Scanner(inputFile2);
            if(!readerL.hasNext()){
                inputFile1.delete();
                return;}
            if(!readerR.hasNext()){
                inputFile2.delete();
                return;}
            Token line1 = new Token(readerL.nextLine());
            Token line2 = new Token(readerR.nextLine());

            while ((readerL.hasNext() || readerR.hasNext())||(line1!=null || line2!=null)) {
                if (line1 == null) {
                    filewriter.write(line2.toString()+"\n");
                    line2 = readLine(readerR);
                } else if (line2 == null) {
                    filewriter.write(line1.toString()+"\n");
                    line1 = readLine(readerL);
                } else if (line1.compareTo(line2) < 0) {
                    filewriter.write(line1.toString()+"\n");
                    line1 = readLine(readerL);
                } else if (line2.compareTo(line1) < 0) {
                    filewriter.write(line2.toString()+"\n");
                    line2 = readLine(readerR);
                }
                else if(line1.compareTo(line2)==0) {
                    line1.addIds(line2.getIds());
                    filewriter.write(line1.toString()+"\n");
                    line1 = readLine(readerL);
                    line2 = readLine(readerR);
                }

            }
            filewriter.close();
            readerL.close();
            readerR.close();
            inputFile1.delete();
            inputFile2.delete();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Token readLine(Scanner reader) {
        if (reader.hasNextLine())
            return new Token(reader.nextLine());
        else
            return null;
    }
}

