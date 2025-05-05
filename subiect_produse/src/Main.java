import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;

final class Produs{
    private final String denumire;
    private final double pret;
    private final int cantitate;

    public Produs(String denumire, double pret, int cantitate) {
        this.denumire = denumire;
        this.pret = pret;
        this.cantitate = cantitate;
    }

    public String getDenumire() {
        return denumire;
    }

    public double getPret() {
        return pret;
    }

    public int getCantitate() {
        return cantitate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Produs.class.getSimpleName() + "[", "]")
                .add("denumire='" + denumire + "'")
                .add("pret=" + pret)
                .add("cantitate=" + cantitate)
                .toString();
    }

    public boolean equals(Produs p) {

        return Objects.equals(denumire, p.denumire);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(denumire);
    }
}

public class Main {
    public static void main(String[] args){
        List<Produs> produse = citireProduse("./dataIN/produse.txt");
        for(Produs p: produse){
            System.out.println(p);
        }
        valoareTotalaStoc(produse);
        System.out.println("-------------Produsele si stocul fiecaruia-----------------");
        produseCuStoc(produse);
      //  raport(produse, "./dataOUT/raport.txt");
        Produs p1 = new Produs("Castraveti", 5.00, 4);
        Produs p2 = new Produs("Castraveti", 5.00, 4);
        System.out.println(p1.equals(p2));

    }

    //metoda citire produse din fisier
    //+ valoarea totala a stocului
    public static List<Produs> citireProduse(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            List<Produs> produse = reader.lines()
                    .map(linie->{
                        String[] parts = linie.split(",");
                        return new Produs(
                                parts[0],
                                Double.parseDouble(parts[1]),
                                Integer.parseInt(parts[2])
                        );
                    }).collect(Collectors.toList());

            return produse;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void valoareTotalaStoc(List<Produs> produse){
        int stoc = produse.stream()
                .mapToInt(p-> p.getCantitate()).sum();

        System.out.println("Valoarea totala a stocului: " + stoc);
    }

    //top trei produse in ordinea descrescatoare valorii stocului

    //facem un map cheie valoare
    //cheie - denumirea produsului
    //valoare - stocul total

    public static void produseCuStoc(List<Produs> produse){
        Map<String, Double> valoareTotala = produse.stream()
                .collect(Collectors.groupingBy(Produs::getDenumire, Collectors.summingDouble(Produs::getPret)));

        valoareTotala.forEach((denumire, stoc)->
        {
            System.out.println(denumire + " cu stocul de: " + stoc);
        });

        valoareTotala.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(System.out::println);




    }

    //metoda generare raport in care avem produsul si apoi pretul mediu (medie ponderata la cantitate)
    public static void raport(List<Produs> produse, String path){
        try(PrintWriter writer = new PrintWriter(path)){
          Map<String, Double> prod =   produse.stream()
                    .collect(Collectors.groupingBy(Produs::getDenumire,
                            Collectors.averagingDouble(Produs::getPret)));
            prod.forEach((denumire, medie)->{
                writer.println(denumire + " " + medie + " lei");
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
