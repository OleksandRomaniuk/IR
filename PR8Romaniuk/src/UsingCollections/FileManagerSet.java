package UsingCollections;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

public class FileManagerSet {

    private File file;
    TreeSet<String> wordSet;
    TreeSet<String> twoWords;

    public FileManagerSet(String fileName){
        file = new File(fileName);
    }

    public FileManagerSet(File file){
        this.file = file;
    }

    public String[] readAll(){
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            String[] words = text.trim().split("\\W+");
            for (int i = 0; i < words.length; i++)
                words[i] = words[i].toLowerCase();
            return words;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Method to read file and split it into words
    public void read() {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            String[] words = text.trim().split("\\W+");
            for (int i = 0; i < words.length; i++)
                words[i] = words[i].toLowerCase();
            wordSet = new TreeSet<String>();
            wordSet.addAll(Arrays.asList(words.clone()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readTwo() {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            String[] words = text.trim().split("\\W+");
            twoWords = new TreeSet<String>();
            for (int i = 0; i < words.length - 1; i++){
                twoWords.add(new StringBuilder().
                        append(words[i].toLowerCase()).append(" ").append(words[i + 1].toLowerCase()).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeSet<String> getWords(){
        return wordSet;
    }
    public TreeSet<String> getTwoWords() {return twoWords; }
}
