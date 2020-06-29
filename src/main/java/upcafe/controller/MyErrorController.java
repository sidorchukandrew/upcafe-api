package upcafe.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController  {
 
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
    	
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	
    	System.out.println(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
    	System.out.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
    	System.out.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
    	
    	if(status != null) {
    		int statusCode = Integer.parseInt(status.toString());
    		
    		if(statusCode == 404) {
    			return "404-error";
    		}
    	}

        return "error";
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
