-- ============================================================
-- Muscle Max — Supabase schema + RLS
-- ============================================================
-- Run once in the Supabase SQL editor. All tables are keyed on
-- auth.uid(), so every policy pattern is "owner-only".
--
-- Column names match the @Serializable Kotlin models when serialized
-- with JsonNamingStrategy.SnakeCase (configured in SupabaseModule).
-- JSONB is used for nested structures (workouts.exercises,
-- routines.exercises, users.settings) — this avoids a relational
-- split for data the client always reads/writes as a whole blob.
-- ============================================================

-- ---------------- USERS ----------------
create table if not exists public.users (
    id              uuid primary key references auth.users(id) on delete cascade,
    display_name    text        not null default '',
    email           text        not null default '',
    photo_url       text,
    height_cm       int         not null default 175,
    settings        jsonb       not null default '{}'::jsonb
);

alter table public.users enable row level security;

drop policy if exists "users_select_own" on public.users;
create policy "users_select_own" on public.users
    for select using (auth.uid() = id);

drop policy if exists "users_insert_own" on public.users;
create policy "users_insert_own" on public.users
    for insert with check (auth.uid() = id);

drop policy if exists "users_update_own" on public.users;
create policy "users_update_own" on public.users
    for update using (auth.uid() = id);

-- ---------------- ROUTINES ----------------
create table if not exists public.routines (
    id              uuid        not null default gen_random_uuid(),
    user_id         uuid        not null references auth.users(id) on delete cascade,
    name            text        not null default '',
    day_of_week     text,                            -- "MON".."SUN"
    exercises       jsonb       not null default '[]'::jsonb,
    primary key (user_id, id)
);

create index if not exists routines_user_name_idx
    on public.routines (user_id, name);

create index if not exists routines_user_day_idx
    on public.routines (user_id, day_of_week);

alter table public.routines enable row level security;

drop policy if exists "routines_owner_all" on public.routines;
create policy "routines_owner_all" on public.routines
    for all using (auth.uid() = user_id) with check (auth.uid() = user_id);

-- ---------------- WORKOUTS ----------------
create table if not exists public.workouts (
    id                  uuid        not null default gen_random_uuid(),
    user_id             uuid        not null references auth.users(id) on delete cascade,
    date                timestamptz not null default now(),
    routine_id          uuid,
    routine_name        text        not null default '',
    duration_minutes    int         not null default 0,
    heavy_multiplier    double precision not null default 1.0,
    notes               text        not null default '',
    calories_burned     int         not null default 0,
    total_volume_kg     double precision not null default 0,
    exercises           jsonb       not null default '[]'::jsonb,
    primary key (user_id, id)
);

create index if not exists workouts_user_date_idx
    on public.workouts (user_id, date desc);

alter table public.workouts enable row level security;

drop policy if exists "workouts_owner_all" on public.workouts;
create policy "workouts_owner_all" on public.workouts
    for all using (auth.uid() = user_id) with check (auth.uid() = user_id);

-- ---------------- BODYWEIGHT LOG ----------------
-- id is YYYY-MM-DD so there's one row per local calendar day per user.
create table if not exists public.bodyweight_log (
    id          text        not null,
    user_id     uuid        not null references auth.users(id) on delete cascade,
    date        timestamptz not null default now(),
    weight_kg   double precision not null,
    primary key (user_id, id)
);

create index if not exists bodyweight_user_date_idx
    on public.bodyweight_log (user_id, date desc);

alter table public.bodyweight_log enable row level security;

drop policy if exists "bodyweight_owner_all" on public.bodyweight_log;
create policy "bodyweight_owner_all" on public.bodyweight_log
    for all using (auth.uid() = user_id) with check (auth.uid() = user_id);

-- ---------------- PRS ----------------
create table if not exists public.prs (
    user_id                         uuid        not null references auth.users(id) on delete cascade,
    exercise_id                     text        not null,
    max_weight_kg                   double precision not null default 0,
    max_weight_date                 timestamptz,
    max_weight_workout_id           uuid,
    estimated_one_rep_max_kg        double precision not null default 0,
    estimated_one_rep_max_date      timestamptz,
    estimated_one_rep_max_workout_id uuid,
    max_volume_session_kg           double precision not null default 0,
    max_volume_date                 timestamptz,
    max_volume_workout_id           uuid,
    primary key (user_id, exercise_id)
);

create index if not exists prs_user_maxweightdate_idx
    on public.prs (user_id, max_weight_date desc nulls last);

alter table public.prs enable row level security;

drop policy if exists "prs_owner_all" on public.prs;
create policy "prs_owner_all" on public.prs
    for all using (auth.uid() = user_id) with check (auth.uid() = user_id);
