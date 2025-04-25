package com.makspro.fxpricehandler.core.publisher;

import com.makspro.fxpricehandler.data.Currency;
import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FxPricePublisherImplTest {

    private FxPricePublisherImpl fxPricePublisher;
    private CurrencyPair EUR_USD;
    private CurrencyPair GBP_USD;

    private static final double DELTA = 0.00000001;
    private static final double MARGIN_COMMISSION = 0.1;

    @BeforeEach
    void setUp() {
        fxPricePublisher = new FxPricePublisherImpl();
        EUR_USD = new CurrencyPair(Currency.EUR, Currency.USD);
        GBP_USD = new CurrencyPair(Currency.GBP, Currency.USD);
    }

    @Test
    void storeAndReturnLatestPriceTest() {
        FxSpotPrice spotPrice = new FxSpotPrice(1L, EUR_USD, 1.1, 1.2, LocalDateTime.now());
        FxClientPrice clientPrice = new FxClientPrice(spotPrice, MARGIN_COMMISSION);

        fxPricePublisher.publish(clientPrice);
        FxClientPrice latest = fxPricePublisher.getLatest(EUR_USD);

        assertNotNull(latest);
        assertEquals(1.0989, latest.getClientBidPrice(), DELTA);
        assertEquals(1.2012, latest.getClientAskPrice(), DELTA);
    }

    @Test
    void overwriteOldPriceWhenPublishingNewTest() {
        FxSpotPrice first = new FxSpotPrice(1L, EUR_USD, 1.1, 1.2, LocalDateTime.now());
        FxClientPrice oldPrice = new FxClientPrice(first, MARGIN_COMMISSION);

        FxSpotPrice second = new FxSpotPrice(2L, EUR_USD, 1.2, 1.3, LocalDateTime.now());
        FxClientPrice newPrice = new FxClientPrice(second, MARGIN_COMMISSION);

        fxPricePublisher.publish(oldPrice);
        fxPricePublisher.publish(newPrice);

        FxClientPrice latest = fxPricePublisher.getLatest(EUR_USD);
        assertEquals(1.1988, latest.getClientBidPrice(), DELTA);
        assertEquals(1.3013, latest.getClientAskPrice(), DELTA);
    }

    @Test
    void returnNullForUnknownCurrencyPair() {
        assertNull(fxPricePublisher.getLatest(GBP_USD));
    }
}