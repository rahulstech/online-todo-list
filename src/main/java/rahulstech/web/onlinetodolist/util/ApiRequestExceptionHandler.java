package rahulstech.web.onlinetodolist.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiRequestExceptionHandler implements ExceptionMapper<ApiRequestException> {

    @Override
    public Response toResponse(ApiRequestException e) {
        return e.toResponse();
    }
}
