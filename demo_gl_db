-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.faculties (
  id character varying NOT NULL DEFAULT ''::text,
  name character varying NOT NULL DEFAULT ''::text UNIQUE,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT faculties_pkey PRIMARY KEY (id)
);
CREATE TABLE public.departments (
  id character varying NOT NULL DEFAULT ''::text,
  faculty_id character varying NOT NULL DEFAULT ''::text,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT departments_pkey PRIMARY KEY (id),
  CONSTRAINT departments_falculty_id_fkey FOREIGN KEY (faculty_id) REFERENCES public.faculties(id)
);
CREATE TABLE public.services (
  id character varying NOT NULL DEFAULT ''::text,
  name character varying NOT NULL DEFAULT ''::text UNIQUE,
  department_id character varying NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  director_id integer,
  CONSTRAINT services_pkey PRIMARY KEY (id),
  CONSTRAINT services_director_id_fkey FOREIGN KEY (director_id) REFERENCES public.members(id),
  CONSTRAINT services_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(id)
);
CREATE TABLE public.members (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL,
  firstname character varying NOT NULL DEFAULT ''::text,
  lastname character varying NOT NULL DEFAULT ''::text,
  start_date timestamp without time zone NOT NULL DEFAULT now(),
  end_date timestamp without time zone,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT members_pkey PRIMARY KEY (id)
);
CREATE TABLE public.members_to_services (
  member_id integer NOT NULL,
  service_id character varying NOT NULL DEFAULT ''::text,
  CONSTRAINT members_to_services_pkey PRIMARY KEY (member_id, service_id),
  CONSTRAINT members_to_services_member_id_fkey FOREIGN KEY (member_id) REFERENCES public.members(id),
  CONSTRAINT members_to_services_service_id_fkey FOREIGN KEY (service_id) REFERENCES public.services(id)
);
CREATE TABLE public.roles (
  id integer GENERATED ALWAYS AS IDENTITY NOT NULL,
  name character varying NOT NULL DEFAULT ''::text UNIQUE,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT roles_pkey PRIMARY KEY (id)
);
CREATE TABLE public.members_to_roles (
  member_id integer NOT NULL,
  role_id integer NOT NULL,
  CONSTRAINT members_to_roles_pkey PRIMARY KEY (member_id, role_id),
  CONSTRAINT members_to_roles_member_id_fkey FOREIGN KEY (member_id) REFERENCES public.members(id),
  CONSTRAINT members_to_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id)
);
