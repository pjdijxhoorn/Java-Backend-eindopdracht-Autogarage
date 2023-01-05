package com.example.garage.Utilities;

public class licenseplateValidator {

    public static Boolean validateLicensePlate(String licenseplate){
        if (licenseplate.matches("..-..-..")){
            // Dutch cars from 1951 to 2005
            return true;
        } else if (licenseplate.matches("^[0-9][0-9]-[A-Z][A-Z][A-Z]-[0-9]$")){
            // Dutch cars from 2005
            return true;}
        else if (licenseplate.matches("^[0-9]-[A-Z][A-Z][A-Z]-[0-9][0-9]$")){
            // Dutch cars from 2009
            return true;
        } else if (licenseplate.matches("^[A-Z][A-Z]-[0-9][0-9][0-9]-[A-Z]$")){
            // Dutch cars from 2006
            return true;
        }
        else if (licenseplate.matches("^[A-Z]-[0-9][0-9][0-9]-[A-Z][A-Z]$")){
            // Dutch cars from 2008
            return true;
        }
        else if (licenseplate.matches("^[A-Z][A-Z][A-Z]-[0-9][0-9]-[A-Z]$")){
            // Dutch cars from 2015
            return true;
        }
        else if (licenseplate.matches("^[0-9]-[A-Z][A-Z]-[0-9][0-9][0-9]$")){
            // Dutch cars from 2016
            return true;
        }
        else if (licenseplate.matches("^[0-9][0-9][0-9]-[A-Z][A-Z]-[0-9]$")){
            // Dutch cars from 2019
            return true;
        }else {
            return false;
        }
    }
}
