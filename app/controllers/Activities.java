package controllers;

import actions.Ajax;
import actions.Authorized;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Category;
import models.Comment;
import models.RateResource;
import models.Resource;
import play.libs.Json;
import play.mvc.Result;
import utilities.Dependencies;
import utilities.FormRequest;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.sort;
import static java.util.Collections.reverse;
import static play.mvc.Results.ok;
import static utilities.FormRequest.formBody;

public class Activities {
    @Ajax
    @Authorized({"admin"})
    public static Result getUserActivity() {
        FormRequest request = formBody();
        int pageNumber = request.getInt("pageNumber", 0);
        int pageSize = 5;

        List<Comment> comments = Dependencies.getCommentDao().findLatest((pageNumber + 1) * pageSize);
        List<RateResource> rates = Dependencies.getRateResourceDao().findLatest((pageNumber + 1) * pageSize);

        List<Activity> activities = activityFromComments(comments);
        activities.addAll(activityFromRates(rates));

        sort(activities);
        reverse(activities);

        JsonNode json = serializeActivities(activities);
        return ok(json);
    }

    private static JsonNode serializeActivities(List<Activity> activities) {
        ObjectNode rootJson = Json.newObject();
        rootJson.put("resultCount", activities.size());
        ArrayNode jsonList = rootJson.putArray("results");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.forLanguageTag("ir"));
        for (Activity activity : activities) {
            ObjectNode json = jsonList.addObject();
            json.put("id", activity.id);
            json.put("resourceId", activity.resourceId);
            json.put("user", activity.user);
            json.put("type", activity.type);
            json.put("value", activity.val);
            json.put("date", dateFormat.format(activity.date));
        }
        return rootJson;
    }

    private static List<Activity> activityFromComments(List<Comment> comments) {
        List<Activity> activities = new ArrayList<Activity>();
        for (Comment comment : comments) {
            Activity activity = new Activity();
            activity.id = comment.id;
            activity.date = comment.date;
            activity.user = comment.user.getFullName();
            activity.type = "comment";
            while (comment.parComment != null) {
                comment = comment.parComment;
            }
            activity.resourceId = comment.parResource.id;
            activities.add(activity);
        }
        return activities;
    }

    private static List<Activity> activityFromRates(List<RateResource> rates) {
        List<Activity> activities = new ArrayList<Activity>();
        for (RateResource rate : rates) {
            Activity activity = new Activity();
            activity.id = rate.id;
            activity.date = rate.date;
            activity.user = rate.user.getFullName();
            activity.type = "rate";
            activity.resourceId = rate.resource.id;
            activity.val = rate.rate;
            activities.add(activity);
        }
        return activities;
    }

    private static class Activity implements Comparable<Activity> {
        public int id;
        public int resourceId;
        public Date date;
        public String user;
        public String type;
        public int val;

        @Override
        public int compareTo(Activity o) {
            return date.compareTo(o.date);
        }
    }
}
