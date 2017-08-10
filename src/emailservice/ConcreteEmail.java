package emailservice;

import java.util.Arrays;
import java.util.Objects;
import java.util.Arrays;
import java.util.Objects;
import java.io.Serializable;

/**
 *
 * @author fabio
 */
public class ConcreteEmail implements Email, Serializable {

    private int id;
    private boolean prefered;
    private boolean letta;
    private String[] destinatario;
    private String mittente, argomento, testo, dataSpedizione;

    public ConcreteEmail() {
    }

    public ConcreteEmail(String mittente, String[] destinatario, String dataSpedizione, String argomento, String testo) {
        this.id = 0;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.argomento = argomento;
        this.testo = testo;
        this.dataSpedizione = dataSpedizione;
        this.letta = false;
        this.prefered = false;

    }

    public ConcreteEmail(int id, String mittente, String[] destinatario, String dataSpedizione, String argomento, String testo, boolean letta, boolean prefered) {
        this.id = id;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.argomento = argomento;
        this.testo = testo;
        this.dataSpedizione = dataSpedizione;
        this.letta = letta;
        this.prefered = prefered;

    }


    public int getID() {
        return id;
    }

    public boolean getPrefered() {
        return prefered;
    }

    public String getMittente() {
        return mittente;
    }

    public String[] getDestinatario() {
        return destinatario;
    }

    public String getArgomento() {
        return argomento;
    }

    public String getTesto() {
        return testo;
    }

    public String getDataSpedizione() {
        return dataSpedizione;
    }

    public boolean isLetta() {
        return letta;
    }

    public void setPrefered(boolean prefered) {
        this.prefered = prefered;
    }

    public void setMittente(String string) {
        this.mittente = string;
    }

    public void setDestinatario(String[] dests) {
        this.destinatario = dests;
    }

    public void setLetta(boolean letta) {
        this.letta = letta;
    }

    @Override
    public String toString() {
        return "<HTML><BR><EM>" + mittente + "<BR>" + argomento + "<BR>" + dataSpedizione;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public boolean equals(String mittente, String testo, String argomento, String data) {
        if (mittente == null || testo == null || argomento == null || data == null) {
            return false;
        }
        if (this.mittente.compareTo(mittente) != 0) {
            return false;
        }
        if (this.argomento.compareTo(argomento) != 0) {
            return false;
        }
        if (this.testo.compareTo(testo) != 0) {
            return false;
        }
        if (this.dataSpedizione.compareTo(data) != 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConcreteEmail other = (ConcreteEmail) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.prefered != other.prefered) {
            return false;
        }
        if (this.letta != other.letta) {
            return false;
        }
        if (!Objects.equals(this.mittente, other.mittente)) {
            return false;
        }
        if (!Objects.equals(this.argomento, other.argomento)) {
            return false;
        }
        if (!Objects.equals(this.testo, other.testo)) {
            return false;
        }
        if (!Objects.equals(this.dataSpedizione, other.dataSpedizione)) {
            return false;
        }
        if (!Arrays.deepEquals(this.destinatario, other.destinatario)) {
            return false;
        }
        return true;
    }

}
