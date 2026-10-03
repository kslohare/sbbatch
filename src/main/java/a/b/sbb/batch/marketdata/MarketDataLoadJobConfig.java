package a.b.sbb.batch.marketdata;

import javax.sql.DataSource;


import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MarketDataLoadJobConfig {

    @Bean
    public FlatFileItemReader<MarketData> marketDataReader() {

        DelimitedLineTokenizer tokenizer =
                new DelimitedLineTokenizer();

        tokenizer.setDelimiter(",");
        tokenizer.setNames(
                "tradeDate",
                "openPrice",
                "highPrice",
                "lowPrice",
                "closePrice",
                "sharesTraded",
                "turnoverCr"
        );

        DefaultLineMapper<MarketData> lineMapper =
                new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(
                new MarketDataFieldSetMapper()
        );

        return new FlatFileItemReaderBuilder<MarketData>()
                .name("marketDataReader")
                .resource(
                        new FileSystemResource(
                                "data/market-data.csv"
                        )
                )
                .linesToSkip(1)
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<MarketData> marketDataWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<MarketData>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO market_data
                    (
                        trade_date,
                        open_price,
                        high_price,
                        low_price,
                        close_price,
                        shares_traded,
                        turnover_cr
                    )
                    VALUES
                    (
                        :tradeDate,
                        :openPrice,
                        :highPrice,
                        :lowPrice,
                        :closePrice,
                        :sharesTraded,
                        :turnoverCr
                    )
                    """)
                .beanMapped()
                .build();
    }

    @Bean
    public Step loadMarketDataStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<MarketData> marketDataReader,
            JdbcBatchItemWriter<MarketData> marketDataWriter) {

        return new StepBuilder(
                "loadMarketDataStep",
                jobRepository
        )
                .<MarketData, MarketData>chunk(100)
                .transactionManager(transactionManager)
                .reader(marketDataReader)
                .writer(marketDataWriter)
                .build();
    }

    @Bean
    public Job marketDataLoadJob(
            JobRepository jobRepository,
            Step loadMarketDataStep) {

        return new JobBuilder(
                "marketDataLoadJob",
                jobRepository
        )
                .start(loadMarketDataStep)
                .build();
    }
}