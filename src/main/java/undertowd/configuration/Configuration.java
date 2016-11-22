package undertowd.configuration;

import lombok.Data;

import java.util.Set;

@Data
public class Configuration {

  private Set<Host> hosts;

}
