package com.makspro.fxpricehandler.core.consumer;

import com.makspro.fxpricehandler.core.publisher.FxPricePublisher;
import com.makspro.fxpricehandler.data.FxClientPrice;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import com.makspro.fxpricehandler.util.FxPriceUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of FxPriceConsumer that processes and publishes FX prices.
 */
public class FxPriceConsumerImpl implements FxPriceConsumer {

    private final FxPricePublisher publisher;
    private final double commissionMargin;
    private final AtomicLong lastAcceptedId = new AtomicLong(-1);

    public FxPriceConsumerImpl(FxPricePublisher publisher, double commissionMargin) {
        this.publisher = publisher;
        this.commissionMargin = commissionMargin;
    }

    /**
     * Validates that each incoming FxSpotPrice has a strictly increasing message ID
     * Transforms the FxSpotPrice into a FxClientPrice
     * Publishes the client price using a provided FxPricePublisher
     *
     * @throws IllegalArgumentException if the message ID is not strictly increasing
     */
    @Override
    public void accept(FxSpotPrice fxPrice)  {
        long current;
        do {
            current = lastAcceptedId.get();
            if (fxPrice.id() <= current) {
                throw new IllegalArgumentException("Message id=[" + fxPrice.id() + "] is lower than expected lastAcceptedId=[" + current + "]");
            }
        } while (!lastAcceptedId.compareAndSet(current, fxPrice.id()));
        FxClientPrice fxClientPrice = FxPriceUtils.calculateClientPrice(fxPrice, commissionMargin);
        publisher.publish(fxClientPrice);
    }
}
