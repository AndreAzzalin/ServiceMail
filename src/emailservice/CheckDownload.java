/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailservice;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabio
 */
public class CheckDownload extends Observable implements Runnable {

    private ArrayList<Email> e;
    private Model m;
    private String usr;

    public CheckDownload(String usr, Model m, ArrayList<Email> e) {
        this.usr = usr;
        this.m = m;
        this.e = e;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume(usr);
            } catch (IOException ex) {
                try {
                     //nel caso il server andasse down prova a riconnettersi notificando al client la situazione
                    Model.getInstance().riconnectToServer();
                } catch (IOException ex1) {
                    Logger.getLogger(CheckDownload.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckDownload.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //cio che fa con l'email prelevata d aoutbox
    void consume(String usr) throws RemoteException, InterruptedException {
        //if (m.downloadMail(usr) != null) {

        ArrayList<Email> updating = m.checkUp(usr);
        /*
            if (updating == null) {
                System.out.println("PER ORA NIENTE DA SCARICARE");
            }
         */
        if (updating != null) {
            for (Email r : updating) {
                //System.out.println("MAIL DA AGGIUNGERE: " + r.getArgomento());
                e.add(r);
                setChanged();
                notifyObservers(r);
                //System.out.println("HO AGGIUNTO IN LOCALE " + r.getTesto());
            }
        }
        //}
    }

}
