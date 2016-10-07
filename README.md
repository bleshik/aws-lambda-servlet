# AWS Lambda adapter for Java's Servlets
The idea behind this little adapter is to have a way of running APIs implemented in Java on AWS Lambda. This apadater basically takes the incoming Lambda Event and transform it to an HTTP Servet Request and pass it directly to the given servlet. No http server is used in this process.

So you just go like this:
```
public class ExampleAdapter extends ServletRequestHandler {
    public ExampleAdapter() {
        // here you just pass your servlet
        super(new MyAwesomeServlet());
    }
}
```

Also there is an implementation for Jersey. What you need is to create a class extending JerseyRequestHandler:
```
public class ExampleAdapter extends JerseyRequestHandler {
    public ExampleAdapter() {
        // here you just pass your resources
        super(TestResource.class);
        // or you could use the ResourceConfig directly
        // super(new ResourceConfig(TestResource.class));
    }
}
```

Then you just deploy ExampleAdapter as a Lambda function and expose it through API Gateway.
