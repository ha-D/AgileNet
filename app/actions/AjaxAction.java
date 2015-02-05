package actions;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utilities.RequestParseException;

public class AjaxAction extends Action<Ajax> {
    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        try {
            return delegate.call(context);
        } catch (RequestParseException e) {
            Result result = badRequest(e.getMessage());
            return F.Promise.pure(result);
        }
    }
}
