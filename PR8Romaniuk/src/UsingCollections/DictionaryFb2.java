package UsingCollections;

import java.io.File;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/*
* body - 0.45
* author last name - 0.15
* author first name - 0.05
* book-title - 0.2
* genre - 0.1
* year - 0.05
 */
public class DictionaryFb2 {

    public static final Map<String, Double> ZONES;
    static{
        HashMap<String,Double> tmp =
                new HashMap<String,Double>();
        tmp.put("body", 0.45);
        tmp.put("last-name", 0.15);
        tmp.put("first-name", 0.05);
        tmp.put("book-title", 0.2);
        tmp.put("genre", 0.1);
        tmp.put("year", 0.05);
        ZONES = Collections.unmodifiableMap(tmp);
    }

    private TreeMap<String, HashMap<Integer, Set<String>>> invertedIndex;
    private TreeSet<File> collection;
    private File dir;

    public static Map<String, Double> getZONES() {
        return ZONES;
    }

    //constructor read all files in 1 thread
    public DictionaryFb2(File dir){
        this.dir = dir;
        collection = new TreeSet<File>();
        for (File f : dir.listFiles()) {
            if (!f.getName().endsWith(".fb2"))
                continue;
            collection.add(f);
        }
    }

    public void makeInvertedIndex(){
        invertedIndex = new TreeMap<String, HashMap<Integer, Set <String>>>();
        int i = 1;
        for (File f : collection){
            fillInvertedIndex(f, i);
            i++;
        }
    }

    //method builds tree from file f - fb2
    private void fillInvertedIndex(File f, int index) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f);
            document.getDocumentElement().normalize();

            XPath xPath =  XPathFactory.newInstance().newXPath();

            for (String key : ZONES.keySet()) {
                NodeList nodeList = (NodeList) xPath.compile("//" + key).evaluate(
                        document, XPathConstants.NODESET);
                putNodes(nodeList, index, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putNodes(NodeList nodeList, int index, String key) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            String word1 = nNode.getTextContent();
            String words[] = word1.trim().split("\\W+");
            for (String word : words) {
                word = word.toLowerCase();
                HashMap<Integer, Set<String>> ids = new HashMap<Integer, Set<String>>();
                Set <String> zones = new HashSet<String>();
                if (invertedIndex.containsKey(word)) {
                    ids = invertedIndex.get(word);
                    if (ids.containsKey(index))
                        zones = ids.get(index);
                }
                zones.add(key);
                ids.put(index, zones);
                invertedIndex.put(word, ids);
            }
        }
    }

    public TreeMap<String, HashMap<Integer, Set<String>>> getInvertedIndex() {
        return invertedIndex;
    }

}
