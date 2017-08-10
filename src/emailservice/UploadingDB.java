/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailservice;

import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesare Iurlaro
 */
public class UploadingDB extends TimerTask {

    private Server server;

    public UploadingDB(Server s) {
    this.server = s;
    }

    public void run() {
        try {
            System.out.println("UPLOADING....");
            server.callUploadDB();
        } catch (RemoteException ex) {
            Logger.getLogger(UploadingDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
