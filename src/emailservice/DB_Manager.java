package emailservice;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import utilities.LoadProperty;

public class DB_Manager {

    public final String JDBC_DRIVER;
    public final String DB_URL;
    public final String DB_USR;

    public DB_Manager() throws IOException {
        JDBC_DRIVER = LoadProperty.giveProperty("JDBC_DRIVER");
        DB_URL = LoadProperty.giveProperty("URL_MYSQL") + LoadProperty.giveProperty("HOST") + LoadProperty.giveProperty("NOME_DB");
        DB_USR = LoadProperty.giveProperty("dbuser");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            System.out.println("Registrazione DB non riuscita\n");
        }

    }

    public boolean addEmail(Email email) {
        boolean flag = false;
        try {
            String query;
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();
            query = "INSERT INTO email (Mittente, Argomento, Testo, DataSpedizione)"
                    + "VALUES ('" + email.getMittente() + "', '" + email.getArgomento() + "', '" + email.getTesto() + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "')";
            st.executeUpdate(query);

            query = "SELECT ID FROM email WHERE MITTENTE = '" + email.getMittente() + "' AND ARGOMENTO = '" + email.getArgomento() + "' AND TESTO = '" + email.getTesto() + "'";// + "' AND DataSpedizione = '" + email.getDataSpedizione()+ "'";
            ResultSet rs = st.executeQuery(query);

            int id = -1;
            while (rs.next()) {
                id = rs.getInt("ID");

            }
            query = "INSERT INTO MAILACTIVE VALUES('" + email.getMittente() + "'," + id + ")";
            st.executeUpdate(query);
            for (String destinatario : email.getDestinatario()) {
                query = "INSERT INTO MAILACTIVE VALUES('" + destinatario + "'," + id + ")";
                st.executeUpdate(query);
                flag = true;
            }

            for (String destinatario : email.getDestinatario()) {
                query = "INSERT INTO destinatari (IDMAIL, DESTINATARIO)"
                        + " VALUES (" + id + ", '" + destinatario + "');";
                st.executeUpdate(query);
                flag = true;
            }
            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            flag = false;
            System.out.println("Query addEmail non riuscita\n");
        }
        return flag;
    }

    /*
    public ArrayList<Email> getAllMailActive(String user) {
        ArrayList<Email> activeList = new ArrayList<>();
        String query = "SELECT ID_USER, ID_MAIL, destinatario, prefered, letta, Mittente, Argomento, Testo, DataSpedizione FROM mailactive as m JOIN destinatari as d ON (m.ID_MAIL = d.IDMAIL)JOIN EMAIL as e ON(e.ID = m.ID_MAIL) WHERE m.ID_USER = "'+user+'"";
        System.out.println(query);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String[] dest = {rs.getString("Destinatario")};
                activeList.add(new ConcreteEmail(rs.getInt("ID_MAIL"), rs.getString("mittente"), dest, rs.getString("dataSpedizione"), rs.getString("argomento"), rs.getString("testo"), false));
            }

            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query getAllMail non riuscita\n");
        }
        return activeList;

    }
     */
    public void uploadDB(Email email) {
        try {
            String query;
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();

            query = "INSERT INTO email (Mittente, Argomento, Testo, DataSpedizione)"
                    + "VALUES ('" + email.getMittente() + "', '" + email.getArgomento() + "', '" + email.getTesto() + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "')";
            st.executeUpdate(query);

            query = "SELECT ID FROM email WHERE MITTENTE = '" + email.getMittente() + "' AND ARGOMENTO = '" + email.getArgomento() + "' AND TESTO = '" + email.getTesto() + "'";
            ResultSet rs = st.executeQuery(query);

            int id = -1;
            while (rs.next()) {
                id = rs.getInt("ID");

            }
            /*
            Controlli per aggiungere a MailActive solo se l'utente non aveva richiesto la rimozione = flag " " alla fine del suo nome nell mail.
             */
            if (!" ".equals(email.getMittente().substring(email.getMittente().length() - 1))) {
                //no in mail active
                query = "INSERT INTO MAILACTIVE VALUES('" + email.getMittente() + "'," + id + ")";
                st.executeUpdate(query);
            } else {
                email.getMittente().trim();
            }

            for (String destinatario : email.getDestinatario()) {
                if (!" ".equals(destinatario.substring(destinatario.length() - 1))) {
                    query = "INSERT INTO MAILACTIVE VALUES('" + destinatario + "'," + id + ")";
                    st.executeUpdate(query);
                } else {
                    destinatario.trim();
                }
            }
            /*
            Fine controllo
             */
            for (String destinatario : email.getDestinatario()) {
                query = "INSERT INTO destinatari (IDMAIL, DESTINATARIO)"
                        + " VALUES (" + id + ", '" + destinatario + "');";
                st.executeUpdate(query);
            }
            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query addEmail non riuscita\n");
        }
    }

    public void deleteEmail(Email mail, boolean flag) {

        String query = null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();
            if (flag) {
                query = "DELETE FROM mailactive WHERE ID_USER='" + mail.getDestinatario()[0] + "' AND ID_MAIL=" + mail.getID();
                st.executeUpdate(query);
            } else {
                query = "DELETE FROM mailactive WHERE ID_USER='" + mail.getMittente() + "' AND ID_MAIL=" + mail.getID();
                st.executeUpdate(query);
            }
            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        /*
        String query = null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();

            if (flag) {
                query = "UPDATE destinatari "
                        + "SET inCasellaDestinatario= 0 "
                        + "WHERE IDMAIL = " + mail.getID() + " AND DESTINATARIO ='" + mail.getDestinatario()[0] + "'";
                System.out.println(query);
                st.executeUpdate(query);
            } else {
                query = "UPDATE email "
                        + "SET InCasellaMittente= 0 "
                        + "WHERE ID = '" + mail.getID() + "'";
                System.out.println(query);
                st.executeUpdate(query);
                System.out.println(query);
            }
            st.close();
            conn.close();
            System.out.println("[RIMOZIONE DAL DATABASE EFFETTUATA CORRETTAMENTE]");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query deleteEmail non riuscita\n");
        }*/
    }

    public void setLetta(Email email, String user) {
        try {
            String query;
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();

            query = "SELECT * FROM destinatari "
                    + "JOIN EMAIL ON (ID = IDMAIL) "
                    + "WHERE mittente = '" + email.getMittente() + "'"
                    + "AND argomento = '" + email.getArgomento() + "'"
                    + "AND testo = '" + email.getTesto() + "'"
                    + "AND dataspedizione =  '" + email.getDataSpedizione() + "'";

            System.out.println(query);

            ResultSet rs = st.executeQuery(query);

            int id = -1;
            while (rs.next()) {
                id = rs.getInt("ID");
            }

            query = "UPDATE destinatari SET letta = true WHERE IDMAIL ='" + id + "' AND DESTINATARIO='" + user + "'";

            System.out.println(query);

            st.executeUpdate(query);
            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Email> getAllMail(String user) {
        ArrayList<Email> mailList = new ArrayList<>();
        String query = "SELECT * FROM mailactive as m JOIN destinatari as d ON (m.ID_MAIL = d.IDMAIL)JOIN EMAIL as e ON(e.ID = m.ID_MAIL) WHERE m.ID_USER = '" + user + "'";
        //System.out.println(query);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null);
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);

            int flag = -1;
            String mittente = null;
            String dataSpedizione = null;
            String argomento = null;
            String testo = null;
            boolean letta = false;
            ArrayList<String> destinatari = new ArrayList<>();

            while (rs.next()) {
                if (flag == -1) {
                    destinatari.add(rs.getString("Destinatario"));
                } else {
                    if (rs.getInt("ID") == flag) {
                        destinatari.add(rs.getString("Destinatario"));
                    } else {
                        mailList.add(new ConcreteEmail(flag, mittente, (String[]) destinatari.toArray(new String[destinatari.size()]), dataSpedizione, argomento, testo, letta, false));
                        destinatari = new ArrayList<>();
                        destinatari.add(rs.getString("Destinatario"));
                    }
                }
                flag = rs.getInt("ID");
                mittente = rs.getString("Mittente");
                dataSpedizione = rs.getString("DataSpedizione");
                argomento = rs.getString("Argomento");
                testo = rs.getString("Testo");
                letta = rs.getBoolean("Letta");

            }
            mailList.add(new ConcreteEmail(flag, mittente, (String[]) destinatari.toArray(new String[destinatari.size()]), dataSpedizione, argomento, testo, letta, false));
            st.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Query getAllMail non riuscita\n");
        }
        return mailList;
    }

    public ArrayList<String> getAllUser() {
        String query;
        ArrayList<String> allUser = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {

            Statement st = conn.createStatement();

            query = "SELECT MAIL FROM UTENTI";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                allUser.add(rs.getString("MAIL"));
            }
            st.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Query getDestinatarioMails non riuscita\n");
        }
        return allUser;
    }

    public String getUsrImg(String user) {
        String query;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {

            Statement st = conn.createStatement();

            query = "SELECT user_img FROM UTENTI WHERE mail='" + user.split("-")[0] + "'";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                return rs.getString("user_img");
            }
            st.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Query getUsrImg non riuscita\n");
        }
        return "";
    }

    /*
    public Email[] getDestinatarioMails(String destinatario) {
        Email[] array = null;
        String query;
        ArrayList<Email> allMail = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {

            Statement st = conn.createStatement();

            query = "SELECT * FROM email WHERE (destinatario = '" + destinatario + "')";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String[] tmp = {rs.getString(4)};
                allMail.add(new ConcreteEmail(rs.getString(5), tmp, rs.getString(8), rs.getString(6), rs.getString(7), rs.getBoolean(2)));
            }

            st.close();
            conn.close();
            array = allMail.toArray(new Email[0]);
        } catch (SQLException ex) {
            System.out.println("Query getDestinatarioMails non riuscita\n");
        }
        return array;
    }
     */
    public boolean autenticateUser(String user, String pwd) {
        boolean flag = false;
        String query = "SELECT COUNT(MAIL) FROM UTENTI WHERE(MAIL =  '" + user + "' AND PASSWORD = '" + pwd + "')";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                flag = (rs.getInt(1) == 1);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public boolean signUser(String usrMail, String pwd) {
        boolean flag = false;
        if (!mailExist(usrMail)) {
            String query = "INSERT INTO UTENTI(MAIL, PASSWORD) VALUES ('" + usrMail + "', '" + pwd + "')";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {
                Statement st = conn.createStatement();
                st.executeUpdate(query);
                flag = true;
                st.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return flag;
    }

    public boolean mailExist(String mail) {
        boolean flag = false;
        String query = "SELECT COUNT(MAIL) FROM UTENTI WHERE(MAIL =  '" + mail + "')";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USR, null)) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                flag = (rs.getInt(1) == 1);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

}
