package com.makspro.fxpricehandler.config;

import com.makspro.fxpricehandler.core.consumer.FxPriceConsumer;
import com.makspro.fxpricehandler.core.consumer.FxPriceConsumerImpl;
import com.makspro.fxpricehandler.core.publisher.FxPricePublisher;
import com.makspro.fxpricehandler.core.publisher.FxPricePublisherImpl;
import com.makspro.fxpricehandler.core.subscriber.FxPriceSubscriber;
import com.makspro.fxpricehandler.core.subscriber.FxPriceSubscriberImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FxPriceHandlerConfig {

    @Value( "${commission-margin:0.1}" )
    private double commissionMargin;

    @Bean
    public FxPricePublisher fxPricePublisher() {
        return new FxPricePublisherImpl();
    }

    @Bean
    public FxPriceConsumer fxPriceConsumer(FxPricePublisher publisher) {
        return new FxPriceConsumerImpl(publisher, commissionMargin);
    }

    @Bean
    public FxPriceSubscriber fxPriceSubscriber(FxPriceConsumer fxPriceConsumer) {
        return new FxPriceSubscriberImpl(fxPriceConsumer);
    }
}
