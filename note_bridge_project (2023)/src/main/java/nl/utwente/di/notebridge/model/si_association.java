package nl.utwente.di.notebridge.model;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class si_association {


    String instrument;
    int expertise;

    public si_association(){
    }

    public si_association(String instrument, int expertise){
        this.instrument = instrument;
        this.expertise = expertise;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getExpertise() {
        return expertise;
    }

    public void setExpertise(int expertise) {
        this.expertise = expertise;
    }
}
