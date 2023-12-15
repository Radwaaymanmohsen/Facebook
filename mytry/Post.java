package mytry;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int postIdCounter = 1;
    private int postId;
    private String user;
    private String content;
    private String date;
    private LikeManager likeManager;
    private CommentManager commentManager;
    private List<Tag> tags;
    private PrivacyLevel privacyLevel;

    public Post(String user, String content, String date) {

        this.postId = postIdCounter++;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likeManager = new LikeManager(this);
        this.commentManager = new CommentManager();
        this.tags = new ArrayList<>();
        this.privacyLevel = PrivacyLevel.GENERAL;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public int getPostId() {
        return postId;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public static PrivacyLevel privacyLevelFromString(String privacyLevelString) {
        return PrivacyLevel.fromString(privacyLevelString);
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public LikeManager getLikeManager() {
        return likeManager;
    }

    public CommentManager getCommentManager() {
        return commentManager;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public List<Tag> getTags() {
        return tags;
    }

}