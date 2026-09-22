-- Ejecutar en Supabase SQL Editor.
create extension if not exists pgcrypto;

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  name text not null,
  career text not null,
  description text,
  photo_path text,
  photo_url text,
  role text not null default 'admin' check (role = 'admin'),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.weeks (
  id uuid primary key default gen_random_uuid(),
  week_number integer not null unique check (week_number between 1 and 16),
  title text not null,
  description text,
  created_at timestamptz not null default now()
);

create table if not exists public.resources (
  id uuid primary key default gen_random_uuid(),
  week_id uuid not null references public.weeks(id) on delete cascade,
  title text not null,
  description text,
  type text not null default 'document' check (type in ('document','activity','link')),
  file_path text,
  file_name text,
  external_url text,
  created_by uuid not null references auth.users(id),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  check (file_path is not null or external_url is not null)
);

insert into public.weeks (week_number,title,description)
select n, 'Semana '||n, 'Contenido académico de la semana '||n
from generate_series(1,16) n
on conflict (week_number) do nothing;

create or replace function public.is_admin()
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select exists(
    select 1 from public.profiles
    where id = auth.uid() and role = 'admin'
  );
$$;

alter table public.profiles enable row level security;
alter table public.weeks enable row level security;
alter table public.resources enable row level security;

drop policy if exists "public read profiles" on public.profiles;
create policy "public read profiles" on public.profiles for select using (true);
drop policy if exists "admin update own profile" on public.profiles;
create policy "admin update own profile" on public.profiles for update using (id=auth.uid() and public.is_admin()) with check (id=auth.uid() and role='admin');
drop policy if exists "admin insert own profile" on public.profiles;
create policy "admin insert own profile" on public.profiles for insert with check (id=auth.uid() and role='admin');

drop policy if exists "public read weeks" on public.weeks;
create policy "public read weeks" on public.weeks for select using (true);
drop policy if exists "admin write weeks" on public.weeks;
create policy "admin write weeks" on public.weeks for all using (public.is_admin()) with check (public.is_admin());

drop policy if exists "public read resources" on public.resources;
create policy "public read resources" on public.resources for select using (true);
drop policy if exists "admin insert resources" on public.resources;
create policy "admin insert resources" on public.resources for insert with check (public.is_admin() and created_by=auth.uid());
drop policy if exists "admin update resources" on public.resources;
create policy "admin update resources" on public.resources for update using (public.is_admin()) with check (public.is_admin());
drop policy if exists "admin delete resources" on public.resources;
create policy "admin delete resources" on public.resources for delete using (public.is_admin());

-- Storage: bucket público para que los visitantes puedan ver/descargar archivos.
insert into storage.buckets (id,name,public)
values ('repository-files','repository-files',true)
on conflict (id) do update set public=true;

drop policy if exists "public read repository files" on storage.objects;
create policy "public read repository files" on storage.objects
for select using (bucket_id='repository-files');

drop policy if exists "admin upload repository files" on storage.objects;
create policy "admin upload repository files" on storage.objects
for insert to authenticated
with check (bucket_id='repository-files' and public.is_admin());

drop policy if exists "admin update repository files" on storage.objects;
create policy "admin update repository files" on storage.objects
for update to authenticated
using (bucket_id='repository-files' and public.is_admin())
with check (bucket_id='repository-files' and public.is_admin());

drop policy if exists "admin delete repository files" on storage.objects;
create policy "admin delete repository files" on storage.objects
for delete to authenticated
using (bucket_id='repository-files' and public.is_admin());

-- IMPORTANTE: crea primero el usuario en Authentication > Users.
-- Después ejecuta, sustituyendo UUID_DEL_USUARIO:
-- insert into public.profiles (id,name,career,description,role)
-- values ('UUID_DEL_USUARIO','Nombre del estudiante','Ingeniería de Sistemas y Computación','Descripción del estudiante','admin');
