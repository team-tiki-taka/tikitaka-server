package com.tikitaka.naechinso.global.config.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

/** 스웨거 리디렉션 용 컨트롤러입니다
 * @author gengminy (220812) */
@Controller
@ApiIgnore
public class SwaggerController {
    @GetMapping("/api-docs")
    public String redirectSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
