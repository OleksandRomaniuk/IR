package UsingCollections;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;

import static java.lang.Math.log;

public class Compress {

    private byte[] compressed;
    private byte[] compressedInv;
    private TreeMap<String, Integer> decoder;
    private int position = 0;
    private int positionInv = 0;
    private int blocksize = 4;
    private File inverted;
    private TreeMap<String, ArrayList<Integer>> invert;

    Compress(File inverted, int blocksize){
        this.inverted = inverted;
        this.blocksize = blocksize;
        compressed = new byte[10000];
        compressedInv = new byte[10000];
        invert = new TreeMap<String, ArrayList<Integer>>();
        decoder = new TreeMap<String, Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inverted))) {
            String s;
            while ((s = reader.readLine()) != null){
                String words[] = s.trim().split(",");
                ArrayList <Integer> fileIds = new ArrayList<Integer>();
                for (int i = 1; i < words.length; i++)
                    fileIds.add(Integer.valueOf(words[i]));
                invert.put(words[0], fileIds);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressInverted(){
        int i = 0;
        for (Map.Entry<String, ArrayList<Integer>> entry : invert.entrySet()){
            ArrayList<Integer> fileIds = entry.getValue();
            fileIds = between(fileIds);
            byte[] res = encode(fileIds);
            decoder.put(entry.getKey(), i);
            i += res.length;
            for (byte b : res) {
                compressedInv[positionInv++] = b;
                if (positionInv == compressedInv.length - 1)
                    resizeInv(positionInv*2);
            }
        }
        resizeInv(positionInv);
    }

    private ArrayList<Integer> between(ArrayList<Integer> fileIds) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        res.add(fileIds.get(0));
        for (int i = 1; i < fileIds.size(); i++)
            res.add(fileIds.get(i) - fileIds.get(i-1));
        return res;
    }

    private static byte[] encodeNumber(int n) {
        if (n == 0) {
            return new byte[]{0};
        }
        int i = (int) (log(n) / log(128)) + 1;
        byte[] rv = new byte[i];
        int j = i - 1;
        do {
            rv[j--] = (byte) (n % 128);
            n /= 128;
        } while (j >= 0);
        rv[i - 1] += 128;
        return rv;
    }

    public static byte[] encode(List<Integer> numbers) {
        ByteBuffer buf = ByteBuffer.allocate(numbers.size() * (Integer.SIZE / Byte.SIZE));
        for (Integer number : numbers) {
            buf.put(encodeNumber(number));
        }
        buf.flip();
        byte[] rv = new byte[buf.limit()];
        buf.get(rv);
        return rv;
    }

    public static List<Integer> decode(byte[] byteStream) {
        List<Integer> numbers = new ArrayList<Integer>();
        int n = 0;
        for (byte b : byteStream) {
            if ((b & 0xff) < 128) {
                n = 128 * n + b;
            } else {
                int num = (128 * n + ((b - 128) & 0xff));
                numbers.add(num);
                n = 0;
            }
        }
        return numbers;
    }

    public void compressDict(){
        Set<String> dict = invert.keySet();
        Iterator<String> i = dict.iterator();
        while (i.hasNext()) {
            String endings[] = new String[4];
            endings[0] = i.next();
            String a = endings[0];
            for (int j = 1; j < blocksize; j++) {
                if (!i.hasNext())
                    break;
                endings[j] = i.next();
                a = greatestCommonPrefix(a, endings[j]);
            }
            byte lengthBeg = (byte) a.length();
            if (position >= compressed.length - a.length() - 1)
                resize(position*2);
            compressed[position++] = lengthBeg;
            addWord(a);
            for (int k = 0; k < endings.length; k++){
                if (endings[k] == null)
                    break;
                String b = endings[k].replaceFirst(a, "");
                lengthBeg = (byte) b.length();
                if (position >= compressed.length - b.length() - 1)
                    resize(position*2);
                compressed[position++] = lengthBeg;
                addWord(b);
            }
        }
        resize(position);
    }

    public void saveCompressedDict(File toSave){
        try (FileOutputStream writter = new FileOutputStream(toSave)) {
            writter.write(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Compressed dictionary made in file " +
                toSave.getName());
    }

    public void saveCompressedInv(File toSave){
        try (FileOutputStream writter = new FileOutputStream(toSave)) {
            writter.write(compressedInv);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Compressed inverted index made in file " +
                toSave.getName());
    }

    public String decompressDictString(File cd){
        String res = "";
        try {
            byte[] dc = Files.readAllBytes(cd.toPath());
            int beg = 0;
            for (int i = 0; i < dc.length; i++){
                int size = dc[i];
                res += size;
                byte[] word = new byte[size];
                for (int j = 0; j < size; j++) {
                    i++;
                    word[j] = dc[i];
                }
                String w = new String(word);
                res += w;
                if (beg % (blocksize + 1) == 0)
                    res += "*";
                beg++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String decompressInvString(File cd){
        String res = "";
        try {
            byte[] dc = Files.readAllBytes(cd.toPath());
            int i = 0;
            Iterator<Map.Entry<String, Integer>> itr = decoder.entrySet().iterator();
            Map.Entry<String, Integer> entry = itr.next();
            while(itr.hasNext()){
                res += entry.getKey();
                i = entry.getValue();
                entry = itr.next();
                byte[] one = Arrays.copyOfRange(compressedInv, i, entry.getValue());
                List<Integer> fileIds = decode(one);
                for (int j : fileIds)
                    res = res + "," + j;
                res += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void resize(int capacity) {
        byte[] copy = new byte[capacity];
        for (int i=0;i<position;i++)
            copy[i]= compressed[i];
        compressed = copy;
    }

    private void resizeInv(int capacity) {
        byte[] copy = new byte[capacity];
        for (int i=0;i<positionInv;i++)
            copy[i]= compressedInv[i];
        compressedInv = copy;
    }

    private void addWord(String a) {
        byte[] word = a.getBytes();
        for (int i = 0; i < word.length; i++)
            compressed[position++] = word[i];
    }

    public String greatestCommonPrefix(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.substring(0, i);
            }
        }
        return a.substring(0, minLength);
    }

    public byte[] getCompressed() {
        return compressed;
    }

    public void setCompressed(byte[] compressed) {
        this.compressed = compressed;
    }
}
