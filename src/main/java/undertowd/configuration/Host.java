package undertowd.configuration;

import lombok.Data;

import java.util.Set;

@Data
public class Host {

  private String host;
  private int port;
  private Set<Path> paths;

}
