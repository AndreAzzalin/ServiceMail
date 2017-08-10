package emailservice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author fabio
 */
public class LogFrame extends JFrame {

    /**
     * L'area destinata a contenere il Log
     */
    private JTextArea area = new JTextArea();
    private Server s;

    
    

    //private JButton buttonStart = new JButton("Start");
    private JButton button_clear = new JButton("Clear");
    private JButton button_export = new JButton("Export.txt");

    private JPanel p = new JPanel();
    private JPanel panel_btn = new JPanel();
    private JLabel label = new JLabel("|------------------ DATA ---------------- ATTORE PRIMARIO ----------------- AZIONE ------------------ ATTORI SECONDARI ----------------- ERRORI ------------------ |");
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    public LogFrame(Server s) {
        super("Mail Server");
        this.s = s;
        add(p, BorderLayout.NORTH);
        add(panel_btn, BorderLayout.SOUTH);
        panel_btn.add(button_export);
        button_export.setBackground(new Color(33, 63, 86));
        button_export.setForeground(Color.WHITE);
        button_export.setFocusPainted(false);
        button_export.setPreferredSize(new Dimension(100, 40));

        panel_btn.add(button_clear);
        button_clear.setBackground(new Color(33, 63, 86));
        button_clear.setForeground(Color.WHITE);
        button_clear.setFocusPainted(false);
        button_clear.setPreferredSize(new Dimension(100, 40));
        area.setEditable(false);
        area.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        area.setBackground(new Color(33, 63, 86));
        area.setForeground(Color.WHITE);
        add(area);
        label.setFont(new Font("Sans Serif", Font.PLAIN, 17));
        button_export.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        button_clear.setFont(new Font("Sans Serif", Font.PLAIN, 14));

        p.add(label);
        area.setFont(new Font("Sans Serif", Font.PLAIN, 15));
        add(new JScrollPane(area));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1200, 700);
        setResizable(false);
        setVisible(true);
       

        

        
        /*
            Eventi e Action Listener
        */
        
        button_export.addActionListener((ActionEvent evt) -> {
            saveAs();
        });

        button_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // clears the text area
                try {
                    area.getDocument().remove(0,
                            area.getDocument().getLength());
                    //standardOut.println("Text area cleared");
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
    addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try {
                    s.callUploadDB();
                } catch (RemoteException ex) {
                    Logger.getLogger(LogFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.getWindow().dispose();
            }
        });
    }

    //"|------------------ DATA ---------------- ATTORE PRIMARIO ----------------- AZIONE ------------------ ATTORI SECONDARI ----------------- ERRORI ------------------ |"
    //aggiungere come prima riga nel .txt 
    public void saveAs() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // !! File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));

            area.write(outFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /*
    public static void main(String[] args) {
        LogFrame log;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LogFrame();
            }
        });
    }
     */

    public JTextArea getArea() {
        return area;
    }

}
