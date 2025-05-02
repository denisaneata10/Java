import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Apartament{
    private final int nr;
    private final int suprafata;
    private final int nrPers;

    public Apartament(int nr, int suprafata, int nrPers) {
        this.nr = nr;
        this.suprafata = suprafata;
        this.nrPers = nrPers;
    }

    public int getNr() {
        return nr;
    }

    public int getSuprafata() {
        return suprafata;
    }

    public int getNrPers() {
        return nrPers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Apartament{");
        sb.append("nr=").append(nr);
        sb.append(", suprafata=").append(suprafata);
        sb.append(", nrPers=").append(nrPers);
        sb.append('}');
        return sb.toString();
    }
    final static int suprafataCamera = 20;

    public int getNrCamere(){
        return this.suprafata/suprafataCamera;
    }
}



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
            List<Apartament> apartamente = citireImobile("./dataIN/imobil.txt");
            for(Apartament a : apartamente){
                System.out.println(a);
            }
        System.out.println("=======CERINTA 1=========");
        System.out.println("Nr total de apartamente: " + apartamente.size());
        System.out.println("Nr de camere pt fiecare ");
        for(Apartament a : apartamente){

            System.out.println("Apartamentul " + a.getNr()+ " are " +a.getNrCamere()+ " camere");
        }
        System.out.println("=======CERINTA 2=========");
        int nrLocatari = apartamente.stream().mapToInt(Apartament::getNrPers).sum();
        int suprafataTotala = apartamente.stream().mapToInt(Apartament::getSuprafata).sum();
        System.out.println("Nr de locatari in total: " + nrLocatari + " persoane");
        System.out.println("Suprafata blocului de apartamente: " + suprafataTotala + " mp");
        System.out.println("=======CERINTA 3=========");
        nrPersPeTipuri(apartamente);

    }

    //metoda citire date din fisier
    public static List<Apartament> citireImobile(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
                List<Apartament> apartamente = reader.lines()
                        .map(linie->{
                            String[] parts = linie.split(",");
                            return new Apartament(
                                    Integer.parseInt(parts[0]),
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2])
                            );
                        }).collect(Collectors.toList());


            return apartamente;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //metoda nr de persoane per tip de apartament (nr de camere)
    public static void nrPersPeTipuri(List<Apartament> apartamente){
        //facem un map cheie valoare
        //cheia e tipul de apartament si valoarea nr de pers din el
        Map<Integer, Integer> nrPersPeCategorii =
                apartamente.stream()
                .collect(Collectors.groupingBy(Apartament::getNrCamere,
                        Collectors.summingInt(Apartament::getNrPers)));

        nrPersPeCategorii.forEach((nrCamere, nrPers)->{
            System.out.println("Apartamentele cu "+ nrCamere +" au " + nrPers +" locatari");
        });

    }




}