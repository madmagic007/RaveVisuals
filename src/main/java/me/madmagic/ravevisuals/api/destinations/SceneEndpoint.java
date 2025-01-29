package me.madmagic.ravevisuals.api.destinations;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.api.DestinationIMPL;
import me.madmagic.ravevisuals.handlers.anim.SceneHandler;
import net.minecraft.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SceneEndpoint extends DestinationIMPL {

    public SceneEndpoint() {
        super("scene");
    }

    @Override
    public NanoHTTPD.Response handleRequest(NanoHTTPD.IHTTPSession session, String file) throws Exception {
        if (!session.getMethod().equals(NanoHTTPD.Method.POST))
            return notAllowed();

        Map<String, String> files = new HashMap<>();
        session.parseBody(files);

        String json = files.get("postData");
        if (StringUtil.isNullOrEmpty(json)) return badRequest("No post data received.");

        JSONObject postData = new JSONObject(json);

        postData.optJSONArray("start", new JSONArray()).forEach(o -> {
            if (!(o instanceof String str)) return;

            Util.runIfNotNull(SceneHandler.getByName(str), SceneHandler::startScene);
        });

        postData.optJSONArray("stop", new JSONArray()).forEach(o -> {
            if (!(o instanceof String str)) return;

            Util.runIfNotNull(SceneHandler.getByName(str), SceneHandler::stopScene);
        });

        return textResponse("ok");
    }
}
