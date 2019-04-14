package org.wincc.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(final Model model) {
        return "default";
    }

    @GetMapping("/overview")
    public String overview() {
        return "index";
    }
}
