/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author fabio
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class LoadProperty {

    public static void main(String[] args) {

        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            // set the properties value

            /* DataBase Properties */
            prop.setProperty("JDBC_DRIVER", "com.mysql.jdbc.Driver");
            prop.setProperty("URL_MYSQL", "jdbc:mysql://");
            prop.setProperty("HOST", "localhost/");
            prop.setProperty("NOME_DB", "db_mail");
            prop.setProperty("dbuser", "root");
            //prop.setProperty("dbpassword", "password");

            /* Ogni quanto il Server aggiorna il DB */
            prop.setProperty("dbUploadingTime", "20000");
            
            /* Porta di connessione del server per RMI */
            prop.setProperty("portaRMI", "1099");
            prop.setProperty("URL_RMI", "rmi://localhost/");

            /* durata di notifica frame */
            prop.setProperty("timeNotifica", "10000");
            
            
            /* image data */
            prop.setProperty("pathImg", "../../Immagini/");
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String giveProperty(String key) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream("config.properties");
        // load a properties file
        prop.load(input);
        // get the property value and print it out
        return prop.getProperty(key);
    }
}
