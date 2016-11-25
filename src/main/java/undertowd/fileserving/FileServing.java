package undertowd.fileserving;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;

import java.nio.file.Paths;

import static io.undertow.Handlers.resource;

public class FileServing {

  public Undertow.Builder create(Undertow.Builder builder, String dir, boolean listingEnabled) {
    return builder.setHandler(resource(new PathResourceManager(Paths.get(dir), 100))
      .setDirectoryListingEnabled(listingEnabled));
  }

}
