create table if not exists accounts (
    id bigserial primary key,
    user_id varchar(255) not null unique,
    name varchar(255) not null,
    birthdate date,
    balance DECIMAL(19,2) not null default 0.00
);