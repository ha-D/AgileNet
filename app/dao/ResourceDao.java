package dao;

import models.Category;
import models.Resource;
import models.ResourceType;
import models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ResourceDao extends BaseDao<Resource> {
    void update(Resource resource);
    Resource create(Resource resource);
    Resource create(ResourceType resourceType, String name, Set<Category> categories, String description, User user, String fileUrl, String url, String owner);
    Resource findById(int id);
    List<Resource> findByCriteria(ResourceSearchCriteria criteria);

    public static class ResourceSearchCriteria {
        public final static int SORT_BY_DATE = 0;
        public final static int SORT_BY_RATE = 1;

        private Set<ResourceType> resourceTypes;
        private Category category;
        private String query;
        private int pageSize;
        private int pageNumber;
        private int sortBy;

        public ResourceSearchCriteria() {
            this.resourceTypes = new HashSet<ResourceType>();
            this.pageSize = 20;
            this.pageNumber = 0;
        }

        public ResourceSearchCriteria(String query, Category category, ResourceType... resourceTypes) {
            this();
            this.query = query;
            this.category = category;

            if (resourceTypes != null) {
                for (ResourceType resourceType : resourceTypes) {
                    addResourceType(resourceType);
                }
            }
        }

        public void addResourceType(ResourceType resourceType) {
            resourceTypes.add(resourceType);
        }

        public Set<ResourceType> getResourceTypes() {
            return resourceTypes;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public Category getCategory() {
            return category;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getSortBy() {
            return sortBy;
        }

        public void setSortBy(int sortBy) {
            this.sortBy = sortBy;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ResourceSearchCriteria)) {
                return false;
            }
            ResourceSearchCriteria criteria = (ResourceSearchCriteria) obj;
            return resourceTypes.equals(criteria.resourceTypes) &&
                    ((category == null && criteria.category == null) ||
                        (category != null && category.equals(criteria.category))) &&
                    pageNumber == criteria.pageNumber &&
                    pageSize == criteria.pageSize &&
                    ((query == null && criteria.query == null) ||
                        (query != null && query.equals(criteria.query)));
        }
    }
}
