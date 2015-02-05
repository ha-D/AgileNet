package models;

import dao.CommentDao;
import play.data.validation.Constraints;
import utilities.Dependencies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Comment extends BaseModel<CommentDao> {
    @Id
    public int id;

    @Constraints.Required
    @ManyToOne
    @Column(nullable = false)
    public User user;

    @Column(nullable = false)
    public String body;

    @Column(nullable = false)
    public Date date;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> comments;

    public Resource getParResource() {
        return Dependencies.getResourceDao().findById(parResource.id);
    }

    public Comment getParComment() {
        return Dependencies.getCommentDao().findById(parComment.id);
    }

    public List<Comment> getComments() {
        List<Comment> ret = new ArrayList<>();
        for(Comment comment: comments)
            ret.add(Dependencies.getCommentDao().findById(comment.id));
        return ret;
    }

    public List<User> getUpvotes() {
        List<User> ret = new ArrayList<>();
        for(User user: upvotes)
            ret.add(Dependencies.getUserDao().findById(user.id));
        return ret;
    }

    @ManyToOne
    public Comment parComment;

    @ManyToOne
    public Resource parResource;

    @ManyToMany()
    public List<User> upvotes;

    public Comment(){
        comments = new ArrayList<Comment>();
        upvotes = new ArrayList<User>();
    }

    public int getRate(){
        return upvotes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comment) {
            Comment comment = (Comment)o;
//            return (r.id == id);
            return comment.id==id;
        }
        return false;
    }

}
