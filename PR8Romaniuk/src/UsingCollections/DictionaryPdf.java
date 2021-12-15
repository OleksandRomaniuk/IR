package UsingCollections;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DictionaryPdf {
    private TreeSet<String> dictionary;
    private TreeMap<String, ArrayList<File>> invertedIndex;
    private TreeMap<String, ArrayList<File>> biwordsIndex;
    private TreeMap<String, TreeMap<File, ArrayList<Integer>>> coordinateIndex;
    private BTree bTree;
    private TreeMap<String, ArrayList<String>> permutermIndex;
    private TreeMap<String, ArrayList<String>> threeGramIndex;
    private int[][] matrix;
    private TreeSet<File> collection;
    private File dir;
    private int numberOfWords = 0;
    private long sizeCollection = 0;

    //constructor read all files in 1 thread
    public DictionaryPdf(File dir){
        this.dir = dir;
        collection = new TreeSet<File>();
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith(".pdf"))
                continue;
            collection.add(f);
        }
    }

    public void makeTrie(){
        if (dictionary ==  null)
            makeDictionary();
        bTree = new BTree();
        bTree.insert(dictionary);
    }

    public void makePermutermIndex() {
        if (dictionary ==  null)
            makeDictionary();
        permutermIndex = new TreeMap<String, ArrayList<String>>();

        //read one word from dictionary and move the first char
        for (String s : dictionary){
            ArrayList<String> move = new ArrayList<String>(s.length() + 1);
            s += "$";
            for (int i = 0; i < s.length(); i++){
                move.add(s);
                char ch = s.charAt(0);
                s = s.substring(1);
                s += Character.toString(ch);
            }
            s = s.substring(0, s.length() - 1);
            permutermIndex.put(s,move);
        }
    }

    public void makeThreeGramIndex() {
        if (dictionary ==  null)
            makeDictionary();
        threeGramIndex = new TreeMap<String, ArrayList<String>>();

        //read one word from dictionary and make all 3grams
        for (String s : dictionary){
            s = "$" + s + "$";
            String three[] = new String[s.length()-2];
            for (int i = 0; i < three.length; i++)
                three[i] = s.substring(i, i+3);
            s = s.substring(1, s.length()-1);
            for (String th : three){
                ArrayList<String> words;
                if (threeGramIndex.containsKey(th))
                    words = threeGramIndex.get(th);
                else
                    words = new ArrayList<String>();
                words.add(s);
                threeGramIndex.put(th, words);
            }
        }
    }

    public void makeBiwordIndex() {
        biwordsIndex = new TreeMap<String, ArrayList<File>>();
        for (File f : collection){
            FileManagerSet fm = new FileManagerSet(f);
            fm.readTwo();
            for (String s : fm.getTwoWords()){
                if (biwordsIndex.containsKey(s)) {
                    ArrayList<File> fileIds = biwordsIndex.get(s);
                    fileIds.add(f);
                    biwordsIndex.put(s, fileIds);
                    continue;
                }
                ArrayList<File> fileIds = new ArrayList<File>();
                fileIds.add(f);
                biwordsIndex.put(s, fileIds);
            }
        }
    }

    public void makeCoordinateIndex() {
        coordinateIndex = new TreeMap<String, TreeMap<File, ArrayList<Integer>>>();
        for (File f : collection){
            FileManagerSet fm = new FileManagerSet(f);
            String[] words = fm.readAll();
            int index = 1;
            for (String s : words){
                ArrayList<Integer> coordinates = new ArrayList<Integer>();
                if (coordinateIndex.containsKey(s)) {
                    TreeMap<File, ArrayList<Integer>> fileIds = coordinateIndex.get(s);
                    if (fileIds.containsKey(f))
                        coordinates = fileIds.get(f);
                    coordinates.add(index);
                    fileIds.put(f, coordinates);
                    coordinateIndex.put(s, fileIds);
                    index++;
                    continue;
                }
                TreeMap<File, ArrayList<Integer>> fileIds = new TreeMap<File, ArrayList<Integer>>();
                coordinates.add(index);
                fileIds.put(f, coordinates);
                coordinateIndex.put(s, fileIds);
                index++;
            }
        }
    }

    //make simple dictionary
    public void makeDictionary(){
        dictionary = new TreeSet<String>();
        collection = new TreeSet<File>();
        for (File f : dir.listFiles()){
            if (!f.getName().endsWith(".pdf"))
                continue;
            collection.add(f);
            sizeCollection += f.length();
            FileManagerSet fm = new FileManagerSet(f);
            fm.read();
            for (String s : fm.getWords()){
                dictionary.add(s);
            }
        }
        numberOfWords = dictionary.size();
    }

    //make matrix
    public void makeMatrix(){
        matrix = new int[dictionary.size()][collection.size()];
        //indexes
        int i = 0;
        int j = 0;
        for (File f : collection){
            FileManagerSet fm = new FileManagerSet(f);
            fm.read();
            for (String s : dictionary) {
                if (fm.getWords().contains(s))
                    matrix[i][j] = 1;
                i++;
            }
            i = 0;
            j++;
        }
    }

    //this method makes inverted index
    public void makeInvertedIndex(){
        invertedIndex = new TreeMap<String, ArrayList<File>>();
        for (File f : collection){
            FileManagerSet fm = new FileManagerSet(f);
            fm.read();
            for (String s : fm.getWords()){
                if (invertedIndex.containsKey(s)) {
                    ArrayList<File> fileIds = invertedIndex.get(s);
                    fileIds.add(f);
                    invertedIndex.put(s, fileIds);
                    continue;
                }
                ArrayList<File> fileIds = new ArrayList<File>();
                fileIds.add(f);
                invertedIndex.put(s, fileIds);
            }
        }
    }

    public void saveThreeGramIndex(File f){
        if (threeGramIndex == null)
            makeThreeGramIndex();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (String s : threeGramIndex.keySet()) {
                writer.write(s + " - ");
                for (String s1 : threeGramIndex.get(s))
                    writer.write(s1 + ", ");
                writer.write("\n");
            }
            writer.close();
            System.out.println("Three Gram Index Made in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void savePermutermIndex(File f){
        if (permutermIndex == null)
            makePermutermIndex();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (String s : permutermIndex.keySet()) {
                writer.write(s + " - ");
                for (String s1 : permutermIndex.get(s))
                    writer.write(s1 + ", ");
                writer.write("\n");
            }
            writer.close();
            System.out.println("Permuterm Index Made in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void saveDictionary(File f){
        if (dictionary == null)
            makeDictionary();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (String s : dictionary) {
                writer.write(s + "\n");
            }
            writer.close();
            System.out.println("Dictionary Made in file " + f);
            System.out.println("Size of collection: " + sizeCollection + " bytes");
            System.out.println("Number Of Words: " + numberOfWords);
            System.out.println("Size of dictionary: " + f.length() + " bytes");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void saveInvertedIndex(File f){
        if (invertedIndex == null)
            makeInvertedIndex();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (Map.Entry<String, ArrayList <File>> entry : invertedIndex.entrySet()) {
                writer.write(entry.getKey() + ",");
                int i = 1;
                ArrayList<File> e = entry.getValue();
                for (File f1 : collection){
                    if (e.contains(f1))
                        writer.write(i+ ",");
                    i++;
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("Inverted Index made in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void saveMatrix(File f){
        //Saving the document
        try {
            Iterator<String> iterator = dictionary.iterator();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (int i = 0; i < matrix.length; i++){
                writer.write( iterator.next() + "   ");
                for (int j = 0; j < matrix[i].length; j++)
                    writer.write(matrix[i][j] + "\t");
                writer.write("\n");
            }
            writer.close();
            System.out.println("Matrix is in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    //saving biwordIndex in file
    public void saveBiwordIndex(File f){
        if (biwordsIndex == null)
            makeBiwordIndex();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (Map.Entry<String, ArrayList <File>> entry : biwordsIndex.entrySet()) {
                writer.write(entry.getKey() + " - ");
                for (int i = 0; i < entry.getValue().size(); i++)
                    writer.write(entry.getValue().get(i) + ", ");
                writer.write("\n");
            }
            writer.close();
            System.out.println("Biwords Index made in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void saveCoordinateIndex(File f){
        if (coordinateIndex == null)
            makeCoordinateIndex();
        //Saving the document
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (Map.Entry<String, TreeMap<File, ArrayList<Integer>>> entry : coordinateIndex.entrySet()){
                writer.write(entry.getKey() + ":\n");
                for (Map.Entry<File, ArrayList<Integer>> entry1 : entry.getValue().entrySet()){
                    writer.write( "< " + entry1.getKey() + ": ");
                    for (int i = 0; i < entry1.getValue().size(); i++)
                        writer.write(entry1.getValue().get(i) + ", ");
                    writer.write(" >\n");
                }
            }
            writer.close();
            System.out.println("Coordinate Index made in file " + f);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public TreeSet<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(TreeSet<String> dictionary) {
        this.dictionary = dictionary;
    }

    public TreeMap<String, ArrayList<File>> getInvertedIndex() {
        return invertedIndex;
    }

    public void setInvertedIndex(TreeMap<String, ArrayList<File>> invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public TreeSet<File> getCollection() {
        return collection;
    }

    public void setCollection(TreeSet<File> collection) {
        this.collection = collection;
    }

    public TreeMap<String, ArrayList<File>> getBiwordsIndex() {
        return biwordsIndex;
    }

    public void setBiwordsIndex(TreeMap<String, ArrayList<File>> biwordsIndex) {
        this.biwordsIndex = biwordsIndex;
    }

    public TreeMap<String, TreeMap<File, ArrayList<Integer>>> getCoordinateIndex() {
        return coordinateIndex;
    }

    public void setCoordinateIndex(TreeMap<String, TreeMap<File, ArrayList<Integer>>> coordinateIndex) {
        this.coordinateIndex = coordinateIndex;
    }

    public BTree getbTree() {
        return bTree;
    }

    public void setbTree(BTree bTree) {
        this.bTree = bTree;
    }

    public TreeMap<String, ArrayList<String>> getPermutermIndex() {
        return permutermIndex;
    }

    public void setPermutermIndex(TreeMap<String, ArrayList<String>> permutermIndex) {
        this.permutermIndex = permutermIndex;
    }

    public TreeMap<String, ArrayList<String>> getThreeGramIndex() {
        return threeGramIndex;
    }

    public void setThreeGramIndex(TreeMap<String, ArrayList<String>> threeGramIndex) {
        this.threeGramIndex = threeGramIndex;
    }
}