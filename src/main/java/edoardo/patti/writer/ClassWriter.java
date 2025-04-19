package edoardo.patti.writer;

import static java.lang.constant.ConstantDescs.CD_String;

import edoardo.patti.model.Model;
import java.io.IOException;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.FieldModel;
import java.lang.classfile.MethodModel;
import java.lang.constant.MethodTypeDesc;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassWriter {

  public static void main(String[] args) throws IOException {
    write(Model.class);
  }

  public static void write(Class<?> clazz) throws IOException {
    var attributes = new ArrayList<FieldModel>();
    var location = "./target/classes/" + clazz.getName().replace(".", "/") + ".class";
    ClassFile cf = ClassFile.of();
    var locationPath = Path.of(location);
    ClassModel classModel = cf.parse(Files.readAllBytes(locationPath));
    byte[] newBytes = cf.build(classModel.thisClass().asSymbol(),
        classBuilder -> {
          for (ClassElement ce : classModel) {
            if(ce instanceof FieldModel fm) {
              attributes.add(fm);
            }
            //let's re-write toString method excluding the current one if exists
            if(!(ce instanceof MethodModel mm && mm.methodName().equalsString("toString")))
              classBuilder.with(ce);
          }
          classBuilder.withMethod("toString", MethodTypeDesc.of(CD_String),1, mb -> {
            mb.withCode(cb -> {
              AtomicInteger argz = new AtomicInteger();
              cb.ldc("{");
              FieldWriter.classDesc.add(CD_String);
              for(int i = 0; i < attributes.size(); i++) {
                if(i > 0 && FieldWriter.canParse(attributes.get(i-1))) {
                  cb.ldc(",");
                  FieldWriter.classDesc.add(CD_String);
                  argz.getAndIncrement();
                }
                var fm = attributes.get(i);
                int written = FieldWriter.write(fm, cb, classModel);
                if(written != -1) {
                  argz.getAndAdd(written);
                }
              }
//              cb.aload(0);
//              cb.getfield(classModel.thisClass().asSymbol(), "pets", CD_List);
              //call static method String.valueOf on the current stack operand
//              cb.invokestatic(CD_String, "valueOf", MethodTypeDesc.of(CD_String, CD_Object));
              cb.ldc("}");
              FieldWriter.classDesc.add(CD_String);
              cb.invokedynamic(
                  StringConcatUtils.getCallSite(argz.get()+2)
              );
              cb.areturn();
            });
          });
        });
    Files.write(locationPath, newBytes);
  }
}
