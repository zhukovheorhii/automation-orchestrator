package com.automation.secret;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DomainPurityTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter().importPackages("com.automation.secret");
    }

    @Test
    void domainShouldNotDependOnInfrastructureSpecificClasses() {
        ArchRule rule = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.web..",
                "org.springframework.kafka..",
                "org.springframework.jdbc..",
                "org.springframework.data.jdbc..",
                "org.hibernate..",
                "org.postgresql..",
                "org.flywaydb..",
                "io.confluent..",
                "org.apache.kafka..",
                "org.apache.avro.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotUseInfrastructureAnnotations() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.GetMapping")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.PostMapping")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.PutMapping")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.DeleteMapping")
            .orShould().beAnnotatedWith("org.springframework.kafka.annotation.KafkaListener")
            .orShould().beAnnotatedWith("org.springframework.transaction.annotation.Transactional")
            .orShould().beAnnotatedWith("org.springframework.context.annotation.Configuration")
            .orShould().beAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainRepositoryImplementations() {
        ArchRule rule = noClasses()
            .that().haveNameMatching(".*Repository.*")
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.jdbc..",
                "org.springframework.data.jdbc..",
                "org.postgresql..",
                "java.sql.."
            ).allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainConcreteRepositoryNames() {
        ArchRule rule = noClasses()
            .should().haveNameMatching(".*PostgresRepository.*")
            .orShould().haveNameMatching(".*JdbcRepository.*")
            .orShould().haveNameMatching(".*DatabaseRepository.*")
            .orShould().haveNameMatching(".*SqlRepository.*");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainKafkaSpecificClasses() {
        ArchRule rule = noClasses()
            .should().haveNameMatching(".*KafkaProducer.*")
            .orShould().haveNameMatching(".*KafkaConsumer.*")
            .orShould().haveNameMatching(".*KafkaListener.*")
            .orShould().haveNameMatching(".*AvroProducer.*")
            .orShould().haveNameMatching(".*AvroConsumer.*")
            .orShould().dependOnClassesThat().haveSimpleName("KafkaTemplate")
            .orShould().dependOnClassesThat().haveSimpleName("ProducerFactory")
            .orShould().dependOnClassesThat().haveSimpleName("ConsumerFactory");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainWebSpecificClasses() {
        ArchRule rule = noClasses()
            .should().haveNameMatching(".*Controller.*")
            .orShould().haveNameMatching(".*RestController.*")
            .orShould().haveNameMatching(".*RequestHandler.*")
            .orShould().haveNameMatching(".*ResponseHandler.*")
            .orShould().dependOnClassesThat().haveSimpleName("HttpServletRequest")
            .orShould().dependOnClassesThat().haveSimpleName("HttpServletResponse")
            .orShould().dependOnClassesThat().haveSimpleName("ResponseEntity");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainConfigurationClasses() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("org.springframework.context.annotation.Configuration")
            .orShould().beAnnotatedWith("org.springframework.context.annotation.Bean")
            .orShould().beAnnotatedWith("org.springframework.boot.context.properties.ConfigurationProperties")
            .orShould().beAnnotatedWith("org.springframework.boot.autoconfigure.EnableAutoConfiguration");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainExternalIntegrationClasses() {
        ArchRule rule = noClasses()
            .that().areNotInterfaces()
            .should().haveNameMatching(".*ClientImpl")
            .orShould().haveNameMatching(".*GatewayImpl")
            .orShould().haveNameMatching(".*AdapterImpl")
            .orShould().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.web.client..",
                "org.springframework.http.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainSerializationImplementations() {
        ArchRule rule = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.fasterxml.jackson..",
                "com.google.gson..",
                "org.apache.avro.specific..",
                "io.confluent.kafka.serializers.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainInterfacesShouldNotExposeInfrastructureTypes() {
        ArchRule rule = noClasses()
            .that().areInterfaces()
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.data..",
                "org.springframework.jdbc..",
                "org.springframework.kafka..",
                "org.springframework.web..",
                "java.sql..",
                "org.apache.kafka..",
                "javax.servlet..",
                "jakarta.servlet.."
            ).allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainTestInfrastructure() {
        ArchRule rule = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.test..",
                "org.springframework.boot.test..",
                "org.testcontainers.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainSecurityRelatedCode() {
        ArchRule rule = noClasses()
            .should().haveNameMatching(".*Security.*")
            .orShould().haveNameMatching(".*Auth.*")
            .orShould().haveNameMatching(".*Login.*")
            .orShould().haveNameMatching(".*Session.*")
            .orShould().dependOnClassesThat().resideInAnyPackage(
                "org.springframework.security..",
                "org.springframework.boot.autoconfigure.security.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotUseJpaOrHibernate() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("javax.persistence.Entity")
            .orShould().beAnnotatedWith("jakarta.persistence.Entity")
            .orShould().beAnnotatedWith("javax.persistence.Table")
            .orShould().beAnnotatedWith("jakarta.persistence.Table")
            .orShould().beAnnotatedWith("javax.persistence.Column")
            .orShould().beAnnotatedWith("jakarta.persistence.Column")
            .orShould().dependOnClassesThat().resideInAnyPackage(
                "javax.persistence..",
                "jakarta.persistence..",
                "org.hibernate.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotUseSpringDataJpaRepositories() {
        ArchRule rule = noClasses()
            .should().dependOnClassesThat().haveSimpleName("JpaRepository")
            .orShould().dependOnClassesThat().haveSimpleName("CrudRepository")
            .orShould().dependOnClassesThat().haveSimpleName("PagingAndSortingRepository")
            .orShould().dependOnClassesThat().resideInAnyPackage("org.springframework.data.jpa..");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainFlywayMigrations() {
        ArchRule rule = noClasses()
            .should().haveNameMatching(".*Migration")
            .orShould().haveNameMatching("V[0-9]+_.*")
            .orShould().dependOnClassesThat().resideInAnyPackage("org.flywaydb..");

        rule.check(importedClasses);
    }

    @Test
    void domainPortInterfacesShouldOnlyDependOnDomainTypes() {
        ArchRule rule = noClasses()
            .that().areInterfaces()
            .and().haveNameMatching(".*Port")
            .should().dependOnClassesThat().resideOutsideOfPackages(
                "com.automation.secret..",
                "java..",
                "org.springframework.stereotype..",
                "org.springframework.context.annotation.."
            ).allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainServicesShouldOnlyBeAnnotatedWithServiceOrComponent() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .and().haveSimpleNameEndingWith("Service")
            .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().beAnnotatedWith("org.springframework.context.annotation.Configuration")
            .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainDtoOrVoSuffixes() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().haveSimpleNameEndingWith("DTO")
            .orShould().haveSimpleNameEndingWith("Dto")
            .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotUseValidationAnnotations() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("javax.validation.constraints.NotNull")
            .orShould().beAnnotatedWith("jakarta.validation.constraints.NotNull")
            .orShould().beAnnotatedWith("javax.validation.Valid")
            .orShould().beAnnotatedWith("jakarta.validation.Valid")
            .orShould().dependOnClassesThat().resideInAnyPackage(
                "javax.validation..",
                "jakarta.validation..",
                "org.hibernate.validator.."
            );

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainScheduledTasks() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("org.springframework.scheduling.annotation.Scheduled")
            .orShould().beAnnotatedWith("org.springframework.scheduling.annotation.Async")
            .orShould().dependOnClassesThat().resideInAnyPackage("org.springframework.scheduling..");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotContainCachingAnnotations() {
        ArchRule rule = noClasses()
            .should().beAnnotatedWith("org.springframework.cache.annotation.Cacheable")
            .orShould().beAnnotatedWith("org.springframework.cache.annotation.CacheEvict")
            .orShould().beAnnotatedWith("org.springframework.cache.annotation.CachePut")
            .orShould().dependOnClassesThat().resideInAnyPackage("org.springframework.cache..");

        rule.check(importedClasses);
    }
}