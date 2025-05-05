import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

final class Student{
    private final String nume;
    private final String disciplina;
    private final int nota;

    public Student(String nume, String disciplina, int nota) {
        this.nume = nume;
        this.disciplina = disciplina;
        if(nota <= 0 || nota > 10)
            throw new IllegalArgumentException("Nota trebuie sa fie intre 1 si 10");
        this.nota = nota;
    }

    public String getNume() {
        return nume;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public int getNota() {
        return nota;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("nume='" + nume + "'")
                .add("disciplina='" + disciplina + "'")
                .add("nota=" + nota)
                .toString();
    }


}


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
                List<Student> studenti = citireStudenti("./dataIN/studenti.txt");
                for(Student s: studenti){
                    System.out.println(s);
                }
                //mai jos testez ca nota sa fie intre 1 si 10
        //Student s = new Student("Bia", "Mate", 11);
        //System.out.println(s);
        System.out.println("-------------CERINTA 2-------------");
        System.out.println("Nr total de studenti: " );
        double nrStud = studenti.stream()
                .map(s->s.getNume()).distinct().count(); //distinct pentru ca am de exemplu acelasi student inregistrat de doua ori cu doua note la materii diferite
        System.out.println(nrStud);
        //daca aveam doar studenti diferiti foloseam studenti.size()
        System.out.println("--------------Cerinta 3-----------------");
        afiseazaStudentiDesc(studenti, "Engleza");
       //-------------CERINTA 4-------------
        // raport(studenti, "./dataOUT/raport.txt");

        System.out.println("--------------alfabetic-----------------");
            studAlfabetic(studenti);

            afisareNote(studenti, "Ioana");

    }

    //metoda citire studenti din fisier
    public static List<Student> citireStudenti(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            List<Student> studenti = reader.lines()
                    .map(linie->{
                        String[] parts = linie.split(",");
                        return new Student(
                                parts[0],
                                parts[1],
                                Integer.parseInt(parts[2])
                        );
                    }).collect(Collectors.toList());

            return studenti;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //sa se afiseze lista sortata descrescator pt o disciplina data de user in functie de note
    public static void afiseazaStudentiDesc(List<Student> studenti, String disciplina){
        Map<String, Integer> studentiCuNote = studenti.stream()
                .filter(s->s.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toMap(Student::getNume, Student::getNota));

        studentiCuNote.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(System.out::println);
    }

    //raport txt care sa contina denumirea disciplinei si numaruk de note pentru fiecare disciplina
    public static void raport(List<Student> studenti, String path){
        try(PrintWriter writer = new PrintWriter(path)){
          //facem un map cheie valoare
            Map<String, Long> disciplineNrStud = studenti.stream()
                    .collect(Collectors.groupingBy(Student::getDisciplina, Collectors.counting()));

            disciplineNrStud.forEach((disciplina, nrStud)->{
                writer.println(disciplina + " " + nrStud);
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //metoda afisare studenti in oridine alfabetica
    public static void studAlfabetic(List<Student> studenti){
        studenti.stream()
                .map(stud->stud.getNota()).sorted()
                .forEach(System.out::println);

    }

    //prelucrare astfel incat sa ne afiseze notele pentru un anumit student
    public static void afisareNote(List<Student> studenti, String nume){
        System.out.println("Notele pentru studentul/a: " + nume);
        studenti.stream()
                .filter(stud->stud.getNume().equalsIgnoreCase(nume))
                .forEach(stud->{
                        System.out.printf("%-30s %10s %5s\n", stud.getNume(), stud.getDisciplina(), stud.getNota());});
    }


}