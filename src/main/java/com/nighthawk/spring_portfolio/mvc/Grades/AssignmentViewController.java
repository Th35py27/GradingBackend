package com.nighthawk.spring_portfolio.mvc.Grades;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // HTTP requests are handled as a controller, using the @Controller annotation
public class AssignmentViewController {


    // CONTROLLER handles GET request for /birds, maps it to birds() method
    @GetMapping("/assignment")
    public String assignment() {

        // load HTML VIEW (birds.html)
        return "assignment";



}
}
