/*

NON USATO
*/


package emailservice;

import emailservice.Email;
import emailservice.Server;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabio
 */
public class Producer extends Thread {

    private Email m;
    private Server server;

    public Producer(Email m, Server s) {
        this.m = m;
        server = s;
    }

    @Override
    public void run() {
        try {
            server.getOutbox().put(m);
        } catch (RemoteException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Email getM() {
        return m;
    }

    
}
