package ui;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class MainWindow extends JFrame {
    public MainWindow() {
        super("Quotation");
        setLayout(new GridLayout());
        JFilePicker filePicker = new JFilePicker("Pick an Excel file", "Browse...");
        filePicker.addFileTypeFilter(".xlsx", "Excel Files");
        add(filePicker);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 150);
        setResizable(false);
        setLocationRelativeTo(null);
    }
}
