package org.wincc.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping(path = "/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping(path = "/history")
    public String history() {
        return "history";
    }

    @GetMapping(path = "/setting")
    public String setting() {
        return "setting";
    }
}
