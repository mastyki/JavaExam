import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static int readBandWidth(File file){
        try(Scanner scanner = new Scanner(file)) {
            return scanner.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static String readText(File file){
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    // Calculate num of files
    public static int Task1(int bandwith, String text){
        int result = 0;
        result = (int) Math.ceil(1.0 * text.length()/bandwith);
        System.out.println(result);
        return result;
    }
    public static void Task2(File file,int numFiles){
       try(PrintWriter writer = new PrintWriter(file)) {
           for (int  i = 1; i <= numFiles; i++){
               writer.println((i+"#"+"text"+".txt"));
           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
    }
    public static void Task3(int numFiles, int bandWidth, String  text){
        for (int  i = 1; i <= numFiles; i++) {
            try {
                File file = new File(i  + "#" + "text" + ".txt");
                if(!file.exists()){
                    file.createNewFile();
                }
                String textToFile = text.substring((i-1)* bandWidth, Math.min(i*bandWidth, text.length()));
                print(file, textToFile);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static void print(File file, String text){
        try(PrintWriter writer = new PrintWriter(file)) {
           writer.print(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void Task4 (File file, int numFiles){
        try(PrintWriter writer = new PrintWriter(file)) {
            String result  = "";
            for (int i = 1; i <= numFiles;i++) {
                result += getText(new File(i  + "#" + "text" + ".txt"));
            }
            writer.print(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getText(File file){
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args) {
        int bandWidth = readBandWidth(new File("input1.txt"));
        String text = readText(new File("input2.txt"));
        System.out.println(text);
        int numFiles = Task1(bandWidth, text);
        Task2(new File("output2.txt"), numFiles);
        Task3(numFiles,bandWidth,text);
        Task4(new File("%result.txt"),numFiles);

    }
}
