import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.math.BigInteger;

public class Blob {
    public File newFile;
    public File ogFile;
    public File temp;
    public String data;
    public String hashedName;
    public String tempPath;
    public boolean isDirectory;
    public String newPath;
    public String ogPath;

    // potentially need to initialize it as data = ""??

    public Blob(Path filePath) throws IOException, NoSuchAlgorithmException {

        ogFile = new File(filePath.toString()); 
        ogPath = filePath.toString(); 
        
        isDirectory = ogFile.isDirectory();

        data = copyData(ogPath);

        if(isDirectory) {
            tempPath = "./git/objects/temp";
            temp = new File(tempPath);
        }
    

        hashedName = convertToSha(data);

        newPath = "./git/objects/" + hashedName;

        newFile = new File(newPath);

        if(!Files.exists(Paths.get(newPath))) {
            if(isDirectory()) {
                temp.renameTo(newFile);
            }
            
            else {
                newFile.createNewFile();
                Files.write(Paths.get(newPath), data.getBytes(), StandardOpenOption.WRITE);
            }
            
        }
        
        indexWrite("./git/index", this);

        indexDirectory("");

    }

    public String copyData(String path) throws IOException {
        if(isDirectory()) {
            return "";
        }
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static boolean indexFileContains(String indexPath, String hash) throws IOException {
        File indexFile = new File(indexPath);
        if(!indexFile.exists() || indexFile.length() == 0) {
            return false;
        }

        List<String> indText = Files.readAllLines(Paths.get(indexFile.getPath()));

        for(String s : indText) {
            String[] sArr = s.split("\\s+");

            if(sArr[1].equals(hash)) {
                return true;
            }
        }

        return false;
    }

    public void indexWrite(String indexPath, Blob blob) throws IOException {
        if(!indexFileContains(indexPath, blob.getHashedName())) {
            Path currBlobOgPath = blob.getOgPath();
            if(blob.isDirectory()) {
                Files.write(Paths.get(indexPath), ("tree " + blob.getHashedName() + " " + currBlobOgPath.subpath(1, currBlobOgPath.getNameCount()) + '\n').getBytes(), StandardOpenOption.APPEND);
            }
            else {
                Files.write(Paths.get(indexPath), ("blob " + blob.getHashedName() + " " + currBlobOgPath.subpath(1, currBlobOgPath.getNameCount()) + '\n').getBytes(), StandardOpenOption.APPEND);
            }
        }
    }

    public Blob[] blobDirContents() throws NoSuchAlgorithmException, IOException {
        Blob[] bArr = null;
        if(isDirectory()) {
            File[] dirList = ogFile.listFiles();
            bArr = new Blob[dirList.length];

            
            if (temp.exists()) {
                temp.delete();
            }
            
            temp.createNewFile();

            for(int i = 0; i < dirList.length; i++) {
                Blob b = new Blob(Paths.get(ogPath + "/" + dirList[i].getName()));
                
                indexWrite(tempPath, b);
            }
        }
        return bArr;
    }

    public String convertToSha(String dataInFile) throws NoSuchAlgorithmException, IOException {

        String tempData = dataInFile;

        if(ogFile.isDirectory()) {
            
            blobDirContents();

            tempData = copyData(newPath);

        }
        
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(tempData.getBytes("UTF-8"));
        return new BigInteger(1, crypt.digest()).toString(16);
    }
    
    public void indexDirectory(String prefixString) throws IOException {
        if(isDirectory()) {
            List<String> dirText = Files.readAllLines(Paths.get(newPath));

            for(String s : dirText) {
                
                String[] sArr = s.split("\\s+");

                if (!indexFileContains("./git/index", sArr[1])) {
    
                    sArr[2] = prefixString + ogFile.getName() + "/" + sArr[2];

                    String sPrefixed = sArr.toString();

                    Files.write(Paths.get("./git/index"), sPrefixed.getBytes(), StandardOpenOption.APPEND);
                    
                }

                if(sArr[0] == "tree") {
                    indexDirectory(prefixString + ogFile.getName() + "/");
                }
            }
        }

    }

    public String getData() {
        return data;
    }

    public String getHashedName() {
        return hashedName;
    }

    public String getPath() {
        return newPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Path getOgPath() {
        return Paths.get(ogPath);
    }

}
