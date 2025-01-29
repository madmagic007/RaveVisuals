package me.madmagic.ravevisuals.api.destinations;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.api.DestinationIMPL;
import me.madmagic.ravevisuals.handlers.VarHandler;
import net.minecraft.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetVarEndpoint extends DestinationIMPL {

    public SetVarEndpoint() {
        super("setvar");
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

        postData.keySet().forEach(key -> {
            Object value = postData.opt(key);

            VarHandler.setValue(key, value);
        });

        return textResponse("ok");
    }
}
