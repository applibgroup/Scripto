![Scripto: Elegant Java-Javascript bridge](art/Scripto.png)

<p align="center">
<a href="https://bintray.com/imangazaliev/maven/scripto/_latestVersion"><img alt="Download" src="https://api.bintray.com/packages/imangazaliev/maven/scripto/images/download.svg" /></a>
<a href="https://android-arsenal.com/details/1/3983"><img alt="Android Arsenal" src="https://img.shields.io/badge/Android%20Arsenal-Scripto-brightgreen.svg?style=flat" /></a>
</p>

Android-библиотека для отправки сообщений между Java и JavaScript в WebView.

# Подключение библиотеки

```gradle
compile 'com.github.imangazalievm:scripto:2.1.1'
```

# Использование библиотеки

В первую очередь мы должны скопировать файл ```scripto.js``` из тестового проекта в папку assets вашего проекта и инициализировать библиотеку:

```java
WebView webView = ...;
Scripto scripto = new Scripto.Builder(webView).build();
scripto.addJsFileFromAssets("scripto.js");
```

### Вызов JS-функций из Java

Например, у нас есть файл ```login.js``` с некоторыми функциями:

```javascript
function setLogin(login) {
    document.getElementById('login_field').value = login;
}

function getLogin() {
    return document.getElementById('login_field').value;
}
```

Для вызова функций нам нужно создать Java-интерфейс с описанием JS-функций:

```java
public interface LoginScript {

    JavaScriptFunctionCall<Void> setLogin(String login);

    JavaScriptFunctionCall<String> getLogin();

}
```

Методы обязательно должны возвращать JavaScriptFunctionCall. В параметрах JavaScriptFunctionCall мы указываем тип, возвращаемый JS-функцией. В нашем случае первая функция ничего не возвращает (Void), а вторая возвращает строку (String).

Далее нам нужно связать Java-интерфейс и JS-файл:

```java
scripto.addJsFileFromAssets("login.js");
LoginScript loginScript = scripto.create(LoginScript.class);
```

Скрипты обязательно должны быть связаны с интерфейсами до загрузки страницы.

Мы не можем сразу использовать наши скрипт, т. к. нам нужно дождаться полной загрузки страницы. Для этого нужно установить слушатель:

```java
scripto.onPrepared(new ScriptoPrepareListener() {
    @Override
    public void onScriptoPrepared() {
         loginScript.setLogin("MySuperLogin").call();
    }
});
```

Для получения данных из функции используйте следующий синтаксис:

```java
loginScript.getLogin()
    .onResponse(login -> Toast.makeText(MainActivity.this, login, Toast.LENGTH_LONG).show())
    .call();
```

Также мы можем обрабатывать ошибки, произошедшие в JS-коде:

```java
.onError(error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show())
```

Для конвертации пользовательских типов данных из JSON используется библиотека GSON.

Если вы хотите получить "голый" JSON, то обязательно используйте класс RawReponse:

```java
JavaScriptFunctionCall<RawResponse> getJson();
```

Получаем JSON:

```java
loginScript.getJson()
    .onResponse(response -> Toast.makeText(MainActivity.this, response.getResponse(), Toast.LENGTH_LONG).show())
    .call();
```

Также мы можем вызывать у функции у объектов, добавив над интерфейсом, описывающем JS-функцию аннотацию JsVariableName:

```
public interface LoginScript {

@JsVariableName("myVar")
public interface LoginScript {

    JavaScriptFunctionCall<Void> setLogin(String login);

    JavaScriptFunctionCall<String> getLogin();

}
```

Аннотацию JsVariableName можно применять к отдельному методу:

```
@JsVariableName("myVar")
JavaScriptFunctionCall<String> getLogin();
```

Если названия метода и вызываемой функции должны быть разными, воспользуйтесь аннотацией @JsFunctionName:

```
@JsFunctionName("myFuncName")
JavaScriptFunctionCall<String> getLogin();
```

### Вызов Java-методов из JavaScript

Вызов Java-методов из JavaScript очень сильно похож, на вызов с помощью JavaScriptInterface. Создайте Java-класс, который будет выступать в качестве JS-интерфейса:

```java
public class AndroidInterface {

    private Context context;

    public AndroidInterface(Context context) {
        this.context = context;
    }

    public void showToastMessage(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
```
Обязательным условием корректной работы интерфейса является, то чтобы в нем не было методов с одинаковыми именами. В ином случае библиотека выдаст сообщение об ошибке. Также нам не нужно ставить над методами аннотацию ```@JavaScriptInterface```.

Добавляем интерфейс:

```java
scripto.addInterface("Android", new AndroidInterface(context));
```

Для вызова метода ```showToastMessage``` нам нужно создать одноименную JS-функцию:

```javascript
function showToastMessage(text) {
  Scripto.call('Android', arguments);
};
```

В ней мы вызываем специальную функцию из нашей библиотеки и передаем ей название JS-интерфейса и аргументы функции.

Вызываем метод из JavaScript:

```javascript
showToastMessage("My super message");
```

Точно также, как и в Java мы можем использовать коллбеки:
```java
public String showToastMessage(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        return "My super response";
}
```

Вызываем метод из JavaScript:

```javascript
showToastMessage("My super message", function(responseString) {
        console.log(responseString);
});
```

Если вам нужно передать из JavaScript пользовательский тип данных, сконвертируйте его в JSON при помощи ```JSON.stringify(object)```.

Если вы хотите защитить методы от несанкционированного вызова, то вы можете использовать защиту при помощи аннотации ```@JavaScriptSecure```:

```java
ScriptoInterfaceConfig config = new ScriptoInterfaceConfig().enableAnnotationProtection(true);
scripto.addInterface("Android", new AndroidInterface(this), config);
```

Не забудьте установить аннотацию над методом:

```java
@JavaScriptSecure
public void showToastMessage(String text) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
}
```

## Лицензия

```
The MIT License

Copyright (c) 2016 Mahach Imangazaliev 

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```