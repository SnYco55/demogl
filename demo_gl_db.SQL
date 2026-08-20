CREATE TABLE departments (
    id varchar(255) DEFAULT ''::text NOT NULL,
    faculty_id varchar(255) DEFAULT ''::text NOT NULL,
    CONSTRAINT departments_pkey PRIMARY KEY (id)
);
CREATE TABLE faculties (
    id varchar(255) DEFAULT ''::text NOT NULL,
    name varchar(255) DEFAULT ''::text NOT NULL UNIQUE,
    CONSTRAINT faculties_pkey PRIMARY KEY (id)
);
CREATE TABLE members (
    id SERIAL NOT NULL,
    firstname varchar(255) DEFAULT ''::text NOT NULL,
    lastname varchar(255) DEFAULT ''::text NOT NULL,
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ,
    CONSTRAINT members_pkey PRIMARY KEY (id)
);
CREATE TABLE members_to_roles (
    member_id int4 NOT NULL,
    role_id int4 NOT NULL,
    CONSTRAINT members_to_roles_pkey PRIMARY KEY (member_id, role_id)
);
CREATE TABLE members_to_services (
    member_id int4 NOT NULL,
    service_id varchar(255) DEFAULT ''::text NOT NULL,
    CONSTRAINT members_to_services_pkey PRIMARY KEY (member_id, service_id)
);
CREATE TABLE roles (
    id SERIAL NOT NULL,
    name varchar(255) DEFAULT ''::text NOT NULL UNIQUE,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);
CREATE TABLE services (
    id varchar(255) DEFAULT ''::text NOT NULL,
    name varchar(255) DEFAULT ''::text NOT NULL UNIQUE,
    department_id varchar(255) NOT NULL,
    director_id int4 NOT NULL,
    CONSTRAINT services_pkey PRIMARY KEY (id)
);
ALTER TABLE members_to_services ADD CONSTRAINT FKmembers_to807670 FOREIGN KEY (member_id) REFERENCES members (id);
ALTER TABLE members_to_services ADD CONSTRAINT FKmembers_to120315 FOREIGN KEY (service_id) REFERENCES services (id);
ALTER TABLE services ADD CONSTRAINT FKservices465428 FOREIGN KEY (department_id) REFERENCES departments (id);
ALTER TABLE services ADD CONSTRAINT FKservices877245 FOREIGN KEY (director_id) REFERENCES members (id);
ALTER TABLE departments ADD CONSTRAINT FKdepartment612153 FOREIGN KEY (faculty_id) REFERENCES faculties (id);
ALTER TABLE members_to_roles ADD CONSTRAINT FKmembers_to750705 FOREIGN KEY (member_id) REFERENCES members (id);
ALTER TABLE members_to_roles ADD CONSTRAINT FKmembers_to227028 FOREIGN KEY (role_id) REFERENCES roles (id);
