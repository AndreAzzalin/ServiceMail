package emailservice;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import utilities.LoadProperty;

class Controller implements Observer {

    private Model model;
    private static Controller singleInstance;

    /*DATI DELLA CASELLA DI POSTA*/
    private String user;
    private ArrayList<Email> personalMailBox, inviate, ricevute;
    private int inboxCount;
    private boolean flag; //true = ricevuti, false = inviati
    private Email onPanel;

    private JLabel userImg, userTag, lbl_menuSend, lbl_menuInbox, lbl_menuDrafts, lbl_menuSent, feedbackLabel, lbl_mittenteNotification, lbl_closeNotification, lbl_previewNotification, cestino, lbl_reduce;
    private JScrollPane midScroll;
    private JButton button_send, button_sendReply, button_sendReplyAll, loginButton, signButton, button_forward;
    private JList mailList;
    private JPanel writePanel, rightPanel, emptyPanel, readPanel, emailSent_OK, emailSent_ERROR, receivedNotification;
    private JDialog autenticationPanel;
    private JTextField autenticationTextField;
    private JFrame notificationFrame;

    /* -------------------------------- SINGLETON -------------------------------------------------------- */
    public static Controller getInstance() throws IOException {
        if (Controller.singleInstance == null) {
            Controller.singleInstance = new Controller();
        }
        return Controller.singleInstance;
    }

    private Controller() throws IOException {
        model = Model.getInstance();

        /* ---------- COMPONENTI ---------- */
        mailList = new JList<>();

        midScroll = new JScrollPane();
        emptyPanel = new JPanel();
        readPanel = new JPanel();
        writePanel = new JPanel();
        rightPanel = new JPanel();
        emailSent_OK = new JPanel();
        emailSent_ERROR = new JPanel();

        autenticationPanel = new JDialog();

        autenticationTextField = new JTextField();

        lbl_menuDrafts = new JLabel();
        lbl_menuSend = new JLabel();
        lbl_menuInbox = new JLabel();
        lbl_menuSent = new JLabel();
        cestino = new JLabel();
        userImg = new JLabel();
        userTag = new JLabel();
        lbl_reduce = new JLabel();

        //userImgPath = LoadProperty.giveProperty("pathImg");
        button_send = new JButton();
        button_sendReply = new JButton();
        button_sendReplyAll = new JButton();
        button_forward = new JButton();
        loginButton = new JButton();
        signButton = new JButton();

        feedbackLabel = new JLabel();

        notificationFrame = new JFrame("Notification");
        receivedNotification = new JPanel();

        lbl_mittenteNotification = new JLabel();
        lbl_previewNotification = new JLabel();

        /* -------------------------------- GESTIONE EVENTI ---------------------------------------------------- */
        setEventDrive();

        /* -------------------------------- PROPRIETÀ GRAFICHE ---------------------------------------------------- */
        setProperties();
    }

    private void setEventDrive() {

        mailList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                rightPanel.removeAll();
                try {
                    int index = mailList.getSelectedIndex();
                    if (index != -1) {
                        Email mail = model.displayMail(index);
                        String s = "";
                        for (String m : mail.getDestinatario()) {
                            if (m != Controller.getInstance().getUser()) {
                                s += m;
                            }
                        }
                        RightPanelView.getInstance().getTextArea_readContent().setText(mail.getTesto());
                        RightPanelView.getInstance().getLbl_readTitle().setText(mail.getMittente() + " + " + s);
                        RightPanelView.getInstance().getLbl_readObject().setText(mail.getArgomento());
                        rightPanel.add(readPanel);

                        if (!flag) {
                            button_sendReply.setVisible(false);
                            button_sendReplyAll.setVisible(false);
                            button_forward.setVisible(false);
                        } else { //ricevute
                            button_sendReply.setVisible(true);
                            button_sendReplyAll.setVisible(true);
                            button_forward.setVisible(true);
                            model.setLetta(user, mail);
                            mail.setLetta(true);

                            if (!ricevute.isEmpty()) {
                                mailList.setModel(model.getAllTheEmailsModel(ricevute, flag));
                            } else {
                                mailList.setModel(new DefaultListModel());
                            }
                        }

                        rightPanel.repaint();
                        rightPanel.revalidate();
                        rightPanel.setVisible(true);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        cestino.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {

                try {
                    rightPanel.removeAll();
                    int index = mailList.getSelectedIndex();
                    if (index != -1) {
                        if (flag) {
                            Email tmpMail = ricevute.get(index);
                            removeFromPersonalMailBox(tmpMail);
                            Model.getInstance().deleteMail(tmpMail, user, flag);
                            removeFromRicevute(tmpMail);
                            if (!ricevute.isEmpty()) {
                                mailList.setModel(model.getAllTheEmailsModel(ricevute, flag));
                            } else {
                                mailList.setModel(new DefaultListModel());
                            }
                        } else {
                            Email tmpMail = inviate.get(index);
                            removeFromPersonalMailBox(tmpMail);
                            Model.getInstance().deleteMail(tmpMail, user, flag);
                            removeFromInviate(tmpMail);
                            if (!inviate.isEmpty()) {
                                mailList.setModel(model.getAllTheEmailsModel(inviate, flag));
                            } else {
                                mailList.setModel(new DefaultListModel());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        );

        button_send.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                try {
                    String argomento = RightPanelView.getInstance().getTextField_sendObject().getText();
                    String destinatari = RightPanelView.getInstance().getTextField_sendTo().getText();
                    String testo = RightPanelView.getInstance().getTextArea_sendContent().getText();
                    String[] dest = destinatari.split(",");
                    Email m = new ConcreteEmail(Controller.getInstance().getUser(), dest, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), argomento, testo);
                    MailWrong mw = model.mandaMail(m);
                    //ci sono destinatari non validi
                    if (mw != null) {
                        rightPanel.removeAll();
                        rightPanel.add(writePanel);
                        RightPanelView.getInstance().getTextField_sendObject().setText(mw.getM().getArgomento());
                        RightPanelView.getInstance().getTextField_sendTo().setText(mw.printValidi());
                        RightPanelView.getInstance().getTextArea_sendContent().setText(mw.getM().getTesto());
                        rightPanel.repaint();
                        rightPanel.revalidate();
                        rightPanel.setVisible(true);
                        autenticationPanel.setVisible(false);
                        lbl_mittenteNotification.setText("Destinatari non esistenti: " + mw.printNonValidi());
                        lbl_previewNotification.setText("Rieseguire l'operazione di invio inserendo gli indirizzi corretti.");
                        notificationFrame.setVisible(true);
                        hideNotification(notificationFrame);
                    } else {
                        rightPanel.removeAll();
                        rightPanel.add(emailSent_OK);
                        rightPanel.repaint();
                        rightPanel.revalidate();
                        lbl_mittenteNotification.setText("Messaggio inviato correttamente");
                        lbl_previewNotification.setText("");
                        notificationFrame.setVisible(true);
                        hideNotification(notificationFrame);
                        rightPanel.setVisible(false);
                    }

                    //System.out.println("MAIL RESTITUITA INFO:" +mw.getM().getDataSpedizione()+", dest non validi: "+ mw.printNonValidi() + ", validi: " + mw.printValidi());
                    System.out.println("[CLIENT ACTION: INVIO MAIL]");
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        );

        button_sendReply.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                try {
                    String[] dests = new String[1];
                    dests[0] = onPanel.getMittente();

                    //imposta nei nuovi destinatari solo il mittente della mail ricevuta
                    //ArrayList<String> newDests = new ArrayList<>();
                    //newDests.add(onPanel.getMittente());
                    //dests = (String[]) newDests.toArray(new String[newDests.size()]);
                    //il mittente sarà l'utente che clicca sul btn
                    onPanel.setMittente(user);
                    rightPanel.removeAll();
                    rightPanel.add(writePanel);
                    RightPanelView.getInstance().getTextField_sendObject().setText("RE: "); //m.getMittente());
                    RightPanelView.getInstance().getTextField_sendTo().setText(dests[0]); //m.getDestinatario()[0]);                    
                    RightPanelView.getInstance().getTextArea_sendContent().setText(""); //m.getDestinatario()[0]);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                    rightPanel.setVisible(true);

                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        button_sendReplyAll.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                try {/*
                    String dest = "";
                    for (String s : onPanel.getDestinatario()) {
                        dest += s + ",";
                    }
                    dest = dest.substring(0, dest.length() - 1);*/
                    String[] dests = onPanel.getDestinatario();
                    //imposta nei nuovi destinatari solo il mittente della mail ricevuta
                    ArrayList<String> newDests = new ArrayList<>();
                    for (int i = 0; i < dests.length; i++) {
                        if (!dests[i].equals(user)) {
                            newDests.add(dests[i]);
                        }
                    }
                    newDests.add(onPanel.getMittente());
                    dests = (String[]) newDests.toArray(new String[newDests.size()]);

                    //il mittente sarà l'utente che clicca sul btn
                    onPanel.setMittente(user);
                    String dest = "";
                    for (String s : dests) {
                        dest += s + ",";
                    }
                    dest = dest.substring(0, dest.length() - 1);
                    rightPanel.removeAll();
                    rightPanel.add(writePanel);
                    RightPanelView.getInstance().getTextField_sendObject().setText("RE: "); //m.getMittente());
                    RightPanelView.getInstance().getTextField_sendTo().setText(dest); //m.getDestinatario()[0]);                    
                    RightPanelView.getInstance().getTextArea_sendContent().setText(""); //m.getDestinatario()[0]);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                    rightPanel.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        button_forward.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                try {
                    String s = " ---------------------" + onPanel.getMittente() + " HA INVIATO: -------------------------------\n\n"
                            + onPanel.getTesto() + "\n\n ------------------------------------------------------------------------------\n\n";
                    //imposta nei nuovi destinatari solo il mittente della mail ricevuta

                    //il mittente sarà l'utente che clicca sul btn
                    onPanel.setMittente(user);
                    rightPanel.removeAll();
                    rightPanel.add(writePanel);
                    RightPanelView.getInstance().getTextField_sendObject().setText("FW: "); //m.getMittente());
                    RightPanelView.getInstance().getTextArea_sendContent().setText(s); //m.getDestinatario()[0]);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                    rightPanel.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        lbl_menuDrafts.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                setComponentColor(lbl_menuDrafts);
                resetComponentColor(lbl_menuSent);
                resetComponentColor(lbl_menuSend);
                resetComponentColor(lbl_menuInbox);
                rightPanel.setVisible(false);
            }
        }
        );

        lbl_menuSend.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                setComponentColor(lbl_menuSend);
                resetComponentColor(lbl_menuSent);
                resetComponentColor(lbl_menuDrafts);
                resetComponentColor(lbl_menuInbox);
                rightPanel.removeAll();
                rightPanel.add(writePanel);
                rightPanel.repaint();
                rightPanel.revalidate();
                rightPanel.setVisible(true);
            }
        }
        );

        lbl_menuInbox.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                flag = true;
                setComponentColor(lbl_menuInbox);
                resetComponentColor(lbl_menuSent);
                resetComponentColor(lbl_menuDrafts);
                resetComponentColor(lbl_menuSend);
                rightPanel.setVisible(false);
                //per ora faccio che se clicchi vedi su console tutte le mail salvate in locale
                System.out.println("[CLIENT-HAI CHIESTO DI VEDERE TUTTE LE TUE MAIL]");
                int c = 1;
                for (Email mail : personalMailBox) {
                    String dests = Arrays.toString(mail.getDestinatario());
                    System.out.println("\tMAIL " + c + " :" + mail.getID() + ", " + mail.getMittente() + ", " + dests + ", " + mail.getArgomento());
                }

                ricevute = new ArrayList<>();
                for (Email mail : personalMailBox) {
                    for (int i = 0; i < mail.getDestinatario().length; i++) {
                        //System.out.println(mail.getID() + " getInCasellaDestinatario " + mail.getDestinatario()[i] + ": " + mail.getInCasellaDestinatario()[i]);
                        if (((mail.getDestinatario()[i]).compareTo(user) == 0)) {
                            ricevute.add(mail);
                        }
                    }
                }
                if (!ricevute.isEmpty()) {
                    mailList.setModel(model.getAllTheEmailsModel(ricevute, flag));
                } else {
                    mailList.setModel(new DefaultListModel());
                }
                lbl_menuInbox.setText("Inbox");
                inboxCount = 0;
            }
        }
        );

        lbl_menuSent.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                flag = false;
                setComponentColor(lbl_menuSent);
                resetComponentColor(lbl_menuInbox);
                resetComponentColor(lbl_menuDrafts);
                resetComponentColor(lbl_menuSend);
                rightPanel.setVisible(false);

                inviate = new ArrayList<>();

                for (Email mail : personalMailBox) {
                    System.out.println(mail.getMittente());
                    if (mail.getMittente() != null && mail.getMittente().equals(user)) {
                        inviate.add(mail);
                    }
                }
                if (!inviate.isEmpty()) {
                    mailList.setModel(model.getAllTheEmailsModel(inviate, flag));
                } else {
                    mailList.setModel(new DefaultListModel());
                }
            }
        }
        );

        loginButton.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                feedbackLabel.setForeground(new Color(253, 255, 138));
                try {
                    Model.getInstance().connectToServer();
                    if (!autenticationTextField.getText().equals("")) {
                        user = autenticationTextField.getText();
                        String[] data = user.split("-"); //data[0] = mail, data[1] = pwd ..scriverle separate da -
                        try {

                            if (model.loginUtente(data[0], data[1])) {
                                System.out.println("[CLIENT LOGIN AS " + getUser() + " : SUCCESS]");
                                autenticationPanel.setVisible(false);
                                lbl_mittenteNotification.setText("Autenticazione");
                                lbl_previewNotification.setText("Effettuata con successo, " + user);
                                userImg.setPreferredSize(new Dimension(70, 70));
                                userImg.setMaximumSize(new Dimension(70, 70));
                                userImg.setMinimumSize(new Dimension(70, 70));
                                userImg.setSize(new Dimension(70, 70));
                                userImg.setIcon(new ImageIcon(getClass().getResource("/immagini/" + model.getUserImg(user) + ".png")));

                                userTag.setText(user.split("-")[0]);
                                notificationFrame.setVisible(true);
                                SwingUtilities.getWindowAncestor(rightPanel).setVisible(true);
                                hideNotification(notificationFrame);
                                /* --------- SCARICA LA MAIL DALLA SD DEL SERVER; SOLO ALL'AVVENUTO LOGIN DEL CLIENT ---------------------------------------------------- */
                                System.out.println("\t[CLIENT INIT SYNC: DOWNLOADING MAIL FROM SERVER]");
                                personalMailBox = model.downloadMail(getUser());
                                /*TESTING PRINT
                                    //stampa tutte le mail scaricata dal server, cioè quelle che sono in commonMailBox al momento del login.
                                    for (Email m : personalMailBox) {
                                    System.out.println("Sono " + getUser() + "," + m.getArgomento());
                                    }
                                    TESTING PRINT*/
                                System.out.println("\t[CLIENT THREAD SYNC START: WAITING FOR MAIL TO DOWNLOAD]");
                                CheckDownload runnable = new CheckDownload(getUser(), model, personalMailBox);
                                runnable.addObserver(Controller.getInstance());
                                Thread t1 = new Thread(runnable);
                                t1.start();
                            }

                        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
                            feedbackLabel.setText("Formato errato: mail-pwd o mail gia esistente");
                        }

                    } else {
                        feedbackLabel.setText("Inserire dati");
                    }
                    user = autenticationTextField.getText().split("-")[0];

                } catch (RemoteException ex) {
                    feedbackLabel.setText("Server down, riprova piu' tardi...");
                } catch (IOException | NotBoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        signButton.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {

                feedbackLabel.setForeground(new Color(253, 255, 138));
                try {
                    Model.getInstance().connectToServer();
                    if (!autenticationTextField.getText().equals("")) {
                        user = autenticationTextField.getText();
                        String[] data = user.split("-"); //data[0] = mail, data[1] = pwd ..scriverle separate da -

                        try {
                            if (model.registraUtente(data[0], data[1])) {
                                System.out.println("[CLIENT SIGNIN & LOGIN AS " + getUser() + " : SUCCESS]");
                                autenticationPanel.setVisible(false);
                                lbl_mittenteNotification.setText("Registrazione");
                                lbl_previewNotification.setText("Effettuata con successo, " + user);
                                model.signUser(data[0], data[1]);
                                userImg.setIcon(new ImageIcon(getClass().getResource("/immagini/" + model.getUserImg(user) + ".png")));
                                userTag.setText(user.split("-")[0]);
                                notificationFrame.setVisible(true);
                                SwingUtilities.getWindowAncestor(rightPanel).setVisible(true);
                                hideNotification(notificationFrame);
                                /* --------- SCARICARE LA MAIL DALLA SD DEL SERVER; SOLO ALL'AVVENUTO LOGIN DEL CLIENT ---------------------------------------------------- */
                                System.out.println("\t[CLIENT INIT SYNC: DOWNLOADING MAIL FROM SERVER]");
                                personalMailBox = model.downloadMail(getUser());
                                /*TESTING PRINT
                            //stampa tutte le mail scaricata dal server, cioè quelle che sono in commonMailBox al momento del login.
                            for (Email m : personalMailBox) {
                                System.out.println("Sono " + getUser() + "," + m.getArgomento());
                            }
                            TESTING PRINT*/
                                System.out.println("\t[CLIENT THREAD SYNC START: WAITING FOR MAIL TO DOWNLOAD]");
                                CheckDownload runnable = new CheckDownload(getUser(), model, personalMailBox);
                                runnable.addObserver(Controller.getInstance());
                                Thread t1 = new Thread(runnable);
                                t1.start();

                            } else {
                                feedbackLabel.setText("Formato errato: mail-pwd o mail gia esistente");
                            }
                        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
                            feedbackLabel.setText("Formato errato: mail-pwd o mail gia esistente");
                        }

                    } else {
                        feedbackLabel.setText("Inserire dati");
                    }
                    user = autenticationTextField.getText().split("-")[0];
                } catch (RemoteException ex) {
                    feedbackLabel.setText("Server down, riprova piu' tardi...");
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        );

        lbl_reduce.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt
            ) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(rightPanel);
                int state = frame.getExtendedState();
                state = Frame.ICONIFIED;
                frame.setExtendedState(state);
            }
        }
        );

    }

    public void hideNotification(JFrame notificationFrame) throws IOException {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                notificationFrame.setVisible(false);
            }
        },
                Integer.parseInt(LoadProperty.giveProperty("timeNotifica"))
        );
    }

    public void connectNotification(String conn) throws IOException {
        if (conn.equals("up")) {
            lbl_mittenteNotification.setText("SERVER UP");
            lbl_previewNotification.setText("Riconnessione avvenuta con successo");
            notificationFrame.setVisible(true);
            button_send.setVisible(true);
            hideNotification(notificationFrame);
        }
        if (conn.equals("down")) {
            lbl_mittenteNotification.setText("SERVER DOWN");
            lbl_previewNotification.setText("Puoi solo leggere le mail in locale, riconnessione in corso...");
            button_send.setVisible(false);
            notificationFrame.setVisible(true);
        }
    }

    private void setComponentColor(Component c) {
        if (c instanceof JLabel) {
            c.setBackground(new Color(22, 42, 57));
        } else if (c instanceof JPanel) {
            c.setBackground(new Color(231, 233, 89));
        }
    }

    private void resetComponentColor(Component c) {
        if (c instanceof JLabel) {
            c.setBackground(new Color(33, 63, 86));
        } else if (c instanceof JPanel) {
            c.setBackground(new Color(254, 255, 194));
        }
    }

    private void setProperties() {

        flag = true;
        setComponentColor(lbl_menuInbox);
        resetComponentColor(lbl_menuSent);
        resetComponentColor(lbl_menuDrafts);
        resetComponentColor(lbl_menuSend);
        rightPanel.setVisible(false);
        //per ora faccio che se clicchi vedi su console tutte le mail salvate in locale

        lbl_reduce.setBackground(new Color(0, 0, 0));
        lbl_reduce.setFont(new Font("Segoe UI", 1, 18));
        lbl_reduce.setForeground(new java.awt.Color(0, 0, 0));
        lbl_reduce.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_reduce.setText("__");

        userTag.setBackground(new Color(33, 63, 86));
        userTag.setFont(new Font("Segoe UI", 1, 18));
        userTag.setForeground(new Color(255, 255, 255));
        userTag.setIconTextGap(10);
        userTag.setOpaque(true);

        cestino.setIcon(new ImageIcon(getClass().getResource("/immagini/trashIcon.png")));

        emptyPanel.setBackground(new Color(52, 83, 106));
        emptyPanel.setBorder(BorderFactory.createLineBorder(new Color(22, 42, 57)));
        emptyPanel.setForeground(new Color(22, 42, 57));

        readPanel.setBackground(new Color(52, 83, 106));
        readPanel.setBorder(BorderFactory.createLineBorder(new Color(22, 42, 57)));
        readPanel.setForeground(new Color(22, 42, 57));

        mailList.setFont(new Font("Dialog", 1, 18));
        mailList.setForeground(new Color(22, 42, 57));
        mailList.setBorder(BorderFactory.createLineBorder(Color.black));
        mailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mailList.setFixedCellHeight(80);
        mailList.setCellRenderer(getRenderer());
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) mailList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.HORIZONTAL);

        writePanel.setBackground(new Color(52, 83, 106));
        writePanel.setBorder(BorderFactory.createLineBorder(new Color(22, 42, 57)));
        writePanel.setForeground(new Color(22, 42, 57));

        button_send.setText("Send");
        button_send.setBorder(null);
        button_send.setFocusPainted(false);

        button_sendReply.setText("Reply");
        button_sendReply.setBorder(null);
        button_sendReply.setFocusPainted(false);

        button_sendReplyAll.setText("Reply All");
        button_sendReplyAll.setBorder(null);

        button_forward.setText("Forward");
        button_forward.setBorder(null);

        lbl_menuDrafts.setBackground(new Color(33, 63, 86));
        lbl_menuDrafts.setFont(new Font("Segoe UI", 1, 18));
        lbl_menuDrafts.setForeground(new Color(255, 255, 255));
        lbl_menuDrafts.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_menuDrafts.setText("Drafts");
        lbl_menuDrafts.setIconTextGap(10);
        lbl_menuDrafts.setOpaque(true);

        lbl_menuSend.setBackground(new Color(33, 63, 86));
        lbl_menuSend.setFont(new Font("Segoe UI", 1, 18));
        lbl_menuSend.setForeground(new Color(255, 255, 255));
        lbl_menuSend.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_menuSend.setText("Send");
        lbl_menuSend.setIconTextGap(10);
        lbl_menuSend.setOpaque(true);

        lbl_menuInbox.setBackground(new Color(22, 42, 57));
        lbl_menuInbox.setFont(new Font("Segoe UI", 1, 18));
        lbl_menuInbox.setForeground(new Color(255, 255, 255));
        lbl_menuInbox.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_menuInbox.setText("Inbox");
        lbl_menuInbox.setIconTextGap(10);
        lbl_menuInbox.setOpaque(true);

        lbl_menuSent.setBackground(new Color(33, 63, 86));
        lbl_menuSent.setFont(new Font("Segoe UI", 1, 18));
        lbl_menuSent.setForeground(new Color(255, 255, 255));
        lbl_menuSent.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_menuSent.setText("Sent");
        lbl_menuSent.setIconTextGap(10);
        lbl_menuSent.setOpaque(true);

        button_send.setText("Send");
        button_send.setBorder(null);
        button_send.setFocusPainted(false);

        loginButton.setText("Login");
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);

        signButton.setText("Sign-in");
        signButton.setBorder(null);
        signButton.setFocusPainted(false);

        midScroll.setViewportView(mailList);
    }

    boolean removeFromPersonalMailBox(Email mail) {
        return personalMailBox.remove(mail);
    }

    boolean removeFromRicevute(Email mail) {
        return ricevute.remove(mail);
    }

    boolean removeFromInviate(Email mail) {
        return inviate.remove(mail);
    }

    JScrollPane getMidScroll() {
        return midScroll;
    }

    JPanel getWritePanel() {
        return writePanel;
    }

    JPanel getRightPanel() {
        return rightPanel;
    }

    JPanel getEmptyPanel() {
        return emptyPanel;
    }

    JPanel getReadPanel() {
        return readPanel;
    }

    JPanel getOkPanel() {
        return singleInstance.emailSent_OK;
    }

    JPanel getErrorPanel() {
        return singleInstance.emailSent_ERROR;
    }

    JButton getSendButton() {
        return button_send;
    }

    JButton getSendReplyButton() {
        return button_sendReply;
    }

    JButton getSendReplyAllButton() {
        return button_sendReplyAll;
    }

    JButton getForward() {
        return button_forward;
    }

    JLabel getLbl_menuSend() {
        return lbl_menuSend;
    }

    JLabel getLbl_menuInbox() {
        return lbl_menuInbox;
    }

    JLabel getLbl_menuDrafts() {
        return lbl_menuDrafts;
    }

    JLabel getLbl_menuSent() {
        return lbl_menuSent;
    }

    JButton getLoginButton() {
        return loginButton;
    }

    JButton getSignButton() {
        return signButton;
    }

    JDialog getAutenticationPanel() {
        return autenticationPanel;
    }

    JLabel getFeedbackLabel() {
        return feedbackLabel;
    }

    JTextField getAutenticationTextField() {
        return autenticationTextField;
    }

    JLabel getLbl_mittenteNotification() {
        return lbl_mittenteNotification;
    }

    JLabel getLbl_previewNotification() {
        return lbl_previewNotification;
    }

    JPanel getReceivedNotification() {
        return receivedNotification;
    }

    JFrame getNotificationFrame() {
        return notificationFrame;
    }

    String getUser() {
        String[] usrEpwd = user.split("-");
        return usrEpwd[0];
    }

    JLabel getCestino() {
        return cestino;
    }

    ArrayList<Email> getPersonalMailBox() {
        return personalMailBox;
    }

    static Controller getSingleInstance() {
        return singleInstance;
    }

    void setPersonalMailBox(ArrayList<Email> personalMailBox) {
        this.personalMailBox = personalMailBox;
    }

    public JLabel getUserImg() {
        return userImg;
    }

    public JLabel getUserTag() {
        return userTag;
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                listCellRendererComponent.setHorizontalTextPosition(JLabel.RIGHT);
                JLabel img = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                img.setIcon(new ImageIcon(getClass().getResource("/immagini/emailIcon.png")));
                img.setHorizontalTextPosition(JLabel.LEFT);
                img.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                return listCellRendererComponent;
            }
        };
    }

    Email getOnPanel() {
        return onPanel;
    }

    void setOnPanel(Email mail) {
        this.onPanel = mail;
    }

    JLabel getLbl_reduce() {
        return lbl_reduce;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!((Email) arg).getMittente().equals(user)) {
            lbl_mittenteNotification.setText("Email ricevuta da " + ((Email) arg).getMittente() + "!");
            lbl_previewNotification.setText(((Email) arg).getArgomento());
            notificationFrame.setVisible(true);
            lbl_menuInbox.setText("Inbox" + " (" + (++inboxCount) + ") ");
        }
    }
}
