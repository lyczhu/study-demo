package com.lawyus.study.batch;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return this.stepBuilderFactory.get("step")
                .chunk(100)
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<Document> mongoReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("_id", Sort.Direction.ASC);
        return new MongoItemReaderBuilder<Document>()
                .name("mongoReader")
                .saveState(false)
                .template(template())
                .collection("")
                .targetType(Document.class)
                .jsonQuery("{}")
                .pageSize(10)
                .sorts(sorts)
                .build();
    }

    @Bean
    @StepScope
    public MongoItemWriter<Document> mongoWriter() {
        return new MongoItemWriterBuilder<Document>()
                .template(template())
                .collection("")
                .build();
    }

    @Bean
    @StepScope
    public KafkaItemWriter kafkaItemWriter() {
//        new KafkaItemWriterBuilder<>()
//                .build();
        return null;
    }

    private MongoOperations template() {
        String url = "";
        ConnectionString connectionString = new ConnectionString(url);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToClusterSettings(s -> s.serverSelectionTimeout(3, TimeUnit.SECONDS))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return new MongoTemplate(mongoClient, "db");
    }
}
