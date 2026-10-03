package a.b.sbb.batch.marketdata;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MarketData {

    private LocalDate tradeDate;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long sharesTraded;
    private BigDecimal turnoverCr;

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Long getSharesTraded() {
        return sharesTraded;
    }

    public void setSharesTraded(Long sharesTraded) {
        this.sharesTraded = sharesTraded;
    }

    public BigDecimal getTurnoverCr() {
        return turnoverCr;
    }

    public void setTurnoverCr(BigDecimal turnoverCr) {
        this.turnoverCr = turnoverCr;
    }
}