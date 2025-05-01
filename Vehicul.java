import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vehicul {
    private final String nrInmatriculare;
    private final String marca;
    private final int nrPasageri;

    private static final List<String>deLux= Arrays.asList("Mercedes", "Audi", "BMW");

    public Vehicul() {
        this.nrInmatriculare=" ";
        this.marca=" ";
        this.nrPasageri=0;
    }

    public Vehicul(String nrInmatriculare, String marca, int nrPasageri) {
        this.nrInmatriculare = nrInmatriculare;
        this.marca = marca;
        this.nrPasageri = nrPasageri;
    }

    public String getNrInmatriculare() {
        return nrInmatriculare;
    }

    public String getMarca() {
        return marca;
    }

    public int getNrPasageri() {
        return nrPasageri;
    }

    public boolean esteDeLux(){
        return deLux.contains(this.marca);
    }

    @Override
    public String toString() {
        return "Vehicul{" +
                "nrInmatriculare='" + nrInmatriculare + '\'' +
                ", marca='" + marca + '\'' +
                ", nrPasageri=" + nrPasageri +
                '}';
    }

    public static void main(String[] args){
//        Vehicul v1=new Vehicul("TR-11-MDI","Opel",4);
//        System.out.println(v1.esteDeLux());
        List<Vehicul> vehicule=new ArrayList<>();

        try(var fisierParcare= new BufferedReader(new FileReader("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\src\\parcare.txt"))){
        String linie;

        while((linie=fisierParcare.readLine())!=null){

         String date[]=linie.split(",");

         String nrInmatriculare=date[0];
         String marca=date[1];
         int nrPasageri=Integer.parseInt(date[2]);

         Vehicul v=new Vehicul(nrInmatriculare, marca, nrPasageri);
         vehicule.add(v);

        }
            fisierParcare.close();
        }

        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int nrVehicule=vehicule.size();
        int nrTotalPasageri=0;
        for( Vehicul v:vehicule)
            nrTotalPasageri+=v.getNrPasageri();

        System.out.printf("%d vehicule cu %d pasageri %n", nrVehicule, nrTotalPasageri);
        int nrPasageriVdeLux=0;
        int nrPasageriRest=0;
        for(Vehicul v: vehicule){
            if(v.esteDeLux())
                nrPasageriVdeLux+= v.getNrPasageri();
            else
                nrPasageriRest+= v.getNrPasageri();

        }
        
        System.out.printf("Vehicule de lux: %d pasageri%n", nrPasageriVdeLux);
        System.out.printf("Alte vehicule: %d pasageri", nrPasageriRest);
    }
}
