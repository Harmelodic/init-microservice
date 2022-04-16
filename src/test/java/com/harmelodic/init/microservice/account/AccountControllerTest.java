package com.harmelodic.init.microservice.account;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
@Provider("Account Service")
@PactBroker
class AccountControllerTest {

    public static final String ACCOUNT_EXISTS_WITH_ID = "ACCOUNT_EXISTS_WITH_ID";
    public static final String ACCOUNT_DOES_NOT_EXIST = "ACCOUNT_DOES_NOT_EXIST";
    public static final String SERVER_ERROR_WILL_OCCUR = "SERVER_ERROR_WILL_OCCUR";
    public static final String ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID = "ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID";
    public static final String THREE_ACCOUNTS_EXIST = "THREE_ACCOUNTS_EXIST";

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Flyway flyway;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    void wipeAccountTable() {
        jdbcTemplate.execute("""
                TRUNCATE TABLE account;
                """);
    }

    @State(value = ACCOUNT_EXISTS_WITH_ID, action = StateChangeAction.SETUP)
    void accountExistsWithIdSetup(Map<String, Object> params) {
        UUID id = UUID.fromString(params.get("id").toString());
        jdbcTemplate.update("""
                        INSERT INTO account (id, `name`, customer_id)
                        VALUES (?, ?, ?);
                        """,
                id, "Some name", UUID.randomUUID());
    }

    @State(value = ACCOUNT_EXISTS_WITH_ID, action = StateChangeAction.TEARDOWN)
    void accountExistsWithIdTeardown() {
        wipeAccountTable();
    }

    @State(value = ACCOUNT_DOES_NOT_EXIST, action = StateChangeAction.SETUP)
    void accountDoesNotExistSetup() {
        wipeAccountTable();
    }

    @State(value = ACCOUNT_DOES_NOT_EXIST, action = StateChangeAction.TEARDOWN)
    void accountDoesNotExistTeardown() {
        wipeAccountTable();
    }

    @State(value = SERVER_ERROR_WILL_OCCUR, action = StateChangeAction.SETUP)
    void serverErrorWillOccurSetup() {
        jdbcTemplate.execute("""
                DROP TABLE account;
                """);
    }

    @State(value = SERVER_ERROR_WILL_OCCUR, action = StateChangeAction.TEARDOWN)
    void serverErrorWillOccurTeardown() {
        flyway.clean();
        flyway.migrate();
    }

    @State(value = ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID, action = StateChangeAction.SETUP)
    void accountExistsWithIdNameAndCustomerIdSetup(Map<String, Object> params) {
        UUID id = UUID.fromString(params.get("id").toString());
        String name = params.get("name").toString();
        UUID customerId = UUID.fromString(params.get("customerId").toString());

        jdbcTemplate.update("""
                        INSERT INTO account (id, `name`, customer_id)
                        VALUES (?, ?, ?);
                        """,
                id, name, customerId);
    }

    @State(value = ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID, action = StateChangeAction.TEARDOWN)
    void accountExistsWithIdNameAndCustomerIdTeardown() {
        wipeAccountTable();
    }

    @State(value = THREE_ACCOUNTS_EXIST, action = StateChangeAction.SETUP)
    void threeAccountsExistSetup() {
        List<Integer> things = List.of(0, 1, 2);
        things.forEach(thing -> jdbcTemplate.update("""
                        INSERT INTO account (id, `name`, customer_id)
                        VALUES (?, ?, ?);
                        """,
                UUID.randomUUID(), "Some Name" + thing, UUID.randomUUID()));
    }

    @State(value = THREE_ACCOUNTS_EXIST, action = StateChangeAction.TEARDOWN)
    void threeAccountsExistTeardown() {
        wipeAccountTable();
    }
}
