import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class Student {
    private final String nume;
    private final String disciplina;
    private final int note;

    public Student(String nume, String disciplina, int note) {
        this.nume = nume;
        this.disciplina = disciplina;
        this.note = note;
    }

    public String getNume() {
        return nume;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public int getNote() {
        return note;
    }
   public static void main(String[] args){

        List<Student> studenti=new ArrayList<>();

        try(var fisierStudenti=new BufferedReader(new FileReader("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\src\\studenti.txt"))) {
            String linie;
            while((linie=fisierStudenti.readLine())!=null){
                String data[]=linie.split(",");
                String nume=data[0];
                String disciplina=data[1];
                int nota=Integer.parseInt(data[2]);

                Student s= new Student(nume, disciplina,nota);
                studenti.add(s);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       int nrStudenti=studenti.size();
       System.out.println("Numarul de studenti este: "+nrStudenti);

       Scanner scanner=new Scanner(System.in);
       System.out.print("Introduceti o disciplina: ");
       String cautata = scanner.nextLine().trim();
   }
}
