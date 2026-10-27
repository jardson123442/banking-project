package com.v1.bms.project.application;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    public static final String CLASS_METHOD_MESSAGE_PATTERN = "Class {}, method {}";
    public static final String PIX_KEY_MESSAGE_PATTERN = "PixKey: {}";
    public static final String CORE_BANK_PRODUCT_ID = "DIGITAL_ACCOUNT";
    public static final String IDEMPOTENCY_ID = "Idempotency-Id";

    public static final String SCOPE_CUSTOMER_PROFILE    = "Customer.Profile";
    public static final String SCOPE_FINTECH_WRITE       = "Fintech.WRITE";
    public static final String SCOPE_FINTECH_TRANSACTION = "Fintech.Customer.Transaction";
    public static final String VIVO_PAY_AGREEMENT = "VIVO_PAY_AGREEMENT";
    public static final String VIVO_PAY_CLOSURE_AGREEMENT = "VIVO_PAY_CLOSURE_AGREEMENT";

    public static final String DUE_TO_FRAUD_VALIDATION_PROCESS = "DUE_TO_FRAUD_VALIDATION_PROCESS";

}
