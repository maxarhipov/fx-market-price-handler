package com.makspro.fxpricehandler.util;

import com.makspro.fxpricehandler.data.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Utility class providing helper methods for working with FX pricing data.
 */
public class FxPriceUtils {

    public static final String TIME_STAMP_FORMAT = "dd-MM-yyyy HH:mm:ss:SSS";
    public static final String MESSAGE_FORMAT_EX_4_FIELDS = "Message should contain at least 4 comma separated fields";
    public static final String SPLIT_REGEX = ",";

    /**
     * Parses a raw message string into an FxSpotPrice instance.
     * <p>
     * The expected format is:
     * <p>
     * id,currencyPair,bid,ask,timestamp
     * e.g. "104,EUR/USD,1.1010,1.2010,01-06-2020 12:01:01:001"
     *
     * @param message the raw string message containing FX price data
     * @return a parsed FxSpotPrice object
     * @throws IllegalArgumentException if the message has fewer than 4 fields
     * @throws NullPointerException     if the message is null
     */
    public static FxSpotPrice parse(String message) {
        Objects.requireNonNull(message);
        String[] parts = message.split(SPLIT_REGEX);
        if (parts.length < 4)
            throw new IllegalArgumentException(MESSAGE_FORMAT_EX_4_FIELDS);
        long id = Long.parseLong(parts[0]);
        Currency ccy1 = Currency.valueOf(parts[1].trim().substring(0, 3));
        Currency ccy2 = Currency.valueOf(parts[1].trim().substring(4, 7));
        CurrencyPair currencyPair = new CurrencyPair(ccy1, ccy2);
        double bid = Double.parseDouble(parts[2]);
        double ask = Double.parseDouble(parts[3]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_STAMP_FORMAT);
        LocalDateTime localDateTime = LocalDateTime.parse(parts[4], formatter);
        return new FxSpotPrice(id, currencyPair, bid, ask, localDateTime);
    }

    /**
     * Calculates a client FX price by applying a commission margin.
     *
     * @param fxSpotPrice the FX spot price
     * @param commissionMargin margin adjustment commission
     * @return a FxClientPrice with markup applied
     */
    public static FxClientPrice calculateClientPrice(FxSpotPrice fxSpotPrice, double commissionMargin) {
        return new FxClientPrice(fxSpotPrice, commissionMargin);
    }
}
