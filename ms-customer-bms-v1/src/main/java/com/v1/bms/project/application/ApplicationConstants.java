package com.v1.bms.project.application;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ApplicationConstants {

    public static final String CLASS_METHOD_MESSAGE_PATTERN = "Class {}, method {}";
    public static final int MINIMUM_AGE = 18;
    public static final String ACCOUNT_ID = "accountId {}";
    public static final String VIVO_PAY = "Vivo Pay";
    public static final String CLOSED_ACCOUNT_USER_REQUESTED = "CLOSED_ACCOUNT_USER_REQUESTED";
    public static final String BC_PROTECT_UNAVAILABLE_REASON = "BC protect is unavailable now";
    public static final String BC_PROTECT_NOT_APPROVED_REASON = "Due to restrictions at BC PROTEGE";
    public static final String ACCOUNT_CLOSED_DESCRIPTION = "Account closed by user request";
    public static final String CREATED_ACCOUNT_USER_REQUESTED = "OPEN_ACCOUNT_USER_REQUESTED";
    public static final String ACCOUNT_CREATED_DESCRIPTION = "Account created with the term of use";
    public static final String DUE_TO_KYC_VALIDATION_PROCESS = "DUE_TO_KYC_VALIDATION_PROCESS";
    public static final String DUE_TO_FRAUD_VALIDATION_PROCESS = "DUE_TO_FRAUD_VALIDATION_PROCESS";

    public static final String LOG_CUSTOMER_ID = "CustomerId {}";
    public static final String LOG_ERROR_LIST_RECURRING = "Erro ao listar recorrências para o usuário {}: {}";
    public static final String LOG_ERROR_FETCH_ACTIVE = "Erro ao buscar recorrências ativas para o usuário {}: {}";
    public static final String LOG_SUCCESS_CANCEL = "Recorrência {} cancelada com sucesso para o usuário {}.";
    public static final String LOG_ERROR_CANCEL = "Erro ao cancelar recorrência {} para o usuário {}: {}";

    public static final BigDecimal PIX_WITHDRAWAL_PIX_CHANGE_DAYTIME_VALUE = BigDecimal.valueOf(3000.00);
    public static final BigDecimal PIX_WITHDRAWAL_PIX_CHANGE_NIGHTTIME_VALUE = BigDecimal.valueOf(1000.00);

    public static final String DOCUMENT_IDENTIFIER_BC_PROTOCOL = "AMES201";
    public static final String OBSERVATION_BC_PROTOCOL = "Envio de arquivo para geração de protocolo no BC Protege";

    public static final String VIVO_USER_PROFILE_NAME_CLAIM = "vivo.user.profile_name";
    public static final String VIVO_USER_PROFILE_NAME_VALUE = "PROF_ATENDIMENTO";
    public static final String VIVO_USER_PROFILE_CLAIM = "vivo.user";
    public static final String REQUEST_TOKEN_UUID_CLAIM = "entryUUID";
    public static final String FINTECH_CUSTOMER_WRITE = "Fintech.Customer_Service.WRITE";

    public static final String NOTIFICATION_SMTP_PROVIDER = "SMTP";
    public static final String NOTIFICATION_EMAIL_MSG_TYPE = "EMAIL";
    public static final String NOTIFICATION_SENDER_ID = "ms-vivo-fintech-bms-personal-accounts-v1";
    public static final String DIGITAL_ACCOUNT = "DIGITAL_ACCOUNT";
    public static final String SERVICE_NAME = "account-closure-service";
    public static final String ATTACHMENT_SOURCE_TYPE = "URL";
    public static final String ATTACHMENT_MIME_TYPE = "application/pdf";

    public static final int INITIALIZER_BATCH_SIZE = 1000;
    public static final String INITIALIZER_TARGET_STATUS = "ACTIVE";
}
