package a.b.sbb.batch.marketdata;

import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MarketDataFieldSetMapper
        implements FieldSetMapper<MarketData> {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    @Override
    public MarketData mapFieldSet(FieldSet fieldSet) {

        MarketData data = new MarketData();

        data.setTradeDate(
                LocalDate.parse(
                        fieldSet.readString("tradeDate").trim(),
                        DATE_FORMATTER
                )
        );

        data.setOpenPrice(
                new BigDecimal(
                        fieldSet.readString("openPrice")
                                .replace(",", "")
                                .trim()
                )
        );

        data.setHighPrice(
                new BigDecimal(
                        fieldSet.readString("highPrice")
                                .replace(",", "")
                                .trim()
                )
        );

        data.setLowPrice(
                new BigDecimal(
                        fieldSet.readString("lowPrice")
                                .replace(",", "")
                                .trim()
                )
        );

        data.setClosePrice(
                new BigDecimal(
                        fieldSet.readString("closePrice")
                                .replace(",", "")
                                .trim()
                )
        );

        data.setSharesTraded(
                Long.parseLong(
                        fieldSet.readString("sharesTraded")
                                .replace(",", "")
                                .trim()
                )
        );

        data.setTurnoverCr(
                new BigDecimal(
                        fieldSet.readString("turnoverCr")
                                .replace(",", "")
                                .trim()
                )
        );

        return data;
    }
}