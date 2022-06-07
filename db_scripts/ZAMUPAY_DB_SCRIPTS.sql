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
   mfs_reference_id                     varchar(100) NOT NULL,
   payment_notes                        varchar(255) NOT NULL,
   recipient_phone_number               varchar(13) NOT NULL,
   recipient_primary_account_number     varchar(255) NOT NULL,
   recipient_mccmnc                     varchar(255),
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
    remitter_ccy                            varchar(3)  DEFAULT NULL,
    remitter_financial_institution          varchar(64) DEFAULT NULL,
    remitter_source_of_funds                varchar(64) DEFAULT NULL,
    remitter_principal_activity             varchar(64) DEFAULT NULL,
    recipient_name                          varchar(64) NOT NULL,
    recipient_address                       varchar(64) DEFAULT NULL,
    recipient_email_address                 varchar(64) DEFAULT NULL,
    recipient_id_type                       varchar(255) DEFAULT NULL,
    recipient_id_number                     varchar(255) DEFAULT NULL,
    recipient_financial_institution         varchar(255) DEFAULT NULL,
    recipient_purpose                       varchar(255) DEFAULT NULL,
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

DROP TABLE IF EXISTS country_master;
CREATE TABLE country_master
(
    country_id          int(11) NOT NULL AUTO_INCREMENT,
    country_code        varchar(2)  DEFAULT NULL,
    country_code_alpha3 varchar(3)  DEFAULT NULL,
    country_name        varchar(45) DEFAULT NULL,
    currency_code       varchar(5)  DEFAULT NULL,
    numeric_code        varchar(5)  DEFAULT NULL,
    phone_code          int(11) DEFAULT NULL,
    PRIMARY KEY (country_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO system_config (config_key, config_value) VALUES ('TOKEN_URL','http://54.224.92.175:10001/connect/token');
INSERT INTO system_config (config_key, config_value) VALUES ('PAYMENT_ORDER_URL','https://sandboxapi.zamupay.com/v1/payment-order/new-order');
INSERT INTO system_config (config_key, config_value) VALUES ('PAYMENT_STATUS_URL','https://sandboxapi.zamupay.com/v1/payment-order/check-status?Id=EDK9CFCCQV9B6ACCTGFK&IdType=OriginatorConversationId');
INSERT INTO system_config (config_key, config_value) VALUES ('FIND_CHARGES_URL','https://sandboxapi.zamupay.com/v1/charge/fetch-charges?transactionTypeId=f04b0548-e884-ec11-ab83-b0b1945cf762');
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


-- ----------------------------
-- MCCMNCMaster Records
-- ----------------------------
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('KE','639','05','Econet');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('KE','639','02','Safaricom Ltd');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('KE','639','07','Telkom');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('KE','639','03','Airtel');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','02','Telkom');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','21','Cape Town Metropolitan');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','07','Cell C');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','10','MTN');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','12','MTN');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','06','Sentech');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','01','Vodacom');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZA','655','19','Wireless Business Solutions (Pty) Ltd');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZW','648','04','Econet');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZW','648','01','NetOne');
INSERT INTO mncmcc_master(country_code,mcc,mnc,operator_name)VALUES('ZW','648','03','Telecel');


-- ----------------------------
-- Records of country_master
-- ----------------------------
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('1', 'Afghanistan', '93', 'AFN', 'AFG', 'AF', '4');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('2', 'Aland Islands', '35818', 'EUR', null, 'AX', '248');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('3', 'Albania', '355', 'ALL', 'ALB', 'AL', '8');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('4', 'Algeria', '213', 'DZD', 'DZA', 'DZ', '12');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('5', 'American Samoa', '1684', 'USD', 'ASM', 'AS', '16');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('6', 'Andorra', '376', 'EUR', 'AND', 'AD', '20');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('7', 'Angola', '244', 'AOA', 'AGO', 'AO', '24');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('8', 'Anguilla', '1264', 'XCD', 'AIA', 'AI', '660');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('9', 'Antarctica', '0', ' ', 'ATA', 'AQ', '10');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('10', 'Antigua and Barbuda', '1268', 'XCD', 'ATG', 'AG', '28');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('11', 'Argentina', '54', 'ARS', 'ARG', 'AR', '32');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('12', 'Armenia', '374', 'AMD', 'ARM', 'AM', '51');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('13', 'Aruba', '297', 'AWG', 'ABW', 'AW', '51');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('14', 'Australia', '61', 'AUD', 'AUS', 'AU', '36');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('15', 'Austria', '43', 'EUR', 'AUT', 'AT', '40');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('16', 'Azerbaijan', '994', 'AZN', 'AZE', 'AZ', '31');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('17', 'Bahamas', '1242', 'BSD', 'BHS', 'BS', '44');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('18', 'Bahrain', '973', 'BHD', 'BHR', 'BH', '44');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('19', 'Bangladesh', '880', 'BDT', 'BGD', 'BD', '50');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('20', 'Barbados', '1246', 'BBD', 'BRB', 'BB', '52');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('21', 'Belarus', '375', 'BYN', 'BLR', 'BY', '112');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('22', 'Belgium', '32', 'EUR', 'BEL', 'BE', '56');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('23', 'Belize', '501', 'BZD', 'BLZ', 'BZ', '84');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('24', 'Benin', '229', 'XOF', 'BEN', 'BJ', '204');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('25', 'Bermuda', '1441', 'BMD', 'BMU', 'BM', '60');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('26', 'Bhutan', '975', 'BTN', 'BTN', 'BT', '64');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('27', 'Plurinational State of Bolivia', '591', 'BOB', 'BOL', 'BO', '68');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('28', 'Sint Eustatius and Saba Bonaire', '5997', 'USD', 'BES', 'BQ', '535');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('29', 'Bosnia and Herzegovina', '387', 'BAM', 'BIH', 'BA', '70');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('30', 'Botswana', '267', 'BWP', 'BWA', 'BW', '72');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('31', 'Bouvet Island', '0', 'NOK', 'BVT', 'BV', '74');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('32', 'Brazil', '55', 'BRL', 'BRA', 'BR', '76');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('33', 'British Indian Ocean Territory', '246', 'USD', 'IOT', 'IO', '86');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('34', 'Brunei Darussalam', '673', 'BND', 'BRN', 'BN', '96');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('35', 'Bulgaria', '359', 'BGN', 'BGR', 'BG', '100');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('36', 'Burkina Faso', '226', 'XOF', 'BFA', 'BF', '854');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('37', 'Burundi', '257', 'BIF', 'BDI', 'BI', '108');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('38', 'Cambodia', '855', 'KHR', 'KHM', 'KH', '116');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('39', 'Cameroon', '237', 'XAF', 'CMR', 'CM', '120');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('40', 'Canada', '1', 'CAD', 'CAN', 'CA', '124');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('41', 'Cape Verde', '238', 'CVE', 'CPV', 'CV', '132');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('42', 'Cayman Islands', '1345', 'KYD', 'CYM', 'KY', '136');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('43', 'Central African Republic', '236', 'XAF', 'CAF', 'CF', '140');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('44', 'Chad', '235', 'XAF', 'TCD', 'TD', '148');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('45', 'Chile', '56', 'CLF', 'CHL', 'CL', '152');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('46', 'China', '86', 'CNY', 'CHN', 'CN', '156');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('47', 'Christmas Island', '6189164', 'AUD', 'CXR', 'CX', '162');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('48', 'Cocos (Keeling,) Islands', '6189162', 'AUD', 'CCK', 'CC', '166');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('49', 'Colombia', '57', 'COP', 'COL', 'CO', '170');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('50', 'Comoros', '269', 'KMF', 'COM', 'KM', '174');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('51', 'Congo', '242', 'XAF', 'COG', 'CG', '178');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('52', 'The Democratic Republic of the Congo', '243', 'CDF', 'COD', 'CD', '180');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('53', 'Cook Islands', '682', 'NZD', 'COK', 'CK', '184');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('54', 'Costa Rica', '506', 'CRC', 'CRI', 'CR', '188');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('55', 'Ivory Coast ', '225', 'XOF', 'CIV', 'CI', '384');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('56', 'Croatia', '385', 'HRK', 'HRV', 'HR', '191');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('57', 'Cuba', '53', 'CUC', 'CUB', 'CU', '192');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('58', 'Curacao', '599', 'ANG', 'CUW', 'CW', '531');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('59', 'Cyprus', '357', 'EUR', 'CYP', 'CY', '196');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('60', 'Czech Republic', '420', 'CZK', 'CZE', 'CZ', '203');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('61', 'Denmark', '45', 'DKK', 'DNK', 'DK', '208');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('62', 'Djibouti', '253', 'DJF', 'DJI', 'DJ', '262');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('63', 'Dominica', '1767', 'XCD', 'DMA', 'DM', '212');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('64', 'Dominican Republic', '0', 'DOP', 'DOM', 'DO', '214');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('65', 'Ecuador', '593', 'USD', 'ECU', 'EC', '218');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('66', 'Egypt', '20', 'EGP', 'EGY', 'EG', '818');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('67', 'El Salvador', '503', 'SVC', 'SLV', 'SV', '222');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('68', 'Equatorial Guinea', '240', 'XAF', 'GNQ', 'GQ', '226');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('69', 'Eritrea', '291', 'ERN', 'ERI', 'ER', '232');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('70', 'Estonia', '372', 'EUR', 'EST', 'EE', '233');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('71', 'Ethiopia', '251', 'ETB', 'ETH', 'ET', '231');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('72', 'Falkland Islands (Malvinas,)', '500', 'FKP', 'FLK', 'FK', '238');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('73', 'Faroe Islands', '298', 'DKK', 'FRO', 'FO', '234');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('74', 'Fiji', '679', 'FJD', 'FJI', 'FJ', '242');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('75', 'Finland', '358', 'EUR', 'FIN', 'FI', '246');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('76', 'France', '33', 'EUR', 'FRA', 'FR', '250');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('77', 'French Guiana', '594', 'EUR', 'GUF', 'GF', '254');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('78', 'French Polynesia', '689', 'XPF', 'PYF', 'PF', '258');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('79', 'French Southern Territories', '0', 'EUR', 'ATF', 'TF', '260');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('80', 'Gabon', '241', 'XAF', 'GAB', 'GA', '266');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('81', 'Gambia', '220', 'GMD', 'GMB', 'GM', '270');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('82', 'Georgia', '995', 'GEL', 'GEO', 'GE', '268');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('83', 'Germany', '49', 'EUR', 'DEU', 'DE', '276');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('84', 'Ghana', '233', 'GHS', 'GHA', 'GH', '288');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('85', 'Gibraltar', '350', 'GIP', 'GBI', 'GI', '292');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('86', 'Greece', '30', 'EUR', 'GRC', 'GR', '300');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('87', 'Greenland', '299', 'DKK', 'GRL', 'GL', '304');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('88', 'Grenada', '1473', 'XCD', 'GRD', 'GD', '308');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('89', 'Guadeloupe', '590', 'EUR', 'GLP', 'GP', '312');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('90', 'Guam', '1671', 'USD', 'GUM', 'GU', '316');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('91', 'Guatemala', '502', 'GTQ', 'GTM', 'GT', '320');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('92', 'Guernsey', '0', 'GBP', 'GGY', 'GG', '831');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('93', 'Guinea', '224', 'GNF', 'GIN', 'GN', '324');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('94', 'Guinea-Bissau', '245', 'XOF', 'GNB', 'GW', '624');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('95', 'Guyana', '592', 'GYD', 'GUY', 'GY', '328');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('96', 'Haiti', '509', 'HTG', 'HTI', 'HT', '332');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('97', 'Heard Island and McDonald Islands', '0', 'AUD', 'HMD', 'HM', '334');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('98', 'Holy See (Vatican City State,)', '0', 'EUR', 'VAT', 'VA', '336');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('99', 'Honduras', '504', 'HNL', 'HND', 'HN', '340');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('100', 'Hong Kong', '852', 'HKD', 'HKG', 'HK', '344');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('101', 'Hungary', '36', 'HUF', 'HUN', 'HU', '348');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('102', 'Iceland', '354', 'ISK', 'HUN', 'IS', '352');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('103', 'India', '91', 'INR', 'IND', 'IN', '356');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('104', 'Indonesia', '62', 'IDR', 'IDN', 'ID', '356');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('105', 'Iran, Islamic Republic of', '0', 'IRR', 'IRN', 'IR', '364');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('106', 'Iraq', '964', 'IQD', 'IRQ', 'IQ', '368');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('107', 'Ireland', '353', 'EUR', 'IRL', 'IE', '372');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('108', 'Isle of Man', '44', 'GBP', 'IMN', 'IM', '833');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('109', 'Israel', '972', 'ILS', 'ISR', 'IL', '376');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('110', 'Italy', '39', 'EUR', 'ITA', 'IT', '380');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('111', 'Jamaica', '1876', 'JMD', 'JAM', 'JM', '388');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('112', 'Japan', '81', 'JPY', 'JPN', 'JP', '392');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('113', 'Jersey', '441534', 'GBP', 'JEY', 'JE', '832');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('114', 'Jordan', '962', 'JOD', 'JOR', 'JO', '400');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('115', 'Kazakhstan', '76', 'KZT', 'KAZ', 'KZ', '398');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('116', 'Kenya', '254', 'KES', 'KEN', 'KE', '404');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('117', 'Kiribati', '686', 'AUD', 'KIR', 'KI', '296');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('118', 'Korea, Democratic Peoples Republic of', '0', 'KPW', 'PRK', 'KP', '408');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('119', 'Korea, Republic of', '0', 'KRW', 'KOR', 'KR', '410');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('120', 'Kuwait', '965', 'KWD', 'KWT', 'KW', '414');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('121', 'Kyrgyzstan', '996', 'KGS', 'KGN', 'KG', '417');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('122', 'Lao Peoples Democratic Republic', '0', 'LAK', 'LAO', 'LA', '418');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('123', 'Latvia', '371', 'EUR', 'LVA', 'LV', '428');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('124', 'Lebanon', '961', 'LBP', 'LBN', 'LB', '422');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('125', 'Lesotho', '266', 'LSL', 'LSO', 'LS', '426');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('126', 'Liberia', '231', 'ZAR', 'LBR', 'LR', '430');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('127', 'Libya', '218', 'LYD', 'LBY', 'LY', '434');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('128', 'Liechtenstein', '423', 'CHF', 'LIE', 'LI', '438');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('129', 'Lithuania', '370', 'EUR', 'LTU', 'LT', '440');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('130', 'Luxembourg', '352', 'EUR', 'LUX', 'LU', '442');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('131', 'Macao', '0', 'MOP', 'MAC', 'MO', '446');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('132', 'Macedonia, the former Yugoslav Republic of', '0', 'MKD', '', 'MK', '807');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('133', 'Madagascar', '261', 'MGA', 'MDG', 'MG', '450');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('134', 'Malawi', '265', 'MWK', 'MWI', 'MW', '454');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('135', 'Malaysia', '60', 'MYR', 'MYS', 'MY', '458');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('136', 'Maldives', '960', 'MVR', 'MDV', 'MD', '462');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('137', 'Mali', '223', 'XOF', 'MLI', 'ML', '466');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('138', 'Malta', '356', 'EUR', 'MLT', 'MT', '470');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('139', 'Marshall Islands', '692', 'USD', 'MHL', 'MH', '584');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('140', 'Martinique', '596', 'EUR', 'MTQ', 'MQ', '474');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('141', 'Mauritania', '222', 'MRU', 'MRT', 'MR', '478');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('142', 'Mauritius', '230', 'MUR', 'MUS', 'MU', '480');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('143', 'Mayotte', '262', 'EUR', 'MYT', 'YT', '175');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('144', 'Mexico', '52', 'MXN', 'MEX', 'MX', '484');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('145', 'Micronesia, Federated States of', '691', 'USD', 'FSM', 'FM', '583');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('146', 'Moldova, Republic of', '0', 'MDL', 'MDA', 'MD', '498');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('147', 'Monaco', '377', 'EUR', 'MCO', 'MC', '492');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('148', 'Mongolia', '976', 'MNT', 'MNG', 'MN', '496');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('149', 'Montenegro', '382', 'EUR', 'MNE', 'ME', '499');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('150', 'Montserrat', '1664', 'XCD', 'MSR', 'MS', '500');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('151', 'Morocco', '212', 'MAD', 'MAR', 'MA', '504');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('152', 'Mozambique', '258', 'MZN', 'MOZ', 'MZ', '508');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('153', 'Myanmar', '95', 'MMK', 'MMR', 'MM', '104');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('154', 'Namibia', '264', 'NAD', 'NAM', 'NA', '516');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('155', 'Nauru', '674', 'AUD', 'NRU', 'NR', '520');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('156', 'Nepal', '977', 'NPR', 'NPL', 'NP', '524');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('157', 'Netherlands', '31', 'EUR', 'NLD', 'NL', '528');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('158', 'New Caledonia', '687', 'XPF', 'NCL', 'NC', '540');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('159', 'New Zealand', '64', 'NZD', 'NZL', 'NZ', '554');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('160', 'Nicaragua', '505', 'NIO', 'NIC', 'NI', '558');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('161', 'Niger', '227', 'XOF', 'NER', 'NE', '562');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('162', 'Nigeria', '234', 'NGN', 'NGA', 'NG', '566');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('163', 'Niue', '683', 'NZD', 'NIU', 'NU', '570');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('164', 'Norfolk Island', '672', 'AUD', 'NFK', 'NF', '574');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('165', 'Northern Mariana Islands', '1670', 'USD', 'MNP', 'MP', '580');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('166', 'Norway', '47', 'NOK', 'NOR', 'NO', '578');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('167', 'Oman', '968', 'OMR', 'OMN', 'OM', '512');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('168', 'Pakistan', '92', 'PKR', 'PAK', 'PK', '586');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('169', 'Palau', '680', 'USD', 'PLW', 'PW', '585');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('170', 'Palestinian Territory, Occupied', '0', ' ', 'PSE', 'PS', '275');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('171', 'Panama', '507', 'PAB', 'PAN', 'PA', '591');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('172', 'Papua New Guinea', '675', 'PGK', 'PNG', 'PG', '598');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('173', 'Paraguay', '595', 'PYG', 'PRY', 'PY', '600');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('174', 'Peru', '51', 'PEN', 'PER', 'PE', '604');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('175', 'Philippines', '63', 'PHP', 'PHL', 'PH', '608');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('176', 'Pitcairn', '64', 'NZD', 'PCN', 'PN', '612');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('177', 'Poland', '48', 'PLN', 'POL', 'PL', '616');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('178', 'Portugal', '351', 'EUR', 'PRT', 'PT', '620');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('179', 'Puerto Rico', '1787', 'USD', 'PRI', 'PR', '630');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('180', 'Qatar', '974', 'QAR', 'QAT', 'QA', '634');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('181', 'Reunion', '262', 'MKD', 'REU', 'RE', '638');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('182', 'Romania', '40', 'RON', 'ROU', 'RO', '642');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('183', 'Russian Federation', '0', 'RUB', 'RUS', 'RU', '643');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('184', 'Rwanda', '250', 'RWF', 'RWA', 'RW', '646');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('185', 'Saint Barthelemy', '590', 'EUR', 'BLM', 'BL', '652');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('186', 'Saint Helena, Ascension and Tristan da Cunha', '290', 'EUR', 'SHN', 'SH', '654');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('187', 'Saint Kitts and Nevis', '1869', 'XCD', 'KNA', 'KN', '659');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('188', 'Saint Lucia', '1758', 'XCD', 'LCA', 'LC', '662');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('189', 'Saint Martin (French part,)', '590', 'EUR', 'MAF', 'MF', '663');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('190', 'Saint Pierre and Miquelon', '508', 'EUR', 'SPM', 'PM', '666');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('191', 'Saint Vincent and the Grenadines', '1784', 'XCD', 'VCT', 'VC', '670');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('192', 'Samoa', '685', 'WST', 'WSM', 'WS', '882');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('193', 'San Marino', '378', 'EUR', 'SMR', 'SM', '674');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('194', 'Sao Tome and Principe', '239', 'STN', 'STP', 'ST', '678');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('195', 'Saudi Arabia', '966', 'SAR', 'SAU', 'SA', '682');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('196', 'Senegal', '221', 'XOF', 'SEN', 'SN', '686');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('197', 'Serbia', '381', 'RSD', 'SRB', 'RS', '688');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('198', 'Seychelles', '248', 'XCR', 'SYC', 'SC', '690');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('199', 'Sierra Leone', '232', 'SLL', 'SLE', 'SL', '694');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('200', 'Singapore', '65', 'SGD', 'SGP', 'SG', '702');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('201', 'Sint Maarten (Dutch part,)', '1721', 'ANG', 'SXM', 'SX', '534');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('202', 'Slovakia', '421', 'EUR', 'SVK', 'SK', '703');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('203', 'Slovenia', '386', 'EUR', 'SVN', 'SI', '705');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('204', 'Solomon Islands', '677', 'SBD', 'SLB', 'SB', '90');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('205', 'Somalia', '252', 'SOS', 'SOM', 'SO', '706');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('206', 'South Africa', '27', 'ZAR', 'ZAF', 'ZA', '710');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('207', 'South Georgia and the South Sandwich Islands', '500', ' ', 'SGS', 'GS', '239');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('208', 'South Sudan', '211', 'SSP', 'SSD', 'SS', '728');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('209', 'Spain', '34', 'EUR', 'ESP', 'ES', '724');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('210', 'Sri Lanka', '94', 'LKR', 'LKA', 'LK', '144');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('211', 'Sudan', '249', 'SDG', 'SDN', 'SD', '729');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('212', 'Suriname', '597', 'SRD', 'SUR', 'SR', '740');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('213', 'Svalbard and Jan Mayen', '0', 'NOK', 'SJM', 'SJ', '744');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('214', 'Swaziland', '268', 'SZL', '', 'SZ', '748');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('215', 'Sweden', '46', 'SEK', 'SWE', 'SE', '752');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('216', 'Switzerland', '41', 'CHE', 'CHE', 'CH', '756');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('217', 'Syrian Arab Republic', '0', 'SYP', 'SYR', 'SY', '760');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('218', 'Taiwan, Province of China', '0', 'TWD', 'TWN', 'TW', '158');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('219', 'Tajikistan', '992', 'TJS', 'TJK', 'TJ', '762');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('220', 'United Republic of Tanzania', '255', 'TZS', 'TZA', 'TZ', '834');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('221', 'Thailand', '66', 'THB', 'THA', 'TH', '764');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('222', 'Timor-Leste', '670', 'USD', 'TLS', 'TL', '626');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('223', 'Togo', '228', 'XOF', 'TGO', 'TG', '768');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('224', 'Tokelau', '690', 'NZD', 'TKL', 'TK', '772');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('225', 'Tonga', '676', 'TOP', 'TON', 'TO', '776');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('226', 'Trinidad and Tobago', '1868', 'TTD', 'TTO', 'TT', '780');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('227', 'Tunisia', '216', 'TND', 'TUN', 'TN', '788');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('228', 'Turkey', '90', 'TRY', 'TUR', 'TR', '792');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('229', 'Turkmenistan', '993', 'TMT', 'TKM', 'TM', '795');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('230', 'Turks and Caicos Islands', '1649', 'USD', 'TCA', 'TC', '796');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('231', 'Tuvalu', '688', 'AUD', 'TUV', 'TV', '798');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('232', 'Uganda', '256', 'UGX', 'UGA', 'UG', '800');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('233', 'Ukraine', '380', 'UAH', 'UKR', 'UA', '804');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('234', 'United Arab Emirates', '971', 'AED', 'ARE', 'AE', '784');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('235', 'United Kingdom', '44', 'GBP', 'GBR', 'GB', '826');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('236', 'United States', '1', 'USD', 'USA', 'US', '840');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('237', 'United States Minor Outlying Islands', '0', 'USD', 'UMI', 'UM', '581');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('238', 'Uruguay', '598', 'UYI', 'URY', 'UY', '858');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('239', 'Uzbekistan', '998', 'UZS', 'UZB', 'UZ', '860');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('240', 'Vanuatu', '678', 'VUV', 'VUT', 'VU', '548');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('241', 'Venezuela, Bolivarian Republic of', '58', 'VEF', 'VEN', 'VE', '862');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('242', 'Viet Nam', '84', 'VND', 'VNM', 'VN', '704');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('243', 'Virgin Islands, British', '0', 'USD', 'VGB', 'VG', '92');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('244', 'Virgin Islands, U.S.', '1340', 'USD', 'VIR', 'VI', '850');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('245', 'Wallis and Futuna', '681', 'XPF', 'WLF', 'WF', '876');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('246', 'Western Sahara', '0', 'MAD', 'ESH', 'EH', '732');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('247', 'Yemen', '967', 'YER', 'YEM', 'YE', '887');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('248', 'Zambia', '260', 'ZMW', 'ZMB', 'ZM', '894');
INSERT INTO country_master(country_id,country_name,phone_code,currency_code,country_code_alpha3,country_code,numeric_code) VALUES ('249', 'Zimbabwe', '263', 'ZWL', 'ZWE', 'ZW', '716');

SET FOREIGN_CHECKS=1;
commit;
