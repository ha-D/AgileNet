package dao.impl;

import com.avaje.ebean.Ebean;
import dao.CommentDao;
import models.*;
import play.db.ebean.Model;
import utilities.Dependencies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDaoImpl implements dao.CommentDao {
    public Model.Finder<Integer,Comment> find = new Model.Finder<Integer,Comment>(
            Integer.class, Comment.class
    );

    @Override
    public Comment create(User user, String body, Resource parResource) {
        Comment comment = new Comment();
        comment.comments = new ArrayList<>();
        comment.user = user;
        comment.body = body;
        comment.date = new Date();
        comment.parResource = parResource;
        parResource.comments.add(comment);
        comment.save();
        Dependencies.getResourceDao().update(parResource);
        return comment;
    }

    @Override
    public Comment create(User user, String body, Comment parComment) {
        Comment comment = new Comment();
        comment.comments = new ArrayList<Comment>();
        comment.user = user;
        comment.body = body;
        comment.date = new Date();
        comment.parComment = parComment;
        parComment.comments.add(comment);
        comment.save();
        update(parComment);
        return comment;
    }

    @Override
    public void update(Comment comment){
        Ebean.save(comment);
    }

    @Override
    public void upvote(Comment comment, User user) {
        if(!comment.upvotes.contains(user))
            comment.upvotes.add(user);
        update(comment);
    }

    @Override
    public void removeVote(Comment comment, User user) {
        comment.upvotes.remove(user);
        update(comment);
    }

    @Override
    public List<Comment> findLatest(int limit) {
        return find.setMaxRows(limit).findList();
    }

    @Override
    public void edit(Comment comment, String body) {
        comment.body = body;
        Ebean.update(comment);
        System.out.println(comment.body);
        System.out.println(Dependencies.getCommentDao().findById(1).body);
    }

    @Override
    public Comment findById(int id) {
        return find.byId(id);
    }

    @Override
    public List<Comment> findAll() {
        return find.all();
    }

    @Override
    public Comment create(Comment object) {
        return null;
    }


}
