package config;

public class AppConfigStore {

  public static Config getConfig() {
    return config;
  }

  public static void setConfig(Config config) {
    AppConfigStore.config = config;
  }

  public static Config config;
}
