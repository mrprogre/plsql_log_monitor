import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Dialogs extends JDialog implements KeyListener {
    public static JTextArea favTabsList;

    public Dialogs(String p_file) {
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(255, 235, 235));
        this.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.setBounds(600, 200, 350, 300);
        this.getContentPane().setLayout(new BorderLayout(0, 0));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);

        if (p_file.equals("favTabListDlg")) {
            this.setTitle("Favorite tables");
            this.setLocationRelativeTo(Gui.showFavoriteTabBtn);
            final JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 27, 324, 233);
            this.getContentPane().add(scrollPane);

            favTabsList = new JTextArea();
            favTabsList.setFont(new Font("Dialog", Font.PLAIN, 13));
            favTabsList.setTabSize(10);
            favTabsList.setEditable(false);
            favTabsList.setLineWrap(true);
            favTabsList.setWrapStyleWord(true);
            favTabsList.setBounds(12, 27, 22, 233);
            scrollPane.setViewportView(favTabsList);
            Common.setDialogFavTabText("fav");
        } else if (p_file.equals("logDlg")){
            this.setTitle("Log");
            this.setLocationRelativeTo(Gui.logBtn);
            final JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 27, 324, 233);
            this.getContentPane().add(scrollPane);

            favTabsList = new JTextArea();
            favTabsList.setFont(new Font("Dialog", Font.PLAIN, 13));
            favTabsList.setTabSize(10);
            favTabsList.setEditable(false);
            favTabsList.setLineWrap(true);
            favTabsList.setWrapStyleWord(true);
            favTabsList.setBounds(12, 27, 22, 233);
            scrollPane.setViewportView(favTabsList);
            Common.setDialogFavTabText("log");
        }
        // делаем фокус на окно, чтобы работал захват клавиш
        this.requestFocusInWindow();
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Закрываем диалоговые окна клавишей ESC
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setVisible(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
