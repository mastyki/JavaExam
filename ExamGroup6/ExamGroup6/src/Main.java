import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Vacancy {

    Integer id;
    String skill;
    String title;
    String priority;

    Vacancy(String text) {
        String line = text;
        //String line = scanner.nextLine();
        String[] data = line.split(";");

       id =(Integer.parseInt(data[0]));
     skill=(data[1]);
      title=(data[2]);
      priority = (data[3]);

    }
    boolean closed;
    boolean late;
    Date openDate;
    Date closeDate;
    Integer recruterId;

}
class Recruter {
    Integer id;
    String surname;
    String name;

    Recruter(String text) {
        String line = text;
        //String line = scanner.nextLine();
        String[] data = line.split(";");

        id =(Integer.parseInt(data[0]));
      //  id=(data[1]);
        surname=(data[1]);
        name = (data[2]);

    }
}

interface Reader {

    void Open(String fileName) throws Exception;
    String ReadLine() throws Exception;
    boolean HasNextLine() throws Exception;
    void Close() throws Exception;

}
class TxtReader implements Reader{

    FileReader fr;
    Scanner scan;

    @Override
    public void Open(String fileName) throws FileNotFoundException {

        fr = new FileReader(fileName);
        scan = new Scanner(fr);
    }

    @Override
    public String ReadLine() {

        String temp;
        temp = scan.nextLine();
        return temp;

    }

    @Override
    public boolean HasNextLine() {
        return scan.hasNextLine();
    }

    @Override
    public void Close() throws IOException {
        fr.close();
    }

}

interface Writer {

    void Open(String fileName) throws Exception;
    void WriteLine(String line) throws Exception;
    void Close() throws Exception;

}
class TxtWriter implements Writer {

    FileWriter fw;

    @Override
    public void Open(String fileName) throws IOException {
        fw = new FileWriter(fileName);
    }

    @Override
    public void WriteLine(String line) throws Exception {
        fw.write(line + "\n");
    }

    @Override
    public void Close() throws IOException {
        fw.close();
    }

}

class SampleComparator implements Comparator<Vacancy> {
    @Override
    public int compare(Vacancy o1, Vacancy o2) {
        return  o1.priority.compareTo(o2.priority);
    }
}
class SampleComparator1 implements Comparator<Vacancy> {
    @Override
    public int compare(Vacancy o1, Vacancy o2) {
        return  -o1.id.compareTo(o2.id);
    }
}
class SampleComparator2 implements Comparator<Recruter> {
    @Override
    public int compare(Recruter o1, Recruter o2) {
        return  o1.id.compareTo(o2.id);
    }
}

public class Main {

    public static List<Vacancy> addVacancyToList() throws Exception {
        LinkedList<Vacancy> vacancies = new LinkedList<>();

        String buffer;
        Reader r = new TxtReader();

        r.Open("input1.txt");
        while(r.HasNextLine()) {
            buffer = r.ReadLine();
            Vacancy temp = new Vacancy(buffer);
            vacancies.add(temp);
        }
        r.Close();

        return  vacancies;
    }
    public static List<Recruter> addRecrutersToList() throws Exception {
        LinkedList<Recruter> vacancies = new LinkedList<>();

        String buffer;
        Reader r = new TxtReader();

        r.Open("input2.txt");
        while(r.HasNextLine()) {
            buffer = r.ReadLine();
            Recruter temp = new Recruter(buffer);
            vacancies.add(temp);
        }
        r.Close();

        return vacancies;
    }
    public static void addDatesToVaccancies(List<Vacancy> vacancyList) throws Exception {
        Reader r = new TxtReader();
        r.Open("input4.txt");

        String buffer;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        while(r.HasNextLine()) {
            buffer = r.ReadLine();
            int id;
            Pattern pattern = Pattern.compile(".+?;");
            Matcher matcher = pattern.matcher(buffer);
            if(matcher.find()) {
                id = Integer.parseInt(buffer.substring(matcher.start(),matcher.end()-1));
                buffer = buffer.substring(matcher.end());

                int k = 0;
                while(k < vacancyList.size()) {
                    if(vacancyList.get(k).id == id) {
                        matcher = pattern.matcher(buffer);
                        if(matcher.find()) {
                            vacancyList.get(k).openDate = formatter.parse(buffer.substring(matcher.start(),matcher.end()-1));
                            buffer = buffer.substring(matcher.end());
                        }
                        matcher = pattern.matcher(buffer);
                        if(matcher.find()) {
                            if(buffer.substring(matcher.start(), matcher.end() - 1).equals("0")) {
                                vacancyList.get(k).closeDate = null;
                                vacancyList.get(k).closed = false;
                            }
                            else {
                                vacancyList.get(k).closeDate = formatter.parse(buffer.substring(matcher.start(), matcher.end() - 1));
                                vacancyList.get(k).closed = true;
                            }
                            buffer = buffer.substring(matcher.end());
                        }
                        vacancyList.get(k).recruterId = Integer.parseInt(buffer);
                    }
                    k++;
                }

            }
        }
    }
    public static int Sallary(long days, String priority) throws Exception {
        Reader r = new TxtReader();
        r.Open("input3.txt");

        String buffer;
        String pr = "";
        int d;
        int salary = 0;
        boolean ind = false;

        while(r.HasNextLine() && !ind) {
            buffer = r.ReadLine();
            Pattern pattern = Pattern.compile(".+?;");
            Matcher matcher = pattern.matcher(buffer);
            if (matcher.find()) {
                pr = buffer.substring(matcher.start(),matcher.end()-1);
                buffer = buffer.replace(buffer.substring(matcher.start(), matcher.end()), "");
            }

            if(Objects.equals(pr, priority)) {
                matcher = pattern.matcher(buffer);
                if (matcher.find()) {
                    d = Integer.parseInt(buffer.substring(matcher.start(), matcher.end() - 1));
                    buffer = buffer.substring(matcher.end());
                    if(d >= days) {

                        salary = Integer.parseInt(buffer);
                        ind = true;
                    }
                }
            }
        }
        r.Close();
        return salary;
    }


    public static void Task1(List<Vacancy> vacancyList) throws Exception {
        vacancyList.sort(new SampleComparator());
        Writer w = new TxtWriter();
        w.Open("output1.txt");

        for ( int i  =0; i < vacancyList.size();i++) {

            w.WriteLine(vacancyList.get(i).id + ";" + vacancyList.get(i).title + ";" + vacancyList.get(i).priority);
        }

        w.Close();
    }
    public static LinkedList<Integer> Task2(List<Vacancy> vacancyList) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        addDatesToVaccancies(vacancyList);
        LinkedList<Vacancy> newList = new LinkedList<>();
        LinkedList<Integer> recrutersIds = new LinkedList<>();
        Writer w = new TxtWriter();
        w.Open("output2.txt");
        int i = 0;

        while(i < vacancyList.size()) {
            if(!vacancyList.get(i).closed) {
                Date now = new Date();
                Instant a = now.toInstant();
                Instant b = vacancyList.get(i).openDate.toInstant();
                long days = ChronoUnit.DAYS.between(a,b);
                if(Math.abs(days) >= 90) newList.add(vacancyList.get(i));
            }
            i++;
        }
        i = 0;
        newList.sort(new SampleComparator1());
        while(i < newList.size()) {
            w.WriteLine(newList.get(i).id + ";" + formatter.format(newList.get(i).openDate));
            recrutersIds.add(newList.get(i).recruterId);
            i++;
        }

        w.Close();
        return recrutersIds;
    }
    public static void Task3(LinkedList<Integer> dateclose) throws Exception {
        LinkedList<Recruter> recruters = (LinkedList<Recruter>) addRecrutersToList();
        LinkedList<Recruter> lateRecruters = new LinkedList<>();
        int i = 0;
        while(i < dateclose.size()) {
            int k = 0;
            while( k < recruters.size()) {
                if(Objects.equals(recruters.get(k).id, dateclose.get(i)) && !lateRecruters.contains(recruters.get(k))) lateRecruters.add(recruters.get(k));
                k++;
            }
            i++;
        }
        lateRecruters.sort(new SampleComparator2());

        i = 0;
        Writer w = new TxtWriter();
        w.Open("output3.txt");

        while(i < lateRecruters.size()) {
            w.WriteLine( lateRecruters.get(i).surname + ";" +  lateRecruters.get(i).name);
            i++;
        }

        w.Close();
    }
    public static void Task4(LinkedList<Vacancy> vacancies) throws Exception {
        Reader r = new TxtReader();
        r.Open("input5.txt");
        int recruter = Integer.parseInt(r.ReadLine());
        r.Close();
        int salary = 0;

        int i = 0;
        while( i < vacancies.size()) {

            if(vacancies.get(i).recruterId == recruter && vacancies.get(i).closed) {

                Date now = new Date();
                Instant a = vacancies.get(i).closeDate.toInstant();
                Instant b = vacancies.get(i).openDate.toInstant();
                long days = ChronoUnit.DAYS.between(a,b);

                salary = salary + Sallary(Math.abs(days),vacancies.get(i).priority);
            }
            i++;
        }

        Writer w = new TxtWriter();
        w.Open("output4.txt");
        w.WriteLine(String.valueOf(salary));
        w.Close();
    }

    public static void main(String[] args) throws Exception {
        LinkedList<Vacancy> vacancyList = (LinkedList<Vacancy>) addVacancyToList();
        Task1(vacancyList);
        LinkedList<Integer> lateIds = Task2(vacancyList);
        Task3(lateIds);
        Task4(vacancyList);
    }

}
