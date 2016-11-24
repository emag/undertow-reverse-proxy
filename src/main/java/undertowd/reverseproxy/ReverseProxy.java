package undertowd.reverseproxy;

import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import undertowd.configuration.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

public class ReverseProxy {

  public Undertow.Builder create(Undertow.Builder builder, Configuration configuration) {
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

    configuration.getHosts().forEach(h -> {
      builder
        .addHttpListener(h.getPort(), h.getHost())
        .setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404));
    });

    return builder;
  }

}
