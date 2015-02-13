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

    @OneToMany(mappedBy = "parComment", cascade = CascadeType.ALL)
    public List<Comment> comments;

    public boolean filtered = false;

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

    /**
     * @return The resource the comment belongs to if there is one, null otherwise
     */
    public Resource getParResource() {
        return Dependencies.getResourceDao().findById(parResource.id);
    }

    /**
     * @return The comments parent if it is a child of another comment, null otherwise
     */
    public Comment getParComment() {
        return Dependencies.getCommentDao().findById(parComment.id);
    }

    /**
     * @return A list of the comments children
     */
    public List<Comment> getComments() {
        List<Comment> ret = new ArrayList<>();
        for(Comment comment: comments) {
            ret.add(Dependencies.getCommentDao().findById(comment.id));
        }
        return ret;
    }

    /**
     * @return The number of upvotes given to the comment
     */
    public List<User> getUpvotes() {
        List<User> ret = new ArrayList<>();
        for(User user: upvotes)
            ret.add(Dependencies.getUserDao().findById(user.id));
        return ret;
    }

    /**
     * @return The number of upvotes given to the comment
     */
    public int getRate(){
        return upvotes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comment) {
            Comment comment = (Comment)o;
            return comment.id == id;
        }
        return false;
    }

}
