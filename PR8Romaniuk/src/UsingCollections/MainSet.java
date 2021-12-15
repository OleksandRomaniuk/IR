package UsingCollections;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MainSet {
    public static void main(String[] args) {
        File dir = new File ("E:/Collection");
        DictionaryFb2 dic = new DictionaryFb2(dir);
        DictionaryPdf dictionaryPdf = new DictionaryPdf(dir);
        BooleanSearchSet search = new BooleanSearchSet(dictionaryPdf);
        PhraselSearch ps = new PhraselSearch(dictionaryPdf);
        SearchClose sc = new SearchClose(dictionaryPdf);
        Wildcard wc = new Wildcard(dictionaryPdf);
        Compress c = new Compress(new File(dir.getAbsolutePath() +
                "/InvertedIndex.txt"), 4);
        RangeSearch rs = new RangeSearch(dic);
        while(true){
            System.out.println("Searching system");
            System.out.println("Enter number: \n1 - boolean search\n2 - save biword index" +
                    "\n3 - phrasal search\n4 - save coordinate index\n5 - search according " +
                    "to how close are words\n6 - save permuterm index\n7 - save 3-gram index" +
                    "\n8 - wildcard search\n9 - save commpressed dictionary\n10 - save compreesed" +
                    " inverted index\n11 - show range index\n12 - range search\n13 - exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int choice = 0;
            try {
                choice = Integer.valueOf(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<File> res = new ArrayList<File>();
            switch (choice){
                case 1:
                    while(true){
                        System.out.print("Enter query: ");
                        String query1 = null;
                        try {
                            query1 = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        res = new ArrayList<File>();
                        try {
                            res = search.search(query1);
                            if (res.isEmpty())
                                System.out.println("No such files");
                            for (int i = 0; i < res.size(); i++)
                                System.out.println(res.get(i));
                        } catch (IllegalQueryException e) {
                            e.printStackTrace();
                            System.out.println(e.message);
                        }
                        System.out.println("Do you want to exit boolean search? Enter 0 to " +
                                "return to the main menu");
                        try {
                            choice = Integer.valueOf(br.readLine());
                            if (choice == 0)
                                break;
                            else
                                continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    System.out.print("Enter file(txt) where to save biwords index.\n To save it in " +
                            "default file enter 0: ");
                    String fileS = null;
                    try {
                        fileS = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!fileS.endsWith("txt"))
                        fileS = "E:/Collection/BiwordIndex.txt";
                    File fS = new File(fileS);
                    dictionaryPdf.saveBiwordIndex(fS);
                    break;
                case 3:
                    while (true){
                        System.out.print("Enter query: ");
                        String query2 = null;
                        try {
                            query2 = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        res = new ArrayList<File>();
                        res = ps.search(query2);
                        if (res.isEmpty())
                            System.out.println("No such files");
                        for (int i = 0; i < res.size(); i++)
                            System.out.println(res.get(i));
                        System.out.println("Do you want to exit phrasal search? Enter 0 to " +
                                "return to the main menu");
                        try {
                            choice = Integer.valueOf(br.readLine());
                            if (choice == 0)
                                break;
                            else
                                continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter file(txt) where to save coordinate index.\n To save it in " +
                            "default file enter 0: ");
                    String fileC = null;
                    try {
                        fileC = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!fileC.endsWith("txt"))
                        fileC = "E:/Collection/CoordinateIndex.txt";
                    File fC = new File(fileC);
                    dictionaryPdf.saveCoordinateIndex(fC);
                    break;
                case 5:
                    while (true){
                        System.out.print("Enter query: ");
                        String query3 = null;
                        try {
                            query3 = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        TreeMap<File, ArrayList<Integer>> result = new TreeMap<>();
                        try {
                            result = sc.search(query3);
                            if (result.isEmpty())
                                System.out.println("No such files");
                            for (Map.Entry<File, ArrayList<Integer>> entry : result.entrySet()){
                                System.out.println(entry.getKey());
                                ArrayList<Integer> r = entry.getValue();
                                for (int i = 0; i < r.size(); i++)
                                    System.out.print(r.get(i) + ", ");
                                System.out.println();
                            }
                        } catch (IllegalQueryException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Do you want to exit phrasal search? Enter 0 to " +
                                "return to the main menu");
                        try {
                            choice = Integer.valueOf(br.readLine());
                            if (choice == 0)
                                break;
                            else
                                continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    System.out.print("Enter file(txt) where to save permuterm index.\n To save it in " +
                            "default file enter 0: ");
                    String fileP = null;
                    try {
                        fileP = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!fileP.endsWith("txt"))
                        fileP = "E:/Collection/PermutermIndex.txt";
                    File fP = new File(fileP);
                    dictionaryPdf.savePermutermIndex(fP);
                    break;
                case 7:
                    System.out.print("Enter file(txt) where to save 3-gram index.\n To save it in " +
                            "default file enter 0: ");
                    String file3g = null;
                    try {
                        file3g = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!file3g.endsWith("txt"))
                        file3g = "E:/Collection/ThreeGramIndex.txt";
                    File f3g = new File(file3g);
                    dictionaryPdf.saveThreeGramIndex(f3g);
                    break;
                case 8:
                    while (true){
                        System.out.print("Enter query: ");
                        String query4 = null;
                        try {
                            query4 = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        res = new ArrayList<File>();
                        try {
                            res = wc.wildcard(query4);
                            if (res.isEmpty())
                                System.out.println("No such files");
                            for (int i = 0; i < res.size(); i++)
                                System.out.println(res.get(i));
                        }catch (IllegalQueryException e){
                            e.printStackTrace();
                        }
                            System.out.println("Do you want to exit phrasal search? Enter 0 to " +
                                "return to the main menu");
                        try {
                            choice = Integer.valueOf(br.readLine());
                            if (choice == 0)
                                break;
                            else
                                continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 9:
                    System.out.print("Enter file(txt) where to save compressed dictionary.\n " +
                            "To save it in default file enter 0: ");
                    file3g = null;
                    try {
                        file3g = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!file3g.endsWith("txt"))
                        file3g = "E:/Collection/CompressedDict.txt";
                    f3g = new File(file3g);
                    c.compressDict();
                    c.saveCompressedDict(f3g);
                    System.out.println(c.decompressDictString(f3g));
                    break;
                case 10:
                    System.out.print("Enter file(txt) where to save compressed index.\n " +
                            "To save it in default file enter 0: ");
                    file3g = null;
                    try {
                        file3g = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!file3g.endsWith("txt"))
                        file3g = "E:/Collection/CompressedInverted.txt";
                    f3g = new File(file3g);
                    c.compressInverted();
                    c.saveCompressedInv(f3g);
                    System.out.println(c.decompressInvString(f3g));
                    break;
                case 11:
                    TreeMap<String, HashMap<Integer, Set<String>>> ii = dic.getInvertedIndex();
                    for (Map.Entry<String, HashMap<Integer, Set<String>>> e : ii.entrySet()){
                        System.out.print(e.getKey() + "-");
                        for (Map.Entry<Integer, Set<String>> e1 : e.getValue().entrySet()){
                            System.out.print(e1.getKey());
                            for (String s : e1.getValue())
                                System.out.print(s + ",");
                            System.out.print(";");
                        }
                        System.out.println();
                    }
                    break;
                case 12:
                    while (true){
                        System.out.print("Enter query: ");
                        String query4 = null;
                        try {
                            query4 = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Map<Integer, Double> resR = rs.search(query4);
                            if (resR.isEmpty())
                                System.out.println("No such files");
                            for (Map.Entry<Integer, Double> entry : resR.entrySet()){
                                System.out.println("File: " + entry.getKey() + " Range: "
                                        + entry.getValue());
                            }
                        }catch (IllegalQueryException e){
                            e.printStackTrace();
                        }
                        System.out.println("Do you want to exit phrasal search? Enter 0 to " +
                                "return to the main menu");
                        try {
                            choice = Integer.valueOf(br.readLine());
                            if (choice == 0)
                                break;
                            else
                                continue;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 13:
                    return;
                default:
                    System.out.println("Illegal number");
            }
        }
    }
}
