package testutils;

import controllers.routes;
import models.*;
import play.mvc.HandlerRef;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.WithApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.*;
import static play.test.Helpers.fakeRequest;

public class BaseTest extends WithApplication {

    @Override
    protected FakeApplication provideFakeApplication() {
        Map<String, String> settings = inMemoryDatabase();
        settings = new HashMap<String, String>(settings);
        settings.put("ebean.default", "models.*");
        return fakeApplication(settings, fakeGlobal());
    }

    protected Result makeRequest(HandlerRef action, Map<String, String> params, String userEmail) {
        FakeRequest fakeRequest = fakeRequest();

        if (params != null) {
            fakeRequest = fakeRequest.withFormUrlEncodedBody(params);
        }

        if (userEmail != null) {
            fakeRequest = fakeRequest.withSession("email", userEmail);
        }

        return callAction(action, fakeRequest);
    }

    protected Result makeRequest(HandlerRef action, Map<String, String> params) {
        return makeRequest(action, params, null);
    }

    protected Result makeRequest(HandlerRef action, String userEmail) {
        return makeRequest(action, null, userEmail);
    }

    protected Result makeRequest(HandlerRef action) {
        return makeRequest(action, null, null);
    }

    protected User newUser(int id, String email, String password) {
        User user = new User();
        user.id = id;
        user.email = email;
        user.setPassword(password);
        return user;
    }

    protected Category newCategory(String name) {
        Category category = new Category();
        category.name = name;
        return category;
    }

    protected Comment newComment(int id) {
        return newComment(id, null);
    }

    protected Comment newComment(int id, User user) {
        Comment comment = new Comment();
        comment.id = id;
        comment.user = user;
        return comment;
    }

    protected Resource newResource(int id, String name) {
        Resource resource = new Resource();
        resource.name = name;
        resource.id = id;
        return resource;
    }

    protected Resource newResource(String name, ResourceType resourceType, Category... categories) {
        Resource resource = new Resource();
        resource.name = name;
        resource.resourceType = resourceType;
        for (Category category : categories) {
            resource.categories.add(category);
        }

        return resource;
    }

    protected Resource newResource(String name, ResourceType resourceType, double rating, Category... categories) {
        Resource resource = newResource(name, resourceType, categories);
        resource.rating = rating;
        return resource;
    }


    protected RateResource newRateResource(Resource resource, User user, int rate) {
        RateResource rateResource = new RateResource();
        rateResource.date = new Date();
        rateResource.resource = resource;
        rateResource.user = user;
        rateResource.rate = rate;
        return rateResource;
    }

}
