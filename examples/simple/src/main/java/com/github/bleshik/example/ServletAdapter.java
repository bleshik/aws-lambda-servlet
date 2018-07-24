package com.github.bleshik.example;

import util.ServletRequestHandler;

public class ServletAdapter extends ServletRequestHandler {
    public ServletAdapter() {
        super("/", new PingServlet());
    }
}
