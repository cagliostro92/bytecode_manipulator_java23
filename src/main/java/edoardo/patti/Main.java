package edoardo.patti;

import edoardo.patti.model.Model;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    System.out.println(getModel());
  }

  private static Model getModel() {
    Model model = new Model();

    // Set boxed types
    model.setBoxedBoolean(Boolean.TRUE);
    model.setBoxedByte(Byte.valueOf((byte) 10));
    model.setBoxedChar(Character.valueOf('A'));
    model.setBoxedShort(Short.valueOf((short) 100));
    model.setBoxedInt(Integer.valueOf(1000));
    model.setBoxedLong(Long.valueOf(10000L));
    model.setBoxedFloat(Float.valueOf(3.14f));
    model.setBoxedDouble(Double.valueOf(9.81));

    // Set primitive types
    model.setPrimitiveBoolean(true);
    model.setPrimitiveByte((byte) 20);
    model.setPrimitiveChar('Z');
    model.setPrimitiveShort((short) 200);
    model.setPrimitiveInt(2000);
    model.setPrimitiveLong(20000L);
    model.setPrimitiveFloat(6.28f);
    model.setPrimitiveDouble(19.62);

    return model;
  }
}