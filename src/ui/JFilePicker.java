package ui;

import main.Quotation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class JFilePicker extends JPanel implements ActionListener {

    private JTextField mFilePath;
    private JTextField mSheets;
    private JFileChooser mFileChooser;

    JFilePicker(String textFieldLabel, String buttonLabel) {
        mFileChooser = new JFileChooser();
        setLayout(new FlowLayout());
        JLabel label = new JLabel(textFieldLabel);
        JLabel label1 = new JLabel("Enter sheet numbers seperated by a space");
        mFilePath = new JTextField(20);
        mSheets = new JTextField(30);
        JButton fileButton = new JButton(buttonLabel);
        fileButton.setActionCommand("File");
        JButton outputButton = new JButton("Generate Quotation");
        fileButton.addActionListener(this);
        outputButton.addActionListener(this);
        add(label);
        add(mFilePath);
        add(fileButton);
        add(label1);
        add(mSheets);
        add(outputButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getActionCommand().equals("File")) {
            if (mFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                mFilePath.setText(mFileChooser.getSelectedFile().getAbsolutePath());
            }
        } else {
            try {
                Quotation quotation = new Quotation(mFilePath.getText(), mSheets.getText());
                quotation.generateQuotation();
                JOptionPane.showMessageDialog(this, quotation.getMessage());
            } catch (IOException e) { JOptionPane.showMessageDialog(this, "Oops! Some error occured."); }
        }
    }

    void addFileTypeFilter(String extension, String description) {
        FileTypeFilter filter = new FileTypeFilter(extension, description);
        mFileChooser.addChoosableFileFilter(filter);
    }
}
