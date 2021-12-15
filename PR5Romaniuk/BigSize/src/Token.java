import java.util.ArrayList;

public class Token implements Comparable{

    private String term;
    private ArrayList<Integer> docID = new ArrayList<>();

    Token(String s, int d){
        term=s;
        docID.add(d);
    }

    Token(String s){
        String[] s2 = s.split(",");
        term=s2[0];
        for (int i = 1; i < s2.length; i++) {
            docID.add(Integer.parseInt(s2[i]));
        }
    }

    void addId(Token t){
        if(docID.get(docID.size()-1)!=t.getFirstID())docID.add(t.getFirstID());
    }

    void addIds(ArrayList<Integer> l){
        for (int i:
             l) {
            if(!docID.contains(i))docID.add(i);
        }
    }

    ArrayList<Integer> getIds() {
        return docID;
    }

    String getTerm(){return term;}

    int getFirstID(){return docID.get(0);}
    @Override
    public int compareTo(Object o) {
        Token other = (Token) o;
        return (other == null) ? 1 : term.compareTo(((Token)other).term);
    }


    public String toString(){
        String s = term;
        for (int i:
             docID) {
            s+="," + i;
        }
        return s;
    }
}
