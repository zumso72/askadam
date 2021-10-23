delete from questions;

insert into questions(id, question, answer, is_anonymous, author_id) values
(1, 'first', 'answer', false, 1),
(2, 'second', 'answer', false, 2),
(3, 'third', 'answer', false, 2),
(4, 'fourth', 'answer', false, 2);

alter sequence hibernate_sequence restart with 10;



