package config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("vertx")
public class VertxConfig {

  private Integer workerPoolSize ;
  private Integer eventLoopPoolSize;
  private Integer blockedThreadCheckInterval ;
  private Integer maxEventLoopExecuteTime ;
  private Integer maxWorkerExecuteTime;
  private Integer warningExceptionTime ;
  private Boolean haEnabled;
  private Boolean preferNativeTransport ;
  private Boolean enableMetrics ;

  public Integer getWorkerPoolSize() {
    return workerPoolSize;
  }

  public void setWorkerPoolSize(Integer workerPoolSize) {
    this.workerPoolSize = workerPoolSize;
  }

  public Integer getEventLoopPoolSize() {
    return eventLoopPoolSize;
  }

  public void setEventLoopPoolSize(Integer eventLoopPoolSize) {
    this.eventLoopPoolSize = eventLoopPoolSize;
  }

  public Integer getBlockedThreadCheckInterval() {
    return blockedThreadCheckInterval;
  }

  public void setBlockedThreadCheckInterval(Integer blockedThreadCheckInterval) {
    this.blockedThreadCheckInterval = blockedThreadCheckInterval;
  }

  public Integer getMaxEventLoopExecuteTime() {
    return maxEventLoopExecuteTime;
  }

  public void setMaxEventLoopExecuteTime(Integer maxEventLoopExecuteTime) {
    this.maxEventLoopExecuteTime = maxEventLoopExecuteTime;
  }

  public Integer getMaxWorkerExecuteTime() {
    return maxWorkerExecuteTime;
  }

  public void setMaxWorkerExecuteTime(Integer maxWorkerExecuteTime) {
    this.maxWorkerExecuteTime = maxWorkerExecuteTime;
  }

  public Integer getWarningExceptionTime() {
    return warningExceptionTime;
  }

  public void setWarningExceptionTime(Integer warningExceptionTime) {
    this.warningExceptionTime = warningExceptionTime;
  }

  public Boolean getHaEnabled() {
    return haEnabled;
  }

  public void setHaEnabled(Boolean haEnabled) {
    this.haEnabled = haEnabled;
  }

  public Boolean getPreferNativeTransport() {
    return preferNativeTransport;
  }

  public void setPreferNativeTransport(Boolean preferNativeTransport) {
    this.preferNativeTransport = preferNativeTransport;
  }

  public Boolean getEnableMetrics() {
    return enableMetrics;
  }

  public void setEnableMetrics(Boolean enableMetrics) {
    this.enableMetrics = enableMetrics;
  }

}
