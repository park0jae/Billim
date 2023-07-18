-- ROLE
INSERT INTO ROLE (role_id, role_type)
values (1, 'ADMIN'),
       (2, 'MANAGER'),
       (3, 'USER');

-- MEMBER // password : 1q2w3e4r!!
INSERT INTO MEMBER
(member_id, username, password, city, district, street, zip_code, introduce, phone_number, profile_image_id, provider, rating)
values
    (1, 'admin123', '$2a$10$LGyuYTpUWfDwlBK.rSak3.U.0TuIFl8r5dU8H9w1EOAI0rn0fnqf2', '서울시', '관악구', '신림동', '56-1', '안녕하세요 ADMIN입니다.', '011-3124-3678', null, null, 0),
    (2, 'manager123', '$2a$10$LGyuYTpUWfDwlBK.rSak3.U.0TuIFl8r5dU8H9w1EOAI0rn0fnqf2', '서울시', '강북구', '미아동', '56-3', '안녕하세요 MANAGER입니다.', '011-1311-5478', null, null, 0),
    (3, 'user123', '$2a$10$LGyuYTpUWfDwlBK.rSak3.U.0TuIFl8r5dU8H9w1EOAI0rn0fnqf2', '서울시', '광진구', '화양동', '620-1', '안녕하세요 USER입니다. 잘부탁드립니다.', '011-3134-7623', null, null, 0),
    (4, 'kdo6301', '$2a$10$LGyuYTpUWfDwlBK.rSak3.U.0TuIFl8r5dU8H9w1EOAI0rn0fnqf2', '전주시', '덕진구', '금암동', '709-4', '안녕하세요 김동웅입니다. 잘부탁드립니다.', '012-6606-3323', null, null, 0),
    (5, 'okvv26', '$2a$10$LGyuYTpUWfDwlBK.rSak3.U.0TuIFl8r5dU8H9w1EOAI0rn0fnqf2', '전주시', '덕진구', '금암동', '709-3', '안녕하세요 박영재입니다. 잘부탁드립니다.', '011-3134-7623', null, null, 0);

-- MEMBER_ROLE
INSERT INTO MEMBER_ROLE (member_id, role_id)
values
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 2),
    (2, 3),
    (3, 3),
    (4, 3),
    (5, 3);

-- CATEGORY
INSERT INTO CATEGORY (category_id, name, parent_id)
values
    (1, '공지사항', null),
    (2, '대여거래 카테고리', null),
    (3, '가전제품', 2),
    (4, '테블릿', 3),
    (5, '웨어러블', 3),
    (6, '오디오/영상관련 기기' , 3),
    (7, '기타', 3),
    (8, '생활용품', 2),
    (9, '주방용품', 8),
    (10, '욕실용품', 8),
    (11, '기타', 8),
    (12, '완구', 2),
    (13, '보드게임', 12),
    (14, '퍼즐', 12),
    (15, '레고', 12),
    (16, '기타', 12),
    (17, '운동기구', 2),
    (18, '낚시', 17),
    (19, '캠핑', 17),
    (20, '기타', 17),
    (21, '차량, 오토바이', 2),
    (22, '악기', 2),
    (23, '현악기', 22),
    (24, '관악기', 22),
    (25, '타악기', 22),
    (26, '국악기', 22),
    (27, '혼성악기', 22),
    (28, '책', 2),
    (29, '소설', 28),
    (30, '자기개발서', 28),
    (31, '역사/인문학', 28),
    (32, '과학', 28),
    (33, '시/에세이', 28),
    (34, '공구', 2),
    (35, '의류', 2),
    (36, '여성의류', 35),
    (37, '남성의류', 35),
    (38, '가방', 35),
    (39, '커뮤니티', null),
    (40, '자유게시판', 39),
    (41, '신고게시판', 39);

INSERT INTO POST (post_id, created_time, last_modified, category_id, content, name, price, quantity, likes, member_id, title)
values
    (1, now() , now() , 1, '공지사항 내용입니다.', null ,null, null, 0, 1, '필독 공지사항'),
    (2, now() , now() , 4, '아이패드5 PRO 대여해드립니다.', '아이패드 프로5' , 50000 , 1, 0, 3, '아이패드5 PRO 대여해드립니다.'),
    (3, now() , now() , 13, '할리갈리, 마피아, 뱅 등 다수 보드게임 대여해드립니다. 자세한 사항은 쪽지나 채팅주세요', '보드 게임' , 5000 , 1, 0, 3, '보드게임 대여해드립니다. 종류 많음');

INSERT INTO COMMENT (comment_id, created_time, last_modified, content, deleted, member_id, parent_id, post_id, root)
values
    (1, now(), now(), '혹시 다빈치코드 있나요?', false, 4, null, 3, true),
    (2, now(), now(), '네 있습니다.', false, 3, 1, 3, false),
    (3, now(), now(), '대여하고 싶습니다..', false, 5, null, 3, true),
    (4, now(), now(), '쪽지 주세요!', false, 3, 3, 3, false);


INSERT INTO MESSAGE (message_id, content, delete_by_receiver, delete_by_sender, receiver_id, sender_id)
values
    (1, 'okvv26입니다. 대여하고 싶어서 쪽지 드렸습니다.', false, false, 3, 5),
    (2, 'kdo6301입니다. 다빈치 코드 8월1일부터 일주일간 대여하고 싶습니다. 혹시 게시글에 쓰여진 가격이 하루당 가격인가요?', false, false, 3, 4),
    (3, '네. 근데 일주일간 빌린다고 하시면 어느정도 깎아드릴게요!!', false, false, 4, 3);






