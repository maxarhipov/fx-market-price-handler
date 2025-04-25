# fx-market-price-handler

This application is divided into the following core components, each represented by an interface and its implementation:

### FxPriceSubscriber

Subscribes to an FX price feed. Receives messages in CSV format, which may contain multiple lines.

### FxPriceConsumer

Consumes individual `FxPrice` objects from the `FxPriceSubscriber`, processes them, and forwards them to the `FxPricePublisher`.

### FxPricePublisher

Publishes `FxClientPrice` to potential clients, stores the last published price, and provides it upon request.

## Configuration

- `commission-margin`: The commission margin applied to the client price calculation. Default value is `0.1`.