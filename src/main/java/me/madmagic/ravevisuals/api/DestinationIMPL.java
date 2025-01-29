package me.madmagic.ravevisuals.api;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public abstract class DestinationIMPL {

    public final String destination;

    public DestinationIMPL(String destination) {
        this.destination = destination;
    }

    protected Response blankResponse() {
        return NanoHTTPD.newFixedLengthResponse("");
    }

    protected Response textResponse(Object o) {
        return NanoHTTPD.newFixedLengthResponse(o.toString());
    }

    public abstract Response handleRequest(IHTTPSession session, String file) throws Exception;

    public static Response serverErr() {
        return NanoHTTPD.newFixedLengthResponse("Internal Server Error");
    }

    public static Response badRequest(String reason) {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", reason);
    }

    public static Response notAllowed() {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, "text/plain", "Method not allowed");
    }

    public static Response notFound() {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Requested endpoint not found");
    }

    public static Response unAuthorised() {
        return NanoHTTPD.newFixedLengthResponse(Response.Status.UNAUTHORIZED, "text/plain", "Unauthorised");
    }
}
