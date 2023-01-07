package com.example.garage.Utilities;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class Utilities {
    public static String getErrorString(BindingResult br) {
        //something's wrong
        StringBuilder sb = new StringBuilder();
        for (
                FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField() + ": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }


}