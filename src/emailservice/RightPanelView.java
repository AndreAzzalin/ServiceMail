package emailservice;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class RightPanelView extends JPanel implements View, Observer {

    private JPanel rightPanel, emptyPanel, readPanel, writePanel, readContainer, emailErrorContainer, emailSent_ERROR, emailOkContainer, emailSent_OK;
    private JLabel lbl_readTitle, lbl_readObject;
    private JTextArea textArea_readContent;
    private JButton button_send, button_sendReply, button_sendReplyAll, button_forward;
    private JTextArea textArea_sendContent;
    private JTextField textField_sendObject, textField_sendTo;
    private JLabel lbl_sendObject, lbl_sendTitle, lbl_sendTo, lbl_ERROR, lbl_ERROR1, lbl_OK;
    private static RightPanelView singleInstance;

    /* -------------------------------- SINGLETON --------------------------------------------------------------- */
    public static RightPanelView getInstance() throws IOException {
        if (RightPanelView.singleInstance == null) {
            RightPanelView.singleInstance = new RightPanelView();
        }
        return RightPanelView.singleInstance;
    }

    private RightPanelView() throws IOException {
        /* -------------------------------- COMPONENTI ----------------------------------------------------------- */
        lbl_sendTo = new JLabel();
        lbl_readObject = new JLabel();
        lbl_sendObject = new JLabel();
        lbl_readTitle = new JLabel();
        lbl_sendTitle = new JLabel();
        lbl_ERROR = new JLabel();
        lbl_ERROR1 = new JLabel();
        lbl_OK = new JLabel();

        textField_sendTo = new JTextField();
        textField_sendObject = new JTextField();
        textArea_sendContent = new JTextArea();
        textArea_readContent = new JTextArea();

        button_send = Controller.getInstance().getSendButton();
        button_sendReply = Controller.getInstance().getSendReplyButton();
        button_sendReplyAll = Controller.getInstance().getSendReplyAllButton();
        button_forward = Controller.getInstance().getForward();

        writePanel = Controller.getInstance().getWritePanel();
        rightPanel = Controller.getInstance().getRightPanel();
        emptyPanel = Controller.getInstance().getEmptyPanel();
        readPanel = Controller.getInstance().getReadPanel();
        emailSent_OK = Controller.getInstance().getOkPanel();
        emailSent_ERROR = Controller.getInstance().getErrorPanel();

        readContainer = new JPanel();
        emailOkContainer = new JPanel();
        emailErrorContainer = new JPanel();

        /* --------------------------------  PROPRIETÀ GRAFICHE --------------------------------------------------- */
        setProperties();

        /*  -------------------------------- POSIZIONAMENTO ------------------------------------------------------ */
        addComponents();
    }

    @Override
    public void updateView(Model model) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private void setProperties() {
        rightPanel.setLayout(new CardLayout());

        lbl_readTitle.setBackground(new Color(244, 246, 85));
        lbl_readTitle.setFont(new Font("Dialog", 1, 18));
        lbl_readTitle.setForeground(new Color(255, 255, 255));
        lbl_readTitle.setSize(1000, 1000);
        lbl_readTitle.setPreferredSize(new Dimension(1000, 1000));
        lbl_readTitle.setText("Magic Wand");

        lbl_readObject.setBackground(new Color(253, 254, 144));
        lbl_readObject.setFont(new Font("Segoe UI", 0, 14));
        lbl_readObject.setForeground(new Color(255, 255, 255));
        lbl_readObject.setText("Sam Nujoma");

        readContainer.setBackground(new Color(52, 83, 106));

        textArea_readContent.setEditable(false);
        textArea_readContent.setColumns(20);
        textArea_readContent.setFont(new Font("Segoe UI", 0, 17));
        textArea_readContent.setForeground(new Color(51, 51, 51));
        textArea_readContent.setLineWrap(true);
        textArea_readContent.setRows(5);
        textArea_readContent.setText("Something came up this weekend, Well. The main confusion comes from the notion that a Magic Wand must look like a Baton, like a orchestra conductor's baton. But this is like saying that all human beings have the name John Smith. Nothing could be further from the truth. Incidentally, the setting for this book on Magic Wands is that of \"Historical Fiction\", that is to say that names have been created to substitute for actual characters that use these Magic Wands, but none the less, the drama and stories about the use of the Wands here comes from actual practice and experience. But back to what a Wand looks like- leave your preconceived notions behind, and you will then come to understand what a Magic Wand actually looks like.");
        textArea_readContent.setWrapStyleWord(true);
        textArea_readContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea_readContent.setCaretColor(new Color(51, 51, 51));

        lbl_sendTo.setBackground(new Color(253, 254, 144));
        lbl_sendTo.setFont(new Font("Segoe UI", 0, 14));
        lbl_sendTo.setForeground(new Color(255, 255, 255));
        lbl_sendTo.setText("a");

        lbl_sendObject.setBackground(new Color(253, 254, 144));
        lbl_sendObject.setFont(new Font("Segoe UI", 0, 14));
        lbl_sendObject.setForeground(new Color(255, 255, 255));
        lbl_sendObject.setText("Oggetto");

        //textArea_sendContent.setColumns(20);
        textArea_sendContent.setFont(new Font("Segoe UI", 0, 17));
        textArea_sendContent.setForeground(new Color(51, 51, 51));
        textArea_sendContent.setLineWrap(true);
        //textArea_sendContent.setRows(5);
        textArea_sendContent.setWrapStyleWord(true);
        textArea_sendContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea_sendContent.setCaretColor(new Color(51, 51, 51));

        lbl_sendTitle.setBackground(new Color(244, 246, 85));
        lbl_sendTitle.setFont(new Font("Dialog", 1, 18));
        lbl_sendTitle.setForeground(new Color(255, 255, 255));
        lbl_sendTitle.setText("Invia una email!");

        emailSent_ERROR.setBackground(new Color(52, 83, 106));
        emailSent_ERROR.setBorder(BorderFactory.createLineBorder(new Color(22, 42, 57)));
        emailSent_ERROR.setForeground(new Color(22, 42, 57));

        emailErrorContainer.setBackground(new Color(52, 107, 174));

        lbl_ERROR.setFont(new Font("Calibri", 1, 36));
        lbl_ERROR.setForeground(new Color(59, 0, 97));
        lbl_ERROR.setText("Message not delivered, saved in drafts");

        lbl_ERROR1.setFont(new Font("Calibri", 1, 24));
        lbl_ERROR1.setForeground(new Color(102, 153, 255));
        lbl_ERROR1.setText("Check receiver's address.");

        emailSent_OK.setBackground(new Color(52, 83, 106));
        emailSent_OK.setBorder(BorderFactory.createLineBorder(new Color(22, 42, 57)));
        emailSent_OK.setForeground(new Color(22, 42, 57));

        emailOkContainer.setBackground(new Color(52, 107, 174));

        lbl_OK.setFont(new Font("Calibri", 1, 36));
        lbl_OK.setForeground(new Color(59, 0, 97));
        lbl_OK.setText("Message delivered correctly");

        rightPanel.setVisible(false);
    }

    private void addComponents() {
        addEmptyPanel();
        addReadPanel();
        addWritePanel();
        addEmailErrorPanel();
        addEmailOkPanel();
    }

    private void addEmptyPanel() {
        GroupLayout emptyPanelLayout = new GroupLayout(emptyPanel);
        emptyPanel.setLayout(emptyPanelLayout);
        emptyPanelLayout.setHorizontalGroup(
                emptyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 889, Short.MAX_VALUE)
        );
        emptyPanelLayout.setVerticalGroup(
                emptyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 563, Short.MAX_VALUE)
        );
        rightPanel.add(emptyPanel, "card2");
    }

    private void addReadPanel() {

        javax.swing.GroupLayout readContainerLayout = new javax.swing.GroupLayout(readContainer);
        readContainer.setLayout(readContainerLayout);
        readContainerLayout.setHorizontalGroup(
                readContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(readContainerLayout.createSequentialGroup()
                                .addGroup(readContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(textArea_readContent, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(readContainerLayout.createSequentialGroup()
                                                .addComponent(button_forward, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(button_sendReplyAll, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(button_sendReply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 284, Short.MAX_VALUE))
        );
        readContainerLayout.setVerticalGroup(
                readContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(readContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(textArea_readContent, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(readContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(button_sendReply, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button_sendReplyAll, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button_forward, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(44, Short.MAX_VALUE))
        );

        GroupLayout readPanelLayout = new GroupLayout(readPanel);
        readPanel.setLayout(readPanelLayout);
        readPanelLayout.setHorizontalGroup(
                readPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(readPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(readPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_readObject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(readContainer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(readPanelLayout.createSequentialGroup()
                                                .addComponent(lbl_readTitle, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
        readPanelLayout.setVerticalGroup(
                readPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(readPanelLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(lbl_readTitle)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_readObject)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(readContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 114, Short.MAX_VALUE))
        );

        rightPanel.add(readPanel, "card2");
    }

    private void addWritePanel() {
        javax.swing.GroupLayout writePanelLayout = new javax.swing.GroupLayout(writePanel);
        writePanel.setLayout(writePanelLayout);
        writePanelLayout.setHorizontalGroup(
                writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(writePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, writePanelLayout.createSequentialGroup()
                                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(textArea_sendContent, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(writePanelLayout.createSequentialGroup()
                                                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lbl_sendTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(textField_sendTo))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lbl_sendObject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(textField_sendObject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(56, 56, 56))
                                        .addGroup(writePanelLayout.createSequentialGroup()
                                                .addComponent(button_send, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(writePanelLayout.createSequentialGroup()
                                                .addComponent(lbl_sendTitle,GroupLayout.PREFERRED_SIZE,300,GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );
        writePanelLayout.setVerticalGroup(
                writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(writePanelLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(lbl_sendTitle)
                                .addGap(18, 18, 18)
                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_sendTo)
                                        .addComponent(lbl_sendObject))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(writePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(textField_sendTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField_sendObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addComponent(textArea_sendContent, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_send, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(51, Short.MAX_VALUE))
        );
        rightPanel.add(writePanel, "card2");
    }

    private void addEmailErrorPanel() {

        GroupLayout emailErrorContainerLayout = new GroupLayout(emailErrorContainer);
        emailErrorContainer.setLayout(emailErrorContainerLayout);
        emailErrorContainerLayout.setHorizontalGroup(
                emailErrorContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailErrorContainerLayout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(emailErrorContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_ERROR, GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbl_ERROR1, GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );
        emailErrorContainerLayout.setVerticalGroup(
                emailErrorContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailErrorContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_ERROR)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_ERROR1)
                                .addContainerGap(14, Short.MAX_VALUE))
        );

        GroupLayout emailSent_ERRORLayout = new GroupLayout(emailSent_ERROR);
        emailSent_ERROR.setLayout(emailSent_ERRORLayout);
        emailSent_ERRORLayout.setHorizontalGroup(
                emailSent_ERRORLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailSent_ERRORLayout.createSequentialGroup()
                                .addContainerGap(294, Short.MAX_VALUE)
                                .addComponent(emailErrorContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        emailSent_ERRORLayout.setVerticalGroup(
                emailSent_ERRORLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailSent_ERRORLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(emailErrorContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(333, Short.MAX_VALUE))
        );

        rightPanel.add(emailSent_ERROR, "card2");
    }

    private void addEmailOkPanel() {

        GroupLayout emailOkContainerLayout = new GroupLayout(emailOkContainer);
        emailOkContainer.setLayout(emailOkContainerLayout);
        emailOkContainerLayout.setHorizontalGroup(
                emailOkContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, emailOkContainerLayout.createSequentialGroup()
                                .addContainerGap(172, Short.MAX_VALUE)
                                .addComponent(lbl_OK)
                                .addContainerGap())
        );
        emailOkContainerLayout.setVerticalGroup(
                emailOkContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailOkContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_OK)
                                .addContainerGap(50, Short.MAX_VALUE))
        );

        GroupLayout emailSent_OKLayout = new GroupLayout(emailSent_OK);
        emailSent_OK.setLayout(emailSent_OKLayout);
        emailSent_OKLayout.setHorizontalGroup(
                emailSent_OKLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailSent_OKLayout.createSequentialGroup()
                                .addContainerGap(294, Short.MAX_VALUE)
                                .addComponent(emailOkContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        emailSent_OKLayout.setVerticalGroup(
                emailSent_OKLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(emailSent_OKLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(emailOkContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(333, Short.MAX_VALUE))
        );

        rightPanel.add(emailSent_OK, "card2");
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public JPanel getEmptyPanel() {
        return emptyPanel;
    }

    public JPanel getReadPanel() {
        return readPanel;
    }

    public JPanel getEmailSent_ERROR() {
        return emailSent_ERROR;
    }

    public JPanel getEmailSent_OK() {
        return emailSent_OK;
    }

    public JPanel getWritePanel() {
        return writePanel;
    }

    public JButton getButton_sendReply() {
        return button_sendReply;
    }

    public JButton getButton_sendReplyAll() {
        return button_sendReplyAll;
    }

    public JButton getButton_send() {
        return button_send;
    }

    public JTextArea getTextArea_readContent() {
        return textArea_readContent;
    }

    public JLabel getLbl_readTitle() {
        return lbl_readTitle;
    }

    public JLabel getLbl_readObject() {
        return lbl_readObject;
    }

    public JTextArea getTextArea_sendContent() {
        return textArea_sendContent;
    }

    public JTextField getTextField_sendObject() {
        return textField_sendObject;
    }

    public JTextField getTextField_sendTo() {
        return textField_sendTo;
    }

}
