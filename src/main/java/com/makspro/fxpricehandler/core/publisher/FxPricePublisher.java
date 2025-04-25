package com.makspro.fxpricehandler.core.publisher;

import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;

public interface FxPricePublisher {
    void publish(FxClientPrice fxPrice);
    FxClientPrice getLatest(CurrencyPair currencyPair);
}
