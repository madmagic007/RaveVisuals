package me.madmagic.ravevisuals.api.destinations;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.api.ApiSetInstance;
import me.madmagic.ravevisuals.api.DestinationIMPL;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import net.minecraft.util.StringUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetEndpoint extends DestinationIMPL {

    public SetEndpoint() {
        super("set");
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
        ApiSetInstance instance = new ApiSetInstance();

        Util.runIfNotNull(postData.optJSONArray("fixtureNames"), arr ->
            arr.forEach(o -> {
                String fixtureName = (String) o;

                Util.runIfNotNull(FixtureHandler.getByName(fixtureName), instance::addFixture);
            })
        );

        Util.runIfNotNull(postData.optJSONArray("groupNames"), arr ->
            arr.forEach(o -> {
                String groupName = (String) o;

                Util.runIfNotNull(GroupHandler.getByName(groupName), l ->
                    l.forEach(instance::addFixture)
                );
            })
        );

        instance.tryDefineState(postData);

        if (instance.isValid()) {
            instance.apply();
            return textResponse("ok");
        } else
            return badRequest("Unable to process request");
    }
}
