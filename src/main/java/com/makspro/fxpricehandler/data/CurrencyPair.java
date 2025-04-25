package com.makspro.fxpricehandler.data;


public record CurrencyPair(Currency ccy1, Currency ccy2) {

    @Override
    public String toString() {
        return ccy1 + "/" +ccy2;
    }
}
