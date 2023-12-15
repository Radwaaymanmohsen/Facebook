package mytry;

import java.util.List;

class LikeManager {
    private int likeCount;
    private boolean liked;
    private Post post;

    public LikeManager(Post post) {
        this.post = post;
    }

    public void like() {
        liked = !liked; // Toggle the liked status
        if (!liked) {
            likeCount++;
            liked = true;

        } else {
            likeCount = Math.max(0, likeCount - 1);
        }
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return liked;
    }
}

class CommentManager {
    private List<String> comments;

    public CommentManager() {
        this.comments = new java.util.ArrayList<>();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public List<String> getComments() {
        return comments;
    }
}
