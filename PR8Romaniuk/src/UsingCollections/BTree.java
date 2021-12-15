package UsingCollections;

import java.util.HashMap;
import java.util.Set;

public class BTree {

    Node root;

    class Node{
        HashMap<Character, Node> children;
        boolean end;

        Node(){
            end = false;
            //26 letters + 9 digits + _
            children = new HashMap<Character, Node>();
        }
    }

    public BTree(){
        root = new Node();
    }

    //this method inserts word in prefix three
    public void insert(Set<String> words){
        for (String word: words) {
            placeNodes(word, root);
        }
    }

    private void placeNodes(String word, Node root) {
        if (word.length() == 0) {
            root.end = true;
        } else {
            char c = word.charAt(0);
            Node next = root.children.get(c);
            if (next == null) {
                next = new Node();
                root.children.put(c, next);
            }
            placeNodes(word.substring(1), root.children.get(c));
        }
    }
}
