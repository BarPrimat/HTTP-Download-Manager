package Download_Manager.UI;

import Download_Manager.Logic.StaticVariable;
import Download_Manager.Logic.WriterManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainMenu extends JFrame {
    private static final int  maxNumberOfThreads = 30;
    private static final int  minNumberOfThreads = 1;

    private JButton browseForDownloadButton;
    private JTextField downloadFormText;
    private JCheckBox checkBox;
    private JTextField downloadFromListText;
    private JButton downloadButton;
    private JTextField pathToDownloadText;
    private JSpinner numberOfTreadsSpinner;
    private JProgressBar progressBar;
    private JButton browseForListButton;
    private JPanel mainJPanel;

    public MainMenu() {
        String homePath = System.getProperty("user.home") + "/Downloads/";
        pathToDownloadText.setText(homePath);
        mainJPanel.setSize(640, 600); // 640, 200
        progressBar.setPreferredSize(new Dimension(200, 30));
        numberOfTreadsSpinner.setValue(8);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        browseForListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "List", "List");
                chooser.setFileFilter(filter);


                int ret = chooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION)
                    downloadFromListText.setText(chooser.getSelectedFile().getPath());
                //    downloadToCurrentPathText.setText(chooser.getSelectedFile());
                 //   open(chooser.getSelectedFile().getPath());
            }
        });
        browseForDownloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                // disable the "All files" option.
                chooser.setAcceptAllFileFilterUsed(false);
                int ret = chooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION)
                    pathToDownloadText.setText(chooser.getSelectedFile().getPath());
            }
        });
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadFromListText.setEnabled(!downloadFromListText.isEnabled());
                browseForListButton.setEnabled(!browseForListButton.isEnabled());
            }
        });
        numberOfTreadsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                numberOfTreadsSpinner.setValue(Math.max(minNumberOfThreads, Math.min(maxNumberOfThreads, (int) numberOfTreadsSpinner.getValue())));
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = new String[] {downloadFormText.getText(), numberOfTreadsSpinner.getValue().toString()};
                WriterManager.setPathToWriteTheFile(pathToDownloadText.getText() + "%");
                IdcDm.startToDownloader(input);
            }
        });
    }

    public void setProgressBar(int newValue){
        progressBar.setValue(newValue);
        progressBar.setString(String.valueOf(newValue));
    }

    public JPanel getMainJPanel() {
        return mainJPanel;
    }
}
