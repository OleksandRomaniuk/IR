import java.io.ByteArrayInputStream;

public class Test
{

    public static void main(String[] args) {
        Test t = new Test();
        t.runSpimi();
    }

    void runSpimi(){
        System.out.println("Available memory: "+ Runtime.getRuntime().freeMemory());
        long l = System.currentTimeMillis();
        Collection c = new Collection("C:\\dict");
        SPIMI s = new SPIMI(c.getCollection());
        long l2 = System.currentTimeMillis();
        System.out.println("Blocked: "+(l-l2));
        Merge m = new Merge("C:\\blocks");
        long l3 = System.currentTimeMillis();
        System.out.println("Merged: "+(l2-l3));
    }
}
