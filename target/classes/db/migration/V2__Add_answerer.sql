insert into users (id, email, is_active, first_name, last_name, password, photo, is_new_answers)
    values (1, 'adamibnkhavaj@gmail.com', true, 'Адам', 'Ельмурзаев', '$2a$12$MwWxoyzOS5mgmsR7bANdEe6/wjU4a3O/tI3HTfEcI3B7oI43EZZsm'
            , 'answerer.png', false);

insert into user_role values (1, 'ANSWERER');