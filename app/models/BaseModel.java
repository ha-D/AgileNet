package models;

import dao.BaseDao;
import play.db.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class BaseModel<T extends BaseDao> extends Model {
    @Id
    public int id;
}
