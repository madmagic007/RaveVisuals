package me.madmagic.ravevisuals.api.destinations;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.api.DestinationIMPL;
import me.madmagic.ravevisuals.handlers.CommandsHandler;
import net.minecraft.util.StringUtil;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsEndpoint extends DestinationIMPL {

    public CommandsEndpoint() {
        super("commands");
    }

    @Override
    public NanoHTTPD.Response handleRequest(NanoHTTPD.IHTTPSession session, String file) throws Exception {
        if (!session.getMethod().equals(NanoHTTPD.Method.POST))
            return notAllowed();

        Map<String, String> files = new HashMap<>();
        session.parseBody(files);

        String json = files.get("postData");
        if (StringUtil.isNullOrEmpty(json)) return badRequest("No post data received.");

        JSONArray postData = new JSONArray(json);

        List<String> commands = new ArrayList<>();

        postData.forEach(o -> {
            if (!(o instanceof String str)) return;

            Util.runIfNotNull(CommandsHandler.getByName(str), commands::addAll, () -> commands.add(str));
        });

        CommandsHandler.runCommands(commands);

        return textResponse("ok");
    }
}
