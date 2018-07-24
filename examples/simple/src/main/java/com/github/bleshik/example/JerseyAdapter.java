package com.github.bleshik.example;

import util.JerseyRequestHandler;

public class JerseyAdapter extends JerseyRequestHandler {
    public JerseyAdapter() {
        super("/", PingResource.class);
    }
}
