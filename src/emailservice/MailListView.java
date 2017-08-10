package emailservice;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class MailListView extends JPanel implements View {

    private JPanel labelPanel, midPanel, leftPanel;
    private JScrollPane midScroll;
    private static MailListView singleInstance;
    private JLabel cestino;

    /* -------------------------------- SINGLETON -------------------------------------------------------- */
    public static MailListView getInstance() throws IOException {
        if (MailListView.singleInstance == null) {
            MailListView.singleInstance = new MailListView();
        }
        return MailListView.singleInstance;
    }

    private MailListView() throws IOException {
        /* -------------------------------- COMPONENTI -------------------------------------------------------- */
        midScroll = Controller.getInstance().getMidScroll();
        labelPanel = LabelPanel.getInstance();
        cestino = Controller.getInstance().getCestino();

        midPanel = new JPanel();
        leftPanel = new JPanel();

        /* --------------------------------  PROPRIETÀ GRAFICHE --------------------------------------------------- */
        setProperties();

        /* -------------------------------- POSIZIONAMENTO ----------------------------------------------------- */
        addComponents();
    }

    private void setProperties() {
        midPanel.setBackground(new Color(146, 157, 166));
        leftPanel.setBackground(new Color(22, 42, 57));
    }

    private void addComponents() {
        javax.swing.GroupLayout midPanelLayout = new javax.swing.GroupLayout(midPanel);
        midPanel.setLayout(midPanelLayout);
        midPanelLayout.setHorizontalGroup(
                midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(midPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(midScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cestino)
                                .addContainerGap())
        );
        midPanelLayout.setVerticalGroup(
                midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(midScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cestino, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );

        GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
                leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(leftPanelLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(labelPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(midPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
        );
        leftPanelLayout.setVerticalGroup(
                leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(labelPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(midPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    public JPanel getMidPanel() {
        return midPanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public JScrollPane getMidScroll() {
        return midScroll;
    }

    @Override
    public void updateView(Model model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
