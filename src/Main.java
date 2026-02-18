import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StringBuilder allData = new StringBuilder();
        try {
            File dataFile = new File("C:\\Users\\hp\\IdeaProjects\\ControllerCode\\22i1043-22i1064-E\\src\\test5.lang");
            Scanner Reader = new Scanner(dataFile);

            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                allData.append(data).append("\n");
            }

            Reader.close();
        }
        catch (Exception e) {
            System.out.println("Error has occurred in opening file.");
            e.printStackTrace();
        }
        String dataString = allData.toString();
        ManualScanner manualScanner = new ManualScanner(dataString);
        manualScanner.ProcessInput();
        manualScanner.PrintStatistics();
    }
}
