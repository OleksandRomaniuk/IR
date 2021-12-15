import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BookParser {
    private final List<String> words = new ArrayList<>();
    private String title = "";
    private String author = "";

    public BookParser(String fileName) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        FB2Handler handler = new FB2Handler();
        saxParser.parse(fileName, handler);
    }

    public class FB2Handler extends DefaultHandler {
        private static final String AUTHOR = "author";
        private static final String TITLE = "title";
        private static final String PARAGRAPH = "p";
        private String elem;

        @Override
        public void characters(char[] ch, int start, int length) {
            elem = new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String name) {
            switch (name) {
                case AUTHOR:
                    author = elem;
                    break;
                case TITLE:
                    title = elem;
                    break;
                case PARAGRAPH:
                    words.addAll(Arrays.asList(elem.split("\\s+")));
                    break;
            }
        }

    }

    public List<String> getWordList() {
        return words;
    }

    public String getAuthor() {
        return author.replaceAll("\n", "");
    }

    public String getTitle() {
        return title.replaceAll("\n", "");
    }

}