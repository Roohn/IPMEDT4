package nl.exocare.ipmedt4;

/**
 * Created by avdbe on 7-6-2017.
 */

public class Vragen {
    private String vraag;
    private String antwoord;

    public Vragen(String vraag, String antwoord) {
        this.vraag = vraag;
        this.antwoord = antwoord;
    }

    @Override
    public String toString() {
        return  "Vraag: "+ '\n' + '\t' + vraag + '\n' + "\n" +
                "Antwoord: " + '\n' + '\t' + antwoord + "\n";
    }
}
