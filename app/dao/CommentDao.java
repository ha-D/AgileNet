package dao;

import models.Comment;
import models.Resource;
import models.User;

import java.util.List;

public interface CommentDao extends BaseDao<Comment> {
    Comment create(User owner, String body, Resource parResource);
    Comment create(User owner, String body, Comment parComment);
    void update(Comment comment);
    void upvote(Comment comment, User user);
    void removeVote(Comment comment, User user);
    List<Comment> findLatest(int limit);
    void edit(Comment comment, String body);
}
