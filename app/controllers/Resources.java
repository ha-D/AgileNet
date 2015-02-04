package controllers;

import actions.Ajax;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ResourceSearchCriteria;
import models.Dependencies;
import models.Resource;
import models.ResourceType;
import play.libs.Json;
import play.mvc.Result;
import utilities.FormRequest;

import java.util.List;

import static play.mvc.Results.ok;
import static utilities.FormRequest.formBody;

public class Resources {
    public static Result searchPage() {
        return ok(views.html.resourceSearch.render());
    }

    @Ajax
    public static Result search() {
        FormRequest request = formBody();
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();

        criteria.setCategory(request.getInt("category"));
        criteria.setQuery(request.get("query"));
        List<String> resourceTypes = request.getList("resource_type");
        if (resourceTypes != null) {
            for (String resourceType : resourceTypes) {
                criteria.addResourceType(ResourceType.fromString(resourceType));
            }
        }
        criteria.setPageSize(request.getInt("page_size", criteria.getPageSize()));
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
            json.put("name", resource.name);
            json.put("description", resource.description);
            json.put("resourceType", resource.resourceType.toString());
            // TODO: json.put("user", ...)
            // TODO: json.put("date", ...)
            // TODO: json.put("rating", ...)
        }

        return rootJson;
    }
}
