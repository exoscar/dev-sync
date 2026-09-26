CREATE FUNCTION public.issue_search_vector_update() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.search_vector
:=
        setweight(
            to_tsvector('english', COALESCE(NEW.title, '')),
            'A'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.description, '')),
            'B'
        );

RETURN NEW;
END;
$$;



CREATE FUNCTION public.project_search_vector_update() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.search_vector
:=
        setweight(
            to_tsvector('english', COALESCE(NEW.name, '')),
            'A'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.description, '')),
            'B'
        );

RETURN NEW;
END;
$$;



CREATE FUNCTION public.user_search_vector_update() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.search_vector
:=
        setweight(
            to_tsvector('english', COALESCE(NEW.email, '')),
            'A'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.first_name, '')),
            'B'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.last_name, '')),
            'B'
        );

RETURN NEW;
END;
$$;



CREATE TABLE public.attachment
(
    id                 uuid                   NOT NULL,
    created_at         timestamp(6) with time zone,
    updated_at         timestamp(6) with time zone,
    content_type       character varying(255) NOT NULL,
    file_size          bigint                 NOT NULL,
    original_file_name character varying(255) NOT NULL,
    stored_file_name   character varying(255) NOT NULL,
    issue_id           uuid                   NOT NULL,
    uploaded_by_id     uuid                   NOT NULL
);



CREATE TABLE public.comment
(
    id         uuid                    NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    content    character varying(5000) NOT NULL,
    author_id  uuid                    NOT NULL,
    issue_id   uuid                    NOT NULL
);



CREATE TABLE public.issue
(
    id            uuid                   NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    description   character varying(5000),
    priority      character varying(255),
    status        character varying(255),
    title         character varying(255) NOT NULL,
    assignee_id   uuid,
    project_id    uuid                   NOT NULL,
    search_vector tsvector,
    CONSTRAINT issue_priority_check CHECK (((priority)::text = ANY ((ARRAY['LOW':: character varying, 'MEDIUM':: character varying, 'HIGH':: character varying, 'CRITICAL':: character varying])::text[])
) ),
    CONSTRAINT issue_status_check CHECK (((status)::text = ANY ((ARRAY['TODO'::character varying, 'IN_PROGRESS'::character varying, 'DONE'::character varying])::text[])))
);


CREATE TABLE public.issue_activity
(
    id            uuid                    NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    activity_type character varying(255),
    description   character varying(5000) NOT NULL,
    issue_id      uuid                    NOT NULL,
    user_id       uuid                    NOT NULL,
    CONSTRAINT issue_activity_activity_type_check CHECK (((activity_type)::text = ANY ((ARRAY['ISSUE_CREATED':: character varying, 'ISSUE_ASSIGNED':: character varying, 'ISSUE_STATUS_CHANGED':: character varying, 'ISSUE_PRIORITY_CHANGED':: character varying, 'ISSUE_UPDATED':: character varying, 'ISSUE_DELETED':: character varying, 'COMMENT_ADDED':: character varying, 'COMMENT_UPDATED':: character varying, 'COMMENT_DELETED':: character varying])::text[])
) )
);



CREATE TABLE public.issue_labels
(
    id            uuid NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    created_by_id uuid,
    issue_id      uuid,
    label_id      uuid
);



CREATE TABLE public.issue_watchers
(
    id         uuid NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    source     character varying(255),
    issue_id   uuid,
    user_id    uuid,
    CONSTRAINT issue_watchers_source_check CHECK (((source)::text = ANY ((ARRAY['MANUAL':: character varying, 'CREATOR':: character varying, 'ASSIGNEE':: character varying])::text[])
) )
);



CREATE TABLE public.labels
(
    id              uuid                   NOT NULL,
    created_at      timestamp(6) with time zone,
    updated_at      timestamp(6) with time zone,
    color           character varying(20)  NOT NULL,
    description     character varying(500),
    name            character varying(50)  NOT NULL,
    normalized_name character varying(50)  NOT NULL,
    status          character varying(255) NOT NULL,
    workspace_id    uuid,
    CONSTRAINT labels_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE':: character varying, 'ARCHIVED':: character varying])::text[])
) )
);



CREATE TABLE public.notification
(
    id            uuid                   NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    is_read       boolean                NOT NULL,
    message       character varying(500) NOT NULL,
    project_id    uuid                   NOT NULL,
    read_at       timestamp(6) with time zone,
    resource_id   uuid                   NOT NULL,
    resource_type character varying(255) NOT NULL,
    title         character varying(100) NOT NULL,
    type          character varying(255) NOT NULL,
    workspace_id  uuid                   NOT NULL,
    recipient_id  uuid                   NOT NULL,
    CONSTRAINT notification_resource_type_check CHECK (((resource_type)::text = ANY ((ARRAY['ISSUE':: character varying, 'COMMENT':: character varying, 'LABEL':: character varying])::text[])
) ),
    CONSTRAINT notification_type_check CHECK (((type)::text = ANY ((ARRAY['ISSUE_ASSIGNED'::character varying, 'ISSUE_STATUS_CHANGED'::character varying, 'ISSUE_PRIORITY_CHANGED'::character varying, 'ISSUE_COMMENTED'::character varying, 'LABEL_ADDED'::character varying, 'LABEL_REMOVED'::character varying])::text[])))
);


CREATE TABLE public.project
(
    id            uuid                   NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    description   character varying(1000),
    name          character varying(255) NOT NULL,
    workspace_id  uuid                   NOT NULL,
    search_vector tsvector
);



CREATE TABLE public.role
(
    id   integer                NOT NULL,
    role character varying(255) NOT NULL
);


CREATE SEQUENCE public.role_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE CACHE 1;



CREATE TABLE public.users
(
    id            uuid                   NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    email         character varying(255) NOT NULL,
    first_name    character varying(255) NOT NULL,
    last_name     character varying(255) NOT NULL,
    password      character varying(255) NOT NULL,
    role_id       integer,
    search_vector tsvector
);



CREATE TABLE public.workspace
(
    id         uuid                   NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    name       character varying(255) NOT NULL,
    owner_id   uuid
);



CREATE TABLE public.workspace_member
(
    id           uuid NOT NULL,
    created_at   timestamp(6) with time zone,
    updated_at   timestamp(6) with time zone,
    role         character varying(255),
    user_id      uuid,
    workspace_id uuid,
    CONSTRAINT workspace_member_role_check CHECK (((role)::text = ANY ((ARRAY['OWNER':: character varying, 'MAINTAINER':: character varying, 'MEMBER':: character varying, 'VIEWER':: character varying])::text[])
) )
);



ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT attachment_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.issue_activity
    ADD CONSTRAINT issue_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.issue_labels
    ADD CONSTRAINT issue_labels_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.issue
    ADD CONSTRAINT issue_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.issue_watchers
    ADD CONSTRAINT issue_watchers_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.labels
    ADD CONSTRAINT labels_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);



ALTER TABLE ONLY public.issue_labels
    ADD CONSTRAINT uk_issue_label UNIQUE (issue_id, label_id);



ALTER TABLE ONLY public.issue_watchers
    ADD CONSTRAINT uk_issue_watcher UNIQUE (user_id, issue_id);



ALTER TABLE ONLY public.labels
    ADD CONSTRAINT uk_label_workspace_name UNIQUE (workspace_id, normalized_name);



ALTER TABLE ONLY public.role
    ADD CONSTRAINT ukbjxn5ii7v7ygwx39et0wawu0q UNIQUE (role);



ALTER TABLE ONLY public.workspace_member
    ADD CONSTRAINT ukehbe3e8pf5wxry5hs5aa5hona UNIQUE (workspace_id, user_id);



ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT uknvqpae1kkfe8j1xfokdwunw2r UNIQUE (stored_file_name);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.workspace_member
    ADD CONSTRAINT workspace_member_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.workspace
    ADD CONSTRAINT workspace_pkey PRIMARY KEY (id);



CREATE INDEX idx_attachment_issue ON public.attachment USING btree (issue_id);



CREATE INDEX idx_issue_search ON public.issue USING gin (search_vector);



CREATE INDEX idx_notification_recipient ON public.notification USING btree (recipient_id);



CREATE INDEX idx_notification_recipient_created ON public.notification USING btree (recipient_id, created_at);



CREATE INDEX idx_notification_recipient_read ON public.notification USING btree (recipient_id, is_read);



CREATE INDEX idx_project_search_vector ON public.project USING gin (search_vector);



CREATE INDEX idx_user_search_vector ON public.users USING gin (search_vector);



CREATE TRIGGER issue_search_vector_trigger
    BEFORE INSERT OR UPDATE ON public.issue FOR EACH ROW
EXECUTE FUNCTION public.issue_search_vector_update();



CREATE TRIGGER project_search_vector_trigger
    BEFORE INSERT OR UPDATE ON public.project FOR EACH ROW
EXECUTE FUNCTION public.project_search_vector_update();



CREATE TRIGGER user_search_vector_trigger
    BEFORE INSERT OR UPDATE ON public.users FOR EACH ROW
EXECUTE FUNCTION public.user_search_vector_update();



ALTER TABLE ONLY public.issue_activity
    ADD CONSTRAINT fk194uo9jxlf9q7u234dwkbsims FOREIGN KEY (user_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.issue_watchers
    ADD CONSTRAINT fk3jk4jjb1eb1lhgwrr5tclc2bn FOREIGN KEY (issue_id) REFERENCES public.issue(id);



ALTER TABLE ONLY public.project
    ADD CONSTRAINT fk3rjghkcnkltjgvaegojpkdolb FOREIGN KEY (workspace_id) REFERENCES public.workspace(id);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4qu1gr772nnf6ve5af002rwya FOREIGN KEY (role_id) REFERENCES public.role(id);



ALTER TABLE ONLY public.labels
    ADD CONSTRAINT fk4v29ku9kvufji3s44m3c9vxpj FOREIGN KEY (workspace_id) REFERENCES public.workspace(id);



ALTER TABLE ONLY public.issue_labels
    ADD CONSTRAINT fkadujrbsnyqqwx2seku9xq1h9n FOREIGN KEY (issue_id) REFERENCES public.issue(id);



ALTER TABLE ONLY public.issue
    ADD CONSTRAINT fkcombytcpeogaqi2012phvvvhy FOREIGN KEY (project_id) REFERENCES public.project(id);



ALTER TABLE ONLY public.workspace
    ADD CONSTRAINT fkcpfj2f5eladpgqpi8ards9j55 FOREIGN KEY (owner_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT fkf85mvmxbijjw30ipfdo66w5tq FOREIGN KEY (issue_id) REFERENCES public.issue(id);



ALTER TABLE ONLY public.notification
    ADD CONSTRAINT fkfcyn9rsga73dqnorl7owfyl4a FOREIGN KEY (recipient_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.attachment
    ADD CONSTRAINT fkgxo6vne5yymnl72hcp765kses FOREIGN KEY (uploaded_by_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.issue_labels
    ADD CONSTRAINT fkh6r80xjfqs7s4rodip4sfbgvv FOREIGN KEY (created_by_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.issue
    ADD CONSTRAINT fkif8mre3uuo6qs1s9qa2wbvphm FOREIGN KEY (assignee_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fkir20vhrx08eh4itgpbfxip0s1 FOREIGN KEY (author_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.issue_labels
    ADD CONSTRAINT fkln0c1ljskwin7doctl515dpas FOREIGN KEY (label_id) REFERENCES public.labels(id);



ALTER TABLE ONLY public.issue_watchers
    ADD CONSTRAINT fkni6muwldpwc36edsyi89j6trm FOREIGN KEY (user_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fkomjg70m9sundkar1el2rtonrn FOREIGN KEY (issue_id) REFERENCES public.issue(id);



ALTER TABLE ONLY public.issue_activity
    ADD CONSTRAINT fkprdw1i4vr1sm6d98lcfsa2e3q FOREIGN KEY (issue_id) REFERENCES public.issue(id);



ALTER TABLE ONLY public.workspace_member
    ADD CONSTRAINT fkqt5hp8najyhtbees6p095nn16 FOREIGN KEY (workspace_id) REFERENCES public.workspace(id);



ALTER TABLE ONLY public.workspace_member
    ADD CONSTRAINT fksabkqitmdtnr620gl9sf45ude FOREIGN KEY (user_id) REFERENCES public.users(id);

INSERT INTO public.role (id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');