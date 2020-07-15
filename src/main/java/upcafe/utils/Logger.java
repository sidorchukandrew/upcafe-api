package upcafe.utils;

import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.Error;

public class Logger {

	public static void logApiExceptions(ApiException exception) {
		int errorNumber = 1;
    	
    	for(Error error : exception.getErrors()) {
    		System.out.println("API Error No. :  " + errorNumber + " out of   "  + exception.getErrors().size());
    		System.out.println("\tField : " + error.getField());
    		System.out.println("\tCode : " + error.getCode());
    		System.out.println("\tDetail : " + error.getDetail());
    		System.out.println("\tCategory : " + error.getCategory());
    		errorNumber++;
    	}
	}
}
