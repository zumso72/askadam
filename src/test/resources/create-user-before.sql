delete from user_role;
delete from users;

insert into users (id, email, password, first_name, last_name, is_active, is_new_answers) values
                  (1, 'adamibnkhavaj@gmail.com', '$2a$12$DsKUR6a.34RL3HtIkm0WUudAXPIk2v/bNse/9VT6dxvqpaGCI5B0u',
                  'Адам', 'Ельмурзаев', true, false),
                  (2, 'islamadam@yandex.ru', '$2a$12$DsKUR6a.34RL3HtIkm0WUudAXPIk2v/bNse/9VT6dxvqpaGCI5B0u',
                   'Ислам', 'Ельмурзаев', true, false);

insert into user_role (user_id, roles) values (1, 'ANSWERER'), (2, 'QUESTIONER');