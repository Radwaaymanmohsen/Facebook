package mytry;

import java.io.File;
import java.io.IOException;

public class ChatInitializer {

    private static final String ALL_CHATS_FILE = "AllChats.txt";

    public static void initializeFile() {
        try {
            File file = new File(ALL_CHATS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing chat data file: " + e.getMessage());
        }
    }
}
