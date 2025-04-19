package edoardo.patti.writer;

import static java.lang.constant.ConstantDescs.CD_String;

import java.lang.classfile.ClassModel;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.FieldModel;
import java.lang.constant.ClassDesc;
import java.util.ArrayList;
import java.util.List;

public class FieldWriter {

  private static final List<String> QUOTED_DATA_TYPES = List.of(
      "Ljava/lang/String;",
      "Ljava/lang/Character;", "C"
  );

  private static final List<String> UNQUOTED_DATA_TYPES = List.of(
      "Ljava/lang/Short;", "S",
      "Ljava/lang/Double;", "D",
      "Ljava/lang/Byte;", "B",
      "Ljava/lang/Integer;", "I",
      "Ljava/lang/Float;", "F",
      "Ljava/lang/Long;", "J",
      "Ljava/lang/Boolean;", "Z"
  );

  static final List<ClassDesc> classDesc = new ArrayList<>();

  static int write(FieldModel fieldModel, CodeBuilder cb, ClassModel classModel) {
    var alias = fieldModel.fieldName().stringValue();
    var type = fieldModel.fieldType().stringValue();
    if(canParse(fieldModel)) {
      //write name
      cb.ldc("\""+ alias +"\":");
      classDesc.add(CD_String);

      ClassDesc typeDescriptor = ClassDesc.ofDescriptor(type);
      if(!isQuoted(type)) {
        //load this
        cb.aload(0);
        //write value
        cb.getfield(classModel.thisClass().asSymbol(), alias, typeDescriptor);
        classDesc.add(typeDescriptor);
        return 2;
      } else {
        cb.ldc("\"");
        classDesc.add(CD_String);
        //load this
        cb.aload(0);
        //write value
        cb.getfield(classModel.thisClass().asSymbol(), alias, typeDescriptor);
        classDesc.add(typeDescriptor);

        cb.ldc("\"");
        classDesc.add(CD_String);
        return 4;
      }
    }
    return -1;
  }

  static boolean canParse(FieldModel fieldModel) {
    var type = fieldModel.fieldType().stringValue();
    return UNQUOTED_DATA_TYPES.contains(type) || QUOTED_DATA_TYPES.contains(type);
  }

  private static boolean isQuoted(String type) {
    return QUOTED_DATA_TYPES.contains(type);
  }
}
