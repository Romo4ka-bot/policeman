package ru.inovus.policeman.common;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.spock.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MongoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getContainerIpAddress);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }


    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                "spring.data.mongodb.host=" + mongoDBContainer.getContainerIpAddress(),
                "spring.data.mongodb.port=" + mongoDBContainer.getFirstMappedPort()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
