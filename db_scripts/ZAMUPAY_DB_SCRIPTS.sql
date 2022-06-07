SET FOREIGN_CHECKS=0;
CREATE
DATABASE IF NOT EXISTS mfsafric_zamupay_test;
USE
mfsafric_zamupay_test;

DROP TABLE IF EXISTS system_config;
CREATE TABLE system_config (
  config_id    int(11) NOT NULL AUTO_INCREMENT,
  config_key   varchar(255) NOT NULL,
  config_value varchar(255) NOT NULL,
  PRIMARY KEY (config_id),
  UNIQUE KEY (config_key)
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
   transaction_log_id                   bigint(11) NOT NULL AUTO_INCREMENT,
   originator_conversation_id           varchar(100) NOT NULL,
   payment_notes                        varchar(255) NOT NULL,
   recipient_phone_number               varchar(13) NOT NULL,
   recipient_primary_account_number     varchar(255) NOT NULL,
   recipient_mccmnc                     varchar(255) ,
   recipient_ccy                        varchar(255) NOT NULL,
   recipient_country                    varchar(255) NOT NULL,
   transaction_routeId                  varchar(255) NOT NULL,
   transaction_Channel_type             varchar(255) NOT NULL,
   remarks                              varchar(255) NOT NULL,
   system_conversation_id               varchar(255) NOT NULL,
   result_code                          varchar(255) NOT NULL,
   result_desc                          varchar(255) NOT NULL,
   transaction_amount                   varchar(255) NOT NULL,
    date_logged  		                datetime NOT NULL,
    PRIMARY KEY (transaction_log_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS kcb_transaction_detail;
CREATE TABLE kcb_transaction_detail
(
    transaction_detail_id                   bigint(20) NOT NULL AUTO_INCREMENT,
    remitter_name                           varchar(128) DEFAULT NULL,
    remitter_address                        varchar(128) DEFAULT NULL,
    remitter_phone_number                   varchar(13) DEFAULT NULL,
    remitter_id_type                        varchar(13) DEFAULT NULL,
    remitter_id_number                      varchar(13) DEFAULT NULL,
    remitter_country                        varchar(50) DEFAULT NULL,
    remitter_ccy                            varchar(3) DEFAULT NULL,
    remitter_financial_institution          varchar(64) DEFAULT NULL,
    remitter_source_of_funds                varchar(64) DEFAULT NULL,
    remitter_principal_activity             varchar(64) DEFAULT NULL,
    recipient_name                          varchar(64) NOT NULL,
    recipient_address                       varchar(64) NOT NULL,
    recipient_email_address                 varchar(64) DEFAULT NULL,
    recipient_id_type                       varchar(255) DEFAULT NULL,
    recipient_id_number                     varchar(255) NOT NULL,
    recipient_financial_institution         varchar(255) DEFAULT NULL,
    recipient_purpose                       varchar(255) NOT NULL,
    transaction_reference                   varchar(255) NOT NULL,
    transaction_system_trace_audit_number   varchar(255) NOT NULL,
    transaction_id                          int(11) NOT NULL,
    PRIMARY KEY (transaction_detail_id),
    UNIQUE KEY UK_transaction_detail (transaction_id),
    CONSTRAINT FK_transaction_detail FOREIGN KEY (transaction_id) REFERENCES transaction_log (transaction_log_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS access_token;
CREATE TABLE access_token
(
    id               bigint NOT NULL AUTO_INCREMENT,
    access_token            longtext NOT NULL,
    created_on     datetime   NOT NULL,
    expires_in      datetime   NOT NULL,
    token_type     varchar(11) NOT NULL,
    scope          varchar(255)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS identity_type;
CREATE TABLE identity_type
(
    id               bigint NOT NULL AUTO_INCREMENT,
    id_type     varchar(3)   NOT NULL,
    id_value     varchar(255)   NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS transaction_route;
CREATE TABLE transaction_route
(
    id               bigint NOT NULL AUTO_INCREMENT,
    route_id     varchar(3)   NOT NULL,
    channel_type     varchar(255)   NOT NULL,
    transaction_type_id varchar(100) NOT NULL
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS mncmcc_master;
CREATE TABLE mncmcc_master
(
    mncmcc_id     int(11) NOT NULL AUTO_INCREMENT,
    country_code  varchar(100) NOT NULL,
    mcc           varchar(3) NOT NULL,
    mnc           varchar(2) NOT NULL,
    operator_name varchar(100) NOT NULL,
    PRIMARY KEY (mncmcc_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO system_config (config_key, config_value) VALUES ('TOKEN_URL','http://54.224.92.175:10001/connect/token');
INSERT INTO system_config (config_key, config_value) VALUES ('CLIENT_ID','PyPay_api');
INSERT INTO system_config (config_key, config_value) VALUES ('CLIENT_SECRET','PyPayApiSecret');
INSERT INTO system_config (config_key, config_value) VALUES ('GRANT_TYPE','client_credentials');
INSERT INTO system_config (config_key, config_value) VALUES ('SCOPE','PyPay_api');
INSERT INTO system_config (config_key, config_value) VALUES ('TIMEOUT','60000');

-- ----------------------------
-- Records of identity_type
-- ----------------------------
insert into identity_type (id_type,id_value) values ('PP','Passport');
insert into identity_type (id_type,id_value) values ('ID','National ID card');
insert into identity_type (id_type,id_value) values ('DL','Driver’s License');


SET FOREIGN_CHECKS=1;
commit;
