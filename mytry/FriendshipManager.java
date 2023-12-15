package mytry;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.nio.file.Files;

public class FriendshipManager {
    private Map<String, FriendshipType> friendships;
    private final String FRIENDSHIPS_FILE = "friendships.txt";

    public FriendshipManager() {
        this.friendships = new HashMap<>();
        loadFriendshipsFromFile();
    }

    public void addFriend(String friendEmail, FriendshipType friendshipType) {
        friendships.put(friendEmail, friendshipType);
        saveFriendshipsToFile();
    }

    public FriendshipType getFriendshipType(String friendEmail) {
        return friendships.getOrDefault(friendEmail, FriendshipType.GENERAL);
    }

    public boolean canSeePost(String friendEmail, Post post) {
        FriendshipType friendshipType = getFriendshipType(friendEmail);
        PrivacyLevel postPrivacy = post.getPrivacyLevel();

        return postPrivacy == PrivacyLevel.GENERAL
                || (postPrivacy == PrivacyLevel.RESTRICTED && friendshipType == FriendshipType.GENERAL);
    }

    public Set<String> getAllFriends() {
        return friendships.keySet();
    }

    private void saveFriendshipsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FRIENDSHIPS_FILE))) {
            for (Map.Entry<String, FriendshipType> entry : friendships.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue().name());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFriendshipsFromFile() {
        try {
            File file = new File(FRIENDSHIPS_FILE);
            if (file.exists()) {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        String friendEmail = parts[0];
                        FriendshipType friendshipType = FriendshipType.valueOf(parts[1]);
                        friendships.put(friendEmail, friendshipType);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum FriendshipType {
        GENERAL,
        RESTRICTED
        // Add more friendship types if needed
    }
}
