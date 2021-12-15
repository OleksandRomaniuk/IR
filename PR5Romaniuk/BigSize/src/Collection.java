import java.io.File;
import java.io.FileFilter;

public class Collection {
   private File[] list;//list of collecion

    Collection(String dir){
        readCollection(dir);
    }

    void readCollectionTxt(String directoryName){
        File dir = new File(directoryName);
        list = dir.listFiles(new ExtFilter("txt"));
    }
    void readCollection(String directoryName){
        File dir = new File(directoryName);
        list = dir.listFiles();
    }

    File[] getCollection() {
        return list;
    }

    private static class ExtFilter implements FileFilter
    {//check type of file
        String ext;

        ExtFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File pathname) {
            String extension = getExtension(pathname);
            return extension.equals(ext);
        }

        private String getExtension(File pathname) {
            String filename = pathname.getPath();
            int i = filename.lastIndexOf('.');
            if ( i>0 && i<filename.length()-1 ) {
                return filename.substring(i+1).toLowerCase();
            }
            return "";
        }
    }

    int size(){
        return list.length;
    }

}
