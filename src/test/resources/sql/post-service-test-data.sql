insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (1, 'kwon@naver.com', 'kwon', 'Seoul', 'aaaa-bbbb-cccc', 'ACTIVE', 0);

insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (2, 'kwon1@naver.com', 'kwon1', 'Seoul', 'aaaa-bbbb-cccc', 'PENDING', 0);

insert into posts (id, content, created_at, modified_at, user_id)
values (1, 'content', 0, 0, 1);
