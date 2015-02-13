package dao;

import models.User;

import java.util.List;

/**
 * Base interface for Data Access Objects
 * @param <T> The model class that is managed by the DAO
 */
public interface BaseDao<T> {
    T findById(int id);
    List<T> findAll();
    T create(T object);
    void update(T object);
}
