package com.example.vertx_workshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfigStore;
import config.Config;
import config.VertxConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;

public class Launcher extends VertxCommandLauncher implements VertxLifecycleHooks {

  private static final int INSTANCES = Runtime.getRuntime().availableProcessors();
  private static final int EVENT_LOOP_POOL_SIZE = INSTANCES * 2;

  private VertxConfig vertxConfig;

  public static void main(String[] args) {
    new Launcher().dispatch(args);
  }

  @Override
  public void afterConfigParsed(JsonObject config){
    try{
      AppConfigStore.setConfig(new ObjectMapper()
        .readValue(config.toString(), Config.class));
      if (AppConfigStore.getConfig().getVertx() != null)
        this.vertxConfig = AppConfigStore.getConfig().getVertx();
    } catch (JsonProcessingException e) {
      System.out.println("Failed Parsing config: " + e.toString());
      System.exit(0);
      return;
    }
  }

  @Override
  public void beforeStartingVertx(VertxOptions options){
    options.setEventLoopPoolSize(EVENT_LOOP_POOL_SIZE);

    if (this.vertxConfig != null) {
      if (vertxConfig.getWorkerPoolSize() != null) {
        options.setWorkerPoolSize(vertxConfig.getWorkerPoolSize());
      }
      if (vertxConfig.getEventLoopPoolSize() != null) {
        options.setEventLoopPoolSize(vertxConfig.getEventLoopPoolSize());
      }
    }
  }

  @Override
  public void afterStartingVertx(Vertx vertx){

  }

  @Override
  public void beforeStoppingVertx(Vertx vertx){

  }

  @Override
  public void afterStoppingVertx(){

  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions){
    deploymentOptions.setInstances(EVENT_LOOP_POOL_SIZE);

    if (this.vertxConfig != null && this.vertxConfig.getEventLoopPoolSize() != null){
      deploymentOptions.setInstances(this.vertxConfig.getEventLoopPoolSize());
    }
  }

  @Override
  public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
    vertx.close();
  }

}
