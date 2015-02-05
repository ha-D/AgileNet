package utilities;

import models.User;

import static play.mvc.Controller.session;

public class UserUtils {
    public static User sessionUser() {
        String email = session().get("email");
        if (email != null) {
            return Dependencies.getUserDao().findByEmail(email);
        }
        return null;
    }
}
