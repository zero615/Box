package com.zero.box.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class RuntimeGenerator {
    public void generateDefault(String pkg, File dir) {

        generate("BoxResources",BoxConstant.BOX_RESOURCE,pkg,dir);
        generate("BoxContext",BoxConstant.BOX_CONTEXT,pkg,dir);
        generate("BoxRuntime",BoxConstant.BOX_RUNTIME,pkg,dir);
        generate("IBoxPlugin",BoxConstant.BOX_PLUGIN,pkg,dir);

    }
    private void generate(String name,String content,String pkg, File dir){
        File target = new File(dir,pkg.replace(".","/")+"/plugin/"+name+".java");
        try {
            FileUtils.writeStringToFile(target,content.replace(BoxConstant.HOLDER,pkg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateRuntime(String pkg, File file) {
        MethodSpec getPath = MethodSpec.methodBuilder("getPath")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(String.class)
                .addCode(CodeBlock.builder()
                        .addStatement("return $L", "path")
                        .build())
                .build();

        MethodSpec getCallerContext = MethodSpec.methodBuilder("getCallerContext")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(TypeVariableName.get("android.content.Context"))
                .addCode(CodeBlock.builder()
                        .addStatement("return $L", "callerContext")
                        .build())
                .build();

        MethodSpec getCaller = MethodSpec.methodBuilder("getCaller")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(ClassLoader.class)
                .addCode(CodeBlock.builder()
                        .addStatement("return $L", "caller")
                        .build())
                .build();


        MethodSpec init = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addParameter(TypeVariableName.get("android.content.Context"), "context")
                .addParameter(ClassLoader.class, "caller")
                .addParameter(String.class, "path")
                .addParameter(Map.class, "extras")
                .addCode(CodeBlock.builder()
                        .addStatement("BoxRuntime.extras = $L", "extras")
                        .addStatement("BoxRuntime.caller = $L", "caller")
                        .addStatement("BoxRuntime.callerContext = $L", "callerContext")
                        .addStatement("BoxRuntime.path = $L", "path")
                        .build())
                .build();

        MethodSpec getExtra = MethodSpec.methodBuilder("getExtra")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameter(String.class, "key")
                .returns(Object.class)
                .addCode(CodeBlock.builder()
                        .addStatement("return extras.get($L)", "key")
                        .build())
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("BoxRuntime")
                .addModifiers(Modifier.PUBLIC)
                .addField(Map.class, "extras", Modifier.PRIVATE, Modifier.STATIC)
                .addField(String.class, "path", Modifier.PRIVATE, Modifier.STATIC)
                .addField(ClassLoader.class, "caller", Modifier.PRIVATE, Modifier.STATIC)
                .addField(TypeVariableName.get("android.content.Context"), "callerContext", Modifier.PRIVATE, Modifier.STATIC)
                .addMethod(getPath)
                .addMethod(getCallerContext)
                .addMethod(getCaller)
                .addMethod(init)
                .addMethod(getExtra)
                .build();

        JavaFile javaFile = JavaFile.builder(pkg+".plugin", typeSpec)
                .addFileComment("auto generateProxyClass code,can not modify")
                .build();

        try {
            javaFile.writeTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("-----" + e.getMessage());
        }
    }
}
