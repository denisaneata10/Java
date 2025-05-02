import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.CoderMalfunctionError;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Camera implements Comparable<Camera> {
    private final String cod;
    private final int nrPaturi;
    private final float tarif;
    private final int nrZile;

    public Camera(String cod, int nrPaturi, float tarif, int nrZile) {
        this.cod = cod;
        this.nrPaturi = nrPaturi;
        this.tarif = tarif;
        this.nrZile = nrZile;
    }

    public String getCod() {
        return cod;
    }

    public int getNrPaturi() {
        return nrPaturi;
    }

    public float getTarif() {
        return tarif;
    }

    public int getNrZile() {
        return nrZile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Camera{");
        sb.append("cod='").append(cod).append('\'');
        sb.append(", nrPaturi=").append(nrPaturi);
        sb.append(", tarif=").append(tarif);
        sb.append(", nrZile=").append(nrZile);
        sb.append('}');
        return sb.toString();
    }

    //metoda care returneaza ce tarif are un pat

    public float tarifPerPat(){
        return getTarif()/getNrPaturi();
    }

    @Override
    public int compareTo(Camera c) {
        return Float.compare(this.tarifPerPat(), c.tarifPerPat());
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Map<String, Camera> camere = citesteCamere("./dataIN/camere.csv");

        for(Camera c: camere.values()){
            System.out.println(c);
            //testam si metoda tarif per pat
            System.out.println(c.tarifPerPat());
        }

        //afisam camerele cu rata de ocupare peste 50
        System.out.println("--------Camere cu rata de ocupare peste 50%----------");
        rata50(camere);

        System.out.println("--------Tipuri de camere ----------");
        TipuriDeCamere(camere);
        
    }

    //metoda citire camere din fisier
    private static Map<String, Camera> citesteCamere(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            return reader.lines()
                    .map(linie-> {
                        String[] parts = linie.split(",");
                        return new Camera(
                                parts[0],
                                Integer.parseInt(parts[1]),
                                Float.parseFloat(parts[2]),
                                Integer.parseInt(parts[3])
                        );
                    }).collect(Collectors.toMap(Camera::getCod, Function.identity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //metoda ca sa afisam camerele cu pesste 50% rata de ocupare
    private static void rata50(Map<String, Camera> camere){
        camere.values().stream()
                .filter(c-> c.getNrZile() > 183)
                .forEach(System.out::println);
    }

    //3.pentru fiecare tip de camera(nr de paturi) sa se afiseze nr de camere
    //+ nr de mediu de zile ocupate pentru respectivul tip de camera

    private static void TipuriDeCamere(Map<String, Camera> camere){
        Map<Integer, Long> tipuri = camere.values().stream()
                .map(c-> c.getNrPaturi())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        tipuri.forEach((nrPaturi, count)->
                System.out.println("Camere cu " + nrPaturi +" paturi : " + count));

        //sa facem si media ratei de ocupare pentru fiecare
        Map<Integer, Double> nrMediuZile = camere.values().stream()
                .collect(Collectors.groupingBy(Camera::getNrPaturi,
                        Collectors.averagingInt(c-> c.getNrZile())));

        tipuri.forEach((nrPaturi, count)->{
                double medie = nrMediuZile.get(nrPaturi);
                System.out.println("Camere cu " + nrPaturi +" paturi : " + count + " cu nr mediu de zile " + medie);
        });
    }
}

