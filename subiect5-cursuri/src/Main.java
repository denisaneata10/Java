import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.*;
import java.util.stream.Collectors;

final class Participant {
    private final String nume;
    private final String titluCurs;
    private final int scor;

    public Participant(String nume, String titluCurs, int scor) {
        this.nume = nume;
        this.titluCurs = titluCurs;
        this.scor = scor;
    }

    public String getNume() {
        return nume;
    }

    public String getTitluCurs() {
        return titluCurs;
    }

    public int getScor() {
        return scor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Participant{");
        sb.append("nume='").append(nume).append('\'');
        sb.append(", titluCurs='").append(titluCurs).append('\'');
        sb.append(", scor=").append(scor);
        sb.append('}');
        return sb.toString();
    }


}
public class Main {
    public static void main(String[] args) {
        List<Participant> participanti = citireParticipanti("./dataIN/cursuri.txt");
        for(Participant p: participanti){
            System.out.println(p);
        }
        System.out.println("Nr total de participanti: " + participanti.size());
        ceiMaiMultiParticipanti(participanti);
        System.out.println("Cursurile si scorurile medii in ordine descrescatoare");
        scorMediu(participanti);
        generareRaport(participanti, "Design grafic");

    }


    //metoda citire din fisier
    public static List<Participant> citireParticipanti(String path){
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            List<Participant> participanti = reader.lines()
                    .map(linie->{
                        String[] parts = linie.split(";");
                        return new Participant(
                                parts[0],
                                parts[1],
                                Integer.parseInt(parts[2])
                        );
                    }).collect(Collectors.toList());


            return participanti;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //metoda aflare a cursului cu cei mai multi participanti
    public static void ceiMaiMultiParticipanti(List<Participant> participanti){
        Map<String, Long> cursuriParticipanti =
                participanti.stream()
                        .collect(Collectors.groupingBy(Participant::getTitluCurs, Collectors.counting()));

        cursuriParticipanti.forEach((curs, nrP)->
                System.out.println(curs + " "+  nrP + " participanti"));
        System.out.println("Cursul cu cei mai multi participanti: ");
        cursuriParticipanti.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry-> System.out.println(entry.getKey()));


    }

    //metoda scorul mediu obtinut de participanti la fiecare curs ordonate descrescator dupa scorul mediu
    public static void scorMediu(List<Participant> participanti){
        Map<String, Double> medii = participanti.stream()
                .collect(Collectors.groupingBy(Participant::getTitluCurs,
                        Collectors.averagingDouble(Participant::getScor)));

      //  medii.forEach((curs, scor)->
              //  System.out.println("Cursul: "+ curs+ " scorul mediu "+ scor));

        medii.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach((entry)-> System.out.println(entry.getKey() + " "+ entry.getValue())
                        );

    }

    //raport cu primii 3 participantri cu cele mai bune scoruri la cursul specificat de utilizator
    public static void generareRaport(List<Participant> participanti, String curs){
        try(PrintWriter writer = new PrintWriter("./dataOUT/raport.txt")){
               List<Participant> partic = participanti.stream().filter(p->p.getTitluCurs().equalsIgnoreCase(curs))
                        .sorted(Comparator.comparing(Participant::getScor).reversed())
                        .limit(3)
                        .collect(Collectors.toList());
               writer.println("Pentru cursul "+ curs+ " primii 3 participanti");
               for(Participant p:partic){
                   writer.printf(p.getNume() + " cu scorul: " +p.getScor());
                   writer.println();
               }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}