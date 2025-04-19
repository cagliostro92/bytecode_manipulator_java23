package edoardo.patti.writer;

import static java.lang.constant.ConstantDescs.CD_String;

import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.CallSite;
import java.lang.invoke.StringConcatFactory;

public class StringConcatUtils {

  static DynamicCallSiteDesc getCallSite(int args) {

    var sb = new StringBuilder();
    FieldWriter.classDesc.forEach(f -> sb.append("\u0001")); //see StringConcatFactory#parseRecipe

    return DynamicCallSiteDesc.of(
        ConstantDescs.ofCallsiteBootstrap(
            //owner
            ClassDesc.of(StringConcatFactory.class.getName()),
            //name
            "makeConcatWithConstants",
            //return type
            ClassDesc.of(CallSite.class.getName()),
            //params
//            ClassDesc.of(MethodHandles.Lookup.class.getName()),
//            CD_String,
//            ClassDesc.of(MethodType.class.getName()),
            CD_String,
            ClassDesc.of(Object.class.getName()).arrayType()
        ),
        "makeConcatWithConstants",
        MethodTypeDesc.of(
            //return type
            CD_String,
            //params
            FieldWriter.classDesc
        ),
        sb.toString() //makeConcatWithConstants.recipe
    );
  }
}
