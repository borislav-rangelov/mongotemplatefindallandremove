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
        ));

        // throws an exception when mapping
        /*
java.lang.IllegalStateException: Failed to execute CommandLineRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:779) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:760) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at com.borislavrangelov.mongotemplatefindallandremove.Application.main(Application.java:53) [main/:na]
Caused by: java.lang.ClassCastException: com.mongodb.BasicDBObject cannot be cast to com.mongodb.BasicDBList
	at org.springframework.data.mongodb.core.convert.MappingMongoConverter.writeInternal(MappingMongoConverter.java:392) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.convert.MappingMongoConverter.write(MappingMongoConverter.java:361) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.convert.MappingMongoConverter.write(MappingMongoConverter.java:84) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.toDbObject(MongoTemplate.java:870) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.doInsert(MongoTemplate.java:852) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.insert(MongoTemplate.java:796) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at org.springframework.data.mongodb.core.MongoTemplate.insert(MongoTemplate.java:787) ~[spring-data-mongodb-1.10.1.RELEASE.jar:na]
	at com.borislavrangelov.mongotemplatefindallandremove.Application.run(Application.java:41) [main/:na]
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:776) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
          */
        List<Customer> list = mongoTemplate.findAllAndRemove(query(where("firstName").regex(Pattern.compile("^FirstName"))), Customer.class);

        Assert.isTrue(list.size() == 2, "Correct size");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
