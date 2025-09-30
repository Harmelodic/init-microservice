package com.harmelodic.init.microservice.app.structure.account;

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.harmelodic.init.microservice.app.structure.account.TestConstants.EXAMPLE_ACCOUNT_SERVICE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
@Provider(EXAMPLE_ACCOUNT_SERVICE)
@PactBroker
class AccountControllerTest {

    public static final String ACCOUNT_DOES_NOT_EXIST = "account_does_not_exist";
    public static final String SERVER_ERROR_WILL_OCCUR = "server_error_will_occur";
    public static final String ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID = "account_exists_with_id_name_and_customer_id";
    public static final String THREE_ACCOUNTS_EXIST = "three_accounts_exist";

    @LocalServerPort
    int port;

    @Autowired
    JdbcClient jdbcClient;

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
        flyway.clean();
        flyway.migrate();
    }

    void wipeAccountTable() {
        jdbcClient.sql("TRUNCATE TABLE account").update();
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
        jdbcClient.sql("DROP TABLE account").update();
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

        jdbcClient.sql("INSERT INTO account (id, name, customer_id) VALUES (:id, :name, :customer_id)")
                .param("id", id)
                .param("name", name)
                .param("customer_id", customerId)
                .update();
    }

    @State(value = ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID, action = StateChangeAction.TEARDOWN)
    void accountExistsWithIdNameAndCustomerIdTeardown() {
        wipeAccountTable();
    }

    @State(value = THREE_ACCOUNTS_EXIST, action = StateChangeAction.SETUP)
    void threeAccountsExistSetup() {
        List<Integer> things = List.of(0, 1, 2);
        things.forEach(thing -> jdbcClient.sql("INSERT INTO account (id, name, customer_id) VALUES (:id, :name, :customer_id)")
                .param("id", UUID.randomUUID())
                .param("name", "Some Name" + thing)
                .param("customer_id", UUID.randomUUID())
                .update());
    }

    @State(value = THREE_ACCOUNTS_EXIST, action = StateChangeAction.TEARDOWN)
    void threeAccountsExistTeardown() {
        wipeAccountTable();
    }
}
