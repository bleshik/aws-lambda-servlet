package com.github.bleshik.example;

import javax.ws.rs.*;

@Path("ping")
public class PingResource {
    @GET
    public String get() {
        return "OK";
    }
}
