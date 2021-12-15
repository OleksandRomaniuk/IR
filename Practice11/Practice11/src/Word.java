import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Word {
    private final int docID;
    private final String word;
    private int occurrence;
    private final Set<Integer> booksAppearance;

    public Word(String word, int docID, int occurrence) {
        this.docID = docID;
        this.word = word;
        this.occurrence = occurrence;
        this.booksAppearance = new TreeSet<>();
        booksAppearance.add(docID);
    }


    public void addFileAppearance(int docID) {
        booksAppearance.add(docID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

//    @Override
//    public String toString() {
//        return "Word(" +
//                "word='" + word + '\'' +
//                ", docIDs='" + booksAppearance.toString() + '\'' +
//                ", occurrence=" + occurrence +
//                ')';
//    }


    @Override
    public String toString() {
        return word + "=" + booksAppearance.toString();
    }

    public int getDocID() {
        return docID;
    }

    public String getWord() {
        return word;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public Set<Integer> getBooksAppearance() {
        return booksAppearance;
    }
}
