package mytry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginSignupPanel extends JPanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField birthDateField;
    private JCheckBox femaleCheckBox;
    private List<String> accounts;
    private LoginSignupPage loginSignupPage;

    public LoginSignupPanel(LoginSignupPage loginSignupPage) {
        this.loginSignupPage = loginSignupPage;

        setLayout(new GridLayout(6, 1));

        
        JPanel emailPasswordPanel = new JPanel(new GridLayout(1, 4));
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(10, 10));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        emailPasswordPanel.add(emailLabel);
        emailPasswordPanel.add(emailField);
        emailPasswordPanel.add(passwordLabel);
        emailPasswordPanel.add(passwordField);

        
        JPanel namePanel = new JPanel(new GridLayout(1, 4));
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        namePanel.add(nameLabel);
        namePanel.add(nameField);

        
        JPanel birthDatePanel = new JPanel(new GridLayout(1, 4));
        JLabel birthDateLabel = new JLabel("Birth Date (YYYY-MM-DD):");
        birthDateField = new JTextField();

        birthDatePanel.add(birthDateLabel);
        birthDatePanel.add(birthDateField);

        JPanel genderPanel = new JPanel(new GridLayout(1, 4));
        JLabel genderLabel = new JLabel("Gender:");
        femaleCheckBox = new JCheckBox("Female");

        genderPanel.add(genderLabel);
        genderPanel.add(femaleCheckBox);


        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 30));

        JButton logInButton = new JButton("Log In");
        signUpButton.setPreferredSize(new Dimension(100, 30));

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String birthDate = birthDateField.getText();
                boolean isFemale = femaleCheckBox.isSelected();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || birthDate.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Please fill in all fields.");
                    return;
                }

                if (!AccountManager.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Invalid email format.");
                    return;
                }

                if (AccountManager.isAccountExists(email, loginSignupPage.getAccounts())) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Account already exists.");
                } else {
                    loginSignupPage.getAccounts()
                            .add(email + ":" + password + ":" + name + ":" + birthDate + ":" + isFemale);

                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Sign Up Successful");

                    loginSignupPage.updateAccountsFile();
                }
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Please enter both email and password.");
                    return;
                }

                if (!AccountManager.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Invalid email format.");
                    return;
                }

                if (AccountManager.isAccountValid(email, password, loginSignupPage.getAccounts())) {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this, "Login successful");
                    loginSignupPage.displayNewsFeedPage(email);
                } else {
                    JOptionPane.showMessageDialog(LoginSignupPanel.this,
                            "Invalid credentials or account doesn't exist.");
                }
            }
        });

        buttonsPanel.add(signUpButton);
        buttonsPanel.add(logInButton);

        add(emailPasswordPanel);
        add(namePanel);
        add(birthDatePanel);
        add(genderPanel);
        add(buttonsPanel);
    }
}
