import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Apartament {
 private final int nrAp;
 private final int suprafata;
 private final int nrPers;

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

    @Override
    public String toString() {
        return "Apartament{" +
                "nrAp=" + nrAp +
                ", suprafata=" + suprafata +
                ", nrPers=" + nrPers +
                '}';
    }

    public static List<Apartament> citireDinFisier(String numeFisier){
        List<Apartament>apartamente=new ArrayList<>();
        try(BufferedReader fisier=new BufferedReader(new FileReader("imobil.txt"))) {
            String linie;
            while((linie=fisier.readLine())!=null){
                String data[]=linie.split(",");
                int nrAp=Integer.parseInt(data[0]);
                int sup=Integer.parseInt(data[1]);
                int nrPers=Integer.parseInt(data[2]);
                
                Apartament a=new Apartament(nrAp,sup,nrPers);
                
                apartamente.add(a);
                
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apartamente;
    }
    public static void main(String[] args){

    }
}

