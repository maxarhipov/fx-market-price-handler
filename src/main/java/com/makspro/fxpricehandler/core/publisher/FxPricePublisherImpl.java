package com.makspro.fxpricehandler.core.publisher;

import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple in-memory implementation of FxPricePublisher
 * Publishes `FxClientPrice` to potential clients, stores the last published price, and provides it upon request.
 */
public class FxPricePublisherImpl implements FxPricePublisher {

    private final Map<CurrencyPair, FxClientPrice> currencyPairFxPriceMap = new ConcurrentHashMap<>();

    @Override
    public void publish(FxClientPrice fxPrice) {
        System.out.println("Publishing price: " + fxPrice);
        currencyPairFxPriceMap.put(fxPrice.getFxSpotPrice().currencyPair(), fxPrice);
    }

    @Override
    public FxClientPrice getLatest(CurrencyPair currencyPair) {
        FxClientPrice price = currencyPairFxPriceMap.get(currencyPair);
        System.out.println("Providing price: " + price);
        return price;
    }
}
