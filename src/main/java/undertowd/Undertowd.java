package undertowd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import undertowd.configuration.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient();
    configuration.getHosts().forEach(h -> {
      h.getPaths().forEach(p -> {
        p.getProxies().forEach(proxy -> {
          try {
            loadBalancer.addHost(new URI(proxy));
          } catch (URISyntaxException e) {
            e.printStackTrace();
          }
        });
      });
    });

    Undertow.Builder builder = Undertow.builder();
    configuration.getHosts().forEach(h -> {
      builder
        .addHttpListener(h.getPort(), h.getHost())
        .setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404));
    });
    Undertow server = builder.build();

    server.start();
  }

}
