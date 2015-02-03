package services;

import dao.CategoryDao;
import dao.ResourceDao;
import dao.stubs.StubCategoryDao;
import dao.stubs.StubResourceDao;
import models.Category;
import models.Dependencies;
import models.Resource;
import models.ResourceType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourceSearcherTest {
    Category category1, category2, category3;
    Resource book1, book2, book3;
    Resource article1, article2, article3;
    Resource video1, video2, video3;
    Resource site1, site2, site3;
    ResourceSearcher searcher;

    @Before
    public void setUp() {
        ResourceDao resourceDao = new StubResourceDao();
        CategoryDao categoryDao = new StubCategoryDao();
        Dependencies.setResourceDao(resourceDao);
        Dependencies.setCategoryDao(categoryDao);

        category1 = newCategory("category1");
        category2 = newCategory("category2");
        category3 = newCategory("category3");
        categoryDao.create(category1);
        categoryDao.create(category2);
        categoryDao.create(category3);

        book1 = newResource("book1 name_query", ResourceType.BOOK);
        book2 = newResource("book2", ResourceType.BOOK, category1);
        book3 = newResource("book3", ResourceType.BOOK, category1, category2);

        article1 = newResource("article1", ResourceType.ARTICLE);
        article2 = newResource("article2 name_query", ResourceType.ARTICLE, category1);
        article3 = newResource("article3", ResourceType.ARTICLE, category1, category2);

        video1 = newResource("video1", ResourceType.VIDEO);
        video2 = newResource("video2", ResourceType.VIDEO, category1);
        video3 = newResource("video3 name_query", ResourceType.VIDEO, category1, category2);

        site1 = newResource("site1 name_query", ResourceType.WEBSITE);
        site2 = newResource("site2", ResourceType.WEBSITE, category1);
        site3 = newResource("site3", ResourceType.WEBSITE, category1, category2);

        book2.description = "felan felan desc_query felan felan";
        article1.description = "felan felan desc_query felan felan";
        video1.description = "felan felan desc_query felan felan";
        site3.description = "felan felan desc_query felan felan";

        resourceDao.create(book1);
        resourceDao.create(book2);
        resourceDao.create(book3);
        resourceDao.create(article1);
        resourceDao.create(article2);
        resourceDao.create(article3);
        resourceDao.create(video1);
        resourceDao.create(video2);
        resourceDao.create(video3);
        resourceDao.create(site1);
        resourceDao.create(site2);
        resourceDao.create(site3);

        searcher = new ResourceSearcher();
    }

    @Test
    public void testRetrieveResources() {
        ResourceSearchCriteria criteria = new ResourceSearchCriteria(null, null, ResourceType.BOOK);
        assertSearch(criteria, book1, book2, book3);

        criteria = new ResourceSearchCriteria(null, category1.id, ResourceType.ARTICLE);
        assertSearch(criteria, article2, article3);

        criteria = new ResourceSearchCriteria(null, category1.id, ResourceType.VIDEO, ResourceType.WEBSITE);
        assertSearch(criteria, video2, site2, video3, site3);

        criteria = new ResourceSearchCriteria(null, category2.id);
        assertSearch(criteria, book3, article3, video3, site3);

        criteria = new ResourceSearchCriteria("name_query", category1.id);
        assertSearch(criteria, article2, video3);

        criteria = new ResourceSearchCriteria("desc_query", null, ResourceType.ARTICLE, ResourceType.WEBSITE);
        assertSearch(criteria, article1, site3);

        // Test Pages
        criteria = new ResourceSearchCriteria(null, null, null);
        criteria.setPageNumber(0);
        criteria.setPageSize(5);
        List<Resource> firstPage = searcher.retrieveResources(criteria);

        criteria.setPageNumber(1);
        List<Resource> secondPage = searcher.retrieveResources(criteria);

        assertEquals("Search should return #'pageNumber' results", 5, firstPage.size());
        assertEquals("Search should return #'pageNumber' results", 5, secondPage.size());
        for (Resource resource : firstPage) {
            assertFalse("Different search pages should contain different results", secondPage.contains(resource));
        }
    }

    private void assertSearch(ResourceSearchCriteria criteria, Resource... resources) {
        List<Resource> resourceList = searcher.retrieveResources(criteria);
        assertEquals(resources.length, resourceList.size());
        for (Resource resource : resources) {
            assertTrue(resourceList.contains(resource));
            resourceList.remove(resource);
        }
    }

    private Resource newResource(String name, ResourceType resourceType, Category... categories) {
        Resource resource = new Resource();
        resource.name = name;
        resource.resourceType = resourceType;
        for (Category category : categories) {
            resource.categories.add(category);
        }

        return resource;
    }

    private Category newCategory(String name) {
        Category category = new Category();
        category.name = name;
        return category;
    }
}
