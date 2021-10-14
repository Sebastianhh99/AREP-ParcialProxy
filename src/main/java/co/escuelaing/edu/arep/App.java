package co.escuelaing.edu.arep;

import spark.Request;
import spark.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static spark.Spark.*;

import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Hello world!
 *
 */
public class App 
{
    private static final String urls[] = new String[]{"http://ec2-34-235-137-26.compute-1.amazonaws.com:34000/","http://ec2-54-197-200-225.compute-1.amazonaws.com:34000/"};
    private static int url=0;

    public static void main( String[] args )
    {
        port(getPort());
        options("/*",
        (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        post("/","application/json",(req,res)->round(req,res));
    }

    static JSONObject round(Request req,Response res) throws JSONException, MalformedURLException, IOException{
        JSONObject data = new JSONObject(req.body());
        url = url+1>1 ? 0:url+1;
        String search = urls[url]+data.getString("function")+"?value="+data.getString("value");
        return new JSONObject(IOUtils.toString(new URL(search),Charset.forName("UTF-8")));
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
