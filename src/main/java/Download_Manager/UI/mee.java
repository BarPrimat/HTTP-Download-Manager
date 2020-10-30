package Download_Manager.UI;//package net.codejava.swing.upload.ftp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// import net.codejava.swing.JFilePicker;

/**
  * A Swing application that uploads files to a FTP server.
  * @author www.codejava.net
  *
  */
public class mee extends JFrame implements
    PropertyChangeListener {

    private JLabel labelHost = new JLabel("Host:");
    private JLabel labelPort = new JLabel("Port:");
    private JLabel labelUsername = new JLabel("Username:");
    private JLabel labelPassword = new JLabel("Password:");
    private JLabel labelUploadPath = new JLabel("Upload path:");

    private JTextField fieldHost = new JTextField(40);
    private JTextField fieldPort = new JTextField(5);
    private JTextField fieldUsername = new JTextField(30);
    private JPasswordField fieldPassword = new JPasswordField(30);
    private JTextField fieldUploadPath = new JTextField(30);

    //private JFilePicker filePicker = new JFilePicker("Choose a file: ",
         //   "Browse");

    private JButton buttonUpload = new JButton("Upload");

    private JLabel labelProgress = new JLabel("Progress:");
    private JProgressBar progressBar = new JProgressBar(0, 100);

    public mee() {
        super("Swing File Upload to FTP server");

        // set up layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // set up components
        // filePicker.setMode(JFilePicker.MODE_OPEN);

        buttonUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonUploadActionPerformed(event);
            }
        });

        progressBar.setPreferredSize(new Dimension(200, 30));
        progressBar.setStringPainted(true);

        // add components to the frame
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(labelHost, constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        add(fieldHost, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(labelPort, constraints);

        constraints.gridx = 1;
        add(fieldPort, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(labelUsername, constraints);

        constraints.gridx = 1;
        add(fieldUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(labelPassword, constraints);

        constraints.gridx = 1;
        add(fieldPassword, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(labelUploadPath, constraints);

        constraints.gridx = 1;
        add(fieldUploadPath, constraints);

        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;

      //  add(filePicker, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        add(buttonUpload, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(labelProgress, constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, constraints);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * handle click event of the Upload button
     */
    private void buttonUploadActionPerformed(ActionEvent event) {
        String host = fieldHost.getText();
        int port = Integer.parseInt(fieldPort.getText());
        String username = fieldUsername.getText();
        String password = new String(fieldPassword.getPassword());
        String uploadPath = fieldUploadPath.getText();
    //    String filePath = filePicker.getSelectedFilePath();

       // File uploadFile = new File(filePath);
        progressBar.setValue(0);
       // UploadTask task = new UploadTask(host, port, username, password,
     //           uploadPath, uploadFile);
     //   task.addPropertyChangeListener(this);
       // task.execute();
    }

    /**
     * Update the progress bar's state whenever the progress of upload changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }
}