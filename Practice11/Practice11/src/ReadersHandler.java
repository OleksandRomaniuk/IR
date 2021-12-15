import java.io.FileNotFoundException;


public class ReadersHandler {

    private final String[] fileNames;
    private Word[] allWords;
    private final FileReader[] readers;

    public static int freeDocID;

    public ReadersHandler(String[] fileNames) {
        this.fileNames = fileNames;
        this.readers = new FileReader[this.fileNames.length];
    }

    public void readFromAllFiles() throws FileNotFoundException {
        int totalAmountOfWords = readFilesAndGetTotalSize();
        fillArray(totalAmountOfWords);
    }


    private void fillArray(int totalAmountOfWords) {
        int lastPosition = 0;
        allWords = new Word[totalAmountOfWords];
        for (int i = 0; i < fileNames.length; i++) {
            int nextPosition = lastPosition + readers[i].getWordsInFile().length;
            int counter = 0;
            for (int j = lastPosition; j < nextPosition; j++) {
                allWords[j] = readers[i].getWordsInFile()[counter++];
            }
            lastPosition = nextPosition;
        }
    }

    private int readFilesAndGetTotalSize() throws FileNotFoundException {
        int totalAmountOfWords = 0;
        for (int i = 0; i < fileNames.length; i++) {
            readers[i] = new FileReader(fileNames[i]);
            readers[i].readFile();
            totalAmountOfWords += readers[i].getWordsInFile().length;
        }
        return totalAmountOfWords;
    }


    public Word[] getAllWords() {
        return allWords;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}

