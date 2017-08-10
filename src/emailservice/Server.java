package emailservice;

import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author fabio
 */
public interface Server extends Remote {

    public boolean callSign(String usrMail, String mail) throws RemoteException;

    public boolean callAutenticate(String usrMail, String pwd) throws RemoteException;

    public void callSend(Email mail) throws RemoteException;

    public BlockingQueue<Email> getOutbox() throws RemoteException, InterruptedException;

    public void dispatchMail() throws RemoteException, InterruptedException;

    public HashMap<String, ArrayList<Email>> getCommonMailBox() throws RemoteException, InterruptedException;

    public ArrayList<Email> getMailFromDB(String user) throws RemoteException;

    public void loadInitHash() throws RemoteException;

    public ArrayList<Email> callDownloadMail(String usr) throws RemoteException;

    public HashMap<String, ArrayList<Email>> getCommonToDownloadBox() throws RemoteException;

    public ArrayList<Email> checkToDownload(String usr) throws RemoteException;

    public boolean checkUserExistence(String usr) throws RemoteException;

    /*
    public CasellaPosta createCasella(ClientName c) throws RemoteException, InterruptedException;
    //non lo metto perche lo definisco tramite ereditarietà, non serve che sia visibile al client
    public CasellaPosta giveAllMail(ClientName c) throws RemoteException, InterruptedException;
    public void dispatchMail() throws RemoteException, InterruptedException;
    public void sendMail(Email e) throws RemoteException, InterruptedException;
    public void deleteMail(String userRichiedente, Email m) throws RemoteException, InterruptedException;
    
    public HashMap<String, CasellaPosta> getCommonMailBox() throws RemoteException, InterruptedException;
    
     */
    public void deleteMail(Email mail, String usr, boolean flag) throws RemoteException;

    public ArrayList<Email> getCommonToUploadBox() throws RemoteException;

    public void callUploadDB() throws RemoteException;

    public void callReply(Email mail) throws RemoteException;

    public void callForeward(Email mail, String usr) throws RemoteException;

    public String getUserImg(String usr) throws RemoteException;

    public void setLetta(String user, Email mail) throws RemoteException;

    public void signUser(String usrMail, String pwd) throws RemoteException;
}
