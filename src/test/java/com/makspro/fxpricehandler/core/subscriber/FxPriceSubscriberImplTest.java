package com.makspro.fxpricehandler.core.subscriber;

import com.makspro.fxpricehandler.core.consumer.FxPriceConsumer;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import com.makspro.fxpricehandler.util.FxPriceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.makspro.fxpricehandler.util.FxPriceUtils.TIME_STAMP_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FxPriceSubscriberImplTest {

    private FxPriceConsumer fxPriceConsumer;
    private FxPriceSubscriberImpl fxPriceSubscriber;
    private final static double DELTA = 0.00000001;

    @BeforeEach
    void setup() {
        fxPriceConsumer = mock(FxPriceConsumer.class);
        fxPriceSubscriber = new FxPriceSubscriberImpl(fxPriceConsumer);
    }

    @Test
    void onMessageTest() {
        String input = """
                100,EUR/USD,1.1000,1.2000,01-06-2020 12:01:01:001
                101,GBP/USD,1.3000,1.4000,01-06-2020 12:01:02:002
                102,USD/JPY,110.10,110.30,01-06-2020 12:01:03:003
                """;

        fxPriceSubscriber.onMessage(input);

        ArgumentCaptor<FxSpotPrice> captor = ArgumentCaptor.forClass(FxSpotPrice.class);
        verify(fxPriceConsumer, times(3)).accept(captor.capture());

        List<FxSpotPrice> prices = captor.getAllValues();

        assertEquals(3, prices.size());

        FxSpotPrice lastPrice = prices.get(1);
        assertNotNull(lastPrice);
        assertEquals(101, lastPrice.id());
        assertEquals("GBP/USD", lastPrice.currencyPair().toString());
        assertEquals(1.3, lastPrice.spotBidPrice(), DELTA);
        assertEquals(1.4, lastPrice.spotAskPrice(), DELTA);
        assertEquals("01-06-2020 12:01:02:002", lastPrice.timeStamp().format(DateTimeFormatter.ofPattern(TIME_STAMP_FORMAT)));


        lastPrice = prices.get(2);
        assertNotNull(lastPrice);
        assertEquals(102, lastPrice.id());
        assertEquals("USD/JPY", lastPrice.currencyPair().toString());
        assertEquals(110.10, lastPrice.spotBidPrice(), DELTA);
        assertEquals(110.30, lastPrice.spotAskPrice(), DELTA);
        assertEquals("01-06-2020 12:01:03:003", lastPrice.timeStamp().format(DateTimeFormatter.ofPattern(TIME_STAMP_FORMAT)));

    }
}