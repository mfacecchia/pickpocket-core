START TRANSACTION;

USE pickpocket;

DROP TABLE IF EXISTS revision_info_seq;
CREATE TABLE revision_info_seq (
    next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO revision_info_seq VALUES (1);

DROP TABLE IF EXISTS revision_info;
CREATE TABLE revision_info (
    id INT NOT NULL,
    timestamp BIGINT NOT NULL,

    CONSTRAINT PK_revision_info PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255) NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    last_login DATETIME(6) NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_user PRIMARY KEY (id),
    CONSTRAINT UK_user__external_id UNIQUE (external_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS attachment;
CREATE TABLE attachment (
    id INT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    mimetype VARCHAR(255) NULL,
    size BIGINT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_attachment PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS account;
CREATE TABLE account (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL,
    wealth DOUBLE NOT NULL,
    user_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_account PRIMARY KEY (id),
    CONSTRAINT FK_account__user_id FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS split;
CREATE TABLE split (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    theoretical_amount DOUBLE NOT NULL,
    available_amount DOUBLE NOT NULL,
    split_percentage SMALLINT NOT NULL,
    active BIT(1) NOT NULL,
    is_default BIT(1) NOT NULL,
    account_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_split PRIMARY KEY (id),
    CONSTRAINT FK_split__account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS goal;
CREATE TABLE goal (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    target_amount DOUBLE NOT NULL,
    current_amount DOUBLE NOT NULL,
    completed BIT(1) NOT NULL,
    completed_at DATETIME(6) NULL,
    split_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_goal PRIMARY KEY (id),
    CONSTRAINT FK_goal__split_id FOREIGN KEY (split_id) REFERENCES split (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS paycheck;
CREATE TABLE paycheck (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL,
    issue_day_of_month SMALLINT NOT NULL,
    amount DOUBLE NOT NULL,
    account_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_paycheck PRIMARY KEY (id),
    CONSTRAINT UK_paycheck__account_id UNIQUE (account_id),
    CONSTRAINT FK_paycheck__account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS issued_paycheck;
CREATE TABLE issued_paycheck (
    id INT NOT NULL AUTO_INCREMENT,
    issued_amount DOUBLE NOT NULL,
    issued_at DATETIME(6) NOT NULL,
    successful BIT(1) NOT NULL,
    paycheck_id INT NULL,
    account_id INT NOT NULL,
    split_id INT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_issued_paycheck PRIMARY KEY (id),
    CONSTRAINT FK_issued_paycheck__paycheck_id FOREIGN KEY (paycheck_id) REFERENCES paycheck (id) ON DELETE SET NULL,
    CONSTRAINT FK_issued_paycheck__account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    CONSTRAINT FK_issued_paycheck__split_id FOREIGN KEY (split_id) REFERENCES split (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS purchase;
CREATE TABLE purchase (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL,
    amount DOUBLE NOT NULL,
    goal_id INT NULL,
    split_id INT NULL,
    account_id INT NOT NULL,
    category ENUM("PURCHASE", "COMPLETED_GOAL") NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_purchase PRIMARY KEY (id),
    CONSTRAINT FK_purchase__goal_id FOREIGN KEY (goal_id) REFERENCES goal (id) ON DELETE SET NULL,
    CONSTRAINT FK_purchase__split_id FOREIGN KEY (split_id) REFERENCES split (id) ON DELETE SET NULL,
    CONSTRAINT FK_purchase__account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS cash_transfer;
CREATE TABLE cash_transfer (
    id INT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL,
    from_split_id INT NULL,
    to_split_id INT NULL,
    account_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_cash_transfer PRIMARY KEY (id),
    CONSTRAINT FK_cash_transfer__from_split_id FOREIGN KEY (from_split_id) REFERENCES split (id) ON DELETE SET NULL,
    CONSTRAINT FK_cash_transfer__to_split_id FOREIGN KEY (to_split_id) REFERENCES split (id) ON DELETE SET NULL,
    CONSTRAINT FK_cash_transfer__account_id FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS purchase_attachment;
CREATE TABLE purchase_attachment (
    id INT NOT NULL AUTO_INCREMENT,
    purchase_id INT NOT NULL,
    attachment_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_purchase_attachment PRIMARY KEY (id),
    CONSTRAINT FK_purchase_attachment__purchase_id FOREIGN KEY (purchase_id) REFERENCES purchase (id) ON DELETE CASCADE,
    CONSTRAINT FK_purchase_attachment__attachment_id FOREIGN KEY (attachment_id) REFERENCES attachment (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS notification;
CREATE TABLE notification (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    public BIT(1) NOT NULL,
    user_id INT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_notification PRIMARY KEY (id),
    CONSTRAINT FK_notification__user_id FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS read_notification;
CREATE TABLE read_notification (
    id INT NOT NULL AUTO_INCREMENT,
    read_at DATETIME(6) NOT NULL,
    notification_id INT NOT NULL,
    user_id INT NOT NULL,

    created_date DATETIME(6) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_date DATETIME(6) NULL,
    modified_by VARCHAR(255) NULL,

    CONSTRAINT PK_read_notification PRIMARY KEY (id),
    CONSTRAINT FK_read_notification__notification_id FOREIGN KEY (notification_id) REFERENCES notification (id) ON DELETE CASCADE,
    CONSTRAINT FK_read_notification__user_id FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

COMMIT;

