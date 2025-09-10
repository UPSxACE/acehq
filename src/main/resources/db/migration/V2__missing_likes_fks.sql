ALTER TABLE public.post_likes ADD CONSTRAINT post_likes_posts_fk FOREIGN KEY (post_id) REFERENCES public.posts(id);
ALTER TABLE public.post_likes ADD CONSTRAINT post_likes_profiles_fk FOREIGN KEY (profile_id) REFERENCES public.profiles(id);

ALTER TABLE public.comment_likes ADD CONSTRAINT comment_likes_comments_fk FOREIGN KEY (comment_id) REFERENCES public."comments"(id);
ALTER TABLE public.comment_likes ADD CONSTRAINT comment_likes_profiles_fk FOREIGN KEY (profile_id) REFERENCES public.profiles(id);
