package undertowd.reverseproxy;

import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ReverseProxy {

  public Undertow.Builder create(Undertow.Builder builder, Set<String> proxies) {
    LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient();

    proxies.forEach(proxy -> {
      try {
        loadBalancer.addHost(new URI(proxy));
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    });

    return builder.setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404));
  }

}
