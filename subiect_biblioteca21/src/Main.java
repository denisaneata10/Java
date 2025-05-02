import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Carte{
    private final String cota;
    private final String titlu;
    private final String autor;
    private final int an;

    public Carte(String cota, String titlu, String autor, int an) {
        this.cota = cota;
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
    }

    public String getCota() {
        return cota;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getAutor() {
        return autor;
    }

    public int getAn() {
        return an;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Carte{");
        sb.append("cota='").append(cota).append('\'');
        sb.append(", titlu='").append(titlu).append('\'');
        sb.append(", autor='").append(autor).append('\'');
        sb.append(", an=").append(an);
        sb.append('}');
        return sb.toString();
    }
}

final class Cititor{
    private final String nume;
    private final Carte carte;
    private final int nrZile;

    public Cititor(String nume, Carte carte, int nrZile) {
        this.nume = nume;
        this.carte = carte;
        this.nrZile = nrZile;
    }

    public String getNume() {
        return nume;
    }

    public Carte getCarte() {
        return carte;
    }

    public int getNrZile() {
        return nrZile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cititor{");
        sb.append("nume='").append(nume).append('\'');
        sb.append(", carte=").append(carte);
        sb.append(", nrZile=").append(nrZile);
        sb.append('}');
        return sb.toString();
    }
}
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
            Map<String, Carte> carti = citesteCarti("./dataIN/carti.txt");

          //  for(Carte c : carti.values()){
          //      System.out.println(c);
            //}
        System.out.println("---------AFISARE CARTI PANA IN 1940 ALFABETIC-----------");
            alfabetic(carti);

        System.out.println("--------AFISARE CITITORI----------");
        List<Cititor> cititori = citesteCititori(carti, "./dataIN/cititori.txt");

        for(Cititor cititor : cititori){
            System.out.println(cititor);
        }

        System.out.println("------afisare doar nume----------");
        for(Cititor cit : cititori){
            System.out.println(cit.getNume());
        }

        System.out.println("---------Nr zile imprumut ordonati descrescator--------");
        nrZile(cititori);
    }

    //metoda de citire a cartilor din fisier intr un map
    private static Map<String, Carte> citesteCarti (String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            return reader.lines()
                    .map(linie-> {
                        String[] parts = linie.split("\t");
                        return new Carte(
                                parts[0],
                                parts[1],
                                parts[2],
                                Integer.parseInt(parts[3])
                        );
                    })
                    .collect(Collectors.toMap(Carte::getCota, Function.identity()));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //metoda citire cititori din fisier
    private static List<Cititor> citesteCititori(Map<String, Carte> carti, String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            return reader.lines()
                    .map(linie->{
                        String[] parts = linie.split("\t");
                        return new Cititor(
                            parts[0],
                            carti.get(parts[1]),
                            Integer.parseInt(parts[2])
                        );
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //metoda afisare carti inainte de 1940, alfabetic dupa titlu
    private static void alfabetic(Map<String, Carte> carti){
        carti.values().stream()
                .filter(c->c.getAn()<1940)
                .sorted(Comparator.comparing(Carte::getTitlu))
                .forEach(System.out::println);
    }

    //metoda afisare total zile de imprumut pe cititor
    //sortare descrescatoare dupa total

    private static void nrZile(List<Cititor> cititori){
        //facem un map cheie valoare : cheia numele valoarea nr de zile


        Map<String, Integer> totalZile =
                cititori.stream()
                        .collect(Collectors.groupingBy(Cititor::getNume,
                                Collectors.summingInt(Cititor::getNrZile)
                        ));

        //sortam mapul
        totalZile.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry-> System.out.printf("%-30s %d%n", entry.getKey(), entry.getValue()));


        //afisam in fisier text
        try(PrintWriter writer = new PrintWriter("./dataOUT/raport.txt")){
            totalZile.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry-> writer.printf("%-30s %d%n", entry.getKey(), entry.getValue()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}