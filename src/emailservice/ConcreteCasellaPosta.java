/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author fabio
 */
public class ConcreteCasellaPosta implements CasellaPosta, Serializable {
    private String nameAccount;
    private ArrayList<Email> eMailList;

    public ConcreteCasellaPosta(String nameAccount) {
        this.nameAccount = nameAccount;
        eMailList = new ArrayList<>();
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public ArrayList<Email> getMailList() {
        return eMailList;
    }    

}
