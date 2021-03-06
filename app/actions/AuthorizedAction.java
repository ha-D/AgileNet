package actions;

import controllers.routes;
import utilities.Dependencies;
import models.Role;
import models.User;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class AuthorizedAction extends Action<Authorized> {
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        String[] roles = configuration.value();
        String email = context.session().get("email");
        User user = Dependencies.getUserDao().findByEmail(email);

        if (email == null || user == null) {
            return unauthorizedRedirect();
        }

        if (roles.length == 0) {
            return delegate.call(context);
        }

        for (String role : roles) {
            for (Role userRole : user.roles) {
                if (role.equals(userRole.name)) {
                    return delegate.call(context);
                }
            }
        }

        return unauthorizedRedirect();
    }

    public F.Promise<Result> unauthorizedRedirect() {
        return F.Promise.pure(redirect(routes.Accounts.login()));
    }
}
