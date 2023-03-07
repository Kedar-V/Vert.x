package com.example.vertx_workshop;

import config.AppConfigStore;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DatabaseVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseVerticle.class);

  JDBCPool pool;

  @Override
  public void start(Promise<Void> start) throws Exception{

    LOGGER.info("DatabaseVerticle Started");

//    vertx.executeBlocking( promise -> {
//
//      // Create DB Pool
//        pool = JDBCPool.pool(
//                vertx,
//                new JDBCConnectOptions()
//                        .setJdbcUrl(AppConfigStore.getConfig().dbConfig.getJdbcUrl())
//                        .setUser(AppConfigStore.getConfig().dbConfig.getUser())
//                        .setPassword(AppConfigStore.getConfig().dbConfig.getPassword()),
//                new PoolOptions()
//                        .setMaxSize(AppConfigStore.getConfig().dbConfig.getMaxPoolSize())
//        );
//
//        // Query
//      pool.query("SELECT * FROM CUSTOMERS")
//        .execute()
//        .onFailure(e -> {
//            LOGGER.error(e);
//          }
//        )
//        .onSuccess(rows -> {
//          for (Row row: rows){
//            LOGGER.info(row.getString("NAME"));
//          }
//        });
//
//      promise.complete();
//    }, asyncResult -> {
//      if (asyncResult.failed()){
//        start.fail(asyncResult.cause());
//      }
//      else{
//        start.complete();
//      }
//    });

    vertx.eventBus().consumer("getData.addr", msg -> {
      databaseConnection().onSuccess( fut -> {
        String q = "SELECT * FROM CUSTOMERS";
        runSQLQuery(q).onFailure(e -> {LOGGER.error(e);})
          .onSuccess(rows -> {
            JsonArray array = new JsonArray();
            LOGGER.info("SUCCESS");
            for (Row row: rows){
              array.add(new JsonObject()
                .put("Name", row.getString("NAME"))
                .put("Age", row.getInteger("AGE"))
                .put("Address", row.getString("ADDRESS")));
            }
            LOGGER.info(array);
            msg.reply(array.toString());
          });
      });
    });

    vertx.eventBus().consumer("getUserData.addr", msg -> {
      databaseConnection().onSuccess( fut -> {
        String q = "SELECT * FROM CUSTOMERS WHERE NAME = ?";
        String name = (String) msg.body();
        runSQLQuery(q, name).onFailure(e -> {LOGGER.error(e);})
          .onSuccess(rows -> {
            JsonArray array = new JsonArray();
            LOGGER.info("SUCCESS");
            for (Row row: rows){
              array.add(new JsonObject()
                .put("Name", row.getString("NAME"))
                .put("Age", row.getInteger("AGE"))
                .put("Address", row.getString("ADDRESS")));
            }
            LOGGER.info(array);
            msg.reply(array.toString());
          });
      });
    });
  }

  private Future<Object> databaseConnection(){

    Promise<Object> dbPromise = Promise.promise();
    Future<Object> dbFuture = dbPromise.future();

    vertx.executeBlocking(promise -> {
      pool = JDBCPool.pool(
        vertx,
        new JDBCConnectOptions()
          .setJdbcUrl(AppConfigStore.getConfig().dbConfig.getJdbcUrl())
          .setUser(AppConfigStore.getConfig().dbConfig.getUser())
          .setPassword(AppConfigStore.getConfig().dbConfig.getPassword()),
        new PoolOptions()
          .setMaxSize(AppConfigStore.getConfig().dbConfig.getMaxPoolSize())
      );
      promise.complete();
    }, asyncResult -> { if (asyncResult.failed()) {dbPromise.fail(asyncResult.cause());} else {dbPromise.complete();} });

    return dbFuture;
  }

  private Future<RowSet<Row>> runSQLQuery(String q, String name){
    return pool.preparedQuery(q)
      .execute(Tuple.of(name));
  }

  private Future<RowSet<Row>> runSQLQuery(String q){
    return pool.query(q)
      .execute();
  }
}
