package mytry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mytry.PrivacyLevel;

public class NewsFeedApp extends JFrame {
    private String userEmail;
    private JFrame frame;
    private DefaultListModel<Post> postListModel;
    private JList<Post> postList;
    private JTextField postTextField;
    private LikeManager likeManager;
    private CommentManager commentManager;
    private Post currentPost;
    private FriendshipManager friendshipManager;
    private PrivacyLevel privacyLevel;

    public NewsFeedApp(String userEmail) {
        this.userEmail = userEmail;
        this.friendshipManager = new FriendshipManager();
        friendshipManager.loadFriendshipsFromFile();
    }

    public enum PrivacyLevel {
        GENERAL, 
        RESTRICTED 
    }

    void createAndShowGUI() {
        frame = new JFrame("News Feed");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        createSidebar();
        createMainPanel();

        frame.setVisible(true);
    }

    private void createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, 600));

        JButton createPostButton = new JButton("Create Post");
        createPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreatePostDialog();
            }
        });

        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddFriendDialog();
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginSignupPage();
            }
        });

        JButton messageButton = new JButton("Message");
        messageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChatPage();
            }
        });

        sidebarPanel.add(createPostButton);
        sidebarPanel.add(addFriendButton);
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(messageButton);

        frame.add(sidebarPanel, BorderLayout.WEST);
    }

    private String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void showChatPage() {
        frame.setVisible(false);

        ChatPageGUI chatPage = new ChatPageGUI();
        chatPage.setTitle("Chat Page - " + userEmail);
        chatPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatPage.setSize(1000, 700);
        chatPage.setLocationRelativeTo(null);
        chatPage.setVisible(true);

        chatPage.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.setVisible(true);
            }
        });
    }

    private void showAddFriendDialog() {
        JPanel dialogPanel = new JPanel(new GridLayout(3, 2));

        JTextField emailField = new JTextField();
        JCheckBox generalFriendshipCheckBox = new JCheckBox("General Friendship");

        dialogPanel.add(new JLabel("Friend's Email:"));
        dialogPanel.add(emailField);
        dialogPanel.add(new JLabel("Friendship Type:"));
        dialogPanel.add(generalFriendshipCheckBox);

        int result = JOptionPane.showConfirmDialog(
                frame,
                dialogPanel,
                "Add Friend",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String friendEmail = emailField.getText();
            boolean generalFriendship = generalFriendshipCheckBox.isSelected();

            System.out.println("Friend Email: " + friendEmail);
            System.out.println("General Friendship: " + generalFriendship);
        }
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        postListModel = new DefaultListModel<>();
        postList = new JList<>(postListModel);
        postList.setCellRenderer(new PostCellRenderer());
        JScrollPane scrollPane = new JScrollPane(postList);
        mainPanel.add(scrollPane);

        frame.add(mainPanel, BorderLayout.CENTER);

        loadPostsFromFile();
        commentManager = new CommentManager();
    }

    private void showCreatePostDialog() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        postTextField = new JTextField();
        JButton postButton = new JButton("Post");

        JCheckBox generalPrivacyCheckBox = new JCheckBox("General Privacy");

        JTextField tagField = new JTextField();

        JPanel privacyPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        privacyPanel.add(new JLabel("Privacy:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        privacyPanel.add(generalPrivacyCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        privacyPanel.add(new JLabel("Tags:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        privacyPanel.add(tagField, gbc);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPost(generalPrivacyCheckBox.isSelected(), tagField.getText());
                JOptionPane.getRootFrame().dispose();
            }
        });

        dialogPanel.add(postTextField, BorderLayout.CENTER);
        dialogPanel.add(privacyPanel, BorderLayout.NORTH);
        dialogPanel.add(postButton, BorderLayout.SOUTH);

        JOptionPane.showOptionDialog(
                frame,
                dialogPanel,
                "Create Post",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] {},
                null);
        currentPost = null;
    }

    private void createPost(boolean generalPrivacy, String tagString) {
        String content = postTextField.getText();
        if (!content.isEmpty()) {
            Post newPost = new Post(userEmail, content, getCurrentDate());

          
            String[] tags = tagString.split("\\s*,\\s*");
            for (String tag : tags) {
                if (!tag.isEmpty()) {
                    Tag userTag = new Tag(tag);
                    newPost.addTag(userTag);
                }
            }

            postListModel.addElement(newPost);

            if (generalPrivacy) {
                System.out.println("Post is set to General Privacy");
            } else {
                System.out.println("Post is set to Restricted Privacy");
            }

            savePostsToFile();
            postTextField.setText("");
            likeManager = newPost.getLikeManager();
        }
    }

    private void savePostsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("posts.txt", true))) {
            Post newPost = postListModel.getElementAt(postListModel.size() - 1);
            writer.write(newPost.getPostId() + "|" +
                    newPost.getUser() + "|" +
                    newPost.getContent() + "|" +
                    newPost.getDate() + "|" +
                    newPost.getPrivacyLevel().getValue() + "|" +
                    getTagsAsString(newPost.getTags()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTagsAsString(List<Tag> tags) {
        StringBuilder tagsString = new StringBuilder();
        for (Tag tag : tags) {
            tagsString.append(tag.getName()).append(",");
        }
        
        if (tagsString.length() > 0) {
            tagsString.setLength(tagsString.length() - 1);
        }
        return tagsString.toString();
    }

    private void loadPostsFromFile() {
        File file = new File("posts.txt");
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        Post post = new Post(parts[0], parts[1], parts[2]);
                        post.setPrivacyLevel(Post.privacyLevelFromString(parts[3]));

                        // Process tags
                        String[] tags = parts[4].split(",");
                        for (String tag : tags) {
                            if (!tag.isEmpty()) {
                                Tag userTag = new Tag(tag);
                                post.addTag(userTag);
                            }
                        }

                        postListModel.addElement(post);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            if (value instanceof Post) {
                Post post = (Post) value;
                JPanel panel = new JPanel(new BorderLayout());

                JLabel postLabel = new JLabel("<html><b>" + post.getUser() + "</b> - " + post.getDate() + "<br>"
                        + post.getContent() + "<br>Tags: " + getTagsAsString(post.getTags()) + "</html>");

                panel.add(postLabel, BorderLayout.CENTER);

                // Display tagged users' emails
                if (!post.getTags().isEmpty()) {
                    JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    tagPanel.add(new JLabel("Tags: "));
                    for (Tag tag : post.getTags()) {
                        tagPanel.add(new JLabel(tag.getName() + " "));
                    }
                    panel.add(tagPanel, BorderLayout.SOUTH);
                }

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                JButton likeButton = new JButton("Like");
                JLabel likeCounterLabel = new JLabel("Likes: " + post.getLikeManager().getLikeCount());

                JButton commentButton = new JButton("Comment");
                JLabel commentCounterLabel = new JLabel("Comments: " + post.getCommentManager().getComments().size());

                likeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(() -> {
                            LikeManager likeManager = post.getLikeManager();
                            likeManager.like();
                            likeCounterLabel.setText("Likes: " + likeManager.getLikeCount());

                            if (likeManager.isLiked()) {
                                JOptionPane.showMessageDialog(frame, "Liked post: " + post.getContent());
                            } else {
                                JOptionPane.showMessageDialog(frame, "Unliked post: " + post.getContent());
                            }
                        });
                    }
                });

                commentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                            @Override
                            protected String doInBackground() throws Exception {
                                return JOptionPane.showInputDialog(frame, "Enter your comment:");
                            }

                            @Override
                            protected void done() {
                                try {
                                    String newComment = get();
                                    if (newComment != null && !newComment.isEmpty()) {
                                        post.getCommentManager().addComment(newComment);
                                        commentCounterLabel
                                                .setText("Comments: " + post.getCommentManager().getComments().size());
                                        JOptionPane.showMessageDialog(frame, "Comment added: " + newComment);
                                    }
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                } catch (ExecutionException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };
                        worker.execute();
                    }
                });

                buttonPanel.add(likeButton);
                buttonPanel.add(likeCounterLabel);
                buttonPanel.add(commentButton);
                buttonPanel.add(commentCounterLabel);

                panel.add(buttonPanel, BorderLayout.SOUTH);

                return panel;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }

        private String getTagsAsString(List<Tag> tags) {
            StringBuilder tagsString = new StringBuilder();
            for (Tag tag : tags) {
                tagsString.append(tag.getName()).append(", ");
            }
           
            if (tagsString.length() > 2) {
                tagsString.setLength(tagsString.length() - 2);
            }
            return tagsString.toString();
        }
    }

    private void showLoginSignupPage() {
        frame.dispose();
        LoginSignupPage loginSignupPage = new LoginSignupPage();
        loginSignupPage.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewsFeedApp("user@example.com").createAndShowGUI());
    }
}
