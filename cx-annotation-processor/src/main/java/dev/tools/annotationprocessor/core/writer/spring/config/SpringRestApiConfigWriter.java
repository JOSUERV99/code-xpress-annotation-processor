package dev.tools.annotationprocessor.core.writer.spring.config;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dev.tools.annotationprocessor.db.executer.JdbcTemplateHolder;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.Modifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class SpringRestApiConfigWriter {

  private static String CONFIG_CLASS_NAME = "BeansConfiguration";

  private String getBeanName(final String className) {
    return ("" + className.charAt(0)).toLowerCase() + className.substring(1);
  }

  private MethodSpec methodSpecForBeanConfigForNamedJdbcTemplate() {
    final ClassName classNameForJdbcTemplate = ClassName.get(NamedParameterJdbcTemplate.class);
    return MethodSpec.methodBuilder(getBeanName(classNameForJdbcTemplate.simpleName()))
        .addAnnotation(Bean.class)
        .addStatement("return $T.getConnection()", JdbcTemplateHolder.getConnection())
        .returns(NamedParameterJdbcTemplate.class)
        .build();
  }

  public JavaFile writeBeansConfigForApi(
      final String packageName,
      final List<ClassName> layers,
      final List<ClassName> layersDependencies) {
    final Collection<MethodSpec> beanMethods =
        IntStream.range(0, layers.size())
            .mapToObj(
                index -> {
                  String className = layers.get(index).simpleName();
                  String beanName = getBeanName(className);

                  String paramClassName = layersDependencies.get(index).simpleName();
                  String paramName = getBeanName(paramClassName);

                  return MethodSpec.methodBuilder(beanName)
                      .addAnnotation(Bean.class)
                      .addParameter(layersDependencies.get(index), paramName, Modifier.FINAL)
                      .addStatement("return new $T($N)", layers.get(index), paramName)
                      .returns(layers.get(index))
                      .build();
                })
            .collect(Collectors.toList());
    // beanMethods.add(methodSpecForBeanConfigForNamedJdbcTemplate());

    final TypeSpec.Builder typeSpecBuilder =
        TypeSpec.classBuilder(CONFIG_CLASS_NAME)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Configuration.class)
            .addField(
                FieldSpec.builder(Environment.class, "env", Modifier.PRIVATE)
                    .addAnnotation(Autowired.class)
                    .build());

    //        for (MethodSpec beanMethod : beanMethods) {
    //            typeSpecBuilder.addMethod(beanMethod);
    //        }

    return JavaFile.builder(packageName, typeSpecBuilder.build()).build();
  }
}
