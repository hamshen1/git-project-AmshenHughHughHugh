import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Blob {
    public File newFile;
    public File ogFile;
    public String data;
    public String hashedName;
    public boolean isDirectory;
    public String newPath;
    public String newPathFormatted;
    public String ogPath;

    // potentially need to initialize it as data = ""??

    public Blob(Path filePath) throws IOException, NoSuchAlgorithmException {
        ogFile = new File(filePath.toString());
        newFile= new File("./git/objects" + filePath);
        newPath = newFile.getPath();
        newPathFormatted = newPath.substring(13);
        isDirectory = ogFile.isDirectory();

        newFile.createNewFile();

        data = copyData(ogPath);
        hashedName = convertToSha(data);
        
        if(isDirectory) {
            Files.write(Paths.get("./git/index"), ("tree " + hashedName + " " + filePath + '\n').getBytes(), StandardOpenOption.APPEND);
        }
        else {
            Files.write(Paths.get("./git/index"), ("blob " + hashedName + " " + filePath + '\n').getBytes(), StandardOpenOption.APPEND);
        }
    }

    public String copyData(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public String convertToSha(String dataInFile) throws NoSuchAlgorithmException, IOException {

        String tempData = dataInFile;

        if(ogFile.isDirectory()) {
            File[] dirList = ogFile.listFiles();
            
            for(int i = 0; i < dirList.length; i++) {
                Blob b = new Blob(Paths.get(ogPath + dirList[i].getName()));
                
                if(isDirectory) {
                    Files.write(Paths.get(newPath), ("tree " + b.getHashedName() + " " + b.getPath() + '\n').getBytes(), StandardOpenOption.APPEND);
                }
                else {
                    Files.write(Paths.get(newPath), ("blob " + b.getHashedName() + " " + b.getPath() + '\n').getBytes(), StandardOpenOption.APPEND);
                }
            }

            tempData = copyData(newPath);

        }
        
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(tempData.getBytes("UTF-8"));
        return new BigInteger(1, crypt.digest()).toString(16);
    }
    // NOT PRINTING CORRECTLY

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
}
