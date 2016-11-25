package undertowd.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class File {

  private String dir;
  @JsonProperty("listing_enabled")
  private boolean listingEnabled;

}
