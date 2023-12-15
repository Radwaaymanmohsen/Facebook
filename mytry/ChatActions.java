package mytry;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class ChatActions {

    private static final String ALL_CHATS_FILE = "AllChats.txt";

    public static void displayChat(String chatName, JTextArea chatTextArea) {
        chatTextArea.setText("");

        try (BufferedReader reader = new BufferedReader(new FileReader(ALL_CHATS_FILE))) {
            String line;
            boolean isInSelectedChat = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("[" + chatName + "]")) {
                    isInSelectedChat = true;
                    continue;
                }

                if (line.startsWith("[") && line.endsWith("]")) {
                    isInSelectedChat = false;
                }

                if (isInSelectedChat) {
                    chatTextArea.append(line + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading chat data file: " + e.getMessage());
        }
    }

    public static void sendMessage(String chatName, String sender, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        displayMessage(chatName, "[" + timestamp + "] " + sender + ": " + message);
    }

    private static void displayMessage(String chatName, String message) {
        JTextArea chatTextArea = new JTextArea();
        chatTextArea.append(message + "\n");
        saveMessageToChatFile(chatName, message);
    }

    private static void saveMessageToChatFile(String chatName, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ALL_CHATS_FILE, true))) {
            writer.write("[" + chatName + "]\n");
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to the chat data file: " + e.getMessage());
        }
    }
}
