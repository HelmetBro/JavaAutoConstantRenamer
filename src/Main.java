import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Eric P on 6/14/2017.
 */
public class Main {

    private static JFrame browser = new JFrame();

    public static void main(String[] args){

        File[] directoryListing;
        File dir;

        boolean invalid;

        do{
            dir = new File(showSaveFileDialog());
            directoryListing = dir.listFiles();

            invalid = directoryListing == null;

            if (invalid)
                System.out.println("Not a project folder.");

        }while(invalid);

        searchAndEdit(dir);

    }

    public static void searchAndEdit(File file)
    {
        File[] list = file.listFiles();
        if(list != null)
            for (File fil : list)
                if (fil.isDirectory())
                    searchAndEdit(fil);
                else if (Objects.equals(getExtension(fil), "java"))
                    refactor(fil);
    }

    public static void refactor(File file){

        Path filePath = file.toPath();
        Scanner scanner = null;

        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            if (isConstantVar(line))
                System.out.println(line);

        }

    }

    static boolean isConstantVar(String line){

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
