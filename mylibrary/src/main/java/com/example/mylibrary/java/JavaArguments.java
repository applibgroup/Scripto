package com.example.mylibrary.java;


import org.devio.hi.json.JSONArray;
import org.devio.hi.json.JSONException;

class JavaArguments {

    private String raw;
    private JSONArray jsonArgsArray;
    private Class<?>[] argsTypes;
    private Object[] argsObjects;

    JavaArguments(String jsonArgs) throws JSONException {
        this.jsonArgsArray = new JSONArray(jsonArgs);

        this.raw = jsonArgs;
        this.argsTypes = initArgsTypes();
        this.argsObjects = initArgs();
    }

    public String getRaw() {
        return raw;
    }

    private Class<?>[] initArgsTypes() throws JSONException {
        Class<?>[] argsTypes = new Class<?>[jsonArgsArray.length()];
        for (int i = 0; i < jsonArgsArray.length(); i++) {
            argsTypes[i] = jsonArgsArray.get(i).getClass();
        }
        return argsTypes;
    }

    private Object[] initArgs() throws JSONException {
        Object[] args = new Object[jsonArgsArray.length()];
        for (int i = 0; i < jsonArgsArray.length(); i++) {
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
