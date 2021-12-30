package com.example.myapplication.script;

import com.example.myapplication.User;
import com.example.mylibrary.js.JavaScriptFunctionCall;
import com.example.mylibrary.js.JsFunctionName;

public interface UserInfoScript {
    @JsFunctionName("getUserData")
    JavaScriptFunctionCall<User> getUser();
}
