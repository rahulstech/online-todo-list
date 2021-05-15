package rahulstech.web.onlinetodolist.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import rahulstech.web.onlinetodolist.model.Todo;
import rahulstech.web.onlinetodolist.model.TodoList;

@Provider
public class GsonMessageBodyProvider implements MessageBodyReader, MessageBodyWriter {

    private Gson gson = null;
    
    public GsonMessageBodyProvider() {}
    
    private Gson getGson() {
        if (null == gson) {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
        }
        return gson;
    }
    
    @Override
    public boolean isReadable(Class type, Type genericType, Annotation[] antns, MediaType mt) {
        //return TodoList.class.equals(genericType) || Todo.class.equals(genericType);
        return true;
    }

    @Override
    public Object readFrom(Class type, Type genericType, Annotation[] antns, MediaType mt, MultivaluedMap mm, InputStream in) throws IOException, WebApplicationException {
        try (InputStreamReader reader = new InputStreamReader(in)){
            Type jsonType = type.equals(genericType) ? genericType : type;
            return getGson().fromJson(reader, jsonType);
        }
    }

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] antns, MediaType mt) {
        //return TodoList.class.equals(genericType) || Todo.class.equals(genericType);
        return true;
    }

    @Override
    public long getSize(Object t, Class type, Type type1, Annotation[] antns, MediaType mt) {
        return -1;
    }

    @Override
    public void writeTo(Object t, Class type, Type genericType, Annotation[] antns, MediaType mt, MultivaluedMap mm, OutputStream out) throws IOException, WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
            Type jsonType = type.equals(genericType) ? genericType : type;
            getGson().toJson(t, jsonType, writer);
        }
    }
}
