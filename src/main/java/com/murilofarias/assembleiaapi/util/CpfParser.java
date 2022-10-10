package com.murilofarias.assembleiaapi.util;

public class CpfParser {

    public static String eliminateDotsAndDashes(String cpf){
        String withoutDots = cpf.replace(".", "");
        String withoutDotsAndDashes = withoutDots.replace("-", "");
        return withoutDotsAndDashes;
    }
}
