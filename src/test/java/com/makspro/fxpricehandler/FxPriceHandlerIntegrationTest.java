package com.makspro.fxpricehandler;

import com.makspro.fxpricehandler.core.publisher.FxPricePublisher;
import com.makspro.fxpricehandler.core.subscriber.FxPriceSubscriber;
import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.makspro.fxpricehandler.data.Currency.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FxPriceHandlerIntegrationTest {
    private final static double DELTA = 0.00000001;

    @Autowired
    private FxPriceSubscriber fxPriceSubscriber;

    @Autowired
    private FxPricePublisher fxPricePublisher;

    @Test
    void test() {

        fxPriceSubscriber.onMessage("""
                104, EUR/USD, 1.1010,1.2010,01-06-2020 12:01:01:001
                105, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
                106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
                107, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:002
                108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
                109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
                110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
                """);

        FxClientPrice fxPrice = fxPricePublisher.getLatest(new CurrencyPair(EUR, USD));

        assertEquals(1.0989, fxPrice.getClientBidPrice(), DELTA);
        assertEquals(1.2012, fxPrice.getClientAskPrice(), DELTA);

        fxPrice = fxPricePublisher.getLatest(new CurrencyPair(EUR, JPY));

        assertEquals(119.49039, fxPrice.getClientBidPrice(), DELTA);
        assertEquals(120.02991, fxPrice.getClientAskPrice(), DELTA);

        fxPriceSubscriber.onMessage("""           
                111, GBP/USD, 1.2520,1.2660,01-06-2020 12:01:02:002
                112, GBP/USD, 1.2599,1.2761,01-06-2020 12:01:02:100
                113, EUR/JPY, 120.61,120.91,01-06-2020 12:01:02:110
                """);

        fxPrice = fxPricePublisher.getLatest(new CurrencyPair(EUR, JPY));

        assertEquals(120.48939, fxPrice.getClientBidPrice(), DELTA);
        assertEquals(121.03091, fxPrice.getClientAskPrice(), DELTA);

        fxPriceSubscriber.onMessage("111, GBP/USD, 1.2520,1.2660,01-06-2020 12:01:02:002");

        fxPriceSubscriber.onMessage("114, GBP/USD, 1.1,1.3,01-06-2020 12:01:02:002");

        fxPrice = fxPricePublisher.getLatest(new CurrencyPair(GBP, USD));

        assertEquals(1.0989, fxPrice.getClientBidPrice(), DELTA);
        assertEquals(1.3013, fxPrice.getClientAskPrice(), DELTA);
    }
}
