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
import utilities.UserUtils;

import java.util.List;

import static play.mvc.Results.*;
import static utilities.FormRequest.formBody;
import static utilities.UserUtils.sessionUser;


public class Resources {
    @Authorized({})
    public static Result newResource() {
        Form<Resource> resourceForm = Form.form(Resource.class);
        return ok(views.html.addResource.render(resourceForm));
    }

    @Authorized({})
    public static Result addResource() {
        Form<Resource> form = Form.form(Resource.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest(views.html.addResource.render(form));
        Resource r = form.get();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart part = body.getFile("content");
        if (r.resourceType != ResourceType.WEBSITE && part != null) {
            File file = part.getFile();
            System.out.println(file.getName());
            try {
                File newFile = new File("public/resources/" + r.id, file.getName());
                FileUtils.moveFile(file, newFile);
                r.url = newFile.getPath();
            } catch (IOException ioe) {
                System.out.println("Problem operating on filesystem");
            }
        }
        r.user = Dependencies.getUserDao().findByEmail(session().get("email"));
        Dependencies.getResourceDao().create(r);
//        return redirect(routes.Accounts.resourceView(r.id));
        return ok(views.html.index.render());
    }

    public static Result searchPage() {
        return ok(views.html.resourceSearch.render(sessionUser()));
    }

    @Ajax
    public static Result search() {
        FormRequest request = formBody();
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();

        Integer categoryId = request.getInt("category");
        if (categoryId != null) {
            Category category = Dependencies.getCategoryDao().findById(categoryId);
            criteria.setCategory(category);
        }

        criteria.setQuery(request.get("query"));
        List<String> resourceTypes = request.getList("resourceType[]");
        if (resourceTypes != null) {
            for (String resourceType : resourceTypes) {
                criteria.addResourceType(ResourceType.fromString(resourceType));
            }
        }
        criteria.setPageSize(request.getInt("pageSize", criteria.getPageSize()));
        criteria.setPageNumber(request.getInt("page", criteria.getPageNumber()));

        List<Resource> resources = Dependencies.getResourceDao().findByCriteria(criteria);
        return ok(serializeResources(resources));
    }

    private static ObjectNode serializeResources(List<Resource> resources) {
        ObjectNode rootJson = Json.newObject();
        rootJson.put("resultCount", resources.size());
        ArrayNode jsonList = rootJson.putArray("results");

        for (Resource resource : resources) {
            ObjectNode json = jsonList.addObject();
            json.put("id", resource.id);
            json.put("name", resource.name);
            json.put("description", resource.description);
            json.put("resourceType", resource.resourceType.toString());
            if (resource.owner != null && !resource.owner.isEmpty()) {
                json.put("user", resource.owner);
            }
            // TODO: json.put("date", ...)
            // TODO: json.put("rating", ...)
        }

        return rootJson;
    }

    @Authorized({})
    public static Result resourceView(Integer id) {
        Resource resource = Dependencies.getResourceDao().findById(id);
        return ok(views.html.resource.render(resource));
    }

    @Authorized({})
    public static Result rateResource() {
        //form with two fields: rate and resourceId

        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        int rate = Integer.parseInt(Form.form().bindFromRequest().get("rate"));
        int resourceId = Integer.parseInt(Form.form().bindFromRequest().get("resource"));
        System.out.println(resourceId);
        Resource resource = Dependencies.getResourceDao().findById(resourceId);
        RateResource rateResource = Dependencies.getRateResourceDao().create(rate, resource, user);

        ObjectNode rootJson = Json.newObject();
        rootJson.put("id", rateResource.id);

        //Result result = ok(rootJson);
        return ok(rootJson);
    }
}
