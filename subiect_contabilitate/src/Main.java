import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

final class Nota {
    private final Date data;
    private final String tip;
    private final int cont_debitor;
    private final int cont_creditor;
    private final double suma;

    public Nota(Date data, String tip, int cont_debitor, int cont_creditor, double suma) {
        this.data = data;
        this.tip = tip;
        this.cont_debitor = cont_debitor;
        this.cont_creditor = cont_creditor;
        this.suma = suma;
    }

    public Date getData() {
        return data;
    }

    public String getTip() {
        return tip;
    }

    public int getCont_debitor() {
        return cont_debitor;
    }

    public int getCont_creditor() {
        return cont_creditor;
    }

    public double getSuma() {
        return suma;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("nota{");
        sb.append("data=").append(data);
        sb.append(", tip='").append(tip).append('\'');
        sb.append(", cont_debitor=").append(cont_debitor);
        sb.append(", cont_creditor=").append(cont_creditor);
        sb.append(", suma=").append(suma);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return cont_debitor == nota.cont_debitor && cont_creditor == nota.cont_creditor && Double.compare(suma, nota.suma) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cont_debitor, cont_creditor, suma);
    }


    public boolean compareTo(Nota n) {
        return this.data == n.data;
    }
}


public class Main {
    public static void main(String[] args) {
        List<Nota> noteContabile = citireNote("./dataIN/note_contabile.txt");
        for(Nota n: noteContabile){
            System.out.println(n);
        }

       double suma= noteContabile.stream().mapToDouble(n-> n.getSuma()).sum();
        System.out.println("Suma totala din operatiuni:"+ suma);

        rulajCont(noteContabile);

        raportCont(noteContabile, 5121);
    }

    //metoda citire a notelor contabile intr o lista
    //la final suma totala
    public static List<Nota> citireNote (String path){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            List<Nota> note = reader.lines()
                    .map(linie->{
                        String[] parts = linie.split(",");
                        Date data = null;
                        try {
                            data = formatter.parse(parts[0]);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        return new Nota(
                                data,
                                parts[1],
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                Double.parseDouble(parts[4])

                        );

                    }).collect(Collectors.toList());

            return note;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //metoda colectare rulaje din fiecare cont in Map<Integer, Double>
    //K: simbolul contului, V: rulajul
    //afisam la consola: simbol cont: rulaj
    public static void rulajCont(List<Nota> noteContabile){
        Map<Integer, Double> rulajConturi = noteContabile.stream()
                .collect(Collectors.groupingBy(Nota::getCont_creditor, Collectors.summingDouble(Nota::getSuma)));

        rulajConturi.forEach((simbol, suma)->{
            System.out.println("Pentru contul: " + simbol + " avem rulajul " + suma);
        });
    }

    //4. sa se salveze intr un fisier csv fisa unui cont specificat de user
    public static void raportCont(List<Nota> noteContabile, Integer cont){
        try(PrintWriter writer = new PrintWriter("./dataOUT/raport.csv")){
            writer.println("Pentru contul: " + cont);
            noteContabile.stream()
                    .filter(n->n.getCont_creditor()==cont)
                    .forEach(n->{
                        writer.println(n.getData());
                        writer.println(n.getTip());
                        writer.println(n.getCont_debitor());
                        writer.println(n.getSuma());
                    });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}