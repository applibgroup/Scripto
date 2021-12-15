package com.example.mylibrary.js;


import com.example.mylibrary.Scripto;
import com.example.mylibrary.converter.JavaToJsonConverter;
import com.example.mylibrary.utils.ScriptoUtils;


class JavaScriptArguments {

    private Scripto scripto;
    private String[] stringArgs;

    JavaScriptArguments(Scripto scripto, Object[] argsObjects) {
        this.scripto = scripto;
        this.stringArgs = initArgs(argsObjects);
    }

    private String[] initArgs(Object[] argsObjects) {
        //the function was called without arguments
        if (argsObjects == null) {
            return new String[0];
        }

        JavaToJsonConverter javaToJsonConverter = scripto.getJavaToJsonConverter();
        String[] resultArgs = new String[argsObjects.length];

        for (int i = 0; i < argsObjects.length; i++) {
            Object argument = argsObjects[i];
            //конвертируем аргумент в строку
            if (argument == null) {
                resultArgs[i] = "null";
            } else if (ScriptoUtils.isPrimitiveWrapper(argument.getClass()) || argument.getClass().isPrimitive()) {
                //если это примитивные типы или обертки, просто конвертируем в строку
                resultArgs[i] = String.valueOf(argument);
            } else if (argument.getClass() == String.class) {
                //добавляем к строке кавычки для правильной передачи
                resultArgs[i] = String.format("'%s'", argument);
            } else {
                //если аргумент является объектом, конвертируем в json
                Object arg = argsObjects[i];
                resultArgs[i] =  resultArgs[i] = String.format("'%s'", javaToJsonConverter.convertToString(arg, arg.getClass()));
            }
        }
        return resultArgs;
    }

    public String[] getArguments() {
        return stringArgs;
    }

    public String getFormattedArguments() {
        String resultArgsString = "";
        for (int i = 0; i < stringArgs.length; i++) {
            //если аргумент не первый, добавляем запятую
            resultArgsString += (i == 0) ? stringArgs[i] : "," + stringArgs[i];
        }
        return resultArgsString;
    }

}
