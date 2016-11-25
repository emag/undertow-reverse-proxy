package undertowd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.Undertow;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import undertowd.configuration.Configuration;
import undertowd.configuration.File;
import undertowd.configuration.Host;
import undertowd.fileserving.FileServing;
import undertowd.reverseproxy.ReverseProxy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class Undertowd {

  public static void main(String... args) {
    CommandLineOption option = new CommandLineOption();
    CmdLineParser parser = new CmdLineParser(option);

    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      parser.printUsage(System.err);
      System.exit(1);
    }

    ObjectMapper mapper = new ObjectMapper();
    Configuration configuration = null;
    try {
      configuration = mapper.readValue(Files.newBufferedReader(Paths.get(option.getConfigPath())), Configuration.class);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Set<Host> hosts = configuration.getHosts();
    hosts.forEach(host -> {
      Undertow.Builder builder = Undertow.builder();

      builder.addHttpListener(host.getPort(), host.getHost());

      host.getPaths().forEach(path -> {
        if (path.getProxies() != null && !path.getProxies().isEmpty()) {
          new ReverseProxy().create(builder, path.getProxies());
        }
        if (path.getFile() != null) {
          File file = path.getFile();
          new FileServing().create(builder, file.getDir(), file.isListingEnabled());
        }
      });

      builder.build().start();
    });

  }

}
