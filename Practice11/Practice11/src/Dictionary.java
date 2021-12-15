import java.io.*;
import java.util.*;

public class Dictionary {
    private long sizeOfDictionary;
    private final int totalAmountOfWords;
    private final Set<Word> dictionary;
    private final ReadersHandler handler;
    private final int amountOfFiles;

    public Dictionary(String[] fileNames) throws FileNotFoundException {
        this.handler = new ReadersHandler(fileNames);
        this.handler.readFromAllFiles();
        this.dictionary = new TreeSet<>(Comparator.comparing(Word::getWord));
        this.amountOfFiles = fileNames.length;
        fillDictionary();
        totalAmountOfWords = handler.getAllWords().length;
    }

    private void fillDictionary() {
        Word[] sortedAllWords = Arrays.stream(handler.getAllWords()).sorted(Comparator.comparing(Word::getWord)).toArray(Word[]::new);
        int counter = 0;
        for (int i = 0; i < sortedAllWords.length; i++) {
            Word word = sortedAllWords[i];
            for (int j = i + 1; j < sortedAllWords.length; j++) {
                if (sortedAllWords[j].equals(word)) {
                    word.setOccurrence(word.getOccurrence() + 1);
                    word.addFileAppearance(sortedAllWords[j].getDocID());
                } else {
                    dictionary.add(word);
                    i = j - 1;
                    break;
                }
            }
        }
    }

    public void writeDictionaryToFile(String filename) {
        writeToFile(dictionary, filename);
        File dict = new File("dictionary.txt");
        sizeOfDictionary = dict.length() / 1024;
    }

    private void writeToFile(Collection<Word> collection, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Word word : collection) {
                writer.write(word.toString() + System.lineSeparator());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public long getSizeOfDictionary() {
        return sizeOfDictionary;
    }

    public int getTotalAmountOfWords() {
        return totalAmountOfWords;
    }

    public Set<Word> getDictionary() {
        return dictionary;
    }

    public ReadersHandler getHandler() {
        return handler;
    }

    public int getAmountOfFiles() {
        return amountOfFiles;
    }
}
