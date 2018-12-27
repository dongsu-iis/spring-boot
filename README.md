# spring-boot
spring bootの学習メモ

## 入門

### spring bootプロジェクト作成

spring bootを作成するには３種類の方法が存在する

1. IDE : Eclipse, Intellij
2. [公式INITIALIZR](https://start.spring.io/) : Web上で作成
3. Spring Boot CLI

### Hello World

IDEでプロジェクト作成後にwebパッケージ作成する。
Controllerクラスを新規追加する。

```java
package com.ds.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// コントローラの注釈
@RestController
public class HelloController {

    // リクエスト注釈
    @RequestMapping("/say")
    public String hello(){
        return "Hello Spring Boot";
    }
}
```