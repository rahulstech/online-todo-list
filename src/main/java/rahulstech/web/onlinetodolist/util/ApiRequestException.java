package rahulstech.web.onlinetodolist.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public class ApiRequestException extends RuntimeException {
    
    private StatusType status;
    
    public ApiRequestException(int code, String reason) {
        this.status = new ApiResponseStatus(code, reason);
    }
    
    public int getCode() {
        return status.getStatusCode();
    }
    
    public String getReason() {
        return status.getReasonPhrase();
    }
    
    public Response toResponse() {
        return Response.status(status).build();
    }
    
    public static ApiRequestException badRequest(String reason) {
        return new ApiRequestException(400,reason);
    }
    
    public static ApiRequestException notFound(String reason) {
        return new ApiRequestException(404,reason);
    }
    
    public static ApiRequestException internalServerError() {
        return new ApiRequestException(500,"Internal Server Error");
    }
    
    private static class ApiResponseStatus implements StatusType {
        
        private int code;
        private Status.Family family;
        private String reason;
        
        public ApiResponseStatus(int code, String reason) {
            this.family = Status.fromStatusCode(code)
                    .getFamily();
            this.code = code;
            this.reason = reason;
        }
        
        public StatusType get() {
            return this;
        }

        @Override
        public int getStatusCode() {
            return code;
        }

        @Override
        public Status.Family getFamily() {
            return family;
        }

        @Override
        public String getReasonPhrase() {
            return reason;
        }
    }
}
