import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Eric P on 6/14/2017.
 */
public class Main {

    private static JFrame browser = new JFrame();

    public static void main(String[] args) {

        File[] directoryListing;
        File dir;

        boolean invalid;

        do {
            dir = new File(showSaveFileDialog());
            directoryListing = dir.listFiles();

            invalid = directoryListing == null;

            if (invalid)
                System.out.println("Not a project folder.");

        } while (invalid);

        searchAndEdit(dir);

    }

    public static void searchAndEdit(File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list)
                if (fil.isDirectory())
                    searchAndEdit(fil);
                else if (Objects.equals(getExtension(fil), "txt"))
                    refactor(fil);
    }

    public static void refactor(File file) {

        Path filePath = file.toPath();
        Scanner scanner = null;

        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 1;
        List<String> lines = null;

        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            //getting updated file line
            if (isConstantVar(line))
                lines.set(count - 1, dealWithData(line));

            count++;

        }

        //list to file
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static String dealWithData(String s) {

        Scanner scan = new Scanner(s);

        scan.next();
        scan.next();
        scan.next();
        scan.next();

        String original = scan.next();
        String name = original;

        if (Character.isLowerCase(name.charAt(0))) {

            //find if string has another uppercase

            int index;
            for (index = 0; index < name.length() - 1; index++){

                if (Character.isUpperCase(name.charAt(index)) && (index + 1) != name.length())
                    if (!Character.isUpperCase(name.charAt(index + 1))){
                        name = new StringBuilder(name).insert(index, "_").toString();
                        index++;
                    }

            }

        }

        return s.replace(original, name.toUpperCase());
    }

    static boolean isConstantVar(String line) {

        return line.contains("static") && line.contains("final") &&
                (line.contains("private") ||
                        line.contains("protected") ||
                        line.contains("public"));

    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String showSaveFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDragEnabled(true);

        int userSelection = fileChooser.showOpenDialog(browser);

        if (userSelection == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getAbsoluteFile().toString();

        return null;
    }

}
