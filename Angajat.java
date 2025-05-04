import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//1. cream clasa
public final class Angajat1 {
    private final String numeAngajat;
    private final String denumireDepartament;
    private final int salariu;

    public Angajat1(String numeAngajat, String denumireDepartament, int salariu) {
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
        return "Angajat1{" +
                "numeAngajat='" + numeAngajat + '\'' +
                ", denumireDepartament='" + denumireDepartament + '\'' +
                ", salariu=" + salariu +
                '}';
    }
//citire din fisier
    public static List<Angajat1> citesteDinFisier(String numeFisier){
        List<Angajat1>angajati=new ArrayList<>();
        try(BufferedReader fisier=new BufferedReader(new FileReader("./fisiere./angajati.txt"))){
            String linie;
            while((linie=fisier.readLine())!=null){
                String[] ceva=linie.split(";");
                String nume=ceva[0];
                String denumire=ceva[1];
                int salariu=Integer.parseInt(ceva[2]);

                Angajat1 a=new Angajat1(nume,denumire,salariu);

                angajati.add(a);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return angajati;
    }
//2.sa se afiseze la consola nr tot de angajati
    public static int nrTotAng(List<Angajat1>angajati){
        return angajati.size();
    }
//3.pt un dep introdus de la consola, sa se afiseze lista de angajati sortata descrescator
    public static void angaatiDescrescator(List<Angajat1> angajati, String departament){
        angajati.stream()
                .filter(a->a.getDenumireDepartament().equalsIgnoreCase(departament))
                .sorted((a1,a2)->Integer.compare(a2.getSalariu(),a1.getSalariu()))
                .forEach(System.out::println);
    }
//am impartit cerinta 4 in 2 functii, aici am calculat mediile salariilor pe fiecare departament
    public static Map<String,Double> salariiPeDepartament(List<Angajat1> angajati){
        return angajati.stream()
                .collect(Collectors.groupingBy(
                        Angajat1::getDenumireDepartament
                        ,Collectors.averagingInt(Angajat1::getSalariu)));
    }
//aici am scris intr-un fisier text
    public static void scrieInFisier(Map<String,Double> mediiSalPeDep){
        try(BufferedWriter raport=new BufferedWriter(new FileWriter("departamente.txt"))) {
            for(Map.Entry<String,Double> entry: mediiSalPeDep.entrySet()){
                raport.write(entry.getKey()+"->"+String.format("%.2f",entry.getValue()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Raport generat");
    }
    //main-ul unde am apelat toate funtiile
    public static void main(String[] args){
        List<Angajat1>angajati=citesteDinFisier(".\fisiere.angajati.txt");
        for(Angajat1 a:angajati){
            System.out.println(a);
        }
        int TotalAngajati=nrTotAng(angajati);
        System.out.println(TotalAngajati+" angajati");

        Scanner scanner=new Scanner(System.in);
        System.out.println("introduceti denumire departamentului: ");
        String departament=scanner.nextLine();
        Angajat1.angaatiDescrescator(angajati, departament);

        Map<String, Double> raport=Angajat1.salariiPeDepartament(angajati);
        Angajat1.scrieInFisier(raport);


    }
}

