package com.example.vertx_workshop;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    LOGGER.info("MainVerticle Started");
//    vertx.createHttpServer().requestHandler(req -> {
//      if(req.path().startsWith("/route1")){
//        req.response().end("hello vert.x route1");
//      }
//      else{
//        req.response().end("hello vert.x");
//      }
//    }).listen(8080);

    Router router = Router.router(vertx);
    router.get("/").handler(this::httpServer);
    router.get("/:name").handler(this::route1);
    router.get("/db/getData").handler(this::getData);
    router.get("/db/getUserData/:name").handler(this::getUserData);

    DeploymentOptions opts = new DeploymentOptions()
      .setWorker(true)
      .setInstances(2);

    vertx.deployVerticle("com.example.vertx_workshop.HelloVerticle", opts);
    vertx.deployVerticle("com.example.vertx_workshop.DatabaseVerticle", opts);


    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8081)
      .onSuccess(httpServer -> LOGGER.info("Verticle Deployed Successfully"))
      .onFailure(err -> LOGGER.error("Verticle failed to deploy"+ err.toString()));
  }


  private void getUserData(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    vertx.eventBus().request("getUserData.addr", name, reply -> {
      routingContext.response().end((String) reply.result().body());
    });
  }
  private void getData(RoutingContext routingContext) {
    vertx.eventBus().request("getData.addr", "", reply -> {
      routingContext.response().end((String) reply.result().body());
    });
  }

  private void route1(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    vertx.eventBus().request("route1.addr", name, reply -> {
      routingContext.response().end((String) reply.result().body());
    });
  }

  private void httpServer(RoutingContext routingContext) {

    vertx.eventBus().request("hello.addr", "", reply -> {
      routingContext.response().end((String) reply.result().body());
    });

  }
}
