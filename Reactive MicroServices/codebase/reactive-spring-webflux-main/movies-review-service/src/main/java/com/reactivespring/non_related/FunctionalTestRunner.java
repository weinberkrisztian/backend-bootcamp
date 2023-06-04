package com.reactivespring.non_related;

public class FunctionalTestRunner {

    public String runFunctionalStringMapper(String testText){
        FunctionalStringMapper func = new FunctionalStringMapper();
        String customString = func.setCustomString(testText).customizeString(text -> text.concat("_functional")).setToUpperCase().get();
        System.out.println(customString);
        return customString;
    }



}
