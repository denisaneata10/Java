import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//1. cream clasa
public final class Angajat {
    private final String numeAngajat;
    private final String denumireDepartament;
    private final int salariu;

    public Angajat(String numeAngajat, String denumireDepartament, int salariu) {
        this.numeAngajat = numeAngajat;
        this.denumireDepartament = denumireDepartament;
        this.salariu = salariu;
    }

    public String getNumeAngajat() {
        return numeAngajat;
    }

    public String getDenumireDepartament() {
        return denumireDepartament;
    }

    public int getSalariu() {
        return salariu;
    }

    @Override
    public String toString() {
        return "Angajati{" +
                "numeAngajat='" + numeAngajat + '\'' +
                ", denumireDepartament='" + denumireDepartament + '\'' +
                ", salariu=" + salariu +
                '}';
    }
//2. citire din fisier+ metoda care sa calculeze nr total de angajati
    public static List<Angajat> citireDinFisier(String numeFisier){
        List<Angajat>angajati=new ArrayList<>();
        try(BufferedReader fisierAngajati=new BufferedReader(new FileReader("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\fisiere\\angajati.txt"))) {
            String linie;
            while((linie=fisierAngajati.readLine())!=null){
                String date[]=linie.split(";");
                String nume=date[0];
                String denumire=date[1];
                int salariu=Integer.parseInt(date[2]);

                Angajat a=new Angajat(nume, denumire,salariu);

                angajati.add(a);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } ;

        return angajati;
    }

    public static int nrTotalAngajati(List<Angajat> angajati){
        int nrTotAng=0;
        nrTotAng=angajati.size();
        return nrTotAng;
   }

   

    public static void main(String[] args){
      List<Angajat>angajati=citireDinFisier("angajati.txt");
       angajati.forEach(System.out::println);
       int nrTotalAng=nrTotalAngajati(angajati);
        System.out.println(nrTotalAng);


    }

}
