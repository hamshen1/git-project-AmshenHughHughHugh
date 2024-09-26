import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;

public class Git {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // File testerFile = new File("gangothy");
        // BufferedWriter bW0 = new BufferedWriter(new FileWriter("gangothy", true));
        // bW0.write("what's up my name is roen beiley");
        // bW0.close();
        // // initGitRepo();
        // Blob blob = new Blob(testerFile.getName());
        // System.out.println(blob.getHashedName());
        // File fileInObjects = new File("git/objects", blob.getHashedName());
        // BufferedWriter bW = new BufferedWriter(new FileWriter(fileInObjects, true));
        // bW.write(blob.getData());
        // bW.close();
        // BufferedWriter bW2 = new BufferedWriter(new FileWriter("git/index", true));
        // bW2.write("\n" + testerFile.getName() + blob.getHashedName());
        // bW2.close();

        // checkInitGitRepo();
        // deleteRecursively(testerFile);
    }
    // THE CURRENT FORM THE TESTER IS IN RIGHT NOW IS JUST PRINTING OUT THE HASHED
    // VERSION OF THE CODE I WROTE IN MY TESTERFILE
    // P.S. IT DOESN'T WORK (NEED TO FIX HASHCODE)

    public static void initGitRepo() throws IOException {
        int existsCounter = 0;
        File gitFile = new File("git");
        gitFile.mkdir();
        if (gitFile.exists()) {
            existsCounter++;
        }
        File objectsFile = new File("git/objects");
        objectsFile.mkdir();
        if (objectsFile.exists()) {
            existsCounter++;
        }
        File index = new File("git/index");
        if (index.exists()) {
            existsCounter++;
        }
        index.createNewFile();
        if (existsCounter >= 3) {
            System.out.println("Git Repositorys already exists");
        }
    }

    public static void checkInitGitRepo() throws IOException {
        File indexPath = new File("git/index");
        if (indexPath.exists()) {
            System.out.println("index file was created successfully");
            deleteRecursively(indexPath);
        } else {
            System.out.println("creating the index file did not work");
        }

        File objectsPath = new File("git/objects");
        if (objectsPath.exists()) {
            System.out.println("objects file was created successfully");
            deleteRecursively(objectsPath);
        } else {
            System.out.println("creating the objects file did not work");
        }

        File gitPath = new File("git");
        if (gitPath.exists()) {
            System.out.println("git file was created successfully");
            deleteRecursively(gitPath);
        } else {
            System.out.println("creating the git file did not work");
        }

    }

    public static void deleteRecursively(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteRecursively(c);
            }
            f.delete();
        }
        else {
            f.delete();
        }
    }
}
