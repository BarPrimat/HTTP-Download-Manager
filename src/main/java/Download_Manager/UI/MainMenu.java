package Download_Manager.UI;

import Download_Manager.Logic.WriterManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    private static final int  maxNumberOfThreads = 30;
    private static final int  minNumberOfThreads = 1;
    private static final int  firstNumberOfThreads = 12;

    private JButton browseForDownloadButton;
    private JTextField downloadFormText;
    private JTextField downloadFromListText;
    private JButton downloadButton;
    private JTextField pathToDownloadText;
    private JSpinner numberOfTreadsSpinner;
    private JProgressBar progressBar;
    private JButton browseForListButton;
    private JPanel mainJPanel;
    private JButton finishButton;
    private JRadioButton URLListRadioButton;
    private JRadioButton URLRadioButton;
    private Thread thread;

    public MainMenu() {
        String homePath = System.getProperty("user.home") + "/Downloads/";
        pathToDownloadText.setText(homePath);
        mainJPanel.setSize(500, 280); // 640, 200
        progressBar.setPreferredSize(new Dimension(200, 30));
        numberOfTreadsSpinner.setValue(firstNumberOfThreads);
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
        numberOfTreadsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                numberOfTreadsSpinner.setValue(Math.max(minNumberOfThreads, Math.min(maxNumberOfThreads, (int) numberOfTreadsSpinner.getValue())));
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (downloadButton.getText().equals("Pause")) {
                    IdcDm.kill();
                    downloadButton.setText("Resume");
                } else {
                    finishButton.setEnabled(false);
                    if (downloadButton.getText().equals("Pause")) progressBar.setValue(0);
                    createDownload();
                }
            }
        });
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        URLRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(URLRadioButton.isSelected())
                    changeWayToDownload(false);
            }
        });
        URLListRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (URLListRadioButton.isSelected()) {
                    changeWayToDownload(true);
                }
            }
        });
    }

    private void changeWayToDownload(Boolean newValue){
        downloadFromListText.setEnabled(newValue);
        downloadFormText.setEnabled(!newValue);
        browseForListButton.setEnabled(newValue);
    }

    private void createDownload() {
        String urlOrList = URLListRadioButton.isSelected() ? downloadFromListText.getText() : downloadFormText.getText();
        String[] input = new String[]{urlOrList, numberOfTreadsSpinner.getValue().toString()};
        WriterManager.setPathToWriteTheFile(pathToDownloadText.getText());
        Display.print(pathToDownloadText.getText());
        downloadButton.setText("Pause");
        disableAll();

        thread = new Thread() {
            @Override
            public void run() {
                IdcDm.startToDownloader(input);
            }
        };
        thread.start();
    }

    public void setProgressBar(int newValue){
        progressBar.setValue(newValue);
        if(newValue == 100) {
            finishButton.setEnabled(true);
            downloadButton.setText("Download");
            enableAll();
        }
        progressBar.setString(String.valueOf(newValue) + '%');
    }

    private void enableAll() {
        browseForDownloadButton.setEnabled(true);
        downloadFormText.setEnabled(true);
        pathToDownloadText.setEnabled(true);
        numberOfTreadsSpinner.setEnabled(true);
        URLRadioButton.setEnabled(true);
        URLListRadioButton.setEnabled(true);
    }

    private void disableAll() {
        downloadFromListText.setEnabled(false);
        browseForListButton.setEnabled(false);
        browseForDownloadButton.setEnabled(false);
        downloadFormText.setEnabled(false);
        pathToDownloadText.setEnabled(false);
        numberOfTreadsSpinner.setEnabled(false);
        browseForListButton.setEnabled(false);
        if(URLRadioButton.isSelected()) changeWayToDownload(false);
        else changeWayToDownload(true);
    }

    public void someProblemHappened(String textToPrint){
        if(!textToPrint.equals("Pause in purpose")){
            JOptionPane.showMessageDialog (null, textToPrint, "Error", JOptionPane.ERROR_MESSAGE);
            downloadButton.setText("Download");
        }
        enableAll();
    }

    public JPanel getMainJPanel() {
        return mainJPanel;
    }
}
