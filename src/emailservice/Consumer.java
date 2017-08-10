package emailservice;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabio
 */
public class Consumer extends Thread {

    private Server server;
    private XML_Manager xmlW;

    public Consumer(Server s) {
        this.server = s;
        this.xmlW = new XML_Manager("C:/Users/Cesare Iurlaro/Desktop/progettoProg3Last/document.xml");
    }

    public void run() {
        while (true) {
            try {
                consume((Email) server.getOutbox().take());
            } catch (Exception ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //cio che fa con l'email prelevata da outbox
    void consume(Email m) throws RemoteException, InterruptedException, IOException {
        System.out.println("\t[SERVER DISPATCH. Mittente: " + m.getMittente() + ", arg: " + m.getArgomento() + "]");
        System.out.println("\t[SERVER UPDATE COMMONMAILBOX & COMMONTODOWNLOADBOX & COMMONTOUPLOADBOX]");
        server.getCommonMailBox().get(m.getMittente()).add(m);
        server.getCommonToUploadBox().add(m);
        System.out.println("\t\t[COMMONTOUPLOADBOX: ADD MAIL]");
        System.out.println("PRINT DEL UPLOAD BOX DOPO DISPATCH");
        for (Email em : server.getCommonToUploadBox()) {
            System.out.println("MAIL:" + em.getMittente());
        }
        System.out.println("\t\t[COMMONMAILBOX: ADD MAIL]");
        if (!server.getCommonToDownloadBox().containsKey(m.getMittente())) {
            server.getCommonToDownloadBox().put(m.getMittente(), new ArrayList<Email>());
        }
        server.getCommonToDownloadBox().get(m.getMittente()).add(m);
        System.out.println("\t\t[COMMONTODOWNLOADBOX: ADD MAIL]");
        for (String d : m.getDestinatario()) {
            server.getCommonMailBox().get(d).add(m);
            //QUI VA MESSA LA SCRITTURA DELLA ENTRY NEL FILE LOCALE XML CON IL SEGUENTE FORMATO EMAIL..
            //RISPETTARE IL FORMATO ATTESO DAL METODO addMail(Email m) del DBMANAGER
            if (!server.getCommonToDownloadBox().containsKey(d)) {
                server.getCommonToDownloadBox().put(d, new ArrayList<Email>());
            }
            server.getCommonToDownloadBox().get(d).add(m);
        }
        System.out.println("STAMPO IN XML");
        xmlW.storeMail(m);
        xmlW.endStore();
    }
    //QUI VA MESSA LA SCRITTURA DELLA ENTRY NEL FILE LOCALE XML CON IL SEGUENTE FORMATO EMAIL..
    //RISPETTARE IL FORMATO ATTESO DAL METODO addMail(Email m) del DBMANAGER
}
