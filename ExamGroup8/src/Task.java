import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Bug {
    private int id;
    private String page;
    private String title;
    private int priorityId;
    private int year;
    private int month;
    private int day;

}
    @Override
    public String toString() {
        return "Bug{" +
                "id=" + id +
                ", page=" + page +
                ", title='" + title + '\'' +
                ", priorityId=" + priorityId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}

class Qa{
    private int id;
    private String surname;
    private String name;

    public Qa(int id, String surname, String name) {
        this.id = id;
        this.surname = surname;
        this.name = name;
    }

    public Qa() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Qa{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

class Priority{
    private String priority;
    private double weight;
    private int id;

    public Priority() {
        this.priority = priority;
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "priority='" + priority + '\'' +
                ", weight=" + weight +
                ", id=" + id +
                '}';
    }
}

public class Task {

    public static List<Bug> readBugs(File file){
        List<Bug> bugs =new ArrayList<>();
        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] data = line.split(";");
                Bug bug = new Bug();
                bug.setId(Integer.parseInt(data[0]));
                bug.setPage(data[1]);
                bug.setTitle(data[2]);
                bug.setPriorityId(Integer.parseInt(data[3]));
                String[] date = data[4].split("\\.");
                bug.setDay(Integer.parseInt(date[0]));
                bug.setMonth(Integer.parseInt(date[1]));
                bug.setYear(Integer.parseInt(date[2]));
                bugs.add(bug);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bugs;
    }
    public static List<Qa> readQas(File file){
        List<Qa> qas =new ArrayList<>();
        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] data = line.split("\\s++|,|;");
                Qa qa = new Qa();
                qa.setId(Integer.parseInt(data[0]));
                qa.setSurname(data[1]);
                qa.setName(data[2]);
                qas.add(qa);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return qas;
    }
    public static Map<Integer, Integer> readBugToQA(File file){
        Map<Integer, Integer> bugsToQAs= new LinkedHashMap<>(); // guarantee saving of order

        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();;
                String data[] = line.split(";");
                bugsToQAs.put(Integer.parseInt(data[0]), Integer.parseInt(data[1]));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bugsToQAs;
    }
    public static List<Priority> readPriorities(File file){
        List<Priority> priorities = new ArrayList<>();
        try(Scanner scanner = new Scanner(file)) {
           double startWeight = 100;
           while (scanner.hasNextLine()){
               String line = scanner.nextLine();
               String[] data = line.split(";");
               Priority priority = new Priority();
               priority.setId(Integer.parseInt(data[0]));
               priority.setPriority(data[1]);
               priority.setWeight(startWeight);
               startWeight /= 10;
               priorities.add(priority);
           }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return priorities;
    }

    public static void Task1(File output,File input, List<Bug>bugs, List<Priority>priorities){
        try(Scanner scanner = new Scanner(input);
            PrintWriter writer = new PrintWriter(output)) {
            if (scanner.hasNext()) {
                String inputPriority = scanner.next();
                int id = -1;
                for (Priority element : priorities) {
                    if (element.getPriority().equals(inputPriority)) {
                        id = element.getId();
                    }
                }
                int finalId = id;
                List<Bug> resultBug = bugs.stream()
                            .filter((b) -> {
                                return b.getPriorityId() == finalId;
                            })
                            .collect(Collectors.toList());

                writer.print(resultBug.get(0).getTitle());
                for ( int i = 1; i < resultBug.size(); i++){
                    writer.print(System.lineSeparator());
                    writer.print(resultBug.get(i).getTitle());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void Task2(File output, List<Bug>bugs, List<Priority>priorities){
        try (PrintWriter writer = new PrintWriter(output)){
            List<Bug> result = bugs.stream()
                    .sorted((b1, b2)-> {
                        double weight1 = priorities.get(b1.getPriorityId()-1).getWeight();
                        double weight2 = priorities.get(b2.getPriorityId()-1).getWeight();
                        return Double.compare(weight2, weight1);
                    })
                    .collect(Collectors.toList());

            writer.print(result.get(0).getTitle()+";"+priorities.get(result.get(0).getPriorityId()-1).getPriority());
            for ( int i = 1; i < result.size(); i++){
                writer.print(System.lineSeparator());
                writer.print(result.get(i).getTitle()+";"+priorities.get(result.get(i).getPriorityId()-1).getPriority());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void Task3(File output, File input, List<Bug> bugs, Map<Integer, Integer> bugsToQAs){
        try(Scanner scanner = new Scanner(input);
        PrintWriter writer = new PrintWriter(output)){
            int qaId = 0;
            if (scanner.hasNext()){
                qaId = Integer.parseInt(scanner.next());
            }
            int finalQaId = qaId;
            List<Bug> result = bugs.stream()
                    .filter((b)->{
                        return bugsToQAs.get(b.getId()).equals(finalQaId);
                    }).collect(Collectors.toList());
            writer.print(result.get(0).getTitle());
            for ( int i = 1; i < result.size(); i++){
                writer.print(System.lineSeparator());
                writer.print(result.get(i).getTitle());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void Task4(File output, File input, List<Bug> bugs, Map<Integer, Integer> bugsToQAs){
        try(Scanner scanner = new Scanner(input);
            PrintWriter writer = new PrintWriter(output)){
            int qaId = 0;
            if (scanner.hasNext()){
                qaId = Integer.parseInt(scanner.next());
            }
            int finalQaId = qaId;
            List<Bug> temp = bugs.stream()
                    .filter((b)->{
                        return bugsToQAs.get(b.getId()).equals(finalQaId);
                    }).collect(Collectors.toList());

            List<Bug> result = temp.stream()
                    .sorted((b1, b2)-> {
                        int compRes = 0;

                        compRes = (b1.getYear()!= b2.getYear())? Integer.compare(b1.getYear(), b2.getYear())
                                : (b1.getMonth()!= b2.getMonth())? Integer.compare(b1.getMonth(), b2.getMonth())
                                : Integer.compare(b1.getDay(),b2.getDay());

                        return compRes;
                    })
                    .collect(Collectors.toList());

            writer.print(result.get(0).getTitle()+ ";" + result.get(0).getDate());
            for ( int i = 1; i < result.size(); i++){
                writer.print(System.lineSeparator());
                writer.print(result.get(i).getTitle()+ ";" + result.get(i).getDate());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        List<Bug> bugs = readBugs(new File("input1.txt"));
        List<Qa> qas = readQas(new File("input2.txt"));
        Map<Integer, Integer> bugsToQA = readBugToQA(new File("input3.txt"));
        List<Priority> priorities = readPriorities(new File("input4.txt"));
        bugs.forEach(System.out::println);
        qas.forEach(System.out::println);
        priorities.forEach(System.out::println);
        bugsToQA.forEach((i1,i2)->System.out.println(i1+"-"+i2));

        Task1(new File("output1.txt"), new File("input5.txt"), bugs, priorities);

        Task2(new File("output2.txt"), bugs, priorities);

        Task3(new File("output3.txt"), new File("input6.txt"), bugs, bugsToQA);

        Task4(new File("output3.txt"), new File("input6.txt"), bugs, bugsToQA);


    }
}
