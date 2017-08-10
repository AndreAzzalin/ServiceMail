package emailservice;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;

public class TotalPanel extends JPanel {

    private JFrame notificationFrame;
    private JDialog autenticationPanel;
    private JPanel leftPanel, rightPanel, totalPanel, labelPanel, receivedNotification;
    private JLabel lbl_mittenteNotification, lbl_closeNotification, lbl_previewNotification, autenticationClose, autenticationFeedback, autenticationTitle;
    private JTextField autenticationTextField;
    private JButton loginButton, signButton;
    private static TotalPanel singleInstance;

    /* -------------------------------- SINGLETON -------------------------------------------------------- */
    public static TotalPanel getInstance() throws IOException {
        if (TotalPanel.singleInstance == null) {
            TotalPanel.singleInstance = new TotalPanel();
        }
        return TotalPanel.singleInstance;
    }

    private TotalPanel() throws IOException {

        /* -------------------------------- COMPONENTI ----------------------------------------------------------- */
        notificationFrame = Controller.getInstance().getNotificationFrame();
        receivedNotification = Controller.getInstance().getReceivedNotification();
        lbl_mittenteNotification = Controller.getInstance().getLbl_mittenteNotification();
        lbl_previewNotification = Controller.getInstance().getLbl_previewNotification();
        lbl_closeNotification = new JLabel();
        autenticationClose = new JLabel();
        autenticationFeedback = Controller.getInstance().getFeedbackLabel();
        autenticationTitle = new JLabel(); 

        autenticationTextField = Controller.getInstance().getAutenticationTextField();

        autenticationPanel = Controller.getInstance().getAutenticationPanel();

        labelPanel = LabelPanel.getInstance();
        leftPanel = MailListView.getInstance().getLeftPanel();
        rightPanel = RightPanelView.getInstance().getRightPanel();

        loginButton = Controller.getInstance().getLoginButton();
        signButton = Controller.getInstance().getSignButton();

        totalPanel = new JPanel();

        /* -------------------------------- PROPRIETÀ GRAFICHE ---------------------------------------------------- */
        setProperties();

        /*  -------------------------------- POSIZIONAMENTO ------------------------------------------------------ */
        addComponents();
    }

    private void setProperties() {
        /* -------------------------------- PANNELLO PRINCIPALE ---------------------------------------------------- */
        totalPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        /* -------------------------------- PANNELLO NOTIFICHE ------------------------------------------------------ */
        receivedNotification.setBackground(new Color(254, 255, 194));
        receivedNotification.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        notificationFrame.add(receivedNotification);
        notificationFrame.setSize(550, 170);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - notificationFrame.getWidth();
        int y = (int) rect.getMaxY() - notificationFrame.getHeight() - 80;
        notificationFrame.setLocation(x, y);
        notificationFrame.setUndecorated(true);
        notificationFrame.setAlwaysOnTop(true);

        lbl_mittenteNotification.setFont(new Font("Dialog", 1, 18));
        lbl_mittenteNotification.setForeground(new Color(51, 51, 51));
        lbl_mittenteNotification.setText("Sam Nujoma");

        lbl_closeNotification.setFont(new Font("Segoe UI", 1, 18));
        lbl_closeNotification.setBackground(new Color(0, 0, 0));
        lbl_closeNotification.setForeground(new Color(0, 0, 0));
        lbl_closeNotification.setText("X");
        lbl_closeNotification.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                notificationFrame.setVisible(false);
            }
        });

        lbl_previewNotification.setFont(new Font("Segoe UI", 0, 14));
        lbl_previewNotification.setForeground(new Color(51, 51, 51));
        lbl_previewNotification.setText("Something came up this weekend\nthe wands broke...");
        //notificationFrame.setVisible(true);
        /* -------------------------------- PANNELLO AUTENTICAZIONE ------------------------------------------------------ */

        autenticationPanel.getContentPane().setBackground(new Color(52, 83, 106));
        autenticationPanel.getContentPane().setForeground(new Color(22, 42, 57));
        autenticationPanel.setSize(400, 190);
        autenticationPanel.setLocationRelativeTo(null);
        autenticationPanel.setUndecorated(true);
        autenticationPanel.setAlwaysOnTop(true);

        autenticationPanel.setVisible(true);

        autenticationClose.setFont(new Font("Segoe UI", 1, 18));
        autenticationClose.setForeground(new Color(0, 0, 0));
        autenticationClose.setText("X");
        autenticationClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.exit(0);
            }
        });
        
        autenticationTitle.setText(" -   Autenticati o registrati");
        autenticationTitle.setFont(new Font("Calibri", 3, 18));
        autenticationTitle.setForeground(Color.BLACK);

    }

    private void addComponents() {
        /* -------------------------------- PANNELLO PRINCIPALE ------------------------------------------------------ */
        GroupLayout totalPanelLayout = new GroupLayout(totalPanel);
        totalPanel.setLayout(totalPanelLayout);
        totalPanelLayout.setHorizontalGroup(
                totalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(totalPanelLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        totalPanelLayout.setVerticalGroup(
                totalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(totalPanelLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(totalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        /* -------------------------------- PANNELLO NOTIFICHE ------------------------------------------------------ */
        GroupLayout notificationLayout = new GroupLayout(receivedNotification);
        receivedNotification.setLayout(notificationLayout);
        notificationLayout.setHorizontalGroup(notificationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(notificationLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(notificationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_previewNotification, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(notificationLayout.createSequentialGroup()
                                        .addGroup(notificationLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbl_mittenteNotification, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lbl_closeNotification, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        notificationLayout.setVerticalGroup(notificationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(notificationLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_closeNotification)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_mittenteNotification)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_previewNotification, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(19, Short.MAX_VALUE))
        );

        /* -------------------------------- PANNELLO AUTENTICAZIONE ------------------------------------------------------ */
        javax.swing.GroupLayout autenticationPanelLayout = new javax.swing.GroupLayout(autenticationPanel.getContentPane());
        autenticationPanel.getContentPane().setLayout(autenticationPanelLayout);
        autenticationPanelLayout.setHorizontalGroup(autenticationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(autenticationPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(autenticationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(autenticationPanelLayout.createSequentialGroup()
                                                .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(signButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(autenticationTextField)
                                        .addGroup(autenticationPanelLayout.createSequentialGroup()
                                                .addComponent(autenticationFeedback)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(autenticationPanelLayout.createSequentialGroup()
                                                .addComponent(autenticationClose)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(autenticationTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        autenticationPanelLayout.setVerticalGroup(autenticationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, autenticationPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(autenticationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(autenticationClose)
                                        .addComponent(autenticationTitle))
                                .addGap(18, 18, 18)
                                .addComponent(autenticationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autenticationFeedback)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(autenticationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(signButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
    }

    JPanel getTotalPanel() {
        return totalPanel;
    }
}
