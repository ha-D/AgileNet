package dao;

import models.User;

import java.util.List;

public interface BaseDao<T> {
    T findById(int id);
    List<T> findAll();
    T create(T object);
    void update(T object);
}
