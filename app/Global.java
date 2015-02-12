import com.avaje.ebean.Ebean;
import dao.impl.*;
import utilities.Dependencies;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        Dependencies.initialize(
                new UserDaoImpl(),
                new RoleDaoImpl(),
                new CategoryDaoImpl(),
                new ResourceDaoImpl(),
                new CommentDaoImpl(),
                new RateResourceDaoImpl());

        if (Dependencies.getRoleDao().getRoleCount() == 0) {
            Ebean.save((List) Yaml.load("initial-data/users_roles.yml"));
            Ebean.save((List) Yaml.load("initial-data/categories_resources.yml"));
        }
    }
}
