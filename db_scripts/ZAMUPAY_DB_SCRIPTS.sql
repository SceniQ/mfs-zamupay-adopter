SET FOREIGN_CHECKS=0;
CREATE
DATABASE IF NOT EXISTS mfsafric_zamupay_test;
USE
mfsafric_zamupay_test;

DROP TABLE IF EXISTS system_config;
CREATE TABLE system_config
(
    config_id    int(11) NOT NULL AUTO_INCREMENT,
    config_key   varchar(100)  NOT NULL,
    config_value varchar(2000) NOT NULL,
    PRIMARY KEY (config_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS event_log;
CREATE TABLE event_log
(
    event_id         bigint(20) NOT NULL AUTO_INCREMENT,
    mfs_reference_id varchar(200),
    date_logged      datetime NOT NULL,
    request          mediumtext,
    response         mediumtext,
    service_name     varchar(200) DEFAULT NULL,
    PRIMARY KEY (event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS transaction_log;
CREATE TABLE transaction_log
(
    transaction_log_id int(10) NOT NULL AUTO_INCREMENT,
    mfs_reference_id   varchar(55) NOT NULL,
    transaction_id     varchar(55),
    receiver_msisdn    varchar(55) NOT NULL,
    primary_account_number varchar(55) NOT NULL,
    amount             double      NOT NULL,
    result_code        int(10),
    result_desc        varchar(200),
    date_logged        datetime    NOT NULL,
    PRIMARY KEY (transaction_log_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_config
-- ----------------------------
insert into system_config(config_key, config_value) values ('B2C_TRANSFER_URL', 'http://localhost:8080/mpambaservice/v1/B2CTransfer');
insert into system_config(config_key, config_value) values ('CHECK_B2C_TRANSACTION_URL', 'http://localhost:8080/mpambaservice/v1/CheckB2CTransaction');
insert into system_config(config_key, config_value) values ('QUERY_KYC_URL', 'http://localhost:8080/mpambaservice/v1/KYC');
insert into system_config(config_key, config_value) values ('QUERY_ACCOUNT_BALANCE_URL', 'http://localhost:8080/mpambaagentservice/v1/QueryBalance');
insert into system_config(config_key, config_value) values ('MOCK_QB_RESPONSE','{"accountHolder":"420243","accountName":"420243 - BILLING ORDINARY AGENT WALLET TEST","accountType":"Organization Commission Received Account","accountNumber":"500000000100202129","accountStatus":"Active","currency":"MWK","availableBalance":1377.6,"reservedBalance":0.0,"unclearedBalance":0.0,"currentBalance":1377.6}');
insert into system_config(config_key, config_value) values ('MOCK_QTS_RESPONSE','{"conversation_id":"AG_20210817_00005b4372a1193a9171","result_code":"401","result_desc":"Initiator authentication error.","response_time":"1629281993744","transaction_id":"","org_tran_id":""}');
insert into system_config(config_key, config_value) values ('MOCK_TS_RESPONSE','{"conversation_id":"AG_20210817_00005b4372a1193a9171","result_code":"401","result_desc":"Initiator authentication error.","response_time":"1629281993744","transaction_id":"","org_tran_id":""}');
insert into system_config(config_key, config_value) values ('MOCK_KYC_RESPONSE','{"resultCode": "200","resultDesc": "Success","msisdn": "+27637938237","first_name": "Samuel","last_name": "Komane"}');
insert into system_config(config_key, config_value) values ('TIMEOUT','60000');
SET FOREIGN_CHECKS=1;
commit;
