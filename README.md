# AWS Lambda adapter for Jersey (JAX-RS implementation)
The idea behind this little adapter is to have a way of running JAX-RS APIs on AWS Lambda. This apadater basically takes the incoming Lambda Event and transform it to an HTTP Servet Request and pass it directly to Jersey. Then it takes the resulted Jersey entity and returns it as the Lambda return value.

What you need is to create a class extending JerseyRequestHandler:
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
