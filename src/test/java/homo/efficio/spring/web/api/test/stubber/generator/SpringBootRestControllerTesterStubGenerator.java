package homo.efficio.spring.web.api.test.stubber.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;
import homo.efficio.spring.web.api.test.stubber.restcontroller.extracted.RequestMappingMethodModel;
import homo.efficio.spring.web.api.test.stubber.restcontroller.extracted.RestControllerModel;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.lang.model.element.Modifier;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 25..
 */
public class SpringBootRestControllerTesterStubGenerator {

    private static final String TEST_METHOD_SUFFIX = "Test";
    private static final String OUTPUT_DIR = "src/test/java";
    private static final String OUTPUT_PKG = "YOUR.PACKAGE";

    private RestControllerModel restControllerModel;

    public SpringBootRestControllerTesterStubGenerator(RestControllerModel restControllerModel) {
        this.restControllerModel = restControllerModel;
    }


    public void generate() throws IOException {

        List<MethodSpec> methodSpecs = getTestMethodSpecs();

        List<FieldSpec> fieldSpecs = buildFieldSpecs();

        MethodSpec setUp = buildSetUpSpec();

        TypeSpec restController = buildTypeSpec(methodSpecs, fieldSpecs, setUp);

        JavaFile javaFile = buildJavaFile(restController);

        javaFile.writeTo(new File(OUTPUT_DIR));
    }

    private JavaFile buildJavaFile(TypeSpec restController) {
        return JavaFile.builder(OUTPUT_PKG, restController)
                .indent("    ")
                .addStaticImport(MockMvcRequestBuilders.class, "*")
                .addStaticImport(MockMvcResultMatchers.class, "*")
                .addStaticImport(MockMvcResultHandlers.class, "print")
                .addStaticImport(Matchers.class, "is")
                .build();
    }

    private TypeSpec buildTypeSpec(List<MethodSpec> methodSpecs, List<FieldSpec> fieldSpecs, MethodSpec setUp) {
        return TypeSpec.classBuilder(this.restControllerModel.getSimpleClassName())
                .addAnnotation(Transactional.class)
                .addAnnotation(
                        AnnotationSpec.builder(RunWith.class)
                                      .addMember("value", "$T.class", SpringJUnit4ClassRunner.class)
                                      .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(ComponentScan.class)
                                      .addMember("basePackages", "{$S, $S}", "YOUR_DTOs_PACKAGE", "YOUR_SERVICEs_PACKAGE")
                                      .build()
                )
                .addAnnotation(SpringBootTest.class)
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs)
                .addMethod(setUp)
                .addMethods(methodSpecs)
                .build();
    }

    private MethodSpec buildSetUpSpec() {
        return MethodSpec.methodBuilder("setUp")
                .addAnnotation(Before.class)
                .addModifiers(Modifier.PUBLIC)
                .addCode(""+
                        "mockMvc = $T.webAppContextSetup(ctx)\n" +
                        "    .alwaysDo(print())\n" +
                        "    .build();",
                        MockMvcBuilders.class
                )
                .build();
    }

    private List<FieldSpec> buildFieldSpecs() {

        FieldSpec mockMvc = FieldSpec.builder(MockMvc.class, "mockMvc")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec ctx = FieldSpec.builder(WebApplicationContext.class, "ctx")
                .addAnnotation(Autowired.class)
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec objectMapper = FieldSpec.builder(ObjectMapper.class, "objectMapper")
                .addAnnotation(Autowired.class)
                .addModifiers(Modifier.PRIVATE)
                .build();

        return Arrays.asList(mockMvc, ctx, objectMapper);
    }

    private List<MethodSpec> getTestMethodSpecs() {
        List<MethodSpec> methodSpecs = new ArrayList<>();

        String[] reqMappedURLsOfClass = restControllerModel.getReqMappedURLs();

        for (String reqMappedURL: reqMappedURLsOfClass) {

            List<RequestMappingMethodModel> requestMappingAnnotatedMethods = restControllerModel.getRequestMappingMethodModels();

            for (RequestMappingMethodModel requestMappingAnnotatedMethod: requestMappingAnnotatedMethods) {

                RequestMethod[] methods = requestMappingAnnotatedMethod.getReqMethods();
                List<String> pathList = Arrays.asList(requestMappingAnnotatedMethod.getPaths());

                if (methods.length == 0) {

                }

                for (RequestMethod reqMethod: methods) {

                    if (RequestMethod.POST.equals(reqMethod) && pathList.isEmpty())
                        methodSpecs.add(createApiTestMethodSpec(requestMappingAnnotatedMethod.getMethodName() + methodSpecs.size(), reqMappedURL, reqMethod));

                    else if (RequestMethod.PUT.equals(reqMethod) && pathList.isEmpty())
                        methodSpecs.add(createApiTestMethodSpec(requestMappingAnnotatedMethod.getMethodName() + methodSpecs.size(), reqMappedURL, reqMethod));

                    else {
                        for (String path: pathList) {
                            methodSpecs.add(createApiTestMethodSpec(requestMappingAnnotatedMethod.getMethodName() + methodSpecs.size(), reqMappedURL + path, reqMethod));
                        }
                    }
                }
            }
        }
        return methodSpecs;
    }

    private MethodSpec createApiTestMethodSpec(String methodName, String apiUrl, RequestMethod reqMethod) {
        return MethodSpec.methodBuilder(methodName + TEST_METHOD_SUFFIX)
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(Exception.class)
                .addCode("" +
                        "$T result = mockMvc.perform(\n" +
                        "        $L($S)\n" +
                        ").andExpect(\n" +
                        "        status().isOk()\n" +
                        ").andExpect(\n" +
                        "        jsonPath($S).value($S)\n" +
                        ").andReturn();\n",
                        MvcResult.class,
                        reqMethod.name().toLowerCase(),
                        apiUrl,
                        "$.YOURKEY",
                        "EXPECTED_VALUE"
                )
                .build();
    }
}
