package com.example.mylibrary.java;


import org.devio.hi.json.HiJson;

class JavaArguments {

    private String raw;
    private HiJson jsonArgsArray;
    private Class<?>[] argsTypes;
    private Object[] argsObjects;

    JavaArguments(String jsonArgs) {
        this.jsonArgsArray = new HiJson(jsonArgs);

        this.raw = jsonArgs;
        this.argsTypes = initArgsTypes();
        this.argsObjects = initArgs();
    }

    public String getRaw() {
        return raw;
    }

    private Class<?>[] initArgsTypes() {
        Class<?>[] argsTypes = new Class<?>[jsonArgsArray.count()];
        for (int i = 0; i < jsonArgsArray.count(); i++) {
            argsTypes[i] = jsonArgsArray.get(i).getClass();
        }
        return argsTypes;
    }

    private Object[] initArgs() {
        Object[] args = new Object[jsonArgsArray.count()];
        for (int i = 0; i < jsonArgsArray.count(); i++) {
            if (jsonArgsArray.get(i) == null) {
                args[i] = null;
            } else {
                args[i] = jsonArgsArray.get(i);
            }
        }
        return args;
    }

    public Object[] getArgs() {
        return argsObjects;
    }

    public Class<?>[] getArgsTypes() {
        return argsTypes;
    }


}
