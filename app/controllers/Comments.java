package controllers;

import actions.Authorized;
import models.*;
import utilities.Dependencies;
import org.apache.commons.io.FileUtils;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;

import static play.mvc.Controller.request;
import static play.mvc.Controller.session;

import actions.Ajax;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ResourceSearchCriteria;
import play.libs.Json;
import utilities.FormRequest;

import java.util.List;

import static play.mvc.Results.*;
import static utilities.FormRequest.formBody;


public class Comments {

    @Authorized({})
    public static Result rateComment() {
        //form with two fields: rate and resourceId

        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        int rate = Integer.parseInt(Form.form().bindFromRequest().get("rate")); //+1 upvote, -1 remove vote
        int commentId = Integer.parseInt(Form.form().bindFromRequest().get("comment"));
        Comment comment = Dependencies.getCommentDao().findById(commentId);
        switch (rate){
            case 1:
                Dependencies.getCommentDao().upvote(comment, user);
                break;
            case -1:
                Dependencies.getCommentDao().removeVote(comment, user);
                break;
        }
        return ok();
    }

    @Authorized({})
    public static Result addComment(){
        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        String type = Form.form().bindFromRequest().get("type");
        int id = Integer.parseInt(Form.form().bindFromRequest().get("id"));
        String body = Form.form().bindFromRequest().get("body");
        Comment comment;
        switch (type){
            case "on resource":
                Resource parResource = Dependencies.getResourceDao().findById(id);
                comment = Dependencies.getCommentDao().create(user, body, parResource);
                parResource.getComments().add(comment);
                Dependencies.getResourceDao().update(parResource);
                break;
            case "on comment":
                Comment parComment = Dependencies.getCommentDao().findById(id);
                comment = Dependencies.getCommentDao().create(user, body, parComment);
                comment.getComments().add(comment);
                Dependencies.getCommentDao().update(parComment);
                break;

        }
        return ok();
    }
    @Authorized({"admin"})
    public static Result filterComment(){
        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        int id = Integer.parseInt(Form.form().bindFromRequest().get("id"));
        Comment comment = Dependencies.getCommentDao().findById(id);
        comment.filtered = true;
        Dependencies.getCommentDao().update(comment);
        return ok();
    }

    @Authorized({"admin"})
    public static Result undoFilterComment(){
        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        int id = Integer.parseInt(Form.form().bindFromRequest().get("id"));
        Comment comment = Dependencies.getCommentDao().findById(id);
        comment.filtered = false;
        Dependencies.getCommentDao().update(comment);
        return ok();
    }
}
