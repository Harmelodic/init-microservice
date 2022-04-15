package com.harmelodic.init.microservice.only.used.in.init;

import com.harmelodic.init.microservice.account.Account;

import java.util.UUID;
import java.util.regex.Pattern;

public class TestConstants {
    public static final String ACCOUNT_DOES_NOT_EXIST = "ACCOUNT_DOES_NOT_EXIST";
    public static final String SERVER_ERROR_WILL_OCCUR = "SERVER_ERROR_WILL_OCCUR";
    public static final String ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID = "ACCOUNT_EXISTS_WITH_ID_NAME_AND_CUSTOMER_ID";
    public static final String THREE_ACCOUNTS_EXIST = "THREE_ACCOUNTS_EXIST";

    public static final Pattern UUID_PATTERN =
            Pattern.compile("[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}");

    public static final Account ACCOUNT_EXAMPLE = new Account(
            UUID.fromString("35e30f1c-bbe3-4ee8-8d1d-d320615e554e"),
            "Matt",
            UUID.fromString("ecaa5fc1-4587-4734-adf1-40cbcbecad8a"));
}
