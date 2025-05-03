import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Apartament {
private final int nrAp;
private final int suprafata;
private final int nrPers;
private static final int supCamera=20;

    public Apartament(int nrAp, int suprafata, int nrPers) {
        this.nrAp = nrAp;
        this.suprafata = suprafata;
        this.nrPers = nrPers;

    }

    public int getNrAp() {
        return nrAp;
    }

    public int getSuprafata() {
        return suprafata;
    }

    public int getNrPers() {
        return nrPers;
    }

    public int getNumarCamere(){
        return suprafata/supCamera;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "nrAp=" + nrAp +
                ", suprafata=" + suprafata +
                ", nrPers=" + nrPers +
                '}';
    }

    public static List<Apartament> citesteDinFisier(String numeFisier){
        List<Apartament>apartamente=new ArrayList<>();
        try(BufferedReader fisier=new BufferedReader(new FileReader("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\src\\imobil.txt"))) {
            String linie;
            while((linie=fisier.readLine())!=null){
                String date[]=linie.split(",");
                int nrAp=Integer.parseInt(date[0]);
                int sup=Integer.parseInt(date[1]);
                int nrPers=Integer.parseInt(date[2]);

                Apartament a=new Apartament(nrAp,sup,nrPers);

                apartamente.add(a);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } ;
        return apartamente;
    }


//urmatoarele 2 funtii sunt in plus, am inteles eu gresit cerinta
    public static int SuprafataTotala(List<Apartament> apartamente){
        int SupTot=0;
        for(Apartament ap:apartamente){
            SupTot+=ap.getSuprafata();
        }
        return SupTot;
    }

    public static int NrTotPers(List<Apartament> apartamente){
        int nrPersTot=0;
        for(Apartament ap:apartamente){
            nrPersTot+=ap.getNrPers();
        }
        return nrPersTot;
    }

    //cerinta 3: nr total de persoane pe fiecare tip de apartament
    public static void nrtotPersPeFiecareTipAp(List<Apartament>apartamente){
        Map<Integer,Integer>persoanePerTip=new HashMap<>();

        for(Apartament a:apartamente){
            int nrCamere=a.getNumarCamere();
            int nrPers=a.getNrPers();
            persoanePerTip.put(nrCamere,persoanePerTip.getOrDefault(nrCamere,0)+a.getNrPers());
        }
        for(Map.Entry<Integer,Integer> entry:persoanePerTip.entrySet() ){
            int nrCamere= entry.getKey();
            int nrPersoane= entry.getValue();
            System.out.println(nrCamere+" camere: "+nrPersoane+" persoane");
        }
    }
    

    public static void main(String[] args){
        //cream o lista in care citim fisierul
        List<Apartament>apartamente=citesteDinFisier("C:\\Users\\Denisa\\IdeaProjects\\ExercitiiTest1\\src\\imobil.txt");
        //afisam
        for(Apartament ap:apartamente){
            System.out.println("Suprafata: "+ap.getSuprafata()+", Numarul de persoane: "+ap.getNrPers());
        }

        int nr=apartamente.size();
        System.out.println(nr+" apartamente");
        //testam metoda de getNrcamere
        int a1= apartamente.get(0).getNumarCamere();
        System.out.println("Primul apartament are "+a1+" camere");

        //functii in plus
        int suprafataTotala=SuprafataTotala(apartamente);
        int numarDePersoane=NrTotPers(apartamente);
        System.out.println("Suprafata totala este "+suprafataTotala+", iar numarul total de persoane este "+ numarDePersoane);

        Apartament.nrtotPersPeFiecareTipAp(apartamente);

    }
}

