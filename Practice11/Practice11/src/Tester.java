import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Tester {
    private static final List<File> files = new ArrayList<>();
    private final Dictionary dictionary;

    public Tester(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    private List<ZoneContainer> findDocumentsWithWeights(String query) throws Exception {
        String[] words = query.split(" ");
        List<ZoneContainer> result = new LinkedList<>();
        for (String word : words) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("dict.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split("=");
                if (split[0].equalsIgnoreCase(word)) {
                    split[1] = split[1].replaceAll("[\\[\\]]", "");
                    String[] docs = split[1].split(",");
                    ArrayList<Integer> indexes = new ArrayList<>();
                    for (String doc : docs) {
                        indexes.add(Integer.parseInt(doc.trim()));
                    }
                    for (int i : indexes) {
                        BookParser bookParser = new BookParser("C:\\Users\\Oleksandr\\Desktop\\fb" + dictionary.getHandler().getFileNames()[i]);
                        ZoneContainer resultDocument = new ZoneContainer(i);
                        List<String> list = Arrays.asList(bookParser.getTitle().split(" "));
                        list = list.stream().filter(it -> !it.isBlank()).collect(Collectors.toList());
                        double titleCounter = 0;
                        for (String title : list) {
                            if (title.equalsIgnoreCase(word)) {
                                resultDocument.addZone(new Zone("Title", Constants.TITLE));
                                titleCounter++;
                            }
                        }
                        Zone title = resultDocument.getZoneByName("Title");
                        if (title != null) {
                            title.setCoefficient(titleCounter / list.size());
                        }
                        List<String> listAuthor = Arrays.asList(bookParser.getAuthor().split(" "));
                        listAuthor = listAuthor.stream().filter(it -> !it.isBlank()).collect(Collectors.toList());
                        double authorCounter = 0;
                        for (String author : listAuthor) {
                            if (author.equalsIgnoreCase(word)) {
                                resultDocument.addZone(new Zone("Author", Constants.AUTHOR));
                                authorCounter++;
                            }
                        }
                        Zone author = resultDocument
                                .getZoneByName("Author");
                        if (author != null) {
                            author.setCoefficient(authorCounter / listAuthor.size());
                        }


                        List<String> listContent = bookParser.getWordList();
                        listContent = listContent.stream().filter(it -> !it.isBlank()).collect(Collectors.toList());
                        double contentCounter = 0;
                        for (String content : listContent) {
                            if (content.equalsIgnoreCase(word)) {
                                resultDocument.addZone(new Zone("Content", Constants.CONTENT));
                                contentCounter++;
                            }
                        }
                        Zone content = resultDocument.getZoneByName("Content");
                        if (content != null) {
                            content.setCoefficient(contentCounter / listContent.size());
                        }
                        result.add(resultDocument);
                    }
                }
            }
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        String[] fileNames = {"1.fb2", "2.fb2", "3.fb2", "4.fb2", "5.fb2", "6.fb2", "7.fb2", "9.fb2"};
        Dictionary dictionary = new Dictionary(fileNames);
        dictionary.writeDictionaryToFile("dict.txt");


        readFromFolder(new File("C:\\Users\\Oleksandr\\Desktop\\fb"));
        Tester zoneWeightTester = new Tester(dictionary);
        while (true) {
            Scanner scanner = new Scanner(System.in);

            String query = scanner.nextLine();

            List<ZoneContainer> result = zoneWeightTester.findDocumentsWithWeights(query);
            Map<String, Double> res = new TreeMap<>();
            Map<Integer, List<ZoneContainer>> collect = result.stream().collect(Collectors.groupingBy(it -> it.getId()));
            for (Map.Entry<Integer, List<ZoneContainer>> entry : collect.entrySet()) {
                double weight = 0;
                for (ZoneContainer zoneContainer : entry.getValue()) {
                    weight += zoneContainer.getFileWeight();
                }
                if (!entry.getValue().isEmpty()) {
                    res.put(dictionary.getHandler().getFileNames()[entry.getKey()], weight);
                }
            }
            List<Map.Entry<String, Double>> res1 = res.entrySet().stream().sorted((o1, o2) -> -o1.getValue().compareTo(o2.getValue())).collect(Collectors.toList());
            for (Map.Entry<String, Double> stringDoubleEntry : res1) {
                System.out.println(stringDoubleEntry.getKey() + " " + stringDoubleEntry.getValue());
            }

        }
    }

    public static List<File> getFiles() {
        return files;
    }

    public static void readFromFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                readFromFolder(file);
            } else {
                files.add(new File(folder.getName() + "/" + file.getName()));
            }
        }
    }
}
