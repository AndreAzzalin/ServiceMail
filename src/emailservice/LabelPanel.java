package emailservice;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.*;

public class LabelPanel extends JPanel {

    /* -------------------------------- SINGLETON -------------------------------------------------------- */
    private JLabel lbl_menuDrafts, lbl_menuInbox, lbl_menuSend, lbl_menuSent, lbl_close, userImg, userTag, lbl_reduce;
    private static LabelPanel singleInstance;

    public static LabelPanel getInstance() throws IOException {
        if (LabelPanel.singleInstance == null) {
            LabelPanel.singleInstance = new LabelPanel();
        }
        return LabelPanel.singleInstance;
    }

    private LabelPanel() throws IOException {
        /* -------------------------------- LAYOUT ESTERNO -------------------------------------------------------- */
        setBackground(new Color(33, 63, 86));

        /* -------------------------------- COMPONENTI ----------------------------------------------------------- */
        lbl_menuSent = Controller.getInstance().getLbl_menuSent();
        lbl_menuInbox = Controller.getInstance().getLbl_menuInbox();
        lbl_menuSend = Controller.getInstance().getLbl_menuSend();
        lbl_menuDrafts = Controller.getInstance().getLbl_menuDrafts();
        lbl_reduce = Controller.getInstance().getLbl_reduce();
        userImg = Controller.getInstance().getUserImg();
        userTag = Controller.getInstance().getUserTag();
        lbl_close = new JLabel();

        lbl_close.setBackground(new Color(0, 0, 0));
        lbl_close.setFont(new Font("Segoe UI", 1, 18));
        lbl_close.setForeground(new Color(0, 0, 0));
        lbl_close.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_close.setText("X");
        lbl_close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.exit(0);
            }
        });

        /*  -------------------------------- POSIZIONAMENTO ------------------------------------------------------ */
        addComponents();
    }

    private void addComponents() {

        javax.swing.GroupLayout labelPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(labelPanelLayout);
        labelPanelLayout.setHorizontalGroup(
                labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labelPanelLayout.createSequentialGroup()
                                .addGap(0, 87, Short.MAX_VALUE)
                                .addGroup(labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbl_menuDrafts, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                        .addComponent(lbl_menuSent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_menuSend, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_menuInbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(labelPanelLayout.createSequentialGroup()
                                .addGroup(labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(labelPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lbl_close, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lbl_reduce))
                                        .addGroup(labelPanelLayout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(userImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(userTag)))
                                .addContainerGap(128, Short.MAX_VALUE))
        );
        labelPanelLayout.setVerticalGroup(
                labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(labelPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_close, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbl_reduce))
                                .addGap(26, 26, 26)
                                .addGroup(labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(userTag)
                                        .addComponent(userImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addComponent(lbl_menuInbox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_menuDrafts, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_menuSent, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_menuSend, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    }
}
