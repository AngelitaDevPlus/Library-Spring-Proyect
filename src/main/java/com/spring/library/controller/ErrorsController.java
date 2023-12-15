package com.spring.library.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";

        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400:{
                errorMsg = "Angelita's message: Requested resource does not exist.";
                break;
            }
            case 403:{
                errorMsg = "Angelita's message: Not authorize to access the resource.";
                break;
            }
            case 401:{
                errorMsg = "Angelita's message: Not authorized.";
                break;
            }
            case 404:{
                errorMsg = "Angelita's message: Requested resource was not found.";
                break;
            }
            case 500:{
                errorMsg = "Angelita's message: Internal error occur.";
                break;
            }
        }
        errorPage.addObject("code", httpErrorCode);
        errorPage.addObject("message", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (int) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    public String getErrorPath() {
        return "/error";
    }
}
