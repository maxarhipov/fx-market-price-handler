package com.makspro.fxpricehandler.core.consumer;

import com.makspro.fxpricehandler.core.publisher.FxPricePublisher;
import com.makspro.fxpricehandler.data.Currency;
import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FxPriceConsumerImplTest {

    private FxPricePublisher publisher;
    private FxPriceConsumerImpl fxPriceConsumer;
    private CurrencyPair EUR_USD;
    private final static double DELTA = 0.00000001;

    @BeforeEach
    void setUp() {
        publisher = mock(FxPricePublisher.class);
        fxPriceConsumer = new FxPriceConsumerImpl(publisher, 0.1);
        EUR_USD = new CurrencyPair(Currency.EUR, Currency.USD);
    }

    @Test
    void publishClientPriceTest() {
        FxSpotPrice spotPrice1 = new FxSpotPrice(101L, EUR_USD, 1.1000, 1.2000, java.time.LocalDateTime.now());
        FxSpotPrice spotPrice2 = new FxSpotPrice(102L, EUR_USD, 1.2000, 1.3000, java.time.LocalDateTime.now());

        fxPriceConsumer.accept(spotPrice1);
        fxPriceConsumer.accept(spotPrice2);

        ArgumentCaptor<FxClientPrice> captor = ArgumentCaptor.forClass(FxClientPrice.class);
        verify(publisher, times(2)).publish(captor.capture());

        List<FxClientPrice> publishedList = captor.getAllValues();

        FxClientPrice published1 = publishedList.getFirst();

        assertNotNull(published1);
        assertEquals(101L, published1.getFxSpotPrice().id());
        assertEquals(1.0989, published1.getClientBidPrice(), DELTA);
        assertEquals(1.2012, published1.getClientAskPrice(), DELTA);

        FxClientPrice published2 = publishedList.getLast();

        assertNotNull(published2);
        assertEquals(102L, published2.getFxSpotPrice().id());
        assertEquals(1.1988, published2.getClientBidPrice(), DELTA);
        assertEquals(1.3013, published2.getClientAskPrice(), DELTA);
    }

    @Test
    void throwExceptionWhenIdIsLowTest() {
        FxSpotPrice first = new FxSpotPrice(200L, EUR_USD, 1.1000, 1.2000, java.time.LocalDateTime.now());
        FxSpotPrice second = new FxSpotPrice(199L, EUR_USD, 1.1010, 1.2010, java.time.LocalDateTime.now());

        fxPriceConsumer.accept(first);
        assertThrows(IllegalArgumentException.class, () -> fxPriceConsumer.accept(second));
    }

}