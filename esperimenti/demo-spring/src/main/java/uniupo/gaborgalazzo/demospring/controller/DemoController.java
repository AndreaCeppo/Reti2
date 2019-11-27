package uniupo.gaborgalazzo.demospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DemoController {

    @RequestMapping(path = "/", method =  {RequestMethod.GET})
    public String index(){
        return "ciao mondo!";
    }
}
