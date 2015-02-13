package controllers;

import actions.Ajax;
import actions.Authorized;
import models.*;
import utilities.Dependencies;
import play.data.Form;
import play.mvc.Result;

import static play.mvc.Controller.session;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import utilities.FormRequest;

import static play.mvc.Results.*;
import static utilities.FormRequest.formBody;
import static utilities.UserUtils.sessionUser;

/**
 * Controller for viewing, adding, modifying and rating comments
 */
public class Comments {
    /**
     * Upvote or Downvote a comment
     * POST rate: 1 if action is an upvote, -1 if downvote
     * POST comment: id of comment to vote on
     *
     * Ajax Method
     * Authorization: User
     */
    @Ajax
    @Authorized({})
    public static Result rateComment() {
        User user = sessionUser();
        FormRequest request = formBody();
        int rate = request.getInt("rate");
        int commentId = request.getInt("comment");
        Comment comment = Dependencies.getCommentDao().findById(commentId);
        switch (rate){
            case 1:
                Dependencies.getCommentDao().upvote(comment, user);
                break;
            case -1:
                Dependencies.getCommentDao().removeVote(comment, user);
                break;
        }
        ObjectNode rootJson = Json.newObject();
        rootJson.put("rate", comment.getRate());
        return ok(rootJson);
    }

    /**
     * Add comment to resource
     * POST rate: 1 if action is an upvote, -1 if downvote
     * POST comment: id of comment to vote on
     *
     * Ajax Method
     * Authorization: User
     */
    @Ajax
    @Authorized({})
    public static Result addComment(){
        User user = sessionUser();
        FormRequest request = formBody();
        String type = request.get("type");
        int id = request.getInt("id");
        String body = request.get("body");

        Comment comment = null;
        ObjectNode rootJson = Json.newObject();
        switch (type){
            case "on resource":
                Resource parResource = Dependencies.getResourceDao().findById(id);
                comment = Dependencies.getCommentDao().create(user, body, parResource);
                rootJson.put("id", comment.id);
                break;
            case "on comment":
                Comment parComment = Dependencies.getCommentDao().findById(id);
                comment = Dependencies.getCommentDao().create(user, body, parComment);
                rootJson.put("id", comment.id);
                break;
        }
        return ok(rootJson);
    }

    /**
     * Filter a comment
     * POST id: id of comment to filter
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Ajax
    @Authorized({"admin"})
    public static Result filterComment(){
        User user = sessionUser();
        FormRequest request = formBody();
        int id = request.getInt("id");
        Comment comment = Dependencies.getCommentDao().findById(id);
        comment.filtered = true;
        System.out.print("filtered");
        System.out.println(id);
        Dependencies.getCommentDao().update(comment);
        return ok();
    }

    /**
     * Unfilter a comment
     * POST id: id of comment to filter
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Authorized({"admin"})
    public static Result undoFilterComment(){
        User user = sessionUser();
        FormRequest request = formBody();
        int id = request.getInt("id");
        Comment comment = Dependencies.getCommentDao().findById(id);
        comment.filtered = false;
        Dependencies.getCommentDao().update(comment);
        return ok();
    }
}
