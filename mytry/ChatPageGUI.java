package mytry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPageGUI extends JFrame {

    private JTextArea chatTextArea;
    private JTextField messageTextField;
    private JList<String> chatList;
    private String userEmail;

    private static final String ALL_CHATS_FILE = "AllChats.txt";

    public ChatPageGUI() {
        initializeFile();

        setTitle("Chat Page - " + userEmail);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);

        messageTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton logOutButton = new JButton("Log Out");

        String[] chatOptions = { "GroupChat1", "DirectChat_User1_User2", "GroupChat2" };
        chatList = new JList<>(chatOptions);
        JScrollPane chatListScrollPane = new JScrollPane(chatList);
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatListScrollPane, BorderLayout.WEST);
        mainPanel.add(chatScrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageTextField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(logOutButton, BorderLayout.WEST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedChat = chatList.getSelectedValue();
                if (selectedChat != null) {
                    ChatActions.sendMessage(selectedChat, "User", messageTextField.getText());
                    messageTextField.setText("");
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginSignupPage loginSignupPage = new LoginSignupPage();
                loginSignupPage.setVisible(true);
            }
        });

        chatList.addListSelectionListener(e -> {
            String selectedChat = chatList.getSelectedValue();
            if (selectedChat != null) {
                ChatActions.displayChat(selectedChat, chatTextArea);
            }
        });
    }

    private void initializeFile() {
        ChatInitializer.initializeFile();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatPageGUI().setVisible(true);
            }
        });
    }
}
