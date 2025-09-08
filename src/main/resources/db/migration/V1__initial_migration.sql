CREATE TABLE public.profiles (
	id uuid NOT NULL,
	clerk_id varchar NOT NULL,
	"type" varchar NOT NULL,
	"role" varchar NULL,
	username varchar NOT NULL,
	email varchar NOT NULL,
	"name" varchar DEFAULT '' NOT NULL,
	avatar varchar NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NULL,
	deleted_at timestamp NULL,
	banned_at timestamp NULL,
	banned_until timestamp NULL,
	banned_reason varchar NULL,
	CONSTRAINT profiles_pk PRIMARY KEY (id),
	CONSTRAINT profiles_unique UNIQUE (clerk_id),
	CONSTRAINT profiles_unique_1 UNIQUE (username),
	CONSTRAINT profiles_unique_2 UNIQUE (email)
);
CREATE INDEX profiles_type_idx ON public.profiles ("type");
CREATE INDEX profiles_role_idx ON public.profiles ("role");
CREATE INDEX profiles_username_idx ON public.profiles (username);
CREATE INDEX profiles_email_idx ON public.profiles (email);
CREATE INDEX profiles_name_idx ON public.profiles ("name");
CREATE INDEX profiles_created_at_idx ON public.profiles (created_at);

CREATE TABLE public.posts (
	id uuid NOT NULL,
	profile_id uuid NOT NULL,
	"text" varchar NOT NULL,
	likes_count bigint DEFAULT 0 NOT NULL,
	comments_count bigint DEFAULT 0 NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NULL,
	deleted_at timestamp NULL,
	CONSTRAINT post_pk PRIMARY KEY (id),
	CONSTRAINT post_profiles_fk FOREIGN KEY (profile_id) REFERENCES public.profiles(id)
);
CREATE INDEX posts_profile_id_idx ON public.posts (profile_id);
CREATE INDEX posts_text_idx ON public.posts ("text");
CREATE INDEX posts_likes_count_idx ON public.posts (likes_count);
CREATE INDEX posts_comments_count_idx ON public.posts (comments_count);
CREATE INDEX posts_created_at_idx ON public.posts (created_at);

CREATE TABLE public."comments" (
	id uuid NOT NULL,
	profile_id uuid NOT NULL,
	post_id uuid NOT NULL,
	"text" varchar NOT NULL,
	likes_count bigint DEFAULT 0 NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NULL,
	deleted_at timestamp NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_profiles_fk FOREIGN KEY (profile_id) REFERENCES public.profiles(id),
	CONSTRAINT comments_posts_fk FOREIGN KEY (post_id) REFERENCES public.posts(id)
);
CREATE INDEX comments_profile_id_idx ON public."comments" (profile_id);
CREATE INDEX comments_post_id_idx ON public."comments" (post_id);
CREATE INDEX comments_text_idx ON public."comments" ("text");
CREATE INDEX comments_likes_count_idx ON public."comments" (likes_count);
CREATE INDEX comments_created_at_idx ON public."comments" (created_at);

CREATE TABLE public.post_likes (
	post_id uuid NOT NULL,
	profile_id uuid NOT NULL,
	liked_at timestamp NOT NULL,
	CONSTRAINT post_likes_pk PRIMARY KEY (post_id,profile_id)
);

CREATE TABLE public.comment_likes (
	comment_id uuid NOT NULL,
	profile_id uuid NOT NULL,
	liked_at timestamp NOT NULL,
	CONSTRAINT comment_likes_pk PRIMARY KEY (comment_id,profile_id)
);
