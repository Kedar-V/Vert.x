package com.example.vertx_workshop;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.UUID;

public class HelloVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloVerticle.class);

    String verticleId = UUID.randomUUID().toString();

    @Override
    public void start() throws Exception{

      LOGGER.info("HelloVerticle Started");

      vertx.eventBus().consumer("hello.addr", msg -> {
        LOGGER.info("here");
        msg.reply("Hello Vert.x");
      });

      vertx.eventBus().consumer("route1.addr", msg -> {
        msg.reply(String.format("Hello Vert.x %s, VerticleId: %s", (String) msg.body(), verticleId));
      });
    }
}
