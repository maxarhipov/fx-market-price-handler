package com.makspro.fxpricehandler.data;

import java.time.LocalDateTime;

public record FxSpotPrice(long id, CurrencyPair currencyPair, double spotBidPrice, double spotAskPrice,
                          LocalDateTime timeStamp) {
}
