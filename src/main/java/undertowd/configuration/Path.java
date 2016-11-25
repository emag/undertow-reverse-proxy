package undertowd.configuration;

import lombok.Data;

import java.util.Set;

@Data
public class Path {

  private String path;
  private Set<String> proxies;
  private File file;

}
