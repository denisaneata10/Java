import java.io.*;
import java.util.*;
//facem clasa
public final class Vehicul{
    private final String nrImatriculare;
    private final String marca;
    private final int nrPasageri;

    public Vehicul(String nrImatriculare, String marca, int nrPasageri) {
        this.nrImatriculare = nrImatriculare;
        this.marca = marca;
        this.nrPasageri = nrPasageri;
    }
//definim lista cu vehicule de lux
    private static final List<String> vehiculeDeLux=Arrays.asList("Mercedes","BMW","Audi");

    public String getNrImatriculare() {
        return nrImatriculare;
    }

    public String getMarca() {
        return marca;
    }

    public int getNrPasageri() {
        return nrPasageri;
    }

    @Override
    public String toString() {
        return "Vehicul{" +
                "nrImatriculare='" + nrImatriculare + '\'' +
                ", marca='" + marca + '\'' +
                ", nrPasageri=" + nrPasageri +
                '}';
    }
//metoda esteDeLux
    public boolean esteDeLux(){
       return vehiculeDeLux.contains(this.marca);

    }
//calculam nr total de vehicule
    public static int nrTotVehicule(List<Vehicul> vehicule){
        return vehicule.size();
    }
//calculam nr tot de pasageri
    public static int nrTotPasageri(List<Vehicul> vehicule){
        int totPasageri=0;
        for(Vehicul v:vehicule){
            totPasageri+=v.getNrPasageri();
        }
        return totPasageri;
    }
//calculam numarul de pasageri pt vehiculele de lux
    public static int nrPasageriDeLux(List<Vehicul> vehicule){
        int pasageriDeLux=0;
        for(Vehicul v:vehicule){
            if(v.esteDeLux()){
                pasageriDeLux+=v.getNrPasageri();
            }
        }
        return pasageriDeLux;
    }
//calculam nr de pasageri pt celelalte vehicule
    public static int PasagerialteVehicule(List<Vehicul> vehicule){
        int rest=0;
        for(Vehicul v:vehicule){
            if(!v.esteDeLux()){
                rest+=v.getNrPasageri();
            }
        }
        return rest;
    }
//citire din fisier
    public static List<Vehicul>citesteDinFisier(String numeFisier){
        List<Vehicul> vehicule=new ArrayList<>();
        try(BufferedReader fisier=new BufferedReader(new FileReader("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\src\\parcare.txt"))) {
            String linie;
            while((linie=fisier.readLine())!=null){
                String date[]=linie.split(",");
                String nr=date[0];
                String marca=date[1];
                int nrPasageri=Integer.parseInt(date[2]);

                Vehicul v=new Vehicul(nr, marca, nrPasageri);
                vehicule.add(v);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vehicule;
    }
//taxa pe judete
    public static Map<String, Double> calculeazaTaxePeJudete(List<Vehicul> vehicule, Double taxa){
        Map<String, Double> taxe=new HashMap<>();

        for(Vehicul v:vehicule){
            String judet=v.getNrImatriculare().split("-")[0];

            if(v.esteDeLux()){
                 taxa=1.2*taxa;
            }
            taxe.put(judet, taxe.getOrDefault(judet,0.0)+taxa);
        }
        return taxe;
    }
//scriere in fisier
    public static void scrieInFisier(Map<String, Double> taxePeJudet){
        try(BufferedWriter raport=new BufferedWriter(new FileWriter("raportParcare.txt"))) {
            for(Map.Entry<String, Double> entry : taxePeJudet.entrySet()){
                raport.write(entry.getKey()+","+ String.format("%.2f",entry.getValue()));
                raport.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//main
    public static void main(String[] args){
    List<Vehicul> vehicule=citesteDinFisier("parcare.txt");
    for(Vehicul v:vehicule){
        System.out.println(v);
    }
    boolean deLux=vehicule.get(0).esteDeLux();
        System.out.println(deLux);

        int totVehicule=Vehicul.nrTotVehicule(vehicule);
        int totPasageri=Vehicul.nrTotPasageri(vehicule);
        System.out.println(totVehicule+" vechicule si "+totPasageri+" pasageri");

        int lux=Vehicul.nrPasageriDeLux(vehicule);
        int rest=Vehicul.PasagerialteVehicule(vehicule);
        System.out.println("Vehicule de lux: "+lux+" pasageri");
        System.out.println("Alte vehicule: "+rest+" pasageri");

       final double taxa=10;
       Map<String, Double> taxe=Vehicul.calculeazaTaxePeJudete(vehicule, taxa);
        System.out.println(taxe);
        Vehicul.scrieInFisier(taxe);
    }
}
