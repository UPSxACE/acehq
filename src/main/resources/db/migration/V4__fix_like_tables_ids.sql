ALTER TABLE public.post_likes DROP CONSTRAINT post_likes_pk;
ALTER TABLE public.post_likes ADD id uuid NOT NULL;
ALTER TABLE public.post_likes ADD CONSTRAINT post_likes_pk PRIMARY KEY (id);
ALTER TABLE public.post_likes ADD CONSTRAINT post_likes_unique UNIQUE (post_id,profile_id);

ALTER TABLE public.comment_likes DROP CONSTRAINT comment_likes_pk;
ALTER TABLE public.comment_likes ADD id uuid NOT NULL;
ALTER TABLE public.comment_likes ADD CONSTRAINT comment_likes_pk PRIMARY KEY (id);
ALTER TABLE public.comment_likes ADD CONSTRAINT comment_likes_unique UNIQUE (comment_id,profile_id);