package dao;

import models.ResourceType;

import java.util.HashSet;
import java.util.Set;

public class ResourceSearchCriteria {
    private Set<ResourceType> resourceTypes;
    private Integer category;
    private String query;
    private int pageSize;
    private int pageNumber;

    public ResourceSearchCriteria() {
        this.resourceTypes = new HashSet<ResourceType>();
        this.pageSize = 20;
        this.pageNumber = 0;
    }

    public ResourceSearchCriteria(String query, Integer category, ResourceType... resourceTypes) {
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

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCategory() {
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
