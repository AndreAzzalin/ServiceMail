package emailservice;

//Importiamo i package e le classi utili per aprire un file
import java.io.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

//DOM: il modello dei dati secondo Java
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML_Manager {

    static String path;

    public XML_Manager(String path) {
        this.path = path;
    }

    public void storeMail(Email mail) {

        try {
            System.out.println("Voglio scrivere in " + path);
            File file = new File(path);/*
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);*/
            if (file.exists()) {
                System.out.println("Il file esiste già! ");
                appendMail(mail, file);
            } else if (file.createNewFile()) {
                System.out.println("Il file non esiste ancora! ");
                appendMail(mail, file);
            } else {
                System.out.println("Il file " + path + " non può essere creato");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endStore() throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(closeRoot);
    }

    private void appendMail(Email email, File file) {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String dest = "";
            for (String s : email.getDestinatario()) {
                dest += s + "-";
            }
            dest = dest.substring(0, dest.length() - 1);

            String append = mail + client + email.getMittente() + closeClient
                    + destinatario + dest + closeDestinatario
                    + data + email.getDataSpedizione() + closeData
                    + title + email.getArgomento() + closeTitle
                    + text + email.getTesto() + closeText + closeMail;

            System.out.println("Sto scrivendo!");
            bw.write("\n" + append + "\n");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Email[] readMails() {
        Email[] mailList;
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = documentFactory.newDocumentBuilder();
            Document document = builder.parse(new File(path));

            NodeList mails = document.getElementsByTagName("mail");
            mailList = new Email[mails.getLength()];

            for (int i = 0; i < mails.getLength(); i++) {
                Node nodo = mails.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element mail = (Element) nodo;

                    String clientName = mail.getElementsByTagName("client").item(0).getFirstChild().getNodeValue();
                    String[] destinatari = mail.getElementsByTagName("destinatario").item(0).getFirstChild().getNodeValue().split("-");
                    String data = mail.getElementsByTagName("data").item(0).getFirstChild().getNodeValue();
                    String title = mail.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                    String text = mail.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
                    String letta = mail.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
                    String prefered = mail.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();

                    //System.out.println("INSERT INTO email (Destinatario, Mittente, Argomento, Testo)"
                    //        + "VALUES ('" + destinatario + "', '" + clientName + "', '" + title + "', '" + text + "');");
                    mailList[i] = new ConcreteEmail(clientName, destinatari, data, title, text);
                }
            }
        } catch (Exception e) {
            mailList = null;
            e.printStackTrace();
        }

        return mailList;
    }

    static final String root = "<root>";
    static final String closeRoot = "</root>";
    static final String mail = "<mail>";
    static final String closeMail = "</mail>";
    static final String client = "<client>";
    static final String closeClient = "</client>";
    static final String destinatario = "<destinatario>";
    static final String closeDestinatario = "</destinatario>";
    static final String data = "<data>";
    static final String closeData = "</data>";
    static final String title = "<title>";
    static final String closeTitle = "</title>";
    static final String text = "<text>";
    static final String closeText = "</text>";
}
