package com.zero.box.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.util.Map;

import javax.lang.model.element.Modifier;

public class RuntimeGenerator {

    public void generateRuntime(String pkg, File file) {
        ClassName context = ClassName.bestGuess("android.content.Context");
        ClassName packageInfo = ClassName.bestGuess("android.content.pm.PackageInfo");
        ClassName keep = ClassName.bestGuess("androidx.annotation.Keep");
        ClassName boxRuntime = ClassName.bestGuess("com.zero.support.box.plugin.BoxRuntime");
        MethodSpec init = MethodSpec.methodBuilder("init")
                .addAnnotation(keep)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameter(ParameterSpec.builder(String.class, "name").build())
                .addParameter(ParameterSpec.builder(context, "callerContext").build())
                .addParameter(ParameterSpec.builder(ClassLoader.class, "caller").build())
                .addParameter(ParameterSpec.builder(packageInfo, "packageInfo").build())
                .addParameter(ParameterSpec.builder(context, "context").build())
                .addParameter(ParameterSpec.builder(InvocationHandler.class, "handler").build())
                .addParameter(ParameterSpec.builder(Map.class, "extras").build())
                .returns(TypeName.VOID)
                .addCode(CodeBlock.builder()
                        .addStatement("com.zero.support.box.plugin.BoxRuntime.init(name, callerContext, caller, packageInfo, context, handler, extras)")
                        .build())
                .build();


        TypeSpec typeSpec = TypeSpec.classBuilder("BoxInitializer")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(keep)
                .addMethod(init)
                .build();

        JavaFile javaFile = JavaFile.builder(pkg, typeSpec)
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
