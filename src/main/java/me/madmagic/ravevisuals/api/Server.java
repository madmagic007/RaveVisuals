package me.madmagic.ravevisuals.api;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.api.destinations.SetEndpoint;

import java.util.Arrays;
import java.util.List;

public class Server extends NanoHTTPD {

    private String authToken = null;
    private boolean checkAuth = false;

    public Server(int port) throws Exception {
        super(port);
        this.start(1000, false);
        Main.console.sendMessage("Started HTTP server at port: " + port);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setCheckAuth(boolean checkAuth) {
        this.checkAuth = checkAuth;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (checkAuth && (authToken == null || !authToken.equals(session.getHeaders().get("authorization"))))
            return DestinationIMPL.unAuthorised();

        String url = session.getUri().toLowerCase();
        url = url.replaceAll("\\\\", "/").replaceFirst("/", "");

        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        String[] split = url.split("/");
        String path = url;
        String file = "";

        if (split.length > 0 && split[split.length - 1].contains(".")) {
            path = String.join("/", Arrays.copyOfRange(split, 0, split.length - 1));
            file = split[split.length - 1];
        }

        for (DestinationIMPL d : destinations) {
            if (d.destination.equals(path)) {
                try {
                    return d.handleRequest(session, file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return DestinationIMPL.serverErr();
                }
            }
        }

        return DestinationIMPL.notFound();
    }

    private static final List<DestinationIMPL> destinations = Arrays.asList(new SetEndpoint());
}
