DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_role;
DROP TABLE IF EXISTS member_team;

CREATE TABLE member
(
    id          bigint      NOT NULL AUTO_INCREMENT,
    member_name varchar(70) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE member_role
(
    id        bigint      NOT NULL AUTO_INCREMENT,
    member_id bigint      NOT NULL,
    role_name varchar(70) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member_team
(
    id        bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    team_name varchar(70) NOT NULL,
    PRIMARY KEY (id)
);

SET SESSION cte_max_recursion_depth = 100000;

INSERT INTO member (member_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    CONCAT('User', LPAD(n, 7, '0'))
FROM cte;


INSERT INTO member_role (member_id, role_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_role', LPAD(n, 7, '0'))
FROM cte;

INSERT INTO member_role (member_id, role_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_role', LPAD(n, 7, '0'))
FROM cte;

INSERT INTO member_role (member_id, role_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_role', LPAD(n, 7, '0'))
FROM cte;

INSERT INTO member_team (member_id, team_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_team', LPAD(n, 7, '0'))
FROM cte;

INSERT INTO member_team (member_id, team_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_team', LPAD(n, 7, '0'))
FROM cte;

INSERT INTO member_team (member_id, team_name)
WITH RECURSIVE cte (n) AS
                   (
                       SELECT 1
                       UNION ALL
                       SELECT n + 1 FROM cte WHERE n < 100000
                   )
SELECT
    n,
    CONCAT('User_team', LPAD(n, 7, '0'))
FROM cte;

