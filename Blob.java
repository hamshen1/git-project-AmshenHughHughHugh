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
    public File file;
    public String data;
    public String hashedName;
    public boolean isDirectory;
    public String objPath;
    // potentially need to initialize it as data = ""??

    public Blob(String fileName) throws IOException, NoSuchAlgorithmException {
        file = new File(fileName);
        isDirectory = file.isDirectory();
        data = copyData();
        hashedName = convertToSha(data);
        

        if(isDirectory) {
            Files.write(Paths.get("./git/index"), ("tree " + hashedName + " " + fileName + '\n').getBytes(), StandardOpenOption.APPEND);
        }
        else {
            Files.write(Paths.get("./git/index"), ("blob " + hashedName + " " + fileName + '\n').getBytes(), StandardOpenOption.APPEND);
        }
    }

    public String copyData() throws IOException {
        Path path = file.toPath();
        return new String(Files.readAllBytes(path));
    }

    public String convertToSha(String dataInFile) throws NoSuchAlgorithmException, IOException {
        if(file.isDirectory()) {
            for(File f : file.listFiles()) {
                Blob b = new Blob(file.getName() + f.getName());
            }
        }
        
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(dataInFile.getBytes("UTF-8"));
        return new BigInteger(1, crypt.digest()).toString(16);
    }
    // NOT PRINTING CORRECTLY

    public String getData() {
        return data;
    }

    public String getHashedName() {
        return hashedName;
    }
}
