create table user_seq
(
    next_val bigint null
);

create table user
(
    id        bigint       not null
        primary key,
    email     varchar(255) not null,
    firstName varchar(255) not null,
    lastName  varchar(255) not null,
    password  varchar(255) not null,
    username  varchar(255) not null,
    constraint UK_e6gkqunxajvyxl5uctpl2vl2p
        unique (email)
);

INSERT INTO user (id, email, firstName, lastName, password, username)
VALUES      (100, 'john@email.be', 'John', 'Doe', '$2a$10$kV6M8rvK2pGjPt.JsyaITel.BU2CKXAMWEIf9A/Tve9.UXFmuEeiK', 'john'),
            (101, 'jane@email.be', 'Jane', 'Doe', '$2a$10$9zs3K.pnqWYKNCi1PZcQze8Zldk4SvuA0XYnG8FY5PPuC.ZE/SA3q', 'jane');


