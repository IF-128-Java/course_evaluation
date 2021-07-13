CREATE TABLE IF NOT EXISTS role
(
    id   bigserial NOT NULL,
    role_name varchar(50),
    CONSTRAINT role_pkey PRIMARY KEY(id)
);


CREATE TABLE IF NOT EXISTS users
(
    id  bigserial NOT NULL,
    first_name varchar(25) NOT NULL,
    last_name varchar(25) NOT NULL,
    email varchar(25) NOT NULL,
    password varchar(30) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS groups
(
    id   bigserial NOT NULL,
    group_name varchar(100) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS question
(
    id bigserial NOT NULL,
    question_text varchar(255),
    is_pattern boolean,
    feedback_request_id int NOT NULL,
    CONSTRAINT question_pkey PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id bigint  NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_roles_roles_fkey FOREIGN KEY(role_id) REFERENCES role(id),
    CONSTRAINT user_roles_user_fkey FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT user_roles_pkey PRIMARY KEY(user_id, role_id)
);



CREATE TABLE IF NOT EXISTS user_group
(
    user_id bigint  NOT NULL UNIQUE,
    group_id integer NOT NULL,
    CONSTRAINT user_group_groups_fkey FOREIGN KEY(group_id) REFERENCES groups(id),
    CONSTRAINT user_group_user_fkey FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT user_group_pkey PRIMARY KEY(user_id, group_id)
);

CREATE TABLE IF NOT EXISTS course
(
    id  bigserial NOT NULL,
    course_name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    teacher_id bigint NOT NULL,
    CONSTRAINT course_pkey PRIMARY KEY(id),
    CONSTRAINT course_user_fkey FOREIGN KEY(teacher_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS course_group
(
    group_user_id bigint  NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT course_group_course_fkey FOREIGN KEY(course_id) REFERENCES course(id),
    CONSTRAINT course_group_group_fkey FOREIGN KEY(group_user_id) REFERENCES groups(id),
    CONSTRAINT course_group_pkey PRIMARY KEY(group_user_id, course_id)
);



CREATE TABLE IF NOT EXISTS course_feedback_request
(
    id  bigserial NOT NULL,
    feedback_description varchar (255) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    duration integer NOT NULL,
    course_id bigint NOT NULL,
    CONSTRAINT course_feedback_request_pkey PRIMARY KEY(id),
    CONSTRAINT course_feedback_request_courses_fkey FOREIGN KEY(course_id) REFERENCES course(id)
);

CREATE TABLE IF NOT EXISTS course_feedback
(
    id  bigserial NOT NULL,
    comment varchar(255),
    timestamp date NOT NULL,
    student_id bigint NOT NULL,
    course_feedback_request_id bigint NOT NULL,
    CONSTRAINT course_feedback_pkey PRIMARY KEY(id),
    CONSTRAINT course_feedback_student_fkey FOREIGN KEY(student_id) REFERENCES users(id),
    CONSTRAINT course_feedback_course_feedback_requests_fkey FOREIGN KEY(course_feedback_request_id) REFERENCES course_feedback_request(id)
);

CREATE TABLE IF NOT EXISTS answer_to_feedbacks
(
    id  bigserial NOT NULL,
    rate integer NOT NULL,
    question_id bigint NOT NULL,
    feedback_id bigint NOT NULL,
    CONSTRAINT answer_feedback_pkey PRIMARY KEY(id),
    CONSTRAINT answer_feedback_questions_fkey FOREIGN KEY(question_id) REFERENCES question(id),
    CONSTRAINT answer_feedback_courses_fkey FOREIGN KEY(feedback_id) REFERENCES course_feedback(id)
);

CREATE TABLE IF NOT EXISTS course_feedback_request_question
(
    question_id bigint  NOT NULL,
    feedback_request_id integer NOT NULL,
    CONSTRAINT course_feedback_request_question_request_fkey FOREIGN KEY(feedback_request_id) REFERENCES course_feedback_request(id),
    CONSTRAINT course_feedback_request_question_question_fkey FOREIGN KEY(question_id) REFERENCES question(id),
    CONSTRAINT course_feedback_request_questions_pkey PRIMARY KEY(question_id, feedback_request_id)
);