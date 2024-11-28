-- public.diary definition

-- Drop table

-- DROP TABLE public.diary;

CREATE TABLE public.diary (
                              id serial4 NOT NULL,
                              diary_date date NOT NULL,
                              diary_content text NULL,
                              CONSTRAINT diary_pkey PRIMARY KEY (id)
);

-- 사용자 테이블 추가
CREATE TABLE public."member" (
                                 email varchar(255) NOT NULL,
                                 "password" varchar(255) NOT NULL,
                                 "name" varchar(255) NOT NULL,
                                 CONSTRAINT member_pk PRIMARY KEY (email)
);
COMMENT ON TABLE public."member" IS '사용자';

-- Column comments

COMMENT ON COLUMN public."member".email IS '이메일주소';
COMMENT ON COLUMN public."member"."password" IS '패스워드';
COMMENT ON COLUMN public."member"."name" IS '사용자이름';


ALTER TABLE public.diary ADD email varchar(255) NULL;


ALTER TABLE public."member" ADD "role" varchar(255) NULL;
COMMENT ON COLUMN public."member"."role" IS '사용자 역할';

-- 2024년 11월 18일 수정
ALTER TABLE public."member" ADD provider varchar(50) NULL;
COMMENT ON COLUMN public."member".provider IS '외부 인증 제공자(google, apple, facebook 등)';
ALTER TABLE public."member" ALTER COLUMN "password" DROP NOT NULL;

-- 2024년 11월 19일 수정
-- 소셜 로그인 기능을 위한 회원정보 테이블
CREATE TABLE public.users (
	id serial NOT NULL,
	provider_user_id varchar(255) NOT NULL,
	provider varchar(50) NOT NULL,
	email varchar(255) NULL,
	name varchar(255) NULL,
	profile_image_url varchar(255) NULL,
	created_at timestamp with time zone DEFAULT now() NOT NULL,
	updated_at timestamp with time zone DEFAULT now() NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_pid_unique UNIQUE (provider_user_id)
);
COMMENT ON TABLE public.users IS '사용자';

-- Column comments

COMMENT ON COLUMN public.users.id IS '고유번호(자동증가)';
COMMENT ON COLUMN public.users.provider_user_id IS '정보제공자 고유 ID';
COMMENT ON COLUMN public.users.provider IS '정보제공자';
COMMENT ON COLUMN public.users.email IS '이메일 주소';
COMMENT ON COLUMN public.users.name IS '이름';
COMMENT ON COLUMN public.users.profile_image_url IS '프로파일 이미지 경로(URL)';
COMMENT ON COLUMN public.users.created_at IS '생성일시';
COMMENT ON COLUMN public.users.updated_at IS '최종수정일시';

-- 2024년 11월 20일 수정
-- member 테이블에 uid 컬럼 추가
ALTER TABLE public."member" ADD uid smallint NULL;
COMMENT ON COLUMN public."member".uid IS 'users 테이블의 id 컬럼값. 소셜 로그인에 의한 회원가입 정보';
