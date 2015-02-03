package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import dao.CategoryDao;
import dao.ResourceDao;
import dao.stubs.StubCategoryDao;
import dao.stubs.StubResourceDao;
import models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class ResourcesTest {
    Resource[] resourceList;
    User user;

    @Before
    public void setUp() {
        ResourceDao resourceDao = new StubResourceDao();
        CategoryDao categoryDao = new StubCategoryDao();
        Dependencies.setResourceDao(resourceDao);
        Dependencies.setCategoryDao(categoryDao);

        user = new User();
        user.firstName = "Hadi";
        user.lastName = "Zolfaghari";
        user.id = 1;

        resourceList = new Resource[3];
        resourceList[0] = resourceDao.create(ResourceType.ARTICLE, "resource1",
                new HashSet<Category>(), "resource 1 description");
        resourceList[1] = resourceDao.create(ResourceType.BOOK, "resource2",
                new HashSet<Category>(), "resource 2 description");
        resourceList[2] = resourceDao.create(ResourceType.VIDEO, "resource3",
                new HashSet<Category>(), "resource 3 description");

        for (Resource resource : resourceList) {
            resourceDao.create(resource);
        }
    }

    @Test
    public void testResourceSearch() {
        try {
            Result result = makeRequest();
            assertResults(result, resourceList[0], resourceList[1], resourceList[2]);

            // Test pagination

            result = makeRequest(ImmutableMap.of(
                "page_size", 2,
                "page_count", 0
            ));
            List<Resource> firstPage = parseResources(result);

            result = makeRequest(ImmutableMap.of(
                    "page_size", 2,
                    "page_count", 1
            ));
            List<Resource> secondPage = parseResources(result);

            assertEquals("No more than page_size results should be returned", 2, firstPage.size());
            assertEquals("No more than page_size results should be returned", 1, secondPage.size());
            assertFalse("Different pages should not contain duplicate results",
                    firstPage.contains(secondPage.get(0)));

            result = makeRequest(ImmutableMap.of(
                    "page_size", 10,
                    "page_count", 0,
                    "query", "something",
                    "resource_type","book,video",
                    "category", 2
            ));
            assertResults(result);

        } catch(JSONException e) {
            fail("Invalid JSON response received");
        }
    }

    private void assertResults(Result result, Resource... resources) throws JSONException {
        assertSuccess("Search request should return valid response with no parameters", result);
        assertThat(contentType(result)).isEqualTo("application/json");

        String content =  contentAsString(result);
        JSONObject json = new JSONObject(content);

        int count = json.getInt("resultCount");
        JSONArray results = json.getJSONArray("results");

        assertEquals("resultCount must match number of results", count, resources.length);
        assertEquals(resources.length, results.length());

        for (Resource resource : resources) {
            int j = 0;
            for (j = 0; j < count; j++) {
                JSONObject jsonResult = results.getJSONObject(j);

                if (jsonResult.getString("name").equals(resource.name)) {
                    assertEquals(resource.description, jsonResult .getString("description"));
                    assertEquals(resource.resourceType.toString().toLowerCase(),
                            jsonResult .getString("resourceType"));
                    //TODO: assertEquals(user.firstName + " " + user.lastName, result.getString("user"));
                    //TODO: assertEquals(resource.date, result.getString("date"));
                    //TODO: assertEquals(resource.rating, result.getString("rating"));
                    break;
                }
            }

            assertFalse("Resource doesn't exist in json response", j == count);
        }
    }

    private List<Resource> parseResources(Result result) throws JSONException {
        String content =  contentAsString(result);
        JSONObject json = new JSONObject(content);

        List<Resource> resources = new ArrayList<Resource>();
        for (int i = 0; i < json.getInt("resultCount"); i++) {
            String name = json.getJSONArray("results").getJSONObject(i).getString("name");
            for (Resource resource : resourceList) {
                if (resource.name.equals(name)) {
                    resources.add(resource);
                }
            }
        }

        return resources;
    }

    private Result makeRequest(Map params) {
        return callAction(
                routes.ref.Resources.search(),
                fakeRequest().withFormUrlEncodedBody(params)
        );
    }

    private Result makeRequest() {
        return callAction(
                routes.ref.Resources.search(),
                fakeRequest()
        );
    }

    private void assertSuccess(String message, Result result) {
        int status = status(result);
        assertTrue(message, status == 200);
    }
}
