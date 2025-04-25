package com.makspro.fxpricehandler.data;

public class FxClientPrice {

    private final FxSpotPrice fxSpotPrice;
    private final double clientBidPrice;
    private final double clientAskPrice;

    public FxClientPrice(FxSpotPrice fxSpotPrice, double marginCommission) {
        this.fxSpotPrice = fxSpotPrice;
        clientBidPrice = fxSpotPrice.spotBidPrice() - (fxSpotPrice.spotBidPrice() * (marginCommission / 100));
        clientAskPrice = fxSpotPrice.spotAskPrice() + (fxSpotPrice.spotAskPrice() * (marginCommission / 100));
    }

    public FxSpotPrice getFxSpotPrice() {
        return fxSpotPrice;
    }

    public double getClientBidPrice() {
        return clientBidPrice;
    }

    public double getClientAskPrice() {
        return clientAskPrice;
    }

    @Override
    public String toString() {
        return "FxClientPrice{" +
                "fxSpotPrice=" + fxSpotPrice +
                ", clientBidPrice=" + clientBidPrice +
                ", clientAskPrice=" + clientAskPrice +
                '}';
    }
}
