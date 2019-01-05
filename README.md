# spring-boot
spring bootの学習メモ

## 入門編

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

### RESTful URL

クライアントのデバイスの多様化（PC,スマホ,iPad, etc）に伴い、サーバーサイド側の統一する設計が必要になった。

本章はWebApiを通して、API開発基礎を学習する。

今回作成するRESTful一覧

- **GET**    http://localhost:8080/api/vi/books    書籍一覧取得
- **POST**   http://localhost:8080/api/vi/books    １冊の本を新規作成
- **GET**    http://localhost:8080/api/vi/books/{id}  １冊の本を取得
- **PUT**    http://localhost:8080/api/vi/books 1冊の本を更新
- **DELETE** http://localhost:8080/api/vi/books/{id} 1冊の本を削除

クラス上に注釈を追加することで、そのクラスはWEBコントローラーになる。

```java
@RestController
@RequestMapping("/api")
public class HelloController {}
```

メソッド上に注釈を追加することで、そのメソッドはWEBリクエスト受付の機能ができる。

```java
@RequestMapping(value = "/say",method = RequestMethod.GET)
public String hello(){
    return "hello";
}

// GETリクエスト（下が省略の書き方）
@RequestMapping(value = "/say",method = RequestMethod.GET)
@GetMapping("/say")

// POSTリクエスト（下が省略の書き方）
@RequestMapping(value = "/say",method = RequestMethod.POST)
@PostMapping("/say")
```

[**PostMan**](https://www.getpostman.com/)を使えばget,postなどのリクエストを投げることができる。  
ただ、社内プロキシ環境ではローカルホストへのリクエストがうまく接続できない。  
Chromeの拡張機能のPostManを使えば対応できる。（拡張機能は廃止予定だが...）


リクエストのパラメータ受け取り方法は２種類ある。

* PathVariable

```java
@GetMapping("/books/{id}/{username:[a-z_]+}")
public Object getOne(@PathVariable long id,
                     @PathVariable String username){}

```

* RequestParam

```java
@PostMapping("/books")
public Object post(@RequestParam("name") String name,
                   @RequestParam("author") String author,
                   @RequestParam("isbn") String isbn
                   // デフォルト値設定
                   @RequestParam(value = "size",defaultValue = "10") int size){
```

### プロパティファイル

resourcesの直下のapplication.propertiesファイルで設定を変更することができる。  
※resources/configに配置しても同じく認識する

```properties
server.port=8082
```

yml形式（application.yml）プロパティファイルにすることもできる。

```yml
server:
  port: 8082
  servlet:
    context-path: /api
logging:
  level:
    root: warn
    com.lrm: debug
  file: logs/my.log
```

#### 自己定義プロパティ

自分で定義した値もプロパティファイルで利用可能。

```yml
book:
  name: インターネットの世界観
  author: 田中洋一
  isbn: ${random.uuid}
  desctiption: ${book.name},この本は悪くない
```

```java
@Value("${book.name}")
private String name;

@Value("${book.author}")
private String author;

@Value("${book.isbn}")
private String isbn;

@Value("${book.desctiption}")
private String desctiption;
```

定義した値をインスタンスに注入する

```java
@Component
@ConfigurationProperties(prefix = "book")
public class Book {

    private String name;

    private String author;

    private String isbn;

    private String desctiption;

    public Book(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }
}
```

```java
public class HelloController {

    @Autowired
    private Book book;

    @GetMapping("/books/{id}")
    public Object getOne(@PathVariable long id){

        return book;
    }
```

#### 環境ごとプロパティファイル

環境ごとのプロパティファイルを定義する場合、以下の命名規則で環境ごとのファイルを作成する。

* 命名規則  
application-{profile}.yml

例

```yml:application.yml
spring:
  profiles:
    active: dev

book:
  name: インターネットの世界観
  author: 田中洋一
  isbn: ${random.uuid}
  desctiption: ${book.name},この本は悪くない
```

```yml:application-dev.yml
server:
  port: 8080
  #Sservlet:
    #context-path: /api
logging:
  level:
    root: debug
    com.ds: debug
  file: logs/debug.log
```

