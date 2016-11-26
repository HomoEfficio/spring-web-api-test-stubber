package homo.efficio.spring.web.api.test.stubber.generator;

import com.sun.tools.javac.code.Symbol;
import homo.efficio.spring.web.api.test.stubber.restcontroller.extracted.ExtractedRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 25..
 */
public class SpringMvcControllerGenerator {

    private ExtractedRestController restController;
    // TODO JavaPoet을 이용한 Java 파일 생성기 작성 및 추가
    // private XXX

    public SpringMvcControllerGenerator(ExtractedRestController restController) {
        this.restController = restController;
    }


    public void generate() {

        // 클래스의 url 추출
        String[] reqMappedURLsOfClass = restController.getReqMappedURLs();


        for (String reqMappedURL: reqMappedURLsOfClass) {

            // @RequestMapping 붙은 메서드 추출
            List<Symbol.MethodSymbol> requestMappingAnnotatedMethods = restController.getMethodsAnnotatatedWith(RequestMapping.class);

            System.out.println("#######");

            // 메서드 하나 별로 stub 코드 생성
            for (Symbol.MethodSymbol methodSymbol: requestMappingAnnotatedMethods) {

                RequestMapping methodRequestMappingAnnotation = methodSymbol.getAnnotation(RequestMapping.class);

                RequestMethod[] methods = methodRequestMappingAnnotation.method();
                List<String> pathList = Arrays.asList(methodRequestMappingAnnotation.value());

                for (RequestMethod method: methods) {

                    // post나 put처럼 메서드는 있으나 path가 없는 경우 처리
                    if (RequestMethod.POST.equals(method) && pathList.isEmpty())
                        showApiEndPoint(reqMappedURL, method, methodSymbol);

                    else if (RequestMethod.PUT.equals(method) && pathList.isEmpty())
                        showApiEndPoint(reqMappedURL, method, methodSymbol);

                    else {
                        for (String path: pathList) {
                            showApiEndPoint(reqMappedURL + path, method, methodSymbol);
                        }
                    }



                }

            }

        }

        // TODO: 앞에서 구한 reqMappedURLs + B 로 api endpoint C를 구하고
        // TODO: B의 method를 추출해서 method 별로 C에 대한 테스트 코드 생성
        /*
        Create
        mockMvc.perform(
                post("/v1/channel-product/load-real-sample-data")
        ).andExpect(
                status().isOk()
        ).andExpect(
                // jsonPath("$.code").value(200)
        );

        Retrieve
        MvcResult result2 = mockMvc.perform(
                get("/v1/channel-product/channels/BN_MDEAL_7363/channel-products")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.code").value(200)
        ).andExpect(
                jsonPath("$.result.channelCode").value("BN_MDEAL_7363")
        ).andExpect(
                jsonPath("$.result.channelProductCodes").isArray()
        ).andExpect(
                jsonPath("$.result.channelProductCodes[0]").value("123456789")
        ).andReturn();
         */

        // TODO: 클래스 단위로 파일 생성
    }

    private void showApiEndPoint(String apiUrl, RequestMethod method, Symbol.MethodSymbol methodSymbol) {

        System.out.println("URL: " + apiUrl);
        System.out.println("HTTP Method: " + method);
        System.out.println("Handler: " + String.join(" ",
                methodSymbol.getModifiers().toString(),
                methodSymbol.isStatic() ? "static" : "",
                methodSymbol.getReturnType().toString(),
                methodSymbol.getSimpleName().toString(),
                methodSymbol.getParameters().toString()
        ));


    }
}
