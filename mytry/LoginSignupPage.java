package mytry;

import javax.swing.*;
import java.util.List;

public class LoginSignupPage extends JFrame {
    private List<String> accounts;
    private NewsFeedApp newsFeedApp;
    private LoginSignupPanel loginSignupPanel;

    public LoginSignupPage() {
        setTitle("Login and Signup Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        accounts = AccountManager.readAccountsFromFile();

        loginSignupPanel = new LoginSignupPanel(this);
        add(loginSignupPanel);
    }

    public void displayNewsFeedPage(String userEmail) {
        loginSignupPanel.setVisible(false);

        newsFeedApp = new NewsFeedApp(userEmail);
        SwingUtilities.invokeLater(() -> newsFeedApp.createAndShowGUI());

        newsFeedApp.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginSignupPanel.setVisible(true);
            }
        });
    }

    public void updateAccountsFile() {
        AccountManager.updateAccountsFile(accounts);
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginSignupPage().setVisible(true));
    }
}
