package config;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Config {

  public VertxConfig vertx;

  public VertxConfig getVertx() {
    return vertx;
  }

  public DbConfig dbConfig;

  public DbConfig getDbConfig() {return dbConfig;}

}
