import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

final class Vehicul{
    private final String nrInm;
    private final String marca;
    private final int nrLocuri;

    public Vehicul(String nrInm, String marca, int nrLocuri) {
        this.nrInm = nrInm;
        this.marca = marca;
        this.nrLocuri = nrLocuri;
    }

    public String getNrInm() {
        return nrInm;
    }

    public String getMarca() {
        return marca;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Vehicul{");
        sb.append("nrInm='").append(nrInm).append('\'');
        sb.append(", marca='").append(marca).append('\'');
        sb.append(", nrLocuri=").append(nrLocuri);
        sb.append('}');
        return sb.toString();
    }

    public boolean esteDeLux(){
        List<String> listaMarci = List.of("Mercedes","BMW", "Audi");
        return listaMarci.contains(this.marca);
    }


}
public class Main {
    final static double TAXA = 10.0;
    public static void main(String[] args) {
        System.out.println("-----Cerinta 1------");
            Vehicul v1= new Vehicul("tr-15-hma", "Mercedes", 5);
            System.out.println(v1.esteDeLux());
        System.out.println("-----Cerinta 2--------");
            List<Vehicul> vehicule = citireVehicule("./dataIN/vehicule.txt");
            for(Vehicul v : vehicule){
                System.out.println(v);
            }
        System.out.println(vehicule.size() + " vehicule citite din fisier");

        int nrLocuri = vehicule.stream()
                .mapToInt(Vehicul::getNrLocuri)
                .sum();
        System.out.println("Nr locuri in total: " +nrLocuri);

        System.out.println("------Cerinta 3--------");

        nrLocuriPeCategorii(vehicule);
        System.out.println("---------Cerinta 4---------");
        raport(vehicule, "./dataOUT/raport.txt");


    }

    //metoda citire date din fisier
    public static List<Vehicul> citireVehicule(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
           List<Vehicul> vehicule =  reader.lines()
                   .map(linie->{
                       String[] parts = linie.split(",");
                       return new Vehicul(
                               parts[0],
                               parts[1],
                               Integer.parseInt(parts[2])
                       );
                   }).collect(Collectors.toList());

            return vehicule;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //metoda suma nr de locuri pt vehicule de lux, si pt celalalte
    public static void nrLocuriPeCategorii(List<Vehicul> vehicule){
        int nrLocuriDeLux = vehicule.stream()
                .filter(Vehicul::esteDeLux)
                .mapToInt(Vehicul::getNrLocuri).sum();
        int nrLocuriStandard = vehicule.stream()
                        .mapToInt(Vehicul::getNrLocuri)
                                .sum() - nrLocuriDeLux;
        System.out.println("Nr locuri in masini de lux: " + nrLocuriDeLux);
        System.out.println("Nr locuri standard: " + nrLocuriStandard);

    }

    //metoda raport in fisier
    public static void raport(List< Vehicul> vehicule, String output_path){
        Map<String, Double> taxePeJudet = new HashMap<>();
        double taxa = 0;
        for(Vehicul v : vehicule){
            String judet = v.getNrInm().split("-")[0];
            if(v.esteDeLux()){
                taxa = 1.2 * TAXA;
            }
            else{
                taxa = TAXA;
            }

            taxePeJudet.put(judet, taxePeJudet.getOrDefault(judet, 0.0) + taxa);

        }

        //afisam raportul in fisier
        //facem un try cu resurse
        try(PrintWriter writer = new PrintWriter(output_path)){
            for(Map.Entry<String, Double> entry : taxePeJudet.entrySet()){
                writer.printf("%-2s %.2f%n", entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



}