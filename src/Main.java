import ui.MainWindow;

import javax.swing.UIManager;

/**
 * @author Sayantan Majumdar (monti.majumdar@gmail.com)
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {System.out.println("Error in loading look-and-feel ...");}
        new MainWindow().setVisible(true);
    }
}