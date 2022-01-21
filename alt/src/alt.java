import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class alt {


    static List<String> mylist = new LinkedList<>() {
    };
    static int activeIndex = 0;
    public class swap {
        void swap(int x, int y, List <String> myList) {
            String s = myList.get(x);
            myList.set(x, myList.get(y));
            myList.set(y, s);

        }
    }
    static void changes(int n){
        String el ="";
        ListIterator<String> iter = mylist.listIterator();

        iter.next();

        for (int i = 0; i < n; i++, iter.next()) {
            if (iter.hasNext()){
                activeIndex = iter.nextIndex();

            }
            else{
                iter = mylist.listIterator();
                activeIndex = 0;
            }
        }
      //  System.out.println("active el: "+mylist.get(activeIndex));
        mylist.add(0,mylist.get(activeIndex));
        mylist.remove(activeIndex+1);
        activeIndex = 0;
        System.out.println("after tab "+mylist);

    }

    static void addProg(String nameProg){

        mylist.add(activeIndex,nameProg);
        System.out.println("after run: "+mylist);
    }

    static void WriteRes() throws IOException {
        FileWriter writer = new FileWriter("output.txt", false);
        for (String item:
                mylist ) {
            writer.write(item + "\n");
        }
        writer.close();


    }

    static void ReadFile(String fName) throws IOException{
        FileReader fr = new FileReader(fName);
        Scanner scan = new Scanner(fr);


        String str = scan.nextLine();
        if (str.contains("Run")){
            mylist.add(str.replace("Run",""));
            System.out.println(mylist);
        }

        while (scan.hasNextLine()){
            String progName;
            str = scan.nextLine();
            if (str.contains("Run")){
               progName =  str.replace("Run","");
               addProg(progName);

            }
            if (str.toLowerCase(Locale.ROOT).contains("alt")){
                String example = str;
                Matcher m = Pattern.compile("\\btab\\b").matcher(example);

                int matches = 0;
                while(m.find())
                    matches++;
                System.out.println("matches:"+matches);
                changes(matches);
            }

        }

        WriteRes();

        scan.close();
        fr.close();


    }
    public static void main(String[] args)throws IOException {
        ReadFile("test.txt");

    }
}
