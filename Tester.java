import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;


public class Tester {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        
        Git.initGitRepo();

        //Git.checkInitGitRepo();

        
        File blobTests = new File("./blobTests");
        System.out.println(blobTests.mkdir());

        String[] blobNames = new String[5];

        for(int i = 0; i < blobNames.length; i++) {
            File f = new File("./blobTests/" + i);
            f.createNewFile();

            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < 500; j++) {
                sb.append((int) (Math.random() * 9));
            }

            File blobFile = new File("./blobTests/" + i);

            Files.write(Paths.get(blobFile.getPath()), 
            sb.toString().getBytes(),
             StandardOpenOption.APPEND);

            
        }

        Blob b = new Blob(Paths.get(blobTests.getPath()));

        // System.out.println(Blob.indexFileContains("./git/index", "c0b3828c877103d3990dc4f7b3a564079ccceb8e"));


    }
}
