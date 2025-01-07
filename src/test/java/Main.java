import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws Exception {
        OkHttpClient c = new OkHttpClient();

        JSONArray names = new JSONArray();
        names.put("left");
        names.put("right");

        JSONObject post = new JSONObject()
                .put("fixtureNames", names)
                .put("state", "bluedustbeam");

        RequestBody rb = RequestBody.create(post.toString(), MediaType.parse("application/json"));

        Request r = new Request.Builder()
                .url("http:localhost:16522/set")
                .post(rb)
                .build();

        System.out.println(c.newCall(r).execute().body().string());
    }
}
