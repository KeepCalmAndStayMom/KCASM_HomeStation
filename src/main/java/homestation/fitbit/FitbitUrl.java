package homestation.fitbit;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class FitbitUrl extends GenericUrl {

  @Key
  private String fields;

  FitbitUrl(String encodedUrl) {
    super(encodedUrl);
  }

  public String getFields() {
    return fields;
  }

  void setFields(String fields) {
    this.fields = fields;
  }
}