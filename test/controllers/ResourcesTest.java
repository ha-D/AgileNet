package controllers;

import com.google.common.collect.ImmutableMap;
import dao.CategoryDao;
import dao.ResourceDao;
import dao.ResourceSearchCriteria;
import dao.stubs.StubCategoryDao;
import models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import play.mvc.Result;
import testutils.BaseTest;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;

public class ResourcesTest extends BaseTest {
    private static Resource[] resourceList;
    private static ResourceDao resourceDao;

    @BeforeClass
    public static void setUp() {
        resourceDao = mock(ResourceDao.class);
        CategoryDao categoryDao = new StubCategoryDao();
        Dependencies.setResourceDao(resourceDao);
        Dependencies.setCategoryDao(categoryDao);

        resourceList = new Resource[3];
        resourceList[0] = new Resource();
        resourceList[0].resourceType = ResourceType.ARTICLE;
        resourceList[0].name = "resource 0";
        resourceList[0].description = "resource 0 description";
        resourceList[1] = new Resource();
        resourceList[1].resourceType = ResourceType.BOOK;
        resourceList[1].name = "resource 1";
        resourceList[1].description = "resource 1 description";
        resourceList[2] = new Resource();
        resourceList[2].resourceType = ResourceType.VIDEO;
        resourceList[2].name = "resource 2";
        resourceList[2].description = "resource 2 description";
    }

    @Test
    public void testSimpleSearch() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        when(resourceDao.findByCriteria(criteria)).thenReturn(Arrays.asList(resourceList));

        Result result = makeRequest();
        assertResults(result, resourceList);
    }

    @Test
    public void testPagination() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        criteria.setPageSize(2);
        criteria.setPageNumber(0);
        when(resourceDao.findByCriteria(criteria)).thenReturn(select(0, 1));

        Result result = makeRequest(ImmutableMap.of(
            "page_size", "2",
            "page", "0"
        ));
        assertResults(result, select(0, 1));


        criteria.setPageNumber(1);
        when(resourceDao.findByCriteria(criteria)).thenReturn(select(2));

        result = makeRequest(ImmutableMap.of(
            "page_size", "2",
            "page", "1"
        ));
        assertResults(result, select(2));
    }

    @Test
    public void testSearchResourceType() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        criteria.addResourceType(ResourceType.BOOK);
        when(resourceDao.findByCriteria(criteria)).thenReturn(select(1));

        Result result = makeRequest(ImmutableMap.of(
            "resource_type", "book"
        ));
        assertResults(result, select(1));
    }

    @Test
    public void testSearchCategory() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        criteria.setCategory(1);
        when(resourceDao.findByCriteria(criteria)).thenReturn(select(0, 2));

        Result result = makeRequest(ImmutableMap.of(
            "category", "1"
        ));
        assertResults(result, select(0, 2));
    }

    @Test
    public void testSearchQuery() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        criteria.setQuery("this is a query");
        when(resourceDao.findByCriteria(criteria)).thenReturn(select(1));

        Result result = makeRequest(ImmutableMap.of(
            "query", "this is a query"
        ));
        assertResults(result, select(1));
    }

    @Test
    public void testSearchEmpty() throws JSONException {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        when(resourceDao.findByCriteria(criteria)).thenReturn(select());

        Result result = makeRequest();
        assertResults(result, select());
    }

    private void assertResults(Result result, List<Resource> resources) throws JSONException {
        assertSuccess("Search request should return valid response with no parameters", result);
        assertThat(contentType(result)).isEqualTo("application/json");

        String content =  contentAsString(result);
        JSONObject json = new JSONObject(content);

        int count = json.getInt("resultCount");
        JSONArray results = json.getJSONArray("results");

        assertEquals("resultCount must match number of results", resources.size(), count);
        assertEquals(resources.size(), results.length());

        for (Resource resource : resources) {
            int j = 0;
            for (j = 0; j < count; j++) {
                JSONObject jsonResult = results.getJSONObject(j);

                if (jsonResult.getString("name").equals(resource.name)) {
                    assertEquals(resource.description, jsonResult .getString("description"));
                    assertEquals(resource.resourceType.toString().toLowerCase(),
                            jsonResult.getString("resourceType"));
                    //TODO: assertEquals(user.firstName + " " + user.lastName, result.getString("user"));
                    //TODO: assertEquals(resource.date, result.getString("date"));
                    //TODO: assertEquals(resource.rating, result.getString("rating"));
                    break;
                }
            }

            assertFalse("Resource doesn't exist in json response", j == count);
        }
    }

    private void assertResults(Result result, Resource[] resources) throws JSONException {
        assertResults(result, Arrays.asList(resources));
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

    private List<Resource> select(int... resourceIndices) {
        List<Resource> list = new ArrayList<Resource>();
        for (int index : resourceIndices) {
            list.add(resourceList[index]);
        }
        return list;
    }
}
