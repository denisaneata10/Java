import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Conferinta{
    private final int idConf;
    private final String numeConf;
    private final String locatieConf;
    private final String organizator;

    public Conferinta(int idConf, String numeConf, String locatieConf, String organizator) {
        this.idConf = idConf;
        this.numeConf = numeConf;
        this.locatieConf = locatieConf;
        this.organizator = organizator;
    }

    public int getIdConf() {
        return idConf;
    }

    public String getNumeConf() {
        return numeConf;
    }

    public String getLocatieConf() {
        return locatieConf;
    }

    public String getOrganizator() {
        return organizator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Conferinta{");
        sb.append("idConf=").append(idConf);
        sb.append(", numeConf='").append(numeConf).append('\'');
        sb.append(", locatieConf='").append(locatieConf).append('\'');
        sb.append(", organizator='").append(organizator).append('\'');
        sb.append('}');
        return sb.toString();
    }


}

final class Prezentare{
    private final String zi;
    private final String ora;
    private final Conferinta conf;
    private final String titlu;
    private final String prezentator;
    private final String tip;
    private final String sala;

    public Prezentare(String zi, String ora, Conferinta conf, String titlu, String prezentator, String tip, String sala) {
        this.zi = zi;
        this.ora = ora;
        this.conf = conf;
        this.titlu = titlu;
        this.prezentator = prezentator;
        this.tip = tip;
        this.sala = sala;
    }

    public String getZi() {
        return zi;
    }

    public String getOra() {
        return ora;
    }

    public Conferinta getConf() {
        return conf;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getPrezentator() {
        return prezentator;
    }

    public String getTip() {
        return tip;
    }

    public String getSala() {
        return sala;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Prezentare{");
        sb.append("zi='").append(zi).append('\'');
        sb.append(", ora='").append(ora).append('\'');
        sb.append(", conf=").append(conf);
        sb.append(", titlu='").append(titlu).append('\'');
        sb.append(", prezentator='").append(prezentator).append('\'');
        sb.append(", tip='").append(tip).append('\'');
        sb.append(", sala='").append(sala).append('\'');
        sb.append('}');
        return sb.toString();
    }
}



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Map<Integer, Conferinta> conferinte = citesteConferinte("./dataIN/conferinte.txt");
        for(Conferinta conf : conferinte.values()){
            System.out.println(conf);
        }
        List<Prezentare> prezentari = citestePrezentari("./dataIN/prezentari.txt", conferinte);
        for(Prezentare p:prezentari){
           System.out.println(p);
        }

        afiseazaPrezentariAlfabetic(prezentari);

        nrPrez(prezentari);

        LocatiiOrdonate(prezentari, conferinte);

        orarPeZi(prezentari, "Luni");

        orar(prezentari);


    }

//functie citire conferinte
    private static Map<Integer, Conferinta> citesteConferinte (String Path){
        try(BufferedReader reader = new BufferedReader(new FileReader(Path))){
            return reader.lines()
                    .map(linie->{
                        String[] parts = linie.split("\t");
                        return new Conferinta(
                                Integer.parseInt(parts[0]),
                                parts[1],
                                parts[2],
                                parts[3]
                        );
                    }).collect(Collectors.toMap(Conferinta::getIdConf, Function.identity()));
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citire", e);
        }
    }

    //functie citire prezentari
    private static List<Prezentare> citestePrezentari(String path, Map<Integer, Conferinta> conferinte){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            return reader.lines()
                    .map(linie->{
                        String[] parts = linie.split("\t");
                        if (parts.length < 7) {
                            throw new IllegalArgumentException("Linie invalidă pentru prezentare: " + linie);
                        }
                        return new Prezentare(
                                parts[0],
                                parts[1],
                                conferinte.get(Integer.parseInt(parts[2])),
                                parts[3],
                                parts[4],
                                parts[5],
                                parts[6]
                        );
                    }).collect(Collectors.toList());
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    //functie afisare prezentari in ordine alfabetica
    private static void afiseazaPrezentariAlfabetic(List<Prezentare> prezentari){
        System.out.println("------Afisare prezentari in ordine alfabetica dupa titlu:------");
        prezentari.stream()
                .map(Prezentare::getTitlu)
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    //nr de prezentari per organizator, separat invitat si sesiune
    private static void nrPrez(List<Prezentare> prezentari){
        //facem  un antet
        System.out.println("Nr prezentari per organizator\n");
        System.out.printf("%-35s %4s %4s\n", "Organizator", "Sesiuni", "Invitat");

        prezentari.stream()
                .collect(Collectors.groupingBy(Prezentare::getConf))
                .forEach(( conf,prez)->{
                    long nrTotal = prez.stream().count();
                    long nrSesiuni = prez.stream()
                            .filter(p->p.getTip().equals("Sesiune")).count();
                    long nrInvitati = nrTotal - nrSesiuni;
                    System.out.printf("%-35s %4s %4s\n", conf.getOrganizator(), nrSesiuni, nrInvitati);
                });

    }

    //lista locatii ordonate descrescator dupa numarul de prezentari
    private static void LocatiiOrdonate(List<Prezentare> prezentari, Map<Integer, Conferinta> conferinte){
        //facem un map cheie valoare -- cheia: Locatia, valoarea: nr de prezentari de la locatie
        Map<String, Long> nrPrez = prezentari.stream()
                .map(prezentare -> prezentare.getConf().getLocatieConf())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        //sortam mapul?
        nrPrez.entrySet()
                .stream()
                .sorted((entry1, entry2)-> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(System.out::println);


    }
    public static void LocatiiOrd2(List<Prezentare> prezentari){
            Map<String, Long> nrprez = prezentari.stream()
                    .collect(Collectors.groupingBy(Prezentare::getTitlu, Collectors.counting()));
    }

    //prelucrare astfel incat sa ne afiseze orarul prezentarilor intr o anumita zi

    private static void orarPeZi(List<Prezentare> prezentari, String zi){
        System.out.println("---------Orarul pentru " + zi + "-------------");
        prezentari.stream()
                .filter(p->p.getZi().equalsIgnoreCase(zi))
                .sorted(Comparator.comparing(Prezentare::getOra))
                .forEach(p->{
                    System.out.printf("%s - %s | %s | %s | %s\n",
                            p.getOra(),
                            p.getTitlu(),
                            p.getPrezentator(),
                            p.getTip(),
                            p.getSala());
                });
    }

    //orarul pe toate zilele
    private static void orar(List <Prezentare> prezentari){
        prezentari.stream()
           .sorted(Comparator.comparingInt(p -> Zi.getIndex(p.getZi())))
           .forEach(p -> {
        System.out.printf("%-10s %-6s %-40s %-10s %-10s\n",
                p.getZi(), p.getOra(), p.getTitlu(), p.getSala(), p.getTip());
    });

    }
    enum Zi {
        Luni, Marți, Miercuri, Joi, Vineri, SAMBATA, DUMINICA;

        public static int getIndex(String ziText) {
            return Zi.valueOf(ziText).ordinal();
        }
    }










}

