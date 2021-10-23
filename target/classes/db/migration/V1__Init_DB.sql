create sequence hibernate_sequence start 2 increment 1;
create table questions (
    id int8 not null,
    is_anonymous boolean,
    answer text,
    answer_time timestamp,
    question text,
    time timestamp,
    topic varchar(255),
    author_id int8,
    primary key (id)
);
create table user_role (
    user_id int8 not null,
    roles varchar(255)
);
create table users (
    id int8 not null,
    activation_code varchar(255),
    email varchar(80),
    first_name varchar(50),
    is_active boolean,
    is_new_answers boolean,
    last_name varchar(50),
    password varchar(255),
    photo varchar(100),
    primary key (id)
);

alter table if exists questions
    add constraint question_user_fk
    foreign key (author_id) references users;

alter table if exists user_role
    add constraint user_user_role_fk
    foreign key (user_id) references users;