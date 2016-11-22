package undertowd;

import org.kohsuke.args4j.Option;

public class CommandLineOption {

  @Option(name = "-c", aliases = "--config", required = true, metaVar = "<config file or dir>", usage = "Config file or dir path")
  private String configPath;

  public String getConfigPath() {
    return configPath;
  }

}
