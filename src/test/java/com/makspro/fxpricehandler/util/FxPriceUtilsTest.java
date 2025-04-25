package com.makspro.fxpricehandler.util;

import com.makspro.fxpricehandler.data.CurrencyPair;
import com.makspro.fxpricehandler.data.FxClientPrice;
import com.makspro.fxpricehandler.data.FxSpotPrice;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.makspro.fxpricehandler.data.Currency.EUR;
import static com.makspro.fxpricehandler.data.Currency.USD;
import static com.makspro.fxpricehandler.util.FxPriceUtils.TIME_STAMP_FORMAT;
import static com.makspro.fxpricehandler.util.FxPriceUtils.calculateClientPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FxPriceUtilsTest {

    private final static double DELTA = 0.00000001;

    @Test
    public void parseTest() {
        FxSpotPrice fxPrice = FxPriceUtils.parse("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001");
        assertEquals(106, fxPrice.id());
        assertEquals(EUR, fxPrice.currencyPair().ccy1());
        assertEquals(USD, fxPrice.currencyPair().ccy2());
        assertEquals( 1.1 , fxPrice.spotBidPrice() , DELTA);
        assertEquals( 1.2 , fxPrice.spotAskPrice() , DELTA);
        assertEquals("01-06-2020 12:01:01:001", fxPrice.timeStamp().format(DateTimeFormatter.ofPattern(TIME_STAMP_FORMAT)));
    }

    @Test
    void calculateFxClientPriceTest() {
        FxSpotPrice fxPrice = new FxSpotPrice(1, new CurrencyPair(EUR, USD), 1.1, 1.2, LocalDateTime.now());

        FxClientPrice fxClientPrice = calculateClientPrice(fxPrice, 0.1);

        assertEquals(1.0989, fxClientPrice.getClientBidPrice(), DELTA);
        assertEquals(1.2012, fxClientPrice.getClientAskPrice(), DELTA);
    }
}