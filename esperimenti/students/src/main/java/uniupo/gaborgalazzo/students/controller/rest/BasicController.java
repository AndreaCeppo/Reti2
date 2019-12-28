package uniupo.gaborgalazzo.students.controller.rest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@Profile({"rest-server"})
public class BasicController {

    @GetMapping
    public String index(){
        return "redirect:swagger-ui.html";
    }
}
