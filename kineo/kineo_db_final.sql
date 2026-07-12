--
-- PostgreSQL database dump
--

\restrict PdskAaAgUZoJlbIyd5DF9qHLYXzHlwlEXBlpXJhftcNcQLbttSag9yR8arqcOXW

-- Dumped from database version 17.9 (Debian 17.9-0+deb13u1)
-- Dumped by pg_dump version 17.9 (Debian 17.9-0+deb13u1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.user_badges DROP CONSTRAINT IF EXISTS user_badges_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.user_badges DROP CONSTRAINT IF EXISTS user_badges_badge_id_fkey;
ALTER TABLE IF EXISTS public.step_sessions DROP CONSTRAINT IF EXISTS step_sessions_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.routes DROP CONSTRAINT IF EXISTS routes_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.leaderboard_cache DROP CONSTRAINT IF EXISTS leaderboard_cache_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.friendships DROP CONSTRAINT IF EXISTS friendships_requester_id_fkey;
ALTER TABLE IF EXISTS ONLY public.friendships DROP CONSTRAINT IF EXISTS friendships_addressee_id_fkey;
ALTER TABLE IF EXISTS ONLY public.daily_goals DROP CONSTRAINT IF EXISTS daily_goals_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.challenges DROP CONSTRAINT IF EXISTS challenges_creator_id_fkey;
ALTER TABLE IF EXISTS ONLY public.challenge_participants DROP CONSTRAINT IF EXISTS challenge_participants_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.challenge_participants DROP CONSTRAINT IF EXISTS challenge_participants_challenge_id_fkey;
DROP TRIGGER IF EXISTS update_users_updated_at ON public.users;
DROP TRIGGER IF EXISTS update_step_sessions_updated_at ON public.step_sessions;
DROP TRIGGER IF EXISTS update_leaderboard_cache_updated_at ON public.leaderboard_cache;
DROP TRIGGER IF EXISTS update_friendships_updated_at ON public.friendships;
DROP TRIGGER IF EXISTS update_challenges_updated_at ON public.challenges;
DROP INDEX IF EXISTS public.idx_user_badges_user;
DROP INDEX IF EXISTS public.idx_step_sessions_user_range;
DROP INDEX IF EXISTS public.idx_step_sessions_user_date;
DROP INDEX IF EXISTS public.idx_route_points_route_time;
DROP INDEX IF EXISTS public.idx_leaderboard_period;
DROP INDEX IF EXISTS public.idx_friendships_requester;
DROP INDEX IF EXISTS public.idx_friendships_addressee;
DROP INDEX IF EXISTS public.idx_challenges_creator;
DROP INDEX IF EXISTS public.idx_challenges_active;
DROP INDEX IF EXISTS public.friendships_user_pair_unique;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_username_key;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_email_key;
ALTER TABLE IF EXISTS ONLY public.user_badges DROP CONSTRAINT IF EXISTS user_badges_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202612 DROP CONSTRAINT IF EXISTS step_sessions_202612_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202611 DROP CONSTRAINT IF EXISTS step_sessions_202611_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202610 DROP CONSTRAINT IF EXISTS step_sessions_202610_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202609 DROP CONSTRAINT IF EXISTS step_sessions_202609_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202608 DROP CONSTRAINT IF EXISTS step_sessions_202608_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202607 DROP CONSTRAINT IF EXISTS step_sessions_202607_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202606 DROP CONSTRAINT IF EXISTS step_sessions_202606_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202605 DROP CONSTRAINT IF EXISTS step_sessions_202605_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202604 DROP CONSTRAINT IF EXISTS step_sessions_202604_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202603 DROP CONSTRAINT IF EXISTS step_sessions_202603_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202602 DROP CONSTRAINT IF EXISTS step_sessions_202602_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions_202601 DROP CONSTRAINT IF EXISTS step_sessions_202601_pkey;
ALTER TABLE IF EXISTS ONLY public.step_sessions DROP CONSTRAINT IF EXISTS step_sessions_pkey;
ALTER TABLE IF EXISTS ONLY public.routes DROP CONSTRAINT IF EXISTS routes_pkey;
ALTER TABLE IF EXISTS ONLY public.route_points DROP CONSTRAINT IF EXISTS route_points_pkey;
ALTER TABLE IF EXISTS ONLY public.leaderboard_cache DROP CONSTRAINT IF EXISTS leaderboard_cache_user_id_period_key;
ALTER TABLE IF EXISTS ONLY public.friendships DROP CONSTRAINT IF EXISTS friendships_requester_addressee_unique;
ALTER TABLE IF EXISTS ONLY public.friendships DROP CONSTRAINT IF EXISTS friendships_pkey;
ALTER TABLE IF EXISTS ONLY public.daily_goals DROP CONSTRAINT IF EXISTS daily_goals_pkey;
ALTER TABLE IF EXISTS ONLY public.challenges DROP CONSTRAINT IF EXISTS challenges_pkey;
ALTER TABLE IF EXISTS ONLY public.challenge_participants DROP CONSTRAINT IF EXISTS challenge_participants_pkey;
ALTER TABLE IF EXISTS ONLY public.badges DROP CONSTRAINT IF EXISTS badges_pkey;
ALTER TABLE IF EXISTS public.route_points ALTER COLUMN id DROP DEFAULT;
DROP VIEW IF EXISTS public.weekly_stats;
DROP TABLE IF EXISTS public.user_badges;
DROP TABLE IF EXISTS public.step_sessions_202612;
DROP TABLE IF EXISTS public.step_sessions_202611;
DROP TABLE IF EXISTS public.step_sessions_202610;
DROP TABLE IF EXISTS public.step_sessions_202609;
DROP TABLE IF EXISTS public.step_sessions_202608;
DROP TABLE IF EXISTS public.step_sessions_202607;
DROP TABLE IF EXISTS public.step_sessions_202606;
DROP TABLE IF EXISTS public.step_sessions_202605;
DROP TABLE IF EXISTS public.step_sessions_202604;
DROP TABLE IF EXISTS public.step_sessions_202603;
DROP TABLE IF EXISTS public.step_sessions_202602;
DROP TABLE IF EXISTS public.step_sessions_202601;
DROP TABLE IF EXISTS public.routes;
DROP SEQUENCE IF EXISTS public.route_points_id_seq;
DROP TABLE IF EXISTS public.route_points;
DROP TABLE IF EXISTS public.leaderboard_cache;
DROP VIEW IF EXISTS public.last_30d_stats;
DROP VIEW IF EXISTS public.friends_weekly_leaderboard;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.step_sessions;
DROP TABLE IF EXISTS public.friendships;
DROP TABLE IF EXISTS public.daily_goals;
DROP TABLE IF EXISTS public.challenges;
DROP TABLE IF EXISTS public.challenge_participants;
DROP TABLE IF EXISTS public.badges;
DROP FUNCTION IF EXISTS public.update_updated_at_column();
DROP EXTENSION IF EXISTS pgcrypto;
--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: public; Owner: pi
--

CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_updated_at_column() OWNER TO pi;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: badges; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.badges (
    id character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    description text NOT NULL,
    icon_url character varying(100) NOT NULL,
    condition_steps integer,
    condition_distance numeric(10,2) DEFAULT NULL::numeric,
    condition_streak_days integer,
    created_at timestamp with time zone DEFAULT now(),
    criteria text,
    is_unlocked boolean DEFAULT false NOT NULL,
    unlocked_at timestamp with time zone
);


ALTER TABLE public.badges OWNER TO pi;

--
-- Name: challenge_participants; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.challenge_participants (
    challenge_id uuid NOT NULL,
    user_id uuid NOT NULL,
    joined_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.challenge_participants OWNER TO pi;

--
-- Name: challenges; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.challenges (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    creator_id uuid NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    target_steps integer NOT NULL,
    starts_at timestamp with time zone NOT NULL,
    ends_at timestamp with time zone NOT NULL,
    status character varying(20) DEFAULT 'active'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    challenger_id uuid,
    metric character varying(50) DEFAULT 'steps'::character varying NOT NULL,
    period character varying(50) DEFAULT 'daily'::character varying NOT NULL,
    winner_id uuid
);


ALTER TABLE public.challenges OWNER TO pi;

--
-- Name: daily_goals; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.daily_goals (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    steps_goal integer DEFAULT 8000 NOT NULL,
    calories_goal numeric(7,2) DEFAULT 320 NOT NULL,
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.daily_goals OWNER TO pi;

--
-- Name: friendships; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.friendships (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    requester_id uuid NOT NULL,
    addressee_id uuid NOT NULL,
    status character varying(10) DEFAULT 'pending'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT friendships_check CHECK ((requester_id <> addressee_id)),
    CONSTRAINT friendships_status_check CHECK (((status)::text = ANY (ARRAY[('pending'::character varying)::text, ('accepted'::character varying)::text, ('blocked'::character varying)::text])))
);


ALTER TABLE public.friendships OWNER TO pi;

--
-- Name: step_sessions; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
)
PARTITION BY RANGE (session_date);


ALTER TABLE public.step_sessions OWNER TO pi;

--
-- Name: users; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    display_name character varying(100),
    total_steps_lifetime bigint DEFAULT 0,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    is_active boolean DEFAULT true,
    last_sync_at timestamp with time zone,
    avatar_url character varying(255)
);


ALTER TABLE public.users OWNER TO pi;

--
-- Name: friends_weekly_leaderboard; Type: VIEW; Schema: public; Owner: pi
--

CREATE VIEW public.friends_weekly_leaderboard AS
 WITH bidirectional_friendships AS (
         SELECT friendships.requester_id AS me,
            friendships.addressee_id AS my_friend
           FROM public.friendships
          WHERE ((friendships.status)::text = 'accepted'::text)
        UNION
         SELECT friendships.addressee_id AS me,
            friendships.requester_id AS my_friend
           FROM public.friendships
          WHERE ((friendships.status)::text = 'accepted'::text)
        )
 SELECT u.id AS user_id,
    u.display_name,
    u.username,
    COALESCE(sum(ss.steps), (0)::bigint) AS total_steps,
    rank() OVER (PARTITION BY f.me ORDER BY COALESCE(sum(ss.steps), (0)::bigint) DESC) AS rank,
    f.me AS viewer_id
   FROM ((bidirectional_friendships f
     JOIN public.users u ON ((u.id = f.my_friend)))
     LEFT JOIN public.step_sessions ss ON (((ss.user_id = f.my_friend) AND (ss.session_date >= date_trunc('week'::text, (CURRENT_DATE)::timestamp with time zone)))))
  GROUP BY u.id, u.display_name, u.username, f.me;


ALTER VIEW public.friends_weekly_leaderboard OWNER TO pi;

--
-- Name: last_30d_stats; Type: VIEW; Schema: public; Owner: pi
--

CREATE VIEW public.last_30d_stats AS
 SELECT user_id,
    session_date,
    steps,
    calories,
    (distance_meters / (1000)::numeric) AS distance_km,
    active_minutes
   FROM public.step_sessions
  WHERE (session_date >= (CURRENT_DATE - 30));


ALTER VIEW public.last_30d_stats OWNER TO pi;

--
-- Name: leaderboard_cache; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.leaderboard_cache (
    user_id uuid NOT NULL,
    period character varying(20) NOT NULL,
    rank integer NOT NULL,
    steps integer NOT NULL,
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.leaderboard_cache OWNER TO pi;

--
-- Name: route_points; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.route_points (
    id bigint NOT NULL,
    route_id text NOT NULL,
    latitude numeric(10,8) NOT NULL,
    longitude numeric(11,8) NOT NULL,
    altitude numeric(7,2),
    "timestamp" timestamp with time zone NOT NULL,
    speed_ms numeric(5,2)
);


ALTER TABLE public.route_points OWNER TO pi;

--
-- Name: route_points_id_seq; Type: SEQUENCE; Schema: public; Owner: pi
--

CREATE SEQUENCE public.route_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.route_points_id_seq OWNER TO pi;

--
-- Name: route_points_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pi
--

ALTER SEQUENCE public.route_points_id_seq OWNED BY public.route_points.id;


--
-- Name: routes; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.routes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    started_at timestamp with time zone NOT NULL,
    ended_at timestamp with time zone,
    distance_meters numeric(9,2) NOT NULL,
    duration_seconds integer NOT NULL,
    avg_speed_kmh numeric(5,2),
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.routes OWNER TO pi;

--
-- Name: step_sessions_202601; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202601 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202601 OWNER TO pi;

--
-- Name: step_sessions_202602; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202602 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202602 OWNER TO pi;

--
-- Name: step_sessions_202603; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202603 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202603 OWNER TO pi;

--
-- Name: step_sessions_202604; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202604 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202604 OWNER TO pi;

--
-- Name: step_sessions_202605; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202605 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202605 OWNER TO pi;

--
-- Name: step_sessions_202606; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202606 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202606 OWNER TO pi;

--
-- Name: step_sessions_202607; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202607 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202607 OWNER TO pi;

--
-- Name: step_sessions_202608; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202608 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202608 OWNER TO pi;

--
-- Name: step_sessions_202609; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202609 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202609 OWNER TO pi;

--
-- Name: step_sessions_202610; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202610 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202610 OWNER TO pi;

--
-- Name: step_sessions_202611; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202611 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202611 OWNER TO pi;

--
-- Name: step_sessions_202612; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.step_sessions_202612 (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    session_date date NOT NULL,
    steps integer DEFAULT 0 NOT NULL,
    calories numeric(8,2) DEFAULT 0 NOT NULL,
    distance_meters numeric(9,2) DEFAULT 0 NOT NULL,
    active_minutes integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    distance double precision DEFAULT 0.0
);


ALTER TABLE public.step_sessions_202612 OWNER TO pi;

--
-- Name: user_badges; Type: TABLE; Schema: public; Owner: pi
--

CREATE TABLE public.user_badges (
    user_id uuid NOT NULL,
    badge_id character varying(50) NOT NULL,
    earned_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.user_badges OWNER TO pi;

--
-- Name: weekly_stats; Type: VIEW; Schema: public; Owner: pi
--

CREATE VIEW public.weekly_stats AS
 SELECT user_id,
    date_trunc('week'::text, (session_date)::timestamp with time zone) AS week_start,
    sum(steps) AS total_steps,
    sum(distance_meters) AS total_distance_meters,
    sum(calories) AS total_calories,
    sum(active_minutes) AS total_active_minutes,
    count(*) AS active_days,
    avg(steps) AS avg_daily_steps
   FROM public.step_sessions
  GROUP BY user_id, (date_trunc('week'::text, (session_date)::timestamp with time zone));


ALTER VIEW public.weekly_stats OWNER TO pi;

--
-- Name: step_sessions_202601; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202601 FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');


--
-- Name: step_sessions_202602; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202602 FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');


--
-- Name: step_sessions_202603; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202603 FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');


--
-- Name: step_sessions_202604; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202604 FOR VALUES FROM ('2026-04-01') TO ('2026-05-01');


--
-- Name: step_sessions_202605; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202605 FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');


--
-- Name: step_sessions_202606; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202606 FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');


--
-- Name: step_sessions_202607; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202607 FOR VALUES FROM ('2026-07-01') TO ('2026-08-01');


--
-- Name: step_sessions_202608; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202608 FOR VALUES FROM ('2026-08-01') TO ('2026-09-01');


--
-- Name: step_sessions_202609; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202609 FOR VALUES FROM ('2026-09-01') TO ('2026-10-01');


--
-- Name: step_sessions_202610; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202610 FOR VALUES FROM ('2026-10-01') TO ('2026-11-01');


--
-- Name: step_sessions_202611; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202611 FOR VALUES FROM ('2026-11-01') TO ('2026-12-01');


--
-- Name: step_sessions_202612; Type: TABLE ATTACH; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions ATTACH PARTITION public.step_sessions_202612 FOR VALUES FROM ('2026-12-01') TO ('2027-01-01');


--
-- Name: route_points id; Type: DEFAULT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.route_points ALTER COLUMN id SET DEFAULT nextval('public.route_points_id_seq'::regclass);


--
-- Data for Name: badges; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.badges VALUES ('first_1000', 'Primeros Pasos', 'Alcanza 1.000 pasos acumulados.', 'ic_badge_first', 1000, NULL, NULL, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('five_k', '5K Runner', 'Camina 5.000 pasos en un solo día.', 'ic_badge_5k', 5000, NULL, NULL, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('ten_k', '10K Club', 'Supera los 10.000 pasos en un solo día.', 'ic_badge_10k', 10000, NULL, NULL, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('week_streak', 'Semana Activa', '7 días consecutivos con más de 5.000 pasos.', 'ic_badge_streak', NULL, NULL, 7, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('early_bird', 'Madrugador', 'Registra tu primera sesión antes de las 8:00.', 'ic_badge_bird', NULL, NULL, NULL, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('marathon', 'Maratoniano', 'Acumula 42.195 metros de distancia total.', 'ic_badge_marathon', NULL, 42195.00, NULL, '2026-05-11 19:20:14.07315+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('andarin_utrera', 'Andarín de Utrera', 'Acumula 70.000 pasos.', 'ic_badge_marathon', 70000, NULL, NULL, '2026-05-16 23:42:42.603659+02', NULL, false, NULL);
INSERT INTO public.badges VALUES ('cachimber_compostela', 'Cachimber de Compostela', 'Acumula 100.000 pasos.', 'ic_badge_streak', 100000, NULL, NULL, '2026-05-16 23:42:42.603659+02', NULL, false, NULL);


--
-- Data for Name: challenge_participants; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.challenge_participants VALUES ('11111111-1111-1111-1111-111111111111', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14 19:35:27.295655+02');
INSERT INTO public.challenge_participants VALUES ('22222222-2222-2222-2222-222222222222', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14 19:35:27.295655+02');
INSERT INTO public.challenge_participants VALUES ('7d948ea6-764f-4939-aeb3-90f0694bc1d0', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14 19:35:27.295655+02');
INSERT INTO public.challenge_participants VALUES ('db89361b-1254-4cb1-a3dc-4b0ee6be6aaa', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14 19:35:27.295655+02');
INSERT INTO public.challenge_participants VALUES ('66656269-7938-43e1-86fd-328b9c8dfd49', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14 19:35:27.295655+02');
INSERT INTO public.challenge_participants VALUES ('a6f9be4c-9dbd-4d9e-8a9c-02db6d697a41', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-16 16:49:25.083804+02');
INSERT INTO public.challenge_participants VALUES ('13a2324d-6b67-4b28-b504-b4602c42a238', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-16 16:49:29.18742+02');
INSERT INTO public.challenge_participants VALUES ('8460721f-afcb-449e-baa3-6f67c7094b13', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-16 16:49:31.491197+02');


--
-- Data for Name: challenges; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.challenges VALUES ('a3ec985d-588c-4157-8b77-cd83c19c3ac1', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto Relámpago de Pasos', 'A ver quién llega primero al objetivo diario', 10000, '2026-05-15 00:00:00+02', '2026-05-22 23:59:59+02', 'active', '2026-05-15 13:43:37.59181+02', '2026-05-15 13:43:37.59181+02', NULL, 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('f95396f1-05f2-49b3-bf2b-d5ad36bf9b83', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-15 13:54:14.985+02', '2026-05-22 13:54:14.985+02', 'pending', '2026-05-15 13:54:16.05004+02', '2026-05-15 13:54:16.05004+02', '60acd23e-aade-4562-8005-c6bf034b3db3', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('7134373d-283d-4f55-b687-3f464e27acb1', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de distance', 'Compite con tu amigo en daily.', 10000, '2026-05-15 13:54:30.704+02', '2026-05-22 13:54:30.704+02', 'pending', '2026-05-15 13:54:31.84447+02', '2026-05-15 13:54:31.84447+02', '60acd23e-aade-4562-8005-c6bf034b3db3', 'distance', 'daily', NULL);
INSERT INTO public.challenges VALUES ('48163071-99fd-410d-923d-b86dd1f48661', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de distance', 'Compite con tu amigo en weekly.', 10000, '2026-05-15 13:55:11.465+02', '2026-05-22 13:55:11.465+02', 'pending', '2026-05-15 13:55:12.572354+02', '2026-05-15 13:55:12.572354+02', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'distance', 'weekly', NULL);
INSERT INTO public.challenges VALUES ('28bda9ca-0232-452d-b514-808175a9e99f', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-15 14:00:43.638+02', '2026-05-22 14:00:43.638+02', 'pending', '2026-05-15 14:00:44.820592+02', '2026-05-15 14:00:44.820592+02', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('ec42162d-4e90-4c02-9854-06e26937bf9d', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-15 14:21:51.982+02', '2026-05-22 14:21:51.982+02', 'pending', '2026-05-15 14:21:53.10248+02', '2026-05-15 14:21:53.10248+02', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('245c90d3-643a-494f-bd7c-e4ac46f60ddb', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de distance', 'Compite con tu amigo en daily.', 10000, '2026-05-16 13:54:10.016+02', '2026-05-23 13:54:10.016+02', 'pending', '2026-05-16 13:54:11.00626+02', '2026-05-16 13:54:11.00626+02', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'distance', 'daily', NULL);
INSERT INTO public.challenges VALUES ('d71f9676-c764-416a-8175-5fe5f32652f7', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-16 13:55:50.836+02', '2026-05-23 13:55:50.836+02', 'pending', '2026-05-16 13:55:51.845565+02', '2026-05-16 13:55:51.845565+02', '8c987cd3-5e97-46ed-8c3d-645a50eccb1f', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('a6f9be4c-9dbd-4d9e-8a9c-02db6d697a41', '60acd23e-aade-4562-8005-c6bf034b3db3', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-16 16:49:24.123+02', '2026-05-23 16:49:24.123+02', 'pending', '2026-05-16 16:49:25.083804+02', '2026-05-16 16:49:25.083804+02', '58ccec7f-6a79-4621-a129-4d4223608f32', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('13a2324d-6b67-4b28-b504-b4602c42a238', '60acd23e-aade-4562-8005-c6bf034b3db3', 'Reto de distance', 'Compite con tu amigo en weekly.', 10000, '2026-05-16 16:49:28.211+02', '2026-05-23 16:49:28.211+02', 'pending', '2026-05-16 16:49:29.18742+02', '2026-05-16 16:49:29.18742+02', '58ccec7f-6a79-4621-a129-4d4223608f32', 'distance', 'weekly', NULL);
INSERT INTO public.challenges VALUES ('8460721f-afcb-449e-baa3-6f67c7094b13', '60acd23e-aade-4562-8005-c6bf034b3db3', 'Reto de steps', 'Compite con tu amigo en daily.', 10000, '2026-05-16 16:49:30.51+02', '2026-05-23 16:49:30.51+02', 'pending', '2026-05-16 16:49:31.491197+02', '2026-05-16 16:49:31.491197+02', '58ccec7f-6a79-4621-a129-4d4223608f32', 'steps', 'daily', NULL);
INSERT INTO public.challenges VALUES ('11111111-1111-1111-1111-111111111111', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 'Primer Reto', 'Camina 10.000 pasos', 10000, '2026-05-11 19:30:24.573425+02', '2026-05-12 19:30:24.573425+02', 'completed', '2026-05-11 19:30:24.573425+02', '2026-05-16 17:26:34.782299+02', NULL, 'steps', 'daily', '60acd23e-aade-4562-8005-c6bf034b3db3');
INSERT INTO public.challenges VALUES ('22222222-2222-2222-2222-222222222222', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 'Reto Semanal', 'Acumula 50.000 pasos en la semana', 50000, '2026-05-11 19:30:24.573425+02', '2026-05-18 19:30:24.573425+02', 'completed', '2026-05-11 19:30:24.573425+02', '2026-05-16 17:26:34.782299+02', NULL, 'steps', 'daily', '60acd23e-aade-4562-8005-c6bf034b3db3');
INSERT INTO public.challenges VALUES ('7d948ea6-764f-4939-aeb3-90f0694bc1d0', '6125612d-6cf0-486e-93e7-68bc0739471a', 'Reto Semanal primo', 'Camina 18077 pasos esta semana', 11408, '2026-05-14 01:26:26.471607+02', '2026-05-21 01:26:26.471607+02', 'completed', '2026-05-14 01:26:26.471607+02', '2026-05-16 17:26:34.782299+02', NULL, 'steps', 'daily', '60acd23e-aade-4562-8005-c6bf034b3db3');
INSERT INTO public.challenges VALUES ('db89361b-1254-4cb1-a3dc-4b0ee6be6aaa', 'fb083a82-a35f-4e1f-af94-226e7f0aba55', 'Reto Semanal cachi', 'Camina 19732 pasos esta semana', 21852, '2026-05-14 01:26:26.471607+02', '2026-05-21 01:26:26.471607+02', 'completed', '2026-05-14 01:26:26.471607+02', '2026-05-16 17:26:34.782299+02', NULL, 'steps', 'daily', '60acd23e-aade-4562-8005-c6bf034b3db3');
INSERT INTO public.challenges VALUES ('66656269-7938-43e1-86fd-328b9c8dfd49', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', 'Reto Semanal traca', 'Camina 28553 pasos esta semana', 17453, '2026-05-14 01:26:26.471607+02', '2026-05-21 01:26:26.471607+02', 'completed', '2026-05-14 01:26:26.471607+02', '2026-05-16 17:26:34.782299+02', NULL, 'steps', 'daily', '60acd23e-aade-4562-8005-c6bf034b3db3');


--
-- Data for Name: daily_goals; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.daily_goals VALUES ('39f39a16-fc64-43ce-9527-90af318dfeb9', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 8000, 320.00, '2026-05-11 19:25:48.618554+02');
INSERT INTO public.daily_goals VALUES ('733754de-22cc-453a-b1fb-3aff9786c2c7', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', 8000, 320.00, '2026-05-11 19:25:48.618554+02');
INSERT INTO public.daily_goals VALUES ('ed64db1f-567a-408a-8ac2-b6a6af5c0758', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 8000, 320.00, '2026-05-11 19:25:48.618554+02');


--
-- Data for Name: friendships; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.friendships VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', 'accepted', '2026-05-11 19:29:07.573134+02', '2026-05-11 19:30:24.569531+02');
INSERT INTO public.friendships VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2bf9ed9c-4a4d-486c-923c-00fe20187191', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'accepted', '2026-05-11 19:29:07.573134+02', '2026-05-11 19:30:24.569531+02');
INSERT INTO public.friendships VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'pending', '2026-05-11 19:29:07.573134+02', '2026-05-11 19:30:24.569531+02');
INSERT INTO public.friendships VALUES ('4799d0f7-e829-47cc-9e12-60ab58c9abfc', 'fb083a82-a35f-4e1f-af94-226e7f0aba55', '6125612d-6cf0-486e-93e7-68bc0739471a', 'accepted', '2026-05-14 01:26:26.468051+02', '2026-05-14 01:26:26.468051+02');
INSERT INTO public.friendships VALUES ('0117dba0-c659-411e-ac03-81e43d4dd441', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', '6125612d-6cf0-486e-93e7-68bc0739471a', 'accepted', '2026-05-14 01:26:26.468051+02', '2026-05-14 01:26:26.468051+02');
INSERT INTO public.friendships VALUES ('b8a8fd31-bdb0-4ac1-a5dc-1c78d91b9855', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', 'fb083a82-a35f-4e1f-af94-226e7f0aba55', 'accepted', '2026-05-14 01:26:26.468051+02', '2026-05-14 01:26:26.468051+02');
INSERT INTO public.friendships VALUES ('fa0b6df4-c5e2-413b-ac89-1a3342c3a6e5', '60acd23e-aade-4562-8005-c6bf034b3db3', 'ce734682-5149-4493-9ade-6c5483816cc4', 'pending', '2026-05-14 12:44:38.574545+02', '2026-05-14 12:44:38.574545+02');
INSERT INTO public.friendships VALUES ('58ccec7f-6a79-4621-a129-4d4223608f32', '2bf9ed9c-4a4d-486c-923c-00fe20187191', '60acd23e-aade-4562-8005-c6bf034b3db3', 'accepted', '2026-05-14 12:44:46.041666+02', '2026-05-14 12:44:46.041666+02');
INSERT INTO public.friendships VALUES ('b3001edc-ba7a-4c4b-80cf-c44d705626dc', '60acd23e-aade-4562-8005-c6bf034b3db3', '26296f7c-1d6f-492d-b287-ccb303e2bc00', 'accepted', '2026-05-14 14:07:42.615737+02', '2026-05-14 14:07:42.615737+02');
INSERT INTO public.friendships VALUES ('cd8b43e4-da80-4e99-9b04-92afc670698a', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'accepted', '2026-05-15 13:43:23.259965+02', '2026-05-15 13:43:23.259965+02');
INSERT INTO public.friendships VALUES ('8c987cd3-5e97-46ed-8c3d-645a50eccb1f', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2bf9ed9c-4a4d-486c-923c-00fe20187191', 'accepted', '2026-05-15 13:43:28.426414+02', '2026-05-15 13:43:28.426414+02');
INSERT INTO public.friendships VALUES ('b7f64714-3c56-4c0b-b521-81700e6c802f', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '60acd23e-aade-4562-8005-c6bf034b3db3', 'accepted', '2026-05-15 13:44:15.064008+02', '2026-05-15 13:44:15.064008+02');
INSERT INTO public.friendships VALUES ('895ba2ba-3eb3-440c-85ef-00c67c161349', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '6125612d-6cf0-486e-93e7-68bc0739471a', 'accepted', '2026-05-15 14:00:46.402506+02', '2026-05-15 14:00:46.402506+02');
INSERT INTO public.friendships VALUES ('0afd2d70-705f-4b13-985f-6a713f277279', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '26296f7c-1d6f-492d-b287-ccb303e2bc00', 'pending', '2026-05-15 14:09:36.239658+02', '2026-05-15 14:09:36.239658+02');
INSERT INTO public.friendships VALUES ('cc65c399-65b0-4cb2-a4b5-8467d77f4306', '92e46b2a-db9b-4f1f-9bab-953916d87306', 'fb083a82-a35f-4e1f-af94-226e7f0aba55', 'pending', '2026-05-15 15:05:08.906553+02', '2026-05-15 15:05:08.906553+02');
INSERT INTO public.friendships VALUES ('19a46250-8902-4c09-a489-42264e8dd8f1', 'a2a279e7-d161-4675-a2d7-015f189ef310', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'pending', '2026-05-16 13:41:43.032407+02', '2026-05-16 13:41:43.032407+02');


--
-- Data for Name: leaderboard_cache; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.leaderboard_cache VALUES ('0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'weekly', 1, 9100, '2026-05-11 19:35:49.587033+02');
INSERT INTO public.leaderboard_cache VALUES ('2bf9ed9c-4a4d-486c-923c-00fe20187191', 'weekly', 2, 6500, '2026-05-11 19:35:49.587033+02');
INSERT INTO public.leaderboard_cache VALUES ('dccd1cd1-488c-468f-9aa1-81e42efb6acd', 'weekly', 3, 4200, '2026-05-11 19:35:49.587033+02');


--
-- Data for Name: route_points; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.route_points VALUES (1, '1778967538798', 37.26466980, -7.02098480, 71.30, '2026-05-16 23:38:55.338+02', NULL);


--
-- Data for Name: routes; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202601; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202602; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202603; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202604; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202605; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.step_sessions_202605 VALUES ('f643f2d0-eaf9-4f18-bc0a-0f14c5554f21', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-15', 5060, 202.40, 3542.00, 42, '2026-05-15 12:27:55.603023+02', '2026-05-16 23:41:13.102343+02', 3542);
INSERT INTO public.step_sessions_202605 VALUES ('f8c9afb3-44d8-4cad-a408-436f2eb8a3ab', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-15', 5060, 202.40, 3542.00, 42, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 3542);
INSERT INTO public.step_sessions_202605 VALUES ('82022aa4-c4be-491c-b424-5198caab79d9', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-14', 15870, 634.80, 11109.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 11109);
INSERT INTO public.step_sessions_202605 VALUES ('3ff35a5e-9e95-4bd1-9ef7-19e75e1bf62d', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-13', 8420, 340.00, 5800.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 5800);
INSERT INTO public.step_sessions_202605 VALUES ('124b8599-39c5-49ce-b60d-19aa57c74e4b', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-12', 10500, 410.00, 7200.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 7200);
INSERT INTO public.step_sessions_202605 VALUES ('b9b7267d-6e99-46ae-a510-86156be37138', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-11', 4200, 180.00, 2900.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 2900);
INSERT INTO public.step_sessions_202605 VALUES ('fc3bcc3a-e35e-4b18-ab38-0913df0cb601', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-10', 12100, 500.00, 8500.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 8500);
INSERT INTO public.step_sessions_202605 VALUES ('52666ae3-f1ff-425e-9607-90a41e873b5c', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-09', 7800, 310.00, 5400.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 5400);
INSERT INTO public.step_sessions_202605 VALUES ('b54f6961-0187-4303-8ea6-cf193e9c5ebb', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-08', 9300, 380.00, 6500.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 6500);
INSERT INTO public.step_sessions_202605 VALUES ('7c282ee5-6d5c-4f78-b4cc-c7972757de14', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-07', 11000, 450.00, 7700.00, 0, '2026-05-16 23:43:09.958657+02', '2026-05-16 23:43:10.161476+02', 7700);
INSERT INTO public.step_sessions_202605 VALUES ('6e54ebc5-658f-49e4-9996-38b67aeec269', '0b94fa39-3ede-4dde-97d7-b8efcba0c440', '2026-05-11', 9100, 410.00, 7200.00, 91, '2026-05-11 19:25:48.626779+02', '2026-05-16 22:48:34.674354+02', 7200);
INSERT INTO public.step_sessions_202605 VALUES ('c79a733b-9b39-4129-a5e3-2be80c6d791a', '2bf9ed9c-4a4d-486c-923c-00fe20187191', '2026-05-11', 6500, 280.50, 5100.00, 68, '2026-05-11 19:25:48.620863+02', '2026-05-16 22:48:34.674354+02', 5100);
INSERT INTO public.step_sessions_202605 VALUES ('108d2c06-b913-473c-979f-f93094cea5c5', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-07', 9800, 392.00, 6860.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 6860);
INSERT INTO public.step_sessions_202605 VALUES ('3b4cf210-dac5-4c12-80de-51c22b959cd5', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-08', 11200, 448.00, 7840.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 7840);
INSERT INTO public.step_sessions_202605 VALUES ('88ffe731-231f-43b0-9b23-7c74893075fa', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-09', 6500, 260.00, 4550.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 4550);
INSERT INTO public.step_sessions_202605 VALUES ('fe3c8c1a-92e0-4e39-9c7d-fbe31fa29ffa', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-10', 13000, 520.00, 9100.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 9100);
INSERT INTO public.step_sessions_202605 VALUES ('95453f3d-3ff9-4ea2-bf88-acecce5abb5c', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-11', 8200, 328.00, 5740.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 5740);
INSERT INTO public.step_sessions_202605 VALUES ('adff10ba-a14e-432c-9021-459673e7b481', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-12', 10100, 404.00, 7070.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 7070);
INSERT INTO public.step_sessions_202605 VALUES ('b75af6e9-b9b9-46e0-9af9-80c3355a85f3', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-13', 14200, 568.00, 9940.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 9940);
INSERT INTO public.step_sessions_202605 VALUES ('cfe7cb19-aa57-4361-b1d4-4cab63972807', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-14', 12000, 480.00, 8400.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 8400);
INSERT INTO public.step_sessions_202605 VALUES ('a3df5a74-0a39-45ce-b72a-baf8e9f14a44', '6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', '2026-05-15', 4300, 172.00, 3010.00, 0, '2026-05-15 13:46:33.28365+02', '2026-05-16 22:48:34.674354+02', 3010);
INSERT INTO public.step_sessions_202605 VALUES ('6952ca90-3a11-42bd-93b3-90cc793d86c5', 'dccd1cd1-488c-468f-9aa1-81e42efb6acd', '2026-05-11', 4200, 190.00, 3300.00, 44, '2026-05-11 19:25:48.623691+02', '2026-05-16 22:48:34.674354+02', 3300);
INSERT INTO public.step_sessions_202605 VALUES ('acc6c0d3-28da-412e-9784-2b9fc3695d39', '7dc219bb-3443-4652-8179-aa467dba555b', '2026-05-16', 951, 38.04, 665.70, 0, '2026-05-16 19:04:40.460385+02', '2026-05-16 23:43:10.161476+02', 665.7);
INSERT INTO public.step_sessions_202605 VALUES ('11f44ebe-017b-4ad2-af10-e77be2a05eba', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-16', 434, 17.36, 303.80, 0, '2026-05-16 19:03:36.13726+02', '2026-05-16 23:41:13.102343+02', 303.8);
INSERT INTO public.step_sessions_202605 VALUES ('38664dc3-6640-46cb-bee6-282ca42d19a7', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-14', 15870, 634.80, 11109.00, 0, '2026-05-14 14:44:26.25987+02', '2026-05-16 23:41:13.102343+02', 11109);
INSERT INTO public.step_sessions_202605 VALUES ('c97e86e1-556f-4223-a69c-9023e3252a2d', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-13', 8420, 340.00, 5800.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 5800);
INSERT INTO public.step_sessions_202605 VALUES ('477b3d18-f9fb-4435-8d45-b643ff7af2b9', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-12', 10500, 410.00, 7200.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 7200);
INSERT INTO public.step_sessions_202605 VALUES ('f51862f0-8b9b-44ed-8a45-5c8a2f32af40', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-11', 4200, 180.00, 2900.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 2900);
INSERT INTO public.step_sessions_202605 VALUES ('532d8498-98bc-45b1-b402-215b0ff7286c', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-10', 12100, 500.00, 8500.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 8500);
INSERT INTO public.step_sessions_202605 VALUES ('de876ad2-c873-444e-8f9c-7a3296ae9d1d', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-09', 7800, 310.00, 5400.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 5400);
INSERT INTO public.step_sessions_202605 VALUES ('93b36027-abe5-47b1-89a9-0a4fc51b04c1', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-08', 9300, 380.00, 6500.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 6500);
INSERT INTO public.step_sessions_202605 VALUES ('5edab540-a212-4cd7-b75a-9dafe00b210d', '60acd23e-aade-4562-8005-c6bf034b3db3', '2026-05-07', 11000, 450.00, 7700.00, 0, '2026-05-14 19:33:46.930674+02', '2026-05-16 23:41:13.102343+02', 7700);


--
-- Data for Name: step_sessions_202606; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202607; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202608; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202609; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202610; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202611; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: step_sessions_202612; Type: TABLE DATA; Schema: public; Owner: pi
--



--
-- Data for Name: user_badges; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'first_1000', '2026-05-16 15:03:15.36984+02');
INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'five_k', '2026-05-16 15:03:15.36984+02');
INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'ten_k', '2026-05-16 15:03:15.36984+02');
INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'marathon', '2026-05-16 15:03:15.36984+02');
INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'week_streak', '2026-05-16 15:03:15.36984+02');
INSERT INTO public.user_badges VALUES ('60acd23e-aade-4562-8005-c6bf034b3db3', 'first_1000', '2026-05-16 16:48:49.881379+02');
INSERT INTO public.user_badges VALUES ('60acd23e-aade-4562-8005-c6bf034b3db3', 'five_k', '2026-05-16 16:48:49.881379+02');
INSERT INTO public.user_badges VALUES ('60acd23e-aade-4562-8005-c6bf034b3db3', 'ten_k', '2026-05-16 16:48:49.881379+02');
INSERT INTO public.user_badges VALUES ('60acd23e-aade-4562-8005-c6bf034b3db3', 'marathon', '2026-05-16 16:48:49.881379+02');
INSERT INTO public.user_badges VALUES ('7dc219bb-3443-4652-8179-aa467dba555b', 'first_1000', '2026-05-16 18:38:39.230202+02');
INSERT INTO public.user_badges VALUES ('7dc219bb-3443-4652-8179-aa467dba555b', 'five_k', '2026-05-16 23:43:09.958657+02');
INSERT INTO public.user_badges VALUES ('7dc219bb-3443-4652-8179-aa467dba555b', 'ten_k', '2026-05-16 23:43:09.958657+02');
INSERT INTO public.user_badges VALUES ('7dc219bb-3443-4652-8179-aa467dba555b', 'marathon', '2026-05-16 23:43:09.958657+02');
INSERT INTO public.user_badges VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'andarin_utrera', '2026-05-16 23:45:15.513527+02');


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: pi
--

INSERT INTO public.users VALUES ('6edbf1ce-1b54-44cf-b54d-18a26c62692b', 'string', 'user@example.com', '$argon2id$v=19$m=65536,t=3,p=4$0XrvHaMUotS6N2aMca6VEg$jxBKd7tLdjLknBN5T+j3fzptlGIOhwI3NqodyODuTA4', NULL, 0, '2026-05-13 17:52:56.458472+02', '2026-05-13 17:52:56.458472+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('b48bc2c9-8998-40e8-a1bf-5854a27b3f79', 'willi', 'use@eample.com', '$argon2id$v=19$m=65536,t=3,p=4$EQLgHCPEOCdk7P1fa20txQ$YjAj/HJFMz2DD5KSBtrX1cIiCSSVVoc6CqUGLP6szh8', NULL, 0, '2026-05-13 17:55:07.266207+02', '2026-05-13 17:55:07.266207+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('ce734682-5149-4493-9ade-6c5483816cc4', 'fornes', 'fornes@example.com', '$argon2id$v=19$m=65536,t=3,p=4$b81ZyzkHQKhVKmVszTmnNA$kXZJkMzDhkhhZVBEJKA0VfIxeWpQnV2oZgHBqhjWKNA', NULL, 0, '2026-05-13 19:21:16.459536+02', '2026-05-13 19:21:16.459536+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('0b94fa39-3ede-4dde-97d7-b8efcba0c440', 'cachimber', 'cachi@kineo.local', '$2b$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6L6s57WyHYyMBNyG', 'Cachi', 54321, '2026-05-11 19:25:48.61466+02', '2026-05-14 00:20:22.5307+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('2bf9ed9c-4a4d-486c-923c-00fe20187191', 'mmg', 'mmg@kineo.local', '$2b$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6L6s57WyHYyMBNyG', 'mmg', 123456, '2026-05-11 19:25:48.61466+02', '2026-05-14 00:27:59.950749+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('6125612d-6cf0-486e-93e7-68bc0739471a', 'primo', 'primo@gmail.com', '\$2b\$12\$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LeCt1Jc5VdUw', 'Primo', 0, '2026-05-14 00:30:55.132729+02', '2026-05-14 01:26:26.46226+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('fb083a82-a35f-4e1f-af94-226e7f0aba55', 'cachi', 'cachi@gmail.com', '\$2b\$12\$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LeCt1Jc5VdUw', 'Cachi', 0, '2026-05-14 01:26:26.46226+02', '2026-05-14 01:26:26.46226+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('dccd1cd1-488c-468f-9aa1-81e42efb6acd', 'traca', 'traca@gmail.com', '\$2b\$12\$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LeCt1Jc5VdUw', 'Traca', 98765, '2026-05-11 19:25:48.61466+02', '2026-05-14 01:26:26.46226+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('547ef527-d3f5-411e-a74f-287ec5be0467', 'hola', 'primo', '$argon2id$v=19$m=65536,t=3,p=4$e0/pHSMEIOQ8R6i1NoawNg$eSk2+bBkJNhjoj/R/1iHNnqT/TuUmrrTDrtI01ItiUs', NULL, 0, '2026-05-14 01:28:43.448956+02', '2026-05-14 01:28:43.448956+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('26296f7c-1d6f-492d-b287-ccb303e2bc00', 'pinini', 'pinini', '$argon2id$v=19$m=65536,t=3,p=4$8X6P0fofI6RUao0xZgxBiA$1L21uR07SUpYGZdUKqehbAn2zXMfgRbQyHA/iSmoMfQ', NULL, 0, '2026-05-14 10:46:18.998965+02', '2026-05-14 10:46:18.998965+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('92e46b2a-db9b-4f1f-9bab-953916d87306', 'antolinpulido', 'antolin@pulido.com', '$argon2id$v=19$m=65536,t=3,p=4$yFnL2VtrzXlvjbG2du699w$Zf3EuBiB0q9aL7/CNvmVfnjutbBOnjj7EkWQX3Z0HXk', '4ntolin', 0, '2026-05-15 11:43:10.225686+02', '2026-05-15 11:43:10.225686+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('a2a279e7-d161-4675-a2d7-015f189ef310', 'manmorgue', 'manmorgue', '$argon2id$v=19$m=65536,t=3,p=4$aC3FmNNaa42x1hqDMCYEgA$Y4e8BJiJrzLELdTdHPfgoP2KYeFESDBuoEwEyyS2RKQ', NULL, 0, '2026-05-16 13:41:24.748997+02', '2026-05-16 13:41:24.748997+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('6aa8f855-fb78-4d4e-a20d-6704b3c5fb9b', 'erlangba', 'erlang@ba.com', '$argon2id$v=19$m=65536,t=3,p=4$XKu1tpayllKKsba2FsLYew$tVAPgTQyoLDCVTHEtN+kiYsdcLXsYhgXGzA6CFzirLw', NULL, 100700, '2026-05-13 23:04:23.352999+02', '2026-05-16 13:47:12.896852+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('60acd23e-aade-4562-8005-c6bf034b3db3', 'Javier', 'javier@mail.com', '$argon2id$v=19$m=65536,t=3,p=4$FCJEiDHmnFMqZYwRglDq/Q$1ccTUtb6R/vp134tplvcyf3Zu4Jrp9E5Vo2/2L9SI5o', NULL, 97650, '2026-05-14 12:26:32.651117+02', '2026-05-16 14:31:51.209148+02', true, NULL, NULL);
INSERT INTO public.users VALUES ('7dc219bb-3443-4652-8179-aa467dba555b', 'F Javier', 'javierDAM@mail.com', '$argon2id$v=19$m=65536,t=3,p=4$fE9pbe2dcw4B4FwrxRhjLA$Ql3mEnOb0MqcKPRTGol02WMO/RelyYNiG2Bras42bwQ', NULL, 0, '2026-05-16 17:41:01.104653+02', '2026-05-16 17:41:01.104653+02', true, NULL, NULL);


--
-- Name: route_points_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pi
--

SELECT pg_catalog.setval('public.route_points_id_seq', 1, true);


--
-- Name: badges badges_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.badges
    ADD CONSTRAINT badges_pkey PRIMARY KEY (id);


--
-- Name: challenge_participants challenge_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.challenge_participants
    ADD CONSTRAINT challenge_participants_pkey PRIMARY KEY (challenge_id, user_id);


--
-- Name: challenges challenges_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.challenges
    ADD CONSTRAINT challenges_pkey PRIMARY KEY (id);


--
-- Name: daily_goals daily_goals_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.daily_goals
    ADD CONSTRAINT daily_goals_pkey PRIMARY KEY (id);


--
-- Name: friendships friendships_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_pkey PRIMARY KEY (id);


--
-- Name: friendships friendships_requester_addressee_unique; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_requester_addressee_unique UNIQUE (requester_id, addressee_id);


--
-- Name: leaderboard_cache leaderboard_cache_user_id_period_key; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.leaderboard_cache
    ADD CONSTRAINT leaderboard_cache_user_id_period_key UNIQUE (user_id, period);


--
-- Name: route_points route_points_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.route_points
    ADD CONSTRAINT route_points_pkey PRIMARY KEY (id);


--
-- Name: routes routes_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);


--
-- Name: step_sessions step_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions
    ADD CONSTRAINT step_sessions_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202601 step_sessions_202601_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202601
    ADD CONSTRAINT step_sessions_202601_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202602 step_sessions_202602_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202602
    ADD CONSTRAINT step_sessions_202602_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202603 step_sessions_202603_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202603
    ADD CONSTRAINT step_sessions_202603_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202604 step_sessions_202604_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202604
    ADD CONSTRAINT step_sessions_202604_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202605 step_sessions_202605_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202605
    ADD CONSTRAINT step_sessions_202605_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202606 step_sessions_202606_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202606
    ADD CONSTRAINT step_sessions_202606_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202607 step_sessions_202607_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202607
    ADD CONSTRAINT step_sessions_202607_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202608 step_sessions_202608_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202608
    ADD CONSTRAINT step_sessions_202608_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202609 step_sessions_202609_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202609
    ADD CONSTRAINT step_sessions_202609_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202610 step_sessions_202610_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202610
    ADD CONSTRAINT step_sessions_202610_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202611 step_sessions_202611_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202611
    ADD CONSTRAINT step_sessions_202611_pkey PRIMARY KEY (id, session_date);


--
-- Name: step_sessions_202612 step_sessions_202612_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.step_sessions_202612
    ADD CONSTRAINT step_sessions_202612_pkey PRIMARY KEY (id, session_date);


--
-- Name: user_badges user_badges_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.user_badges
    ADD CONSTRAINT user_badges_pkey PRIMARY KEY (user_id, badge_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: friendships_user_pair_unique; Type: INDEX; Schema: public; Owner: pi
--

CREATE UNIQUE INDEX friendships_user_pair_unique ON public.friendships USING btree (LEAST(requester_id, addressee_id), GREATEST(requester_id, addressee_id)) WHERE ((status)::text = ANY ((ARRAY['pending'::character varying, 'accepted'::character varying])::text[]));


--
-- Name: idx_challenges_active; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_challenges_active ON public.challenges USING btree (status, ends_at) WHERE ((status)::text = 'active'::text);


--
-- Name: idx_challenges_creator; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_challenges_creator ON public.challenges USING btree (creator_id, status);


--
-- Name: idx_friendships_addressee; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_friendships_addressee ON public.friendships USING btree (addressee_id, status);


--
-- Name: idx_friendships_requester; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_friendships_requester ON public.friendships USING btree (requester_id, status);


--
-- Name: idx_leaderboard_period; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_leaderboard_period ON public.leaderboard_cache USING btree (period, rank);


--
-- Name: idx_route_points_route_time; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_route_points_route_time ON public.route_points USING btree (route_id, "timestamp");


--
-- Name: idx_step_sessions_user_date; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_step_sessions_user_date ON ONLY public.step_sessions USING btree (user_id, session_date);


--
-- Name: idx_step_sessions_user_range; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_step_sessions_user_range ON ONLY public.step_sessions USING btree (user_id, session_date DESC);


--
-- Name: idx_user_badges_user; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX idx_user_badges_user ON public.user_badges USING btree (user_id);


--
-- Name: step_sessions_202601_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202601_user_id_session_date_idx ON public.step_sessions_202601 USING btree (user_id, session_date);


--
-- Name: step_sessions_202601_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202601_user_id_session_date_idx1 ON public.step_sessions_202601 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202602_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202602_user_id_session_date_idx ON public.step_sessions_202602 USING btree (user_id, session_date);


--
-- Name: step_sessions_202602_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202602_user_id_session_date_idx1 ON public.step_sessions_202602 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202603_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202603_user_id_session_date_idx ON public.step_sessions_202603 USING btree (user_id, session_date);


--
-- Name: step_sessions_202603_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202603_user_id_session_date_idx1 ON public.step_sessions_202603 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202604_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202604_user_id_session_date_idx ON public.step_sessions_202604 USING btree (user_id, session_date);


--
-- Name: step_sessions_202604_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202604_user_id_session_date_idx1 ON public.step_sessions_202604 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202605_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202605_user_id_session_date_idx ON public.step_sessions_202605 USING btree (user_id, session_date);


--
-- Name: step_sessions_202605_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202605_user_id_session_date_idx1 ON public.step_sessions_202605 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202606_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202606_user_id_session_date_idx ON public.step_sessions_202606 USING btree (user_id, session_date);


--
-- Name: step_sessions_202606_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202606_user_id_session_date_idx1 ON public.step_sessions_202606 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202607_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202607_user_id_session_date_idx ON public.step_sessions_202607 USING btree (user_id, session_date);


--
-- Name: step_sessions_202607_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202607_user_id_session_date_idx1 ON public.step_sessions_202607 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202608_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202608_user_id_session_date_idx ON public.step_sessions_202608 USING btree (user_id, session_date);


--
-- Name: step_sessions_202608_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202608_user_id_session_date_idx1 ON public.step_sessions_202608 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202609_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202609_user_id_session_date_idx ON public.step_sessions_202609 USING btree (user_id, session_date);


--
-- Name: step_sessions_202609_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202609_user_id_session_date_idx1 ON public.step_sessions_202609 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202610_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202610_user_id_session_date_idx ON public.step_sessions_202610 USING btree (user_id, session_date);


--
-- Name: step_sessions_202610_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202610_user_id_session_date_idx1 ON public.step_sessions_202610 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202611_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202611_user_id_session_date_idx ON public.step_sessions_202611 USING btree (user_id, session_date);


--
-- Name: step_sessions_202611_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202611_user_id_session_date_idx1 ON public.step_sessions_202611 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202612_user_id_session_date_idx; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202612_user_id_session_date_idx ON public.step_sessions_202612 USING btree (user_id, session_date);


--
-- Name: step_sessions_202612_user_id_session_date_idx1; Type: INDEX; Schema: public; Owner: pi
--

CREATE INDEX step_sessions_202612_user_id_session_date_idx1 ON public.step_sessions_202612 USING btree (user_id, session_date DESC);


--
-- Name: step_sessions_202601_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202601_pkey;


--
-- Name: step_sessions_202601_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202601_user_id_session_date_idx;


--
-- Name: step_sessions_202601_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202601_user_id_session_date_idx1;


--
-- Name: step_sessions_202602_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202602_pkey;


--
-- Name: step_sessions_202602_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202602_user_id_session_date_idx;


--
-- Name: step_sessions_202602_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202602_user_id_session_date_idx1;


--
-- Name: step_sessions_202603_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202603_pkey;


--
-- Name: step_sessions_202603_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202603_user_id_session_date_idx;


--
-- Name: step_sessions_202603_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202603_user_id_session_date_idx1;


--
-- Name: step_sessions_202604_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202604_pkey;


--
-- Name: step_sessions_202604_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202604_user_id_session_date_idx;


--
-- Name: step_sessions_202604_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202604_user_id_session_date_idx1;


--
-- Name: step_sessions_202605_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202605_pkey;


--
-- Name: step_sessions_202605_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202605_user_id_session_date_idx;


--
-- Name: step_sessions_202605_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202605_user_id_session_date_idx1;


--
-- Name: step_sessions_202606_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202606_pkey;


--
-- Name: step_sessions_202606_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202606_user_id_session_date_idx;


--
-- Name: step_sessions_202606_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202606_user_id_session_date_idx1;


--
-- Name: step_sessions_202607_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202607_pkey;


--
-- Name: step_sessions_202607_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202607_user_id_session_date_idx;


--
-- Name: step_sessions_202607_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202607_user_id_session_date_idx1;


--
-- Name: step_sessions_202608_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202608_pkey;


--
-- Name: step_sessions_202608_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202608_user_id_session_date_idx;


--
-- Name: step_sessions_202608_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202608_user_id_session_date_idx1;


--
-- Name: step_sessions_202609_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202609_pkey;


--
-- Name: step_sessions_202609_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202609_user_id_session_date_idx;


--
-- Name: step_sessions_202609_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202609_user_id_session_date_idx1;


--
-- Name: step_sessions_202610_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202610_pkey;


--
-- Name: step_sessions_202610_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202610_user_id_session_date_idx;


--
-- Name: step_sessions_202610_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202610_user_id_session_date_idx1;


--
-- Name: step_sessions_202611_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202611_pkey;


--
-- Name: step_sessions_202611_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202611_user_id_session_date_idx;


--
-- Name: step_sessions_202611_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202611_user_id_session_date_idx1;


--
-- Name: step_sessions_202612_pkey; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.step_sessions_pkey ATTACH PARTITION public.step_sessions_202612_pkey;


--
-- Name: step_sessions_202612_user_id_session_date_idx; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_date ATTACH PARTITION public.step_sessions_202612_user_id_session_date_idx;


--
-- Name: step_sessions_202612_user_id_session_date_idx1; Type: INDEX ATTACH; Schema: public; Owner: pi
--

ALTER INDEX public.idx_step_sessions_user_range ATTACH PARTITION public.step_sessions_202612_user_id_session_date_idx1;


--
-- Name: challenges update_challenges_updated_at; Type: TRIGGER; Schema: public; Owner: pi
--

CREATE TRIGGER update_challenges_updated_at BEFORE UPDATE ON public.challenges FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: friendships update_friendships_updated_at; Type: TRIGGER; Schema: public; Owner: pi
--

CREATE TRIGGER update_friendships_updated_at BEFORE UPDATE ON public.friendships FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: leaderboard_cache update_leaderboard_cache_updated_at; Type: TRIGGER; Schema: public; Owner: pi
--

CREATE TRIGGER update_leaderboard_cache_updated_at BEFORE UPDATE ON public.leaderboard_cache FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: step_sessions update_step_sessions_updated_at; Type: TRIGGER; Schema: public; Owner: pi
--

CREATE TRIGGER update_step_sessions_updated_at BEFORE UPDATE ON public.step_sessions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: users update_users_updated_at; Type: TRIGGER; Schema: public; Owner: pi
--

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON public.users FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: challenge_participants challenge_participants_challenge_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.challenge_participants
    ADD CONSTRAINT challenge_participants_challenge_id_fkey FOREIGN KEY (challenge_id) REFERENCES public.challenges(id) ON DELETE CASCADE;


--
-- Name: challenge_participants challenge_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.challenge_participants
    ADD CONSTRAINT challenge_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: challenges challenges_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.challenges
    ADD CONSTRAINT challenges_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: daily_goals daily_goals_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.daily_goals
    ADD CONSTRAINT daily_goals_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: friendships friendships_addressee_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_addressee_id_fkey FOREIGN KEY (addressee_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: friendships friendships_requester_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_requester_id_fkey FOREIGN KEY (requester_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: leaderboard_cache leaderboard_cache_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.leaderboard_cache
    ADD CONSTRAINT leaderboard_cache_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: routes routes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.routes
    ADD CONSTRAINT routes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: step_sessions step_sessions_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE public.step_sessions
    ADD CONSTRAINT step_sessions_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_badges user_badges_badge_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.user_badges
    ADD CONSTRAINT user_badges_badge_id_fkey FOREIGN KEY (badge_id) REFERENCES public.badges(id);


--
-- Name: user_badges user_badges_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pi
--

ALTER TABLE ONLY public.user_badges
    ADD CONSTRAINT user_badges_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict PdskAaAgUZoJlbIyd5DF9qHLYXzHlwlEXBlpXJhftcNcQLbttSag9yR8arqcOXW

