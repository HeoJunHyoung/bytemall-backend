-- 기존 데이터 초기화
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE categories;
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 1. FASHION (패션) - ID 대역: 100~
-- ==========================================

-- 1뎁스: 성별 분류
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (101, 'FASHION', '여성', NULL, 1, 1, '101', NOW(), NOW()),
    (102, 'FASHION', '남성', NULL, 1, 2, '102', NOW(), NOW());

-- 2뎁스 (여성 하위: 상의/하의 등)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (111, 'FASHION', '상의', 101, 2, 1, '101/111', NOW(), NOW()),
    (112, 'FASHION', '하의', 101, 2, 2, '101/112', NOW(), NOW()),
    (113, 'FASHION', '원피스', 101, 2, 3, '101/113', NOW(), NOW()),
    (114, 'FASHION', '아우터', 101, 2, 4, '101/114', NOW(), NOW());

-- 3뎁스 (여성 상의 하위 세부항목)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (121, 'FASHION', '티셔츠', 111, 3, 1, '101/111/121', NOW(), NOW()),
    (122, 'FASHION', '블라우스', 111, 3, 2, '101/111/122', NOW(), NOW()),
    (123, 'FASHION', '셔츠', 111, 3, 3, '101/111/123', NOW(), NOW());

-- 3뎁스 (여성 하의 하위 세부항목)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (124, 'FASHION', '청바지', 112, 3, 1, '101/112/124', NOW(), NOW()),
    (125, 'FASHION', '슬랙스', 112, 3, 2, '101/112/125', NOW(), NOW()),
    (126, 'FASHION', '스커트', 112, 3, 3, '101/112/126', NOW(), NOW());

-- 2뎁스 (남성 하위: 상의/하의 등)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (131, 'FASHION', '상의', 102, 2, 1, '102/131', NOW(), NOW()),
    (132, 'FASHION', '하의', 102, 2, 2, '102/132', NOW(), NOW()),
    (133, 'FASHION', '아우터', 102, 2, 3, '102/133', NOW(), NOW());

-- 3뎁스 (남성 상의 하위 세부항목)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (141, 'FASHION', '티셔츠', 131, 3, 1, '102/131/141', NOW(), NOW()),
    (142, 'FASHION', '셔츠/남방', 131, 3, 2, '102/131/142', NOW(), NOW()),
    (143, 'FASHION', '맨투맨/후드', 131, 3, 3, '102/131/143', NOW(), NOW());

-- 3뎁스 (남성 하의 하위 세부항목)
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (144, 'FASHION', '청바지', 132, 3, 1, '102/132/144', NOW(), NOW()),
    (145, 'FASHION', '슬랙스', 132, 3, 2, '102/132/145', NOW(), NOW()),
    (146, 'FASHION', '반바지', 132, 3, 3, '102/132/146', NOW(), NOW());

-- ==========================================
-- 2. BEAUTY (뷰티) - ID 대역: 200~
-- ==========================================

-- 1뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (201, 'BEAUTY', '스킨케어', NULL, 1, 1, '201', NOW(), NOW()),
    (202, 'BEAUTY', '메이크업', NULL, 1, 2, '202', NOW(), NOW()),
    (203, 'BEAUTY', '향수', NULL, 1, 3, '203', NOW(), NOW());

-- 2뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (204, 'BEAUTY', '스킨/토너', 201, 2, 1, '201/204', NOW(), NOW()),
    (205, 'BEAUTY', '로션/에멀젼', 201, 2, 2, '201/205', NOW(), NOW());


-- ==========================================
-- 3. FOOD (식품) - ID 대역: 300~
-- ==========================================

-- 1뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (301, 'FOOD', '신선식품', NULL, 1, 1, '301', NOW(), NOW()),
    (302, 'FOOD', '가공식품', NULL, 1, 2, '302', NOW(), NOW()),
    (303, 'FOOD', '건강식품', NULL, 1, 3, '303', NOW(), NOW());

-- 2뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (304, 'FOOD', '과일', 301, 2, 1, '301/304', NOW(), NOW()),
    (305, 'FOOD', '채소', 301, 2, 2, '301/305', NOW(), NOW()),
    (306, 'FOOD', '쌀/잡곡', 301, 2, 3, '301/306', NOW(), NOW());


-- ==========================================
-- 4. DAILY (생필품) - ID 대역: 400~
-- ==========================================
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (401, 'DAILY', '세탁세제', NULL, 1, 1, '401', NOW(), NOW()),
    (402, 'DAILY', '화장지/물티슈', NULL, 1, 2, '402', NOW(), NOW()),
    (403, 'DAILY', '청소용품', NULL, 1, 3, '403', NOW(), NOW());


-- ==========================================
-- 5. ELECTRONICS (가전/디지털) - ID 대역: 500~
-- ==========================================

-- 1뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (501, 'ELECTRONICS', '컴퓨터/노트북', NULL, 1, 1, '501', NOW(), NOW()),
    (502, 'ELECTRONICS', '모바일/태블릿', NULL, 1, 2, '502', NOW(), NOW()),
    (503, 'ELECTRONICS', '생활가전', NULL, 1, 3, '503', NOW(), NOW());

-- 2뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (504, 'ELECTRONICS', '노트북', 501, 2, 1, '501/504', NOW(), NOW()),
    (505, 'ELECTRONICS', '데스크탑', 501, 2, 2, '501/505', NOW(), NOW()),
    (506, 'ELECTRONICS', '모니터', 501, 2, 3, '501/506', NOW(), NOW());

-- 3뎁스
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (507, 'ELECTRONICS', '게이밍 노트북', 504, 3, 1, '501/504/507', NOW(), NOW()),
    (508, 'ELECTRONICS', '울트라북', 504, 3, 2, '501/504/508', NOW(), NOW());

INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (509, 'ELECTRONICS', '게이밍 PC', 505, 3, 1, '501/505/509', NOW(), NOW()),
    (510, 'ELECTRONICS', '사무용 PC', 505, 3, 2, '501/505/510', NOW(), NOW());

-- ==========================================
-- 6. FURNITURE (가구/인테리어) - ID 대역: 600~
-- ==========================================
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (601, 'FURNITURE', '침실가구', NULL, 1, 1, '601', NOW(), NOW()),
    (602, 'FURNITURE', '거실가구', NULL, 1, 2, '602', NOW(), NOW()),
    (603, 'FURNITURE', '주방가구', NULL, 1, 3, '603', NOW(), NOW());


-- ==========================================
-- 7. BOOK (도서/문구) - ID 대역: 700~
-- ==========================================
INSERT INTO categories
(category_id, root_category, name, parent_id, depth, display_order, path, created_at, last_modified_at)
VALUES
    (701, 'BOOK', '베스트셀러', NULL, 1, 1, '701', NOW(), NOW()),
    (702, 'BOOK', '학습/수험', NULL, 1, 2, '702', NOW(), NOW()),
    (703, 'BOOK', '만화/웹툰', NULL, 1, 3, '703', NOW(), NOW());


-- ==========================================
-- 99. MEMBERS (회원 초기 데이터)
-- ==========================================
-- 관리자 (ID: 1) / 비밀번호: 1234
INSERT IGNORE INTO members (member_id, username, password, role, grade, created_at, last_modified_at)
VALUES (1, 'admin', '$2a$10$5n5tLjiitv4esmk.QVCwjuG/hARJFt2rhd6Q.ur1/QwAW4PmeRqJ2', 'ADMIN', 'DIAMOND', NOW(), NOW());

-- 판매자 (ID: 2) / 비밀번호: 1234
INSERT IGNORE INTO members (member_id, username, password, role, grade, created_at, last_modified_at)
VALUES (2, 'seller', '$2a$10$eASvfc8sqvTPPRfOJiC3XexodIHZOz6V2Q5zLZQ4RkNFYSExqzpIu', 'SELLER', 'IRON', NOW(), NOW());

-- 구매자 (ID: 3) / 비밀번호: 1234
INSERT IGNORE INTO members (member_id, username, password, role, grade, created_at, last_modified_at)
VALUES (3, 'qwe', '$2a$10$eASvfc8sqvTPPRfOJiC3XexodIHZOz6V2Q5zLZQ4RkNFYSExqzpIu', 'CUSTOMER', 'IRON', NOW(), NOW());