package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class handling default routes and redirects in the application.
 */
@Controller
public class DefaultController {

    /**
     * Handles the root ("/") endpoint, directing to the "index" view.
     *
     * @param model the model for the view
     * @return the name of the view to render
     */
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * Handles requests to any path that does not contain a file extension, redirecting to the root ("/").
     *
     * @return the forward URL to redirect to the root
     */
    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}