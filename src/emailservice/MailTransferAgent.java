/*

PER ORA è SYNC SOLO IL SERVER, CLIENT DA VALUTARE SE FARLO

 */
package emailservice;

import static emailservice.XML_Manager.path;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.LoadProperty;

/**
 *
 * @author fabio
 */
public class MailTransferAgent extends UnicastRemoteObject implements Server {

    /*------------------------------------------- VARIABILI DI ISTANZA e COSTRUTTORE-------------------------------------------------*/
    private DB_Manager db;
    // keeps reference of standard output stream
    private static PrintStream standardOut = System.out;
    private static PrintStream standardErr = System.err;


    /*
        outbox: coda sync per contenere le mail che il server deve inviare
        commonMailBox: HashMap contenente tutte le mail; key: nameAccount, value: ArrayList<Email>
        commonToDownloadBox: mantiene tutte le mail di Key da scaricarsi ancora in locale. Il client svuota la propria Key dopo essersi aggiornato
     */
    private BlockingQueue<Email> outbox = new ArrayBlockingQueue<>(100);
    //da decidere se sincronizzare hashSet 
    private HashMap<String, ArrayList<Email>> commonMailBox = new HashMap<>();

    private HashMap<String, ArrayList<Email>> commonToDownloadBox = new HashMap<>();

    private ArrayList<Email> commonToUploadBox = new ArrayList<>();

    public MailTransferAgent() throws RemoteException, IOException {
        db = new DB_Manager();
    }


    /*------------------------------------------------- METODI -------------------------------------------------*/
    public boolean checkUserExistence(String usr) {
        return db.mailExist(usr);
    }

    public boolean callSign(String usrMail, String pwd) {
        //registro l'utente anche nella sd..
        if (!getCommonMailBox().containsKey(usrMail)) {
            getCommonMailBox().put(usrMail, new ArrayList<Email>());

        }
        return db.signUser(usrMail, pwd);
    }

    public void callSend(Email mail) {
        try {
            //db.addEmail(mail);
            outbox.put(mail);
        } catch (InterruptedException ex) {
            Logger.getLogger(MailTransferAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Smista le mail presenti i outbox, aggiornando commonMailBox per tenere traccia delle caselle di posta aggiornate.
     */
    public void dispatchMail() {
        Consumer cons = new Consumer(this);
        cons.start();
    }

    public boolean callAutenticate(String usrMail, String pwd) {
        return db.autenticateUser(usrMail, pwd);
    }

    //TEST SUCCESS
    public ArrayList<Email> getMailFromDB(String user) {
        //return db.getAllMail(user);
        return db.getAllMail(user);
    }

    //sync perche si accede all'unico obj comune commonMailBox
    public void loadInitHash() {
        System.out.println("|----- + " + new Date() + " + -- + loadInitHash + -- + Synchronization with data base + -- + NONE + -- + NONE + -- +");
        ArrayList<Email> tmpMails = new ArrayList<>();
        ArrayList<String> users = db.getAllUser();
        for (String user : users) {
            //System.out.println(user);
            tmpMails = getMailFromDB(user);
            synchronized (this) {
                if (!getCommonMailBox().containsKey(user)) {
                    getCommonMailBox().put(user, tmpMails);
                }
            }
        }
    }

    /*
    Il client avrà un thread che cercherà se esiste in questa Map. Se c'è vuol dire che deve scaricarsi la posta qui presente, else no.
    Se si trova, dopo scaricarla, deve cancellarsi dalla Map in questione.
     */
    //sync perche si accede all'unico obj comune commonToDownloadBox
    public ArrayList<Email> checkToDownload(String usr) {
        ArrayList<Email> to = new ArrayList<>();
        synchronized (this) {
            if (getCommonToDownloadBox().containsKey(usr)) {
                to = getCommonToDownloadBox().get(usr);
                //System.out.println("RIMOZIONE IN CORSO DI "+usr);
                getCommonToDownloadBox().remove(usr);
            }
        }
        return to;
    }

    //sync perche si accede all'unico obj comune commonMailBox
    public synchronized ArrayList<Email> callDownloadMail(String usr) {
        //getCommonToDownloadBox().remove(usr);
        //System.out.println("USER "+usr+" sta scaricando la posta al suo start");
        //System.out.println("è presente in toDownload pure ?" + getCommonToDownloadBox().containsKey(usr));
        if (getCommonToDownloadBox().containsKey(usr)) {
            //System.out.println("è presente quindi le prende gia da commonMailBox");
            getCommonToDownloadBox().remove(usr);
        }
        return getCommonMailBox().get(usr);
    }

    //----------------- -------METODO CHE RITORNA L' IP---------------------------
    public static InetAddress getIpServer(String protocolVersion) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            // verifico se l'interfaccia di rete è up
            if (netint.isUp() && !netint.isLoopback() && !netint.isVirtual()) {
                //carico gli IP assegnati al'interfaccia di rete 
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (protocolVersion.equals("IPv4")) {
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress;
                        }
                    } else {
                        if (inetAddress instanceof Inet6Address) {
                            return inetAddress;
                        }
                    }
                }
            }
        }
        return null;
    }
    //--------------------------------------------------------------------------

    public static void main(String[] args) throws InterruptedException, RemoteException, IOException {
        Server server = new MailTransferAgent();
        LogFrame log = new LogFrame(server);
        PrintStream printStream = new PrintStream(new CustomOutputStream(log.getArea()));

        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
        System.out.println("|----- + " + new Date() + " + -- + Server Main + -- + Main process starts + -- + NONE + -- + NONE + -- +");
        lanciaRMIRegistry();
        //--------------------------AZZA-----------------------------------------
        try {

            Naming.rebind("/" + getIpServer("IPv4") + "/server", server);
            //--------------------------------------------------------------------------

            //System.out.println("Server bound in registry");
            server.loadInitHash();

            System.out.println("[SERVER START: " + getIpServer("IPv4") + "]");

            System.out.println("[STAMPA DEL CONTENUTO ACTIVE LOCALE DEL DB]");
            for (Map.Entry<String, ArrayList<Email>> element : server.getCommonMailBox().entrySet()) {
                System.out.println("\t[" + element.getKey() + "]----------STAMPA DI TUTTE LE MAIL PERSONALI");
                for (Email mail : element.getValue()) {
                    String allDest = "";
                    String[] rec = mail.getDestinatario();
                    for (int i = 0; i < rec.length; i++) {
                        allDest += "-" + rec[i];
                    }
                    System.out.println("\t\tDestinatario: " + allDest + ", Titolo: " + mail.getArgomento());
                }
            }

            System.out.println("\n\n");

            server.dispatchMail();
            System.out.println("|----- + " + new Date() + " + -- + DispatchMail + -- + starts run + -- + NONE + -- + NONE + -- +");
            //Thread.sleep(40000);
            /*
            System.out.println("[DOPO DISPATCHING][STAMPA DEL CONTENUTO LOCALE DEL DB]");
            for (Map.Entry<String, ArrayList<Email>> element : server.getCommonMailBox().entrySet()) {
                System.out.println("\t[" + element.getKey() + "]----------STAMPA DI TUTTE LE MAIL PERSONALI");
                for (Email mail : element.getValue()) {
                    String allDest = "";
                    String[] rec = mail.getDestinatario();
                    for (int i = 0; i < rec.length; i++) {
                        allDest += "-" + rec[i];
                    }
                    System.out.println("\t\tDestinatario: " + allDest + ", Titolo: " + mail.getArgomento());
                }
            }
            System.out.println("\n\n");
             */
 /*
            System.out.println("[DOPO DISPATCHING][STAMPA DEL CONTENUTO LOCALE DELLA DOWNLOADING MAP]");
            for (Map.Entry<String, ArrayList<Email>> element : server.getCommonToDownloadBox().entrySet()) {
                System.out.println("\t[" + element.getKey() + "]----------STAMPA DI TUTTE LE MAIL PERSONALI DA SCARICARE");
                for (Email mail : element.getValue()) {
                    String allDest = "";
                    String[] rec = mail.getDestinatario();
                    for (int i = 0; i < rec.length; i++) {
                        allDest += "-" + rec[i];
                    }
                    System.out.println("\t\tDestinatario: " + allDest + ", Titolo: " + mail.getArgomento());
                }
            }
            System.out.println("\n\n");
             */

            Timer timer = new Timer();

            timer.schedule(new UploadingDB(server), 0, Integer.parseInt(LoadProperty.giveProperty("dbUploadingTime")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callUploadDB() {
        System.out.println("[CALL UPLOAD DEL DB DA TOUPLOAD SD]");

        for (Email em : getCommonToUploadBox()) {
            db.uploadDB(em);
        }
        getCommonToUploadBox().clear();
        //delete xml
        File file = new File(path);
        file.delete();
    }

    public synchronized void deleteMail(Email mail, String usr, boolean flag) throws RemoteException {
        int index = 0;

        for (int i = 0; i < getCommonMailBox().get(usr).size(); i++) {
            if (getCommonMailBox().get(usr).get(i).equals(mail)) {
                index = i;
            }
        }
        getCommonMailBox().get(usr).remove(index);

        if (getCommonToUploadBox().size() != 0) {
            for (int i = 0; i < getCommonToUploadBox().size(); i++) {
                if (getCommonToUploadBox().get(i).equals(mail)) {
                    index = i;
                }
            }
            Email tmp = getCommonToUploadBox().get(index);
            System.out.println("TMP TO DEL" + tmp.getArgomento());
            //flagghiamo con uno spazio concatenato l'utente che vuole leiminare la mail
            //se è il mittente
            if (tmp.getMittente().equals(usr)) {
                tmp.setMittente(tmp.getMittente() + " ");
                System.out.println("MITT VUOLO DEL" + tmp.getArgomento());
            } else {
                //se è un destinatario
                String[] dests = tmp.getDestinatario();
                for (int i = 0; i < dests.length; i++) {
                    System.out.println("DEST VUOLE DEL" + tmp.getArgomento());
                    if (dests[i].equals(usr)) {
                        dests[i] = dests[i] + " ";
                    }
                }
                tmp.setDestinatario(dests);
            }
        }
        db.deleteEmail(mail, flag);
    }

    public void callReply(Email mail) {
        try {
            outbox.put(mail);
        } catch (InterruptedException ex) {
            Logger.getLogger(MailTransferAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*usare callSend poiche la nuova mail verrà creata nel client(newMittente = usrClient, Destinatari = li deve specificare da GUI)    */
    public void callForeward(Email mail, String usr) {
        try {
            //return db.addEmail(mail);
            outbox.put(mail);
        } catch (InterruptedException ex) {
            Logger.getLogger(MailTransferAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void lanciaRMIRegistry() throws IOException {
        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(Integer.parseInt(LoadProperty.giveProperty("portaRMI")));
            System.out.println("|----- + " + new Date() + " + -- + lanciaRMIRegistry + -- + java RMI registry created + -- + + NONE + -- + NONE + -- +");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("|----- + " + new Date() + " + -- + lanciaRMIRegistry + -- + java RMI registry already exists + -- + + NONE + -- + NONE + -- +");
        }

    }

    public String getUserImg(String usr) throws RemoteException {
        return db.getUsrImg(usr);

        //return "user";
    }

    @Override
    public BlockingQueue<Email> getOutbox() {
        return outbox;
    }

    public ArrayList<Email> getCommonToUploadBox() {
        return commonToUploadBox;
    }

    public HashMap<String, ArrayList<Email>> getCommonMailBox() {
        return commonMailBox;
    }

    public HashMap<String, ArrayList<Email>> getCommonToDownloadBox() {
        return commonToDownloadBox;
    }

    public void setLetta(String user, Email mail) throws RemoteException {
        int index = -1;

        System.out.println("Cerco la mail " + mail.getID() + " di " + user + " in commonmailbox");
        for (int i = 0; i < getCommonMailBox().get(user).size(); i++) {
            if (getCommonMailBox().get(user).get(i).equals(mail)) {
                System.out.println("L'ho trovata!");
                index = i;
            }
        }
        if (index != -1) {
            getCommonMailBox().get(user).get(index).setLetta(true);
        }

        if (getCommonToUploadBox().size() != 0) {
            System.out.println("\nCerco la mail " + mail.getID() + " di " + user + " in commontouploadbox");
            for (int i = 0; i < getCommonToUploadBox().size(); i++) {
                if (getCommonToUploadBox().get(i).equals(mail)) {
                    System.out.println("L'ho trovata!");
                    index = i;
                }
            }
            Email tmp = getCommonToUploadBox().get(index);
            System.out.println("TROVATA " + tmp.getArgomento());
            if (!tmp.getMittente().equals(user)) {                
                tmp.setLetta(true);
                System.out.println("SET LETTA MAIL " + tmp.getArgomento());
            }
        }
        //MAIL PRENDE L'ID DA COMMONTODOWNLOADBOX, QUANDO ANCORA E' 0, E CERCA NEL DB CHI HA QUELL'ID (OVVIAMENTE NESSUNO).
        //SOLUZIONE? FACCIO RESTITUIRE A ADDMAIL L'ID DELLA MAIL APPENA AGGIUNTA, E AGGIORNO IL RIFERIMENTO IN COMMONTODOWNLOADBOX E QUINDI POI ANCHE DI PERSONALMAILBOX
        db.setLetta(mail, user);
    }
    
    public void signUser(String usrMail, String pwd) throws RemoteException {
        db.signUser(usrMail, pwd);
    }
}
