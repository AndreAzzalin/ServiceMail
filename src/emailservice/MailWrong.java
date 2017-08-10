/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailservice;

import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class MailWrong {

    private final Email m;
    private final ArrayList<String> destinatariValidi;
    private final ArrayList<String> destinatariNonValidi;

    public MailWrong(Email m, ArrayList<String> dv, ArrayList<String> dnv) {
        this.m = m;
        this.destinatariValidi = dv;
        this.destinatariNonValidi = dnv;
    }

    public Email getM() {
        return m;
    }

    public ArrayList<String> getDestinatariValidi() {
        return destinatariValidi;
    }

    public ArrayList<String> getDestinatariNonValidi() {
        return destinatariNonValidi;
    }

    public String printNonValidi() {
        String res = "";
        for (String s : destinatariNonValidi) {
            res += s + ",";
        }
        StringBuilder sb = new StringBuilder(res);
        if (res.contains(",")) {
            sb.deleteCharAt(res.lastIndexOf(","));
        }
        return sb.toString();
    }

    public String printValidi() {
        String res = "";
        for (String s : destinatariValidi) {
            res += s + ",";
        }
        StringBuilder sb = new StringBuilder(res);
        if (res.contains(",")) {
            sb.deleteCharAt(res.lastIndexOf(","));
        }
        return sb.toString();
    }
}
