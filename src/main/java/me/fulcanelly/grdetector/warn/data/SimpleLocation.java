package me.fulcanelly.grdetector.warn.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
@Builder
public class SimpleLocation {
  int x, y, z;

  WorldType worldType;

  public SimpleLocation max(SimpleLocation another) {
    return new SimpleLocation(
        Math.max(x, another.x),
        Math.max(y, another.y),
        Math.max(z, another.z),
        worldType);
  }

  public SimpleLocation min(SimpleLocation another) {
    return new SimpleLocation(
        Math.min(x, another.x),
        Math.min(y, another.y),
        Math.min(z, another.z),
        worldType);
  }

}
