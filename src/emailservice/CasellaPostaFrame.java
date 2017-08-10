package emailservice;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class CasellaPostaFrame extends JFrame {

    private Model model;
    private Controller controller;
    private JPanel totalPanel;

    public Model getModel() {
        return model;
    }

    public Controller getController() {
        return controller;
    }

    public CasellaPostaFrame() throws IOException {

        model = Model.getInstance();
        controller = Controller.getInstance();


        /* -------------------------------- LAYOUT ESTERNO -------------------------------------------------------- */
        setSize(1552, 567);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(22, 41, 57));
        setUndecorated(true);

        /* -------------------------------- COMPONENTI ----------------------------------------------------------- */
        totalPanel = (TotalPanel.getInstance()).getTotalPanel();

        /*  -------------------------------- POSIZIONAMENTO ------------------------------------------------------ */
        addComponents();
        //dichiarare la view come ascoltatrice del model e settare il layout aggiungendo le varie componenti 
    }

    private void addComponents() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

}
