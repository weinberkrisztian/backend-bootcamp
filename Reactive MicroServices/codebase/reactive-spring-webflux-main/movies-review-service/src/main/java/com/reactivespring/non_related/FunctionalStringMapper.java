package com.reactivespring.non_related;

public class FunctionalStringMapper {

    private static String customString;

    public StringMapper setCustomString(String text){
        customString = text;
        return new StringMapper();
    }

    protected static class StringMapper {

        public StringMapper customizeString(StringMapperFunctionalIF stringMapper){
            customString = stringMapper.handle(customString);
            return this;
        }

        public StringMapper setToUpperCase(){
            customString = customString.toUpperCase();
            return this;
        }

        public String get(){
            return customString;
        }
    }
}
