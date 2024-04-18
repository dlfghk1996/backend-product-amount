package antigravity.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface BaseEnum {
  String getName();

  String getLabel();
}
