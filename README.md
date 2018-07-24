[![Build Status](https://travis-ci.org/bleshik/aws-lambda-servlet.svg?branch=master)](https://travis-ci.org/bleshik/aws-lambda-servlet)
# AWS Lambda adapter for Java's Servlets
The idea behind this little adapter is to have a way of running APIs implemented in Java on AWS Lambda. This apadater basically takes the incoming Lambda Event and transform it to an HTTP Servet Request and pass it directly to the given servlet. No http server is used in this process.

Add the project's bintray repository in your pom.xml:
```
<repositories>
    <repository>
        <id>bintray</id>
        <url>http://dl.bintray.com/bleshik/maven</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

Then the dependency itself:
```
<dependency>
    <groupId>com.github.bleshik</groupId>
    <artifactId>aws-lambda-servlet</artifactId>
    <version>1.0</version>
</dependency>
```

Then you just go like this:
```
public class ExampleAdapter extends ServletRequestHandler {
    public ExampleAdapter() {
        // here you just pass context path and your servlet
        super("/", new MyAwesomeServlet());
    }
}
```

Also there is an implementation for Jersey. What you need is to create a class extending JerseyRequestHandler:
```
public class ExampleAdapter extends JerseyRequestHandler {
    public ExampleAdapter() {
        // here you just pass context path and your resources
        super("/", TestResource.class);
        // or you could use the ResourceConfig directly
        // super("/", new ResourceConfig(TestResource.class));
    }
}
```

Then you just deploy ExampleAdapter as a Lambda function and expose it through API Gateway.

For the complete working example, see the corresponding directory: https://github.com/bleshik/aws-lambda-servlet/tree/master/examples/simple
