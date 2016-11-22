package undertowd;

import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.net.URI;
import java.net.URISyntaxException;

public class Undertowd {

  public static void main(String... args) {
    try {
      LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient()
        .addHost(new URI("http://localhost:8081"))
        .addHost(new URI("http://localhost:8082"))
        .addHost(new URI("http://localhost:8083"))
        .setConnectionsPerThread(20);

      Undertow reverseProxy = Undertow.builder()
        .addHttpListener(8080, "localhost")
        .setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404))
        .build();
      reverseProxy.start();

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}
