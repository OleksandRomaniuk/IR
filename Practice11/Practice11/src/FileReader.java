import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FileReader {

    private Word[] wordsInFile;
    private final String filename;

    public FileReader(String filename) {
        this.filename = filename;
    }

    public void readFile() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        scanFile(sb);
        String textWithoutXMLTags = deleteXMLTags(sb);
        String[] splitWords = textWithoutXMLTags.split("((?<=\\s)('))|[ !\"#$%&()*+,-./:;<=>?@\\[\\]^_`{|}~]");
        fillArrayOfWords(splitWords);
        clearArrayFromEmptyStrings();
    }

    private void clearArrayFromEmptyStrings() {
        wordsInFile = Arrays.stream(wordsInFile).filter(it -> !it.getWord().isEmpty())
                .toArray(Word[]::new);
    }

    private void scanFile(StringBuilder sb) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("D:\\NaUKMA\\TAML\\Test\\fb2\\"+filename));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                sb.append(line);
            }
        }
    }

    private String deleteXMLTags(StringBuilder sb) {
        return sb.toString().replaceAll("<.*?>", " ");
    }

    private void fillArrayOfWords(String[] splitWords) {
        int bookID = ReadersHandler.freeDocID++;
        wordsInFile = new Word[splitWords.length];
        for (int i = 0; i < wordsInFile.length; i++) {
            wordsInFile[i] = new Word(splitWords[i].toLowerCase(),bookID, 1);
        }
    }

    public Word[] getWordsInFile() {
        return wordsInFile;
    }
}

