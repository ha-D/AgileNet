package controllers;

import actions.Ajax;
import actions.Authorized;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import org.apache.commons.io.FileUtils;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utilities.Dependencies;
import utilities.FormRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static dao.ResourceDao.ResourceSearchCriteria;
import static play.mvc.Controller.request;
import static play.mvc.Controller.session;
import static play.mvc.Results.*;
import static utilities.FormRequest.formBody;
import static utilities.UserUtils.sessionUser;

/**
 * Controllers for viewing, adding, modifying, commenting and rating resources
 */
public class Resources {
    /**
     * POST form submit to add a new resource
     *
     * Authorization: Expert
     */
    @Authorized({"expert"})
    public static Result addResource() {
        Form<Resource> form = Form.form(Resource.class).bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Accounts.settings() + "#new-resource");
        }

        Resource r = form.get();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart part = body.getFile("content");
        r = Dependencies.getResourceDao().create(r);

        if (r.resourceType != ResourceType.WEBSITE && part != null) {
            File file = part.getFile();
            System.out.println(file.getName());
            try {
                File newFile = new File("public/resources/" + r.id, part.getFilename());
                FileUtils.moveFile(file, newFile);
                r.fileUrl = "resources/" + newFile.getName(); // newFile.getPath();
            } catch (IOException ioe) {
                System.out.println("Problem operating on filesystem");
            }
        }

        r.user = Dependencies.getUserDao().findByEmail(session().get("email"));
        Dependencies.getResourceDao().update(r);

        return redirect(routes.Resources.resourceView(r.id));
    }

    /**
     * Render resource search page
     */
    public static Result searchPage() {
        return ok(views.html.resourceSearch.render(sessionUser()));
    }

    /**
     * Search for resources with the given criteria
     * POST category: Category id of desired resources
     * POST resourceType[]: A list of resource types to be included
     * POST query: A query string to search for in resource names and descriptions
     * POST sortBy: Determines what to order search results by, either 'date' or 'rate'
     * POST pageSize: Maximum number of results to return in each page
     * POST pageNumber: Page number of results
     *
     * Ajax Method
     */
    @Ajax
    public static Result search() {
        FormRequest request = formBody();
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();

        Integer categoryId = request.getInt("category", null);
        if (categoryId != null) {
            Category category = Dependencies.getCategoryDao().findById(categoryId);
            criteria.setCategory(category);
        }

        criteria.setQuery(request.get("query", null));
        List<String> resourceTypes = request.getList("resourceType[]", null);
        if (resourceTypes != null) {
            for (String resourceType : resourceTypes) {
                criteria.addResourceType(ResourceType.fromString(resourceType));
            }
        }
        criteria.setPageSize(request.getInt("pageSize", criteria.getPageSize()));
        criteria.setPageNumber(request.getInt("page", criteria.getPageNumber()));

        String sortBy = request.get("sortBy", null);
        if ("date".equals(sortBy)) {
            criteria.setSortBy(ResourceSearchCriteria.SORT_BY_DATE);
        } else if ("rating".equals(sortBy)) {
            criteria.setSortBy(ResourceSearchCriteria.SORT_BY_RATE);
        }

        List<Resource> resources = Dependencies.getResourceDao().findByCriteria(criteria);
        return ok(serializeResources(resources));
    }

    private static ObjectNode serializeResources(List<Resource> resources) {
        ObjectNode rootJson = Json.newObject();
        rootJson.put("resultCount", resources.size());
        ArrayNode jsonList = rootJson.putArray("results");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.forLanguageTag("ir"));
        for (Resource resource : resources) {
            ObjectNode json = jsonList.addObject();
            json.put("id", resource.id);
            json.put("name", resource.name);
            json.put("description", resource.description);
            json.put("resourceType", resource.resourceType.toString());
            if (resource.owner != null && !resource.owner.isEmpty()) {
                json.put("user", resource.owner);
            }
            json.put("date", dateFormat.format(resource.date));
            json.put("rating", resource.getRate());
            ArrayNode cats = json.putArray("categories");
            for (Category category : resource.categories) {
                cats.add(category.id);
            }
        }

        return rootJson;
    }

    /**
     * Render the detail page of a resource
     *
     * Authorization: User
     */
    @Authorized({})
    public static Result resourceView(Integer id) {
        Resource resource = Dependencies.getResourceDao().findById(id);
        User user = sessionUser();
        int userRate = Dependencies.getRateResourceDao().getRate(user, resource);
        return ok(views.html.resource.render(resource, userRate, user));
    }

    /**
     * Rate a resource from 1 to 5
     * POST resource: The of the resource being rated
     * POST rate: The rate to give to the resource
     *
     * Ajax Method
     * Authorization: User
     */
    @Ajax
    @Authorized({})
    public static Result rateResource() {
        User user = sessionUser();
        FormRequest request = formBody();
        int rate = request.getInt("rate");
        int resourceId = request.getInt("resource");

        Resource resource = Dependencies.getResourceDao().findById(resourceId);
        RateResource rateResource = Dependencies.getRateResourceDao().create(rate, resource, user);

        ObjectNode rootJson = Json.newObject();
        rootJson.put("rate", resource.getRate());

        return ok(rootJson);
    }

    /**
     * Add a category to a resource
     * POST category: The id of the category to add to the resource
     * POST resource: The id of the resource to add the category to
     *
     * Ajax Method
     * Authorization: Expert
     */
    @Ajax
    @Authorized({"expert"})
    public static Result addCategory() {
        FormRequest request = formBody();
        int categoryId = request.getInt("category");
        int resourceId = request.getInt("resource");

        Resource resource = Dependencies.getResourceDao().findById(resourceId);
        Category category = Dependencies.getCategoryDao().findById(categoryId);
        if (resource == null) {
            return badRequest("No resource with id " + resourceId);
        }
        if (category == null) {
            return badRequest("No category with id " + categoryId);
        }

        resource.categories.add(category);
        Dependencies.getResourceDao().update(resource);
        return ok();
    }

    /**
     * Remove a category from a resource
     * POST category: The id of the category to remove from the resource
     * POST resource: The id of the resource to remove the category from
     *
     * Ajax Method
     * Authorization: Expert
     */
    @Ajax
    @Authorized({"expert"})
    public static Result removeCategory() {
        FormRequest request = formBody();
        int categoryId = request.getInt("category");
        int resourceId = request.getInt("resource");

        Resource resource = Dependencies.getResourceDao().findById(resourceId);
        Category category = Dependencies.getCategoryDao().findById(categoryId);
        if (resource == null) {
            return badRequest("No resource with id " + resourceId);
        }
        if (category == null) {
            return badRequest("No category with id " + categoryId);
        }

        resource.categories.remove(category);
        Dependencies.getResourceDao().update(resource);
        return ok();
    }
}
