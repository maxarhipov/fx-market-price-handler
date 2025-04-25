package com.makspro.fxpricehandler.core.subscriber;

import com.makspro.fxpricehandler.core.consumer.FxPriceConsumer;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import com.makspro.fxpricehandler.util.FxPriceUtils;

import java.util.Objects;

/**
 * Subscriber to FxPrice feed.
 * Receives CSV-formatted messages, splits into individual entries,
 * parses them into FxSpotPrice objects, and passes them to the provided consumer.
 */
public class FxPriceSubscriberImpl implements FxPriceSubscriber {

    public static final String MESSAGE_NULL_MSG = "Incoming FX message string must not be null";
    private final FxPriceConsumer fxPriceConsumer;

    public FxPriceSubscriberImpl(FxPriceConsumer fxPriceConsumer) {
        this.fxPriceConsumer = fxPriceConsumer;
    }

    @Override
    public void onMessage(String messageString) {
        Objects.requireNonNull(messageString, MESSAGE_NULL_MSG);
        String[] messages = messageString.split("\n");
        for(String message : messages) {
            message = message.trim();
            if (message.isBlank()) continue;
            try {
                FxSpotPrice fxPrice = FxPriceUtils.parse(message);
                fxPriceConsumer.accept(fxPrice);
            } catch (Exception e) {
                System.err.println("Failed to parse message: " + message + " - " + e.getMessage());
            }
        }
    }
}
