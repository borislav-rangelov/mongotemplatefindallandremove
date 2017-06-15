package com.borislavrangelov.mongotemplatefindallandremove;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient();
    }

    @Bean
    public MongoTemplate mongoTemplate(Mongo mongo) {
        return new MongoTemplate(mongo, "test");
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection(Customer.class);

        mongoTemplate.insert(Arrays.asList(
                new Customer("FirstName1", "LastName1"),
                new Customer("FirstName2", "LastName2"),
                new Customer("FirstName3", "LastName3")
        ), "customer");

        // throws an exception when mapping
        /*
java.lang.IllegalStateException: Failed to execute CommandLineRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:779) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:760) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at com.borislavrangelov.mongotemplatefindallandremove.Application.main(Application.java:75) [main/:na]
Caused by: java.lang.IllegalArgumentException: Type must not be null!
	at org.springframework.util.Assert.notNull(Assert.java:134) ~[spring-core-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.data.util.ClassTypeInformation.from(ClassTypeInformation.java:72) ~[spring-data-commons-1.13.1.RELEASE.jar:na]
	at org.springframework.data.mapping.context.AbstractMappingContext.getPersistentEntity(AbstractMappingContext.java:145) ~[spring-data-commons-1.13.1.RELEASE.jar:na]
	at org.springframework.data.mapping.context.AbstractMappingContext.getPersistentEntity(AbstractMappingContext.java:70) ~[spring-data-commons-1.13.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.doFindAndRemove(MongoTemplate.java:1825) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.findAndRemove(MongoTemplate.java:748) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.findAllAndRemove(MongoTemplate.java:1517) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at com.borislavrangelov.mongotemplatefindallandremove.Application.run(Application.java:69) [main/:na]
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:776) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	... 6 common frames omitted
          */
        List<Customer> list = mongoTemplate.findAllAndRemove(query(where("firstName").regex(Pattern.compile("^FirstName"))), "customer");

        Assert.isTrue(list.size() == 2, "Correct size");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
