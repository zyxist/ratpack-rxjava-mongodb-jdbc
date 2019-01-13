Ratpack, RxJava, MongoDB and JDBC integration
=============================================

An example application showing how to integrate Ratpack HTTP server, RxJava,
MongoDB and JDBC, and how to work with a non-blocking I/O.

Building and running
--------------------

You need:

 * Java 1.8
 * Gradle (wrapper available)
 * MongoDB local installation
 
For JDBC, the example uses embedded HSQLDB.
 
Build the code:

```bash
$ ./gradlew :build
```

Run it:

```bash
$ ./gradlew run
```

The REST API will start at [http://localhost:5050](http://localhost:5050/).

Using the API
-------------

Get all the articles from MongoDB database - fully reactive example:

```
GET /mongo/articles
```

Insert new articles into MongoDB database - fully reactive example:

```
POST /mongo/articles
[
   {
      "id": 1,
      "title": "Foo",
      "content": "Lorem ipsum..."
      "references": [
         "http://www.example.com"
      ]
   },
   {
      "id": 2,
      "title": "Bar",
      "content": "Lorem ipsum..."
      "references": [
         "http://www.example.com"
      ]
   }
]
```

Get all the articles from a relational database - mix of a reactive and blocking code:

```
GET /relational/articles
```

Insert new articles into a relational database - mix of a reactive and blocking code:

```
POST /relational/articles
[
   {
      "id": 1,
      "title": "Foo",
      "content": "Lorem ipsum..."
      "references": [
         "http://www.example.com"
      ]
   },
   {
      "id": 2,
      "title": "Bar",
      "content": "Lorem ipsum..."
      "references": [
         "http://www.example.com"
      ]
   }
]
```

License
-------

The code is available under the MIT license.

Author: Tomasz Jędrzejewski ([www.zyxist.com](https://www.zyxist.com/))