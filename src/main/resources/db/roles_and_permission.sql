INSERT INTO role (id, role_name)
VALUES (0, 'ROLE_STUDENT');
INSERT INTO role (id, role_name)
VALUES (1, 'ROLE_TEACHER');
INSERT INTO role (id, role_name)
VALUES (2, 'ROLE_ADMIN');
INSERT INTO permission(id, permission_name, role_id)
VALUES (0, 'READ', 0);
INSERT INTO permission(id, permission_name, role_id)
VALUES (1, 'WRITE', 1);
INSERT INTO permission(id, permission_name, role_id)
VALUES (2, 'UPDATE', 2);
