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
ただ、社内環境ではローカルホストへのリクエストがプロキシに阻まれてうまく接続できない。  
Chromeの拡張機能のPostManを使えば対応できる。（拡張機能は廃止予定だが...）

**追記**

Chromeの拡張機能[**Advanced REST client**](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo?hl=ja)は社内環境でも使える。


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

### JPA

JPA(Java Prosistence API)は関係データベースのデータを扱うアプリケーションを開発するためのJava用フレームワークである。

Spring Data JPAは、Java Persistence API（JPA）のリポジトリサポートを提供する。  
JPAデータソースにアクセスする必要があるアプリケーションの開発を容易にする。

#### JPA初期設定

Maven（pom.xml）の参照を追加

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

プロパティファイル

```yml
spring:
  datasource:
    data-username: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/book?useUnicode=true&characterEncoding=UTF-8&serverTimezone=JST
    username: root
    password: password
  jpa:
    hibernate:
      # ddl-autoをcreateに設定すると、APP起動するたび既存のデータが削除される。updateが一般的
      ddl-auto: update
    # SQL文の出力
    show-sql: true
```

Entityクラス

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // アノテーションEntityを注入
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //これを付けないとJsonErrorになる…
public class Book {

    @Id // 主キー
    @GeneratedValue(strategy = GenerationType.AUTO) // 生成方法:自動
    private long id;

    private String name;

    public Book(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```


#### JPA基本操作

エンティティのリポジトリのインターフェースを作成

```java
package com.ds.domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

}
```

リポジトリを呼び出すサービスクラスを作成

```java
package com.ds.service;

import com.ds.domain.Book;
import com.ds.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService{

    // リポジトリ
    @Autowired
    private BookRepository bookRepository;

    /**
     * 一覧取得
     * @return
     */
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    /**
     * 新規作成or更新
     * @param book
     * @return
     */
    public Book sava(Book book){
        return bookRepository.save(book);
    }

    /**
     * 1件取得
     * @param id
     * @return
     */
    public Book getOne(long id){
        return bookRepository.getOne(id);
    }

    /**
     * 本を１冊削除
     * @param id
     */
    public void deleteOne(long id){
        bookRepository.deleteById(id);
    }
}
```

リクエストを受け付けるコントローラーを作成

```java
package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookApp {

    // サービス
    @Autowired
    private BookService bookService;

    /**
     * 書籍一覧を取得
     * @return
     */
    @GetMapping("/books")
    public List<Book> getAll(){
        return bookService.findAll();
    }

    /**
     * 新規作成
     * @return
     */
    @PostMapping("/books")
    public Book post(Book book){
//        Book book = new Book();
//        book.setName(name);
//        book.setAuthor(author);
//        book.setDesctiption(description);
//        book.setStatus(status);
        return bookService.sava(book);
    }


    /**
     * 本を1件を取得
     * @param id
     * @return
     */
    @GetMapping("/books/{id}")
    public Book getOne(@PathVariable long id){
        return bookService.getOne(id);
    }

    /**
     * 本1件を更新
     * @param id
     * @param name
     * @param author
     * @param description
     * @param status
     * @return
     */
    @PutMapping("/books")
    public Book update(@RequestParam long id,
                       @RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String description,
                       @RequestParam int status){
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setDesctiption(description);
        book.setStatus(status);
        return bookService.sava(book);
    }

    /**
     * 本1件削除処理
     * @param id
     */
    @DeleteMapping("/books/{id}")
    public void deleteOne(@PathVariable long id){
        bookService.deleteOne(id);
    }
}
```

**注意点**

**POST**メソッドのリクエストBodyのパラメータ形式は**form-data**で問題ないが、**PUT**メソッドのリクエストBodyのパラメータ形式は**x-www-form-urlencoded**にする必要がある。

実業務ではリクエストはGETとPOSTだけ使われることが多い。
