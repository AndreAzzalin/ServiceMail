package emailservice;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import utilities.LoadProperty;

class Model extends Observable {

    private static Model singleInstance;
    private Email[] allTheEmails;
    private DefaultListModel allTheEmailsModel;
    private Server server;
    private final String url;

    /* -------------------------------- SINGLETON -------------------------------------------------------- */
    public static Model getInstance() throws IOException {
        if (Model.singleInstance == null) {
            Model.singleInstance = new Model();
        }
        return Model.singleInstance;
    }

    private Model() throws IOException {
        url = LoadProperty.giveProperty("URL_RMI");
    }

    public void connectToServer() throws RemoteException, NotBoundException, MalformedURLException {
        server = (Server) Naming.lookup(url + "server");
    }

    // GESTIONE ECCEZIONE UNA VOLTA LOGGATO USATO PER CHECK DOWNLOAD(DA RIPULIRE
    public boolean riconnectToServer() {
        boolean riconnect = false;
        try {
            connectToServer();
            try {
                Controller.getInstance().getLbl_mittenteNotification().setText("SERVER UP");
                Controller.getInstance().getLbl_previewNotification().setText("Riconnessione avvenuta con successo");
                Controller.getInstance().getNotificationFrame().setVisible(true);
                Controller.getInstance().hideNotification(Controller.getInstance().getNotificationFrame());
            } catch (IOException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
            riconnect = true;

        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            try {
                // si potrebbero disabilitare anche i pulsanti per l'invio/reply/replyAll per evitare eccezioni 
                Controller.getInstance().getLbl_mittenteNotification().setText("SERVER DOWN");
                Controller.getInstance().getLbl_previewNotification().setText("Puoi solo leggere le mail in locale, riconnessione in corso...");
                Controller.getInstance().getNotificationFrame().setVisible(true);
            } catch (IOException ex1) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return riconnect;
    }

    public boolean registraUtente(String mail, String pwd) throws RemoteException {
        return server.callSign(mail, pwd);
    }

    public boolean loginUtente(String mail, String pwd) throws RemoteException, IOException {
        boolean login = false;
        login = server.callAutenticate(mail, pwd);
        // System.out.println("[CLIENT LOGIN: FAIL]");
        //Controller.getInstance().getFeedbackLabel().setText("Server down, riprova piu' tardi...");
        return login;
    }

    public MailWrong mandaMail(Email m) throws RemoteException, IOException {
        //controllo dei destinatari validi, la mail verrà mandata solo a loro
        //occorre notificare il client dell'errore e dei mancati messaggi e a chi
        ArrayList<String> destValidi = new ArrayList<>();
        ArrayList<String> destInesistenti = new ArrayList<>();
        String[] allDest = m.getDestinatario();
        for (int i = 0; i < allDest.length; i++) {
            if (server.checkUserExistence(allDest[i])) {
                destValidi.add(m.getDestinatario()[i]);
                System.out.println("OK:" + allDest[i]);
            } else {
                destInesistenti.add(allDest[i]);
                System.out.println("INSESISTENTI:" + allDest[i]);
            }
        }
        //se è vuoto vuol dire che tutti i destinatari sono esistenti
        if (destInesistenti.isEmpty()) {
            server.callSend(m);
            return null;
        } else {
            return new MailWrong(m, destValidi, destInesistenti);
        }
    }

    public void signUser(String usrMail, String pwd) throws RemoteException {
        server.signUser(usrMail, pwd);
    }

    public void deleteMail(Email mail, String usr, boolean flag) throws IOException {
        server.deleteMail(mail, usr, flag);
    }

    public Email displayMail(int index) throws IOException {
        Email mail = allTheEmails[index];
        Controller.getInstance().setOnPanel(mail);
        //mail.setLetta(true);
        /*
        for (Email m: Controller.getInstance().getPersonalMailBox()) {
            if (m.get)m.
        }
         */

        return mail;
    }

    public ArrayList<Email> downloadMail(String usr) throws RemoteException {
        return server.callDownloadMail(usr);
    }

    public ArrayList<Email> checkUp(String usr) throws RemoteException {
        ArrayList<Email> m = server.checkToDownload(usr);
        if (m != null) {
            for (Email e : m) {
                if (e.getMittente().equals(usr)) {
                    System.out.println("\t[CLIENT-ORA PUOI LEGGERE LA MAIL IN INVIATE]");
                } else {
                    System.out.println("[CLIENT-HAI UNA NUOVA MAIL]");
                }
            }
        }
        return m;
    }

    public DefaultListModel getAllTheEmailsModel(ArrayList<Email> emails, boolean flag) {

        allTheEmails = emails.toArray(new ConcreteEmail[emails.size()]);
        allTheEmailsModel = new DefaultListModel();

        //for (Email mail : allTheEmails) {
        for (int i = 0; i < allTheEmails.length; i++) {
            if (flag) { //ricevute
                if (allTheEmails[i].isLetta()) {
                    String element = "<html><h3>" + allTheEmails[i].getMittente() + "<BR>" + allTheEmails[i].getArgomento() + "<BR>" + allTheEmails[i].getDataSpedizione() + "</h3></html>";
                    allTheEmailsModel.addElement(element);
                } else {
                    String element = "<html>" + allTheEmails[i].getMittente() + "<BR>" + allTheEmails[i].getArgomento() + "<BR>" + allTheEmails[i].getDataSpedizione() + " </html>";
                    allTheEmailsModel.addElement(element);
                }
            } else {
                String element = "<html><h3>" + allTheEmails[i].getMittente() + "<BR>" + allTheEmails[i].getArgomento() + "<BR>" + allTheEmails[i].getDataSpedizione() + "</h3></html>";
                allTheEmailsModel.addElement(element);
            }
        }

        return allTheEmailsModel;
    }

    public void callReply(Email mail) throws RemoteException {
        server.callReply(mail);
    }

    /*usare callSend poiche la nuova mail verrà creata nel client(newMittente = usrClient, Destinatari = li deve specificare da GUI)    */
    public void callForeward(Email mail, String usr) throws RemoteException {
        server.callForeward(mail, usr);
    }

    public String getUserImg(String usr) throws RemoteException {
        return server.getUserImg(usr);
    }

    public void setLetta(String user, Email mail) throws RemoteException {
        server.setLetta(user, mail);
    }

}
