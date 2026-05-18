--
-- PostgreSQL database dump
--


-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

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

--
-- Name: dbo; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA dbo;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: air_details; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.air_details (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    price_adult bigint,
    price_child bigint,
    price_infant bigint,
    age_adult character varying(100),
    age_child character varying(100),
    age_infant character varying(100),
    flight_info text,
    included_items text,
    excluded_items text,
    guide_name character varying(100),
    guide_phone character varying(50),
    meeting_location text,
    meeting_time character varying(50),
    notes text,
    insurance_info text,
    emergency_contact text,
    passport_visa_info text,
    other_notices text,
    surcharge_info text,
    terms text,
    reservation_notes text,
    entry_regulations text,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: air_details_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.air_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: air_details_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.air_details_id_seq OWNED BY dbo.air_details.id;


--
-- Name: air_itineraries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.air_itineraries (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    day_number integer NOT NULL,
    title character varying(200) NOT NULL,
    description text,
    hotel_name character varying(200),
    shopping_center_name character varying(200),
    shopping_exchange_info text,
    shopping_info text,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: air_itineraries_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.air_itineraries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: air_itineraries_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.air_itineraries_id_seq OWNED BY dbo.air_itineraries.id;


--
-- Name: air_itinerary_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.air_itinerary_images (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    product_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    image_type character varying(20) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    schedule_id bigint
);


--
-- Name: air_itinerary_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.air_itinerary_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: air_itinerary_images_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.air_itinerary_images_id_seq OWNED BY dbo.air_itinerary_images.id;


--
-- Name: air_itinerary_schedules; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.air_itinerary_schedules (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    "time" character varying(10),
    description text NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: air_itinerary_schedules_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.air_itinerary_schedules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: air_itinerary_schedules_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.air_itinerary_schedules_id_seq OWNED BY dbo.air_itinerary_schedules.id;


--
-- Name: bookings; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.bookings (
    booking_id bigint NOT NULL,
    booking_number character varying(30) NOT NULL,
    user_id bigint NOT NULL,
    product_id bigint NOT NULL,
    name character varying(50) NOT NULL,
    phone character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    adult_count integer DEFAULT 0 NOT NULL,
    child_count integer DEFAULT 0 NOT NULL,
    infant_count integer DEFAULT 0 NOT NULL,
    desired_departure_at date,
    total_price bigint,
    request_memo text,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    admin_memo text,
    payment_status character varying(20) DEFAULT 'UNPAID'::character varying NOT NULL,
    payment_method character varying(30),
    paid_amount bigint,
    paid_at timestamp without time zone,
    pg_transaction_id character varying(100),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    admin_checked boolean DEFAULT false NOT NULL
);


--
-- Name: bookings_booking_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.bookings_booking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bookings_booking_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.bookings_booking_id_seq OWNED BY dbo.bookings.booking_id;


--
-- Name: categories; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.categories (
    category_id bigint NOT NULL,
    parent_id bigint,
    category_code character varying(50) NOT NULL,
    category_name character varying(100) NOT NULL,
    depth integer NOT NULL,
    sort_order integer,
    is_active character(1),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: categories_category_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.categories_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: categories_category_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.categories_category_id_seq OWNED BY dbo.categories.category_id;


--
-- Name: cruise_details; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.cruise_details (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    included_items text,
    excluded_items text,
    guide_name character varying(100),
    guide_phone character varying(30),
    meeting_location character varying(300),
    meeting_time character varying(100),
    notes text,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    insurance_info text,
    emergency_contact text,
    passport_visa_info text,
    other_notices text,
    age_adult character varying(50),
    age_child character varying(50),
    age_infant character varying(50),
    terms text,
    reservation_notes text,
    entry_regulations text,
    surcharge_info text
);


--
-- Name: cruise_details_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.cruise_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cruise_details_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.cruise_details_id_seq OWNED BY dbo.cruise_details.id;


--
-- Name: cruise_itineraries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.cruise_itineraries (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    day_number integer NOT NULL,
    title character varying(200) NOT NULL,
    description text,
    shopping_center_name character varying(200),
    shopping_exchange_info text,
    shopping_info text,
    hotel_name character varying(200),
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT now()
);


--
-- Name: cruise_itineraries_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.cruise_itineraries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cruise_itineraries_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.cruise_itineraries_id_seq OWNED BY dbo.cruise_itineraries.id;


--
-- Name: cruise_itinerary_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.cruise_itinerary_images (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    product_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    image_type character varying(10) NOT NULL,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT now(),
    schedule_id bigint,
    CONSTRAINT cruise_itinerary_images_image_type_check CHECK (((image_type)::text = ANY ((ARRAY['LOCATION'::character varying, 'HOTEL'::character varying, 'SCHEDULE'::character varying])::text[])))
);


--
-- Name: cruise_itinerary_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.cruise_itinerary_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cruise_itinerary_images_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.cruise_itinerary_images_id_seq OWNED BY dbo.cruise_itinerary_images.id;


--
-- Name: cruise_itinerary_schedules; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.cruise_itinerary_schedules (
    id bigint CONSTRAINT itinerary_schedules_id_not_null NOT NULL,
    itinerary_id bigint CONSTRAINT itinerary_schedules_itinerary_id_not_null NOT NULL,
    "time" character varying(10),
    description text,
    sort_order integer DEFAULT 0 CONSTRAINT itinerary_schedules_sort_order_not_null NOT NULL,
    created_at timestamp without time zone DEFAULT now() CONSTRAINT itinerary_schedules_created_at_not_null NOT NULL
);


--
-- Name: cruise_prices; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.cruise_prices (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    cabin_type character varying(50),
    price_adult bigint,
    price_child bigint,
    price_infant bigint,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT now()
);


--
-- Name: cruise_prices_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.cruise_prices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cruise_prices_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.cruise_prices_id_seq OWNED BY dbo.cruise_prices.id;


--
-- Name: domestic_details; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.domestic_details (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    price_adult bigint,
    price_child bigint,
    price_infant bigint,
    age_adult character varying(100),
    age_child character varying(100),
    age_infant character varying(100),
    included_items text,
    excluded_items text,
    guide_name character varying(100),
    guide_phone character varying(50),
    meeting_location text,
    meeting_time character varying(100),
    notes text,
    insurance_info text,
    emergency_contact text,
    other_notices text,
    surcharge_info text,
    terms text,
    reservation_notes text,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: domestic_details_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.domestic_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: domestic_details_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.domestic_details_id_seq OWNED BY dbo.domestic_details.id;


--
-- Name: domestic_itineraries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.domestic_itineraries (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    day_number integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    hotel_name character varying(255),
    shopping_center_name character varying(255),
    shopping_exchange_info text,
    shopping_info text,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: domestic_itineraries_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.domestic_itineraries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: domestic_itineraries_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.domestic_itineraries_id_seq OWNED BY dbo.domestic_itineraries.id;


--
-- Name: domestic_itinerary_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.domestic_itinerary_images (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    product_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    image_type character varying(20) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    schedule_id bigint,
    CONSTRAINT domestic_itinerary_images_image_type_check CHECK (((image_type)::text = ANY ((ARRAY['LOCATION'::character varying, 'HOTEL'::character varying, 'SCHEDULE'::character varying])::text[])))
);


--
-- Name: domestic_itinerary_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.domestic_itinerary_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: domestic_itinerary_images_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.domestic_itinerary_images_id_seq OWNED BY dbo.domestic_itinerary_images.id;


--
-- Name: domestic_itinerary_schedules; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.domestic_itinerary_schedules (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    "time" character varying(10),
    description text NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: domestic_itinerary_schedules_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.domestic_itinerary_schedules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: domestic_itinerary_schedules_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.domestic_itinerary_schedules_id_seq OWNED BY dbo.domestic_itinerary_schedules.id;


--
-- Name: email_verifications; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.email_verifications (
    id bigint NOT NULL,
    code character varying(10) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    verified boolean NOT NULL
);


--
-- Name: email_verifications_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.email_verifications ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.email_verifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: inquiries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.inquiries (
    inquiry_id bigint NOT NULL,
    product_id bigint,
    category_id bigint,
    category character varying(20) CONSTRAINT inquiries_inquiry_type_not_null NOT NULL,
    name character varying(50) NOT NULL,
    phone character varying(20) NOT NULL,
    email character varying(255) NOT NULL,
    organization_name character varying(100),
    headcount integer,
    desired_start_date date,
    desired_end_date date,
    budget_range character varying(100),
    content text NOT NULL,
    status character varying(20) DEFAULT 'NEW'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id bigint,
    title character varying(200) DEFAULT ''::character varying NOT NULL,
    answer text,
    CONSTRAINT ck_inquiries_status CHECK (((status)::text = ANY (ARRAY['NEW'::text, 'IN_PROGRESS'::text, 'COMPLETED'::text, 'CLOSED'::text]))),
    CONSTRAINT inquiries_category_check CHECK (((category)::text = ANY ((ARRAY['예약문의'::character varying, '상품문의'::character varying, '결제문의'::character varying, '기타'::character varying])::text[])))
);


--
-- Name: inquiries_inquiry_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.inquiries ALTER COLUMN inquiry_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.inquiries_inquiry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: itinerary_schedules_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.itinerary_schedules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: itinerary_schedules_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.itinerary_schedules_id_seq OWNED BY dbo.cruise_itinerary_schedules.id;


--
-- Name: mileage_history; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.mileage_history (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    booking_id bigint,
    type character varying(20) NOT NULL,
    points integer NOT NULL,
    description character varying(255),
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: mileage_history_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.mileage_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: mileage_history_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.mileage_history_id_seq OWNED BY dbo.mileage_history.id;


--
-- Name: password_reset_tokens; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.password_reset_tokens (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    token_hash character varying(255) NOT NULL,
    used boolean NOT NULL,
    user_id bigint
);


--
-- Name: password_reset_tokens_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.password_reset_tokens ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.password_reset_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: popular_keywords; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.popular_keywords (
    id bigint NOT NULL,
    keyword character varying(50) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    is_active character(1) DEFAULT 'Y'::bpchar NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_popular_keywords_is_active CHECK (((is_active)::text = ANY (ARRAY['Y'::text, 'N'::text])))
);


--
-- Name: popular_keywords_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.popular_keywords ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.popular_keywords_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: product_files; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.product_files (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    file_path character varying(500) NOT NULL,
    file_name character varying(255) NOT NULL,
    file_type character varying(20) DEFAULT 'ETC'::character varying NOT NULL,
    file_size bigint,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_product_files_type CHECK (((file_type)::text = ANY (ARRAY['PDF'::text, 'EXCEL'::text, 'WORD'::text, 'IMAGE'::text, 'ETC'::text])))
);


--
-- Name: product_files_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.product_files ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.product_files_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: product_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.product_images (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    image_type character varying(20) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_product_images_type CHECK (((image_type)::text = ANY (ARRAY['THUMBNAIL'::text, 'DETAIL'::text])))
);


--
-- Name: product_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.product_images ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.product_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: products; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.products (
    product_id bigint NOT NULL,
    category_id bigint NOT NULL,
    product_code character varying(50) NOT NULL,
    product_name character varying(200) NOT NULL,
    product_subname character varying(200),
    summary character varying(500),
    description text,
    thumbnail_url character varying(500),
    status character varying(20) DEFAULT 'DRAFT'::character varying NOT NULL,
    is_featured character(1) DEFAULT 'N'::bpchar NOT NULL,
    is_active character(1) DEFAULT 'Y'::bpchar NOT NULL,
    exposure_start_at timestamp without time zone,
    exposure_end_at timestamp without time zone,
    seo_title character varying(200),
    seo_description character varying(500),
    view_count integer DEFAULT 0 NOT NULL,
    created_by bigint NOT NULL,
    updated_by bigint,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    video_path character varying(500),
    travel_type character varying(20),
    min_people integer,
    max_people integer,
    price_per_person numeric(12,0),
    video_url character varying(500),
    departure_location character varying(200),
    departure_at timestamp without time zone,
    arrival_location character varying(200),
    arrival_at timestamp without time zone,
    hashtags character varying(500),
    transport_type character varying(30),
    has_shopping character(1) DEFAULT 'N'::bpchar,
    has_guide_fee character(1) DEFAULT 'N'::bpchar,
    has_escort character(1) DEFAULT 'N'::bpchar,
    has_optional_tour character(1) DEFAULT 'N'::bpchar,
    confirmed_count integer DEFAULT 0,
    reserved_count integer DEFAULT 0 NOT NULL,
    CONSTRAINT ck_products_is_active CHECK ((is_active = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
    CONSTRAINT ck_products_is_featured CHECK ((is_featured = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
    CONSTRAINT ck_products_status CHECK (((status)::text = ANY ((ARRAY['DRAFT'::character varying, 'PUBLISHED'::character varying, 'HIDDEN'::character varying, 'ENDED'::character varying])::text[]))),
    CONSTRAINT products_transport_type_check CHECK (((transport_type)::text = ANY ((ARRAY['CRUISE'::character varying, 'DOMESTIC_AIR'::character varying, 'INTERNATIONAL_AIR'::character varying, 'BUS'::character varying])::text[])))
);


--
-- Name: products_product_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.products_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: products_product_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.products_product_id_seq OWNED BY dbo.products.product_id;


--
-- Name: refresh_token; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.refresh_token (
    id bigint NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    revoked boolean NOT NULL,
    token_hash character varying(255) NOT NULL,
    user_id bigint
);


--
-- Name: refresh_token_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.refresh_token ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.refresh_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: review_comments; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.review_comments (
    id bigint NOT NULL,
    review_id bigint NOT NULL,
    user_id bigint NOT NULL,
    content text NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: review_comments_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.review_comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: review_comments_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.review_comments_id_seq OWNED BY dbo.review_comments.id;


--
-- Name: review_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.review_images (
    id bigint NOT NULL,
    review_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: review_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.review_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: review_images_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.review_images_id_seq OWNED BY dbo.review_images.id;


--
-- Name: reviews; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.reviews (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    user_id bigint NOT NULL,
    writer_type character varying(20) DEFAULT 'GENERAL'::character varying NOT NULL,
    rating smallint NOT NULL,
    content text NOT NULL,
    status character varying(20) DEFAULT 'PUBLISHED'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT reviews_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


--
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.reviews_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.reviews_id_seq OWNED BY dbo.reviews.id;


--
-- Name: school_trip_details; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.school_trip_details (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    transport_info text,
    price_adult bigint,
    price_child bigint,
    price_infant bigint,
    age_adult character varying(100),
    age_child character varying(100),
    age_infant character varying(100),
    included_items text,
    excluded_items text,
    guide_name character varying(100),
    guide_phone character varying(50),
    meeting_location text,
    meeting_time character varying(100),
    notes text,
    insurance_info text,
    emergency_contact text,
    other_notices text,
    surcharge_info text,
    terms text,
    reservation_notes text,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: school_trip_details_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.school_trip_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_trip_details_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.school_trip_details_id_seq OWNED BY dbo.school_trip_details.id;


--
-- Name: school_trip_inquiries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.school_trip_inquiries (
    inquiry_id bigint NOT NULL,
    user_id bigint,
    school_name character varying(100) NOT NULL,
    name character varying(50) NOT NULL,
    phone character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    student_count integer,
    teacher_count integer,
    desired_date date,
    destination character varying(200),
    content text NOT NULL,
    status character varying(20) DEFAULT 'NEW'::character varying NOT NULL,
    admin_memo text,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT school_trip_inquiries_status_check CHECK (((status)::text = ANY ((ARRAY['NEW'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CLOSED'::character varying])::text[])))
);


--
-- Name: school_trip_inquiries_inquiry_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.school_trip_inquiries_inquiry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_trip_inquiries_inquiry_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.school_trip_inquiries_inquiry_id_seq OWNED BY dbo.school_trip_inquiries.inquiry_id;


--
-- Name: school_trip_itineraries; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.school_trip_itineraries (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    day_number integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    hotel_name character varying(255),
    shopping_center_name character varying(255),
    shopping_exchange_info text,
    shopping_info text,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: school_trip_itineraries_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.school_trip_itineraries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_trip_itineraries_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.school_trip_itineraries_id_seq OWNED BY dbo.school_trip_itineraries.id;


--
-- Name: school_trip_itinerary_images; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.school_trip_itinerary_images (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    product_id bigint NOT NULL,
    image_path character varying(500) NOT NULL,
    image_type character varying(20) NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    schedule_id bigint,
    CONSTRAINT school_trip_itinerary_images_image_type_check CHECK (((image_type)::text = ANY ((ARRAY['LOCATION'::character varying, 'HOTEL'::character varying, 'SCHEDULE'::character varying])::text[])))
);


--
-- Name: school_trip_itinerary_images_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.school_trip_itinerary_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_trip_itinerary_images_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.school_trip_itinerary_images_id_seq OWNED BY dbo.school_trip_itinerary_images.id;


--
-- Name: school_trip_itinerary_schedules; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.school_trip_itinerary_schedules (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    "time" character varying(10),
    description text NOT NULL,
    sort_order integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: school_trip_itinerary_schedules_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

CREATE SEQUENCE dbo.school_trip_itinerary_schedules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: school_trip_itinerary_schedules_id_seq; Type: SEQUENCE OWNED BY; Schema: dbo; Owner: -
--

ALTER SEQUENCE dbo.school_trip_itinerary_schedules_id_seq OWNED BY dbo.school_trip_itinerary_schedules.id;


--
-- Name: search_keywords; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.search_keywords (
    keyword character varying(100) NOT NULL,
    search_count bigint DEFAULT 1 NOT NULL,
    last_searched_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: dbo; Owner: -
--

CREATE TABLE dbo.users (
    user_id bigint NOT NULL,
    birth date,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    login_id character varying(50) NOT NULL,
    name character varying(50),
    password character varying(255) NOT NULL,
    phone character varying(20),
    provider character varying(20) NOT NULL,
    provider_id character varying(255),
    role character varying(20) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    agreed_privacy_at timestamp(6) without time zone,
    agreed_terms_at timestamp(6) without time zone,
    agreed_third_party_at timestamp(6) without time zone,
    marketing_agreed boolean,
    marketing_agreed_at timestamp(6) without time zone,
    mileage_point integer DEFAULT 0 NOT NULL,
    CONSTRAINT users_provider_check CHECK (((provider)::text = ANY ((ARRAY['LOCAL'::character varying, 'KAKAO'::character varying, 'NAVER'::character varying, 'GOOGLE'::character varying])::text[]))),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);


--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: dbo; Owner: -
--

ALTER TABLE dbo.users ALTER COLUMN user_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME dbo.users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: air_details id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_details ALTER COLUMN id SET DEFAULT nextval('dbo.air_details_id_seq'::regclass);


--
-- Name: air_itineraries id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itineraries ALTER COLUMN id SET DEFAULT nextval('dbo.air_itineraries_id_seq'::regclass);


--
-- Name: air_itinerary_images id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_images ALTER COLUMN id SET DEFAULT nextval('dbo.air_itinerary_images_id_seq'::regclass);


--
-- Name: air_itinerary_schedules id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_schedules ALTER COLUMN id SET DEFAULT nextval('dbo.air_itinerary_schedules_id_seq'::regclass);


--
-- Name: bookings booking_id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.bookings ALTER COLUMN booking_id SET DEFAULT nextval('dbo.bookings_booking_id_seq'::regclass);


--
-- Name: categories category_id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.categories ALTER COLUMN category_id SET DEFAULT nextval('dbo.categories_category_id_seq'::regclass);


--
-- Name: cruise_details id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_details ALTER COLUMN id SET DEFAULT nextval('dbo.cruise_details_id_seq'::regclass);


--
-- Name: cruise_itineraries id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itineraries ALTER COLUMN id SET DEFAULT nextval('dbo.cruise_itineraries_id_seq'::regclass);


--
-- Name: cruise_itinerary_images id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_images ALTER COLUMN id SET DEFAULT nextval('dbo.cruise_itinerary_images_id_seq'::regclass);


--
-- Name: cruise_itinerary_schedules id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_schedules ALTER COLUMN id SET DEFAULT nextval('dbo.itinerary_schedules_id_seq'::regclass);


--
-- Name: cruise_prices id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_prices ALTER COLUMN id SET DEFAULT nextval('dbo.cruise_prices_id_seq'::regclass);


--
-- Name: domestic_details id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_details ALTER COLUMN id SET DEFAULT nextval('dbo.domestic_details_id_seq'::regclass);


--
-- Name: domestic_itineraries id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itineraries ALTER COLUMN id SET DEFAULT nextval('dbo.domestic_itineraries_id_seq'::regclass);


--
-- Name: domestic_itinerary_images id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_images ALTER COLUMN id SET DEFAULT nextval('dbo.domestic_itinerary_images_id_seq'::regclass);


--
-- Name: domestic_itinerary_schedules id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_schedules ALTER COLUMN id SET DEFAULT nextval('dbo.domestic_itinerary_schedules_id_seq'::regclass);


--
-- Name: mileage_history id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.mileage_history ALTER COLUMN id SET DEFAULT nextval('dbo.mileage_history_id_seq'::regclass);


--
-- Name: products product_id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products ALTER COLUMN product_id SET DEFAULT nextval('dbo.products_product_id_seq'::regclass);


--
-- Name: review_comments id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_comments ALTER COLUMN id SET DEFAULT nextval('dbo.review_comments_id_seq'::regclass);


--
-- Name: review_images id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_images ALTER COLUMN id SET DEFAULT nextval('dbo.review_images_id_seq'::regclass);


--
-- Name: reviews id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.reviews ALTER COLUMN id SET DEFAULT nextval('dbo.reviews_id_seq'::regclass);


--
-- Name: school_trip_details id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_details ALTER COLUMN id SET DEFAULT nextval('dbo.school_trip_details_id_seq'::regclass);


--
-- Name: school_trip_inquiries inquiry_id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_inquiries ALTER COLUMN inquiry_id SET DEFAULT nextval('dbo.school_trip_inquiries_inquiry_id_seq'::regclass);


--
-- Name: school_trip_itineraries id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itineraries ALTER COLUMN id SET DEFAULT nextval('dbo.school_trip_itineraries_id_seq'::regclass);


--
-- Name: school_trip_itinerary_images id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_images ALTER COLUMN id SET DEFAULT nextval('dbo.school_trip_itinerary_images_id_seq'::regclass);


--
-- Name: school_trip_itinerary_schedules id; Type: DEFAULT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_schedules ALTER COLUMN id SET DEFAULT nextval('dbo.school_trip_itinerary_schedules_id_seq'::regclass);


--
-- Name: air_details air_details_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_details
    ADD CONSTRAINT air_details_pkey PRIMARY KEY (id);


--
-- Name: air_details air_details_product_id_key; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_details
    ADD CONSTRAINT air_details_product_id_key UNIQUE (product_id);


--
-- Name: air_itineraries air_itineraries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itineraries
    ADD CONSTRAINT air_itineraries_pkey PRIMARY KEY (id);


--
-- Name: air_itinerary_images air_itinerary_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_images
    ADD CONSTRAINT air_itinerary_images_pkey PRIMARY KEY (id);


--
-- Name: air_itinerary_schedules air_itinerary_schedules_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_schedules
    ADD CONSTRAINT air_itinerary_schedules_pkey PRIMARY KEY (id);


--
-- Name: bookings bookings_booking_number_key; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.bookings
    ADD CONSTRAINT bookings_booking_number_key UNIQUE (booking_number);


--
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (booking_id);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);


--
-- Name: cruise_details cruise_details_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_details
    ADD CONSTRAINT cruise_details_pkey PRIMARY KEY (id);


--
-- Name: cruise_details cruise_details_product_id_key; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_details
    ADD CONSTRAINT cruise_details_product_id_key UNIQUE (product_id);


--
-- Name: cruise_itineraries cruise_itineraries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itineraries
    ADD CONSTRAINT cruise_itineraries_pkey PRIMARY KEY (id);


--
-- Name: cruise_itinerary_images cruise_itinerary_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_images
    ADD CONSTRAINT cruise_itinerary_images_pkey PRIMARY KEY (id);


--
-- Name: cruise_prices cruise_prices_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_prices
    ADD CONSTRAINT cruise_prices_pkey PRIMARY KEY (id);


--
-- Name: domestic_details domestic_details_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_details
    ADD CONSTRAINT domestic_details_pkey PRIMARY KEY (id);


--
-- Name: domestic_details domestic_details_product_id_key; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_details
    ADD CONSTRAINT domestic_details_product_id_key UNIQUE (product_id);


--
-- Name: domestic_itineraries domestic_itineraries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itineraries
    ADD CONSTRAINT domestic_itineraries_pkey PRIMARY KEY (id);


--
-- Name: domestic_itinerary_images domestic_itinerary_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_images
    ADD CONSTRAINT domestic_itinerary_images_pkey PRIMARY KEY (id);


--
-- Name: domestic_itinerary_schedules domestic_itinerary_schedules_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_schedules
    ADD CONSTRAINT domestic_itinerary_schedules_pkey PRIMARY KEY (id);


--
-- Name: email_verifications email_verifications_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.email_verifications
    ADD CONSTRAINT email_verifications_pkey PRIMARY KEY (id);


--
-- Name: inquiries inquiries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.inquiries
    ADD CONSTRAINT inquiries_pkey PRIMARY KEY (inquiry_id);


--
-- Name: cruise_itinerary_schedules itinerary_schedules_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_schedules
    ADD CONSTRAINT itinerary_schedules_pkey PRIMARY KEY (id);


--
-- Name: mileage_history mileage_history_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.mileage_history
    ADD CONSTRAINT mileage_history_pkey PRIMARY KEY (id);


--
-- Name: password_reset_tokens password_reset_tokens_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.password_reset_tokens
    ADD CONSTRAINT password_reset_tokens_pkey PRIMARY KEY (id);


--
-- Name: popular_keywords popular_keywords_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.popular_keywords
    ADD CONSTRAINT popular_keywords_pkey PRIMARY KEY (id);


--
-- Name: product_files product_files_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.product_files
    ADD CONSTRAINT product_files_pkey PRIMARY KEY (id);


--
-- Name: product_images product_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.product_images
    ADD CONSTRAINT product_images_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (product_id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: review_comments review_comments_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_comments
    ADD CONSTRAINT review_comments_pkey PRIMARY KEY (id);


--
-- Name: review_images review_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_images
    ADD CONSTRAINT review_images_pkey PRIMARY KEY (id);


--
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- Name: school_trip_details school_trip_details_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_details
    ADD CONSTRAINT school_trip_details_pkey PRIMARY KEY (id);


--
-- Name: school_trip_details school_trip_details_product_id_key; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_details
    ADD CONSTRAINT school_trip_details_product_id_key UNIQUE (product_id);


--
-- Name: school_trip_inquiries school_trip_inquiries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_inquiries
    ADD CONSTRAINT school_trip_inquiries_pkey PRIMARY KEY (inquiry_id);


--
-- Name: school_trip_itineraries school_trip_itineraries_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itineraries
    ADD CONSTRAINT school_trip_itineraries_pkey PRIMARY KEY (id);


--
-- Name: school_trip_itinerary_images school_trip_itinerary_images_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_images
    ADD CONSTRAINT school_trip_itinerary_images_pkey PRIMARY KEY (id);


--
-- Name: school_trip_itinerary_schedules school_trip_itinerary_schedules_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_schedules
    ADD CONSTRAINT school_trip_itinerary_schedules_pkey PRIMARY KEY (id);


--
-- Name: search_keywords search_keywords_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.search_keywords
    ADD CONSTRAINT search_keywords_pkey PRIMARY KEY (keyword);


--
-- Name: products uk_products_product_code; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products
    ADD CONSTRAINT uk_products_product_code UNIQUE (product_code);


--
-- Name: password_reset_tokens ukajre85ybxavf1tt4omkrs5p6g; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.password_reset_tokens
    ADD CONSTRAINT ukajre85ybxavf1tt4omkrs5p6g UNIQUE (token_hash);


--
-- Name: refresh_token ukkdj16cltjxdksuyiosdhliveg; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.refresh_token
    ADD CONSTRAINT ukkdj16cltjxdksuyiosdhliveg UNIQUE (token_hash);


--
-- Name: popular_keywords uq_popular_keywords_keyword; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.popular_keywords
    ADD CONSTRAINT uq_popular_keywords_keyword UNIQUE (keyword);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: idx_air_itineraries_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_air_itineraries_product_id ON dbo.air_itineraries USING btree (product_id);


--
-- Name: idx_air_itinerary_images_itinerary_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_air_itinerary_images_itinerary_id ON dbo.air_itinerary_images USING btree (itinerary_id);


--
-- Name: idx_air_itinerary_schedules_itinerary; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_air_itinerary_schedules_itinerary ON dbo.air_itinerary_schedules USING btree (itinerary_id);


--
-- Name: idx_categories_parent_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_categories_parent_id ON dbo.categories USING btree (parent_id);


--
-- Name: idx_cruise_itineraries_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_cruise_itineraries_product_id ON dbo.cruise_itineraries USING btree (product_id);


--
-- Name: idx_cruise_prices_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_cruise_prices_product_id ON dbo.cruise_prices USING btree (product_id);


--
-- Name: idx_inquiries_created_at; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_inquiries_created_at ON dbo.inquiries USING btree (created_at);


--
-- Name: idx_inquiries_inquiry_type; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_inquiries_inquiry_type ON dbo.inquiries USING btree (category);


--
-- Name: idx_inquiries_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_inquiries_product_id ON dbo.inquiries USING btree (product_id);


--
-- Name: idx_inquiries_status; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_inquiries_status ON dbo.inquiries USING btree (status);


--
-- Name: idx_itinerary_images_itinerary_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_itinerary_images_itinerary_id ON dbo.cruise_itinerary_images USING btree (itinerary_id);


--
-- Name: idx_itinerary_schedules_itinerary_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_itinerary_schedules_itinerary_id ON dbo.cruise_itinerary_schedules USING btree (itinerary_id);


--
-- Name: idx_product_files_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_product_files_product_id ON dbo.product_files USING btree (product_id);


--
-- Name: idx_product_images_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_product_images_product_id ON dbo.product_images USING btree (product_id);


--
-- Name: idx_product_images_type; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_product_images_type ON dbo.product_images USING btree (image_type);


--
-- Name: idx_products_category_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_products_category_id ON dbo.products USING btree (category_id);


--
-- Name: idx_products_created_at; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_products_created_at ON dbo.products USING btree (created_at);


--
-- Name: idx_products_created_by; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_products_created_by ON dbo.products USING btree (created_by);


--
-- Name: idx_products_is_featured; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_products_is_featured ON dbo.products USING btree (is_featured);


--
-- Name: idx_products_status; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_products_status ON dbo.products USING btree (status);


--
-- Name: idx_review_comments_review_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_review_comments_review_id ON dbo.review_comments USING btree (review_id);


--
-- Name: idx_review_images_review_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_review_images_review_id ON dbo.review_images USING btree (review_id);


--
-- Name: idx_reviews_product_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_reviews_product_id ON dbo.reviews USING btree (product_id);


--
-- Name: idx_reviews_status; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_reviews_status ON dbo.reviews USING btree (status);


--
-- Name: idx_reviews_user_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE INDEX idx_reviews_user_id ON dbo.reviews USING btree (user_id);


--
-- Name: uk_categories_code_sub; Type: INDEX; Schema: dbo; Owner: -
--

CREATE UNIQUE INDEX uk_categories_code_sub ON dbo.categories USING btree (parent_id, category_code) WHERE (parent_id IS NOT NULL);


--
-- Name: uk_categories_code_top; Type: INDEX; Schema: dbo; Owner: -
--

CREATE UNIQUE INDEX uk_categories_code_top ON dbo.categories USING btree (category_code) WHERE (parent_id IS NULL);


--
-- Name: uq_product_images_thumbnail; Type: INDEX; Schema: dbo; Owner: -
--

CREATE UNIQUE INDEX uq_product_images_thumbnail ON dbo.product_images USING btree (product_id) WHERE ((image_type)::text = 'THUMBNAIL'::text);


--
-- Name: uq_users_email; Type: INDEX; Schema: dbo; Owner: -
--

CREATE UNIQUE INDEX uq_users_email ON dbo.users USING btree (email);


--
-- Name: uq_users_login_id; Type: INDEX; Schema: dbo; Owner: -
--

CREATE UNIQUE INDEX uq_users_login_id ON dbo.users USING btree (login_id);


--
-- Name: air_details air_details_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_details
    ADD CONSTRAINT air_details_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: air_itineraries air_itineraries_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itineraries
    ADD CONSTRAINT air_itineraries_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: air_itinerary_images air_itinerary_images_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_images
    ADD CONSTRAINT air_itinerary_images_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.air_itineraries(id) ON DELETE CASCADE;


--
-- Name: air_itinerary_images air_itinerary_images_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_images
    ADD CONSTRAINT air_itinerary_images_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: air_itinerary_images air_itinerary_images_schedule_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_images
    ADD CONSTRAINT air_itinerary_images_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES dbo.air_itinerary_schedules(id) ON DELETE CASCADE;


--
-- Name: air_itinerary_schedules air_itinerary_schedules_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.air_itinerary_schedules
    ADD CONSTRAINT air_itinerary_schedules_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.air_itineraries(id) ON DELETE CASCADE;


--
-- Name: bookings bookings_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.bookings
    ADD CONSTRAINT bookings_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id);


--
-- Name: bookings bookings_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.bookings
    ADD CONSTRAINT bookings_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: cruise_details cruise_details_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_details
    ADD CONSTRAINT cruise_details_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: cruise_itineraries cruise_itineraries_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itineraries
    ADD CONSTRAINT cruise_itineraries_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: cruise_itinerary_images cruise_itinerary_images_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_images
    ADD CONSTRAINT cruise_itinerary_images_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.cruise_itineraries(id) ON DELETE CASCADE;


--
-- Name: cruise_itinerary_images cruise_itinerary_images_schedule_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_images
    ADD CONSTRAINT cruise_itinerary_images_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES dbo.cruise_itinerary_schedules(id) ON DELETE CASCADE;


--
-- Name: cruise_prices cruise_prices_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_prices
    ADD CONSTRAINT cruise_prices_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: domestic_details domestic_details_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_details
    ADD CONSTRAINT domestic_details_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: domestic_itineraries domestic_itineraries_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itineraries
    ADD CONSTRAINT domestic_itineraries_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: domestic_itinerary_images domestic_itinerary_images_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_images
    ADD CONSTRAINT domestic_itinerary_images_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.domestic_itineraries(id) ON DELETE CASCADE;


--
-- Name: domestic_itinerary_images domestic_itinerary_images_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_images
    ADD CONSTRAINT domestic_itinerary_images_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: domestic_itinerary_images domestic_itinerary_images_schedule_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_images
    ADD CONSTRAINT domestic_itinerary_images_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES dbo.domestic_itinerary_schedules(id) ON DELETE CASCADE;


--
-- Name: domestic_itinerary_schedules domestic_itinerary_schedules_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.domestic_itinerary_schedules
    ADD CONSTRAINT domestic_itinerary_schedules_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.domestic_itineraries(id) ON DELETE CASCADE;


--
-- Name: categories fk_categories_parent; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.categories
    ADD CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES dbo.categories(category_id) ON DELETE SET NULL;


--
-- Name: inquiries fk_inquiries_category; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.inquiries
    ADD CONSTRAINT fk_inquiries_category FOREIGN KEY (category_id) REFERENCES dbo.categories(category_id) ON UPDATE RESTRICT ON DELETE SET NULL;


--
-- Name: inquiries fk_inquiries_product; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.inquiries
    ADD CONSTRAINT fk_inquiries_product FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON UPDATE RESTRICT ON DELETE SET NULL;


--
-- Name: product_files fk_product_files_product; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.product_files
    ADD CONSTRAINT fk_product_files_product FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- Name: product_images fk_product_images_product; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.product_images
    ADD CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- Name: products fk_products_category; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products
    ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES dbo.categories(category_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: products fk_products_created_by; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products
    ADD CONSTRAINT fk_products_created_by FOREIGN KEY (created_by) REFERENCES dbo.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: products fk_products_updated_by; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.products
    ADD CONSTRAINT fk_products_updated_by FOREIGN KEY (updated_by) REFERENCES dbo.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: refresh_token fkjtx87i0jvq2svedphegvdwcuy; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.refresh_token
    ADD CONSTRAINT fkjtx87i0jvq2svedphegvdwcuy FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: password_reset_tokens fkk3ndxg5xp6v7wd4gjyusp15gq; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.password_reset_tokens
    ADD CONSTRAINT fkk3ndxg5xp6v7wd4gjyusp15gq FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: inquiries inquiries_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.inquiries
    ADD CONSTRAINT inquiries_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id) ON DELETE SET NULL;


--
-- Name: cruise_itinerary_schedules itinerary_schedules_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.cruise_itinerary_schedules
    ADD CONSTRAINT itinerary_schedules_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.cruise_itineraries(id) ON DELETE CASCADE;


--
-- Name: mileage_history mileage_history_booking_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.mileage_history
    ADD CONSTRAINT mileage_history_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES dbo.bookings(booking_id);


--
-- Name: mileage_history mileage_history_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.mileage_history
    ADD CONSTRAINT mileage_history_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: review_comments review_comments_review_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_comments
    ADD CONSTRAINT review_comments_review_id_fkey FOREIGN KEY (review_id) REFERENCES dbo.reviews(id) ON DELETE CASCADE;


--
-- Name: review_comments review_comments_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_comments
    ADD CONSTRAINT review_comments_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: review_images review_images_review_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.review_images
    ADD CONSTRAINT review_images_review_id_fkey FOREIGN KEY (review_id) REFERENCES dbo.reviews(id) ON DELETE CASCADE;


--
-- Name: reviews reviews_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.reviews
    ADD CONSTRAINT reviews_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id);


--
-- Name: reviews reviews_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.reviews
    ADD CONSTRAINT reviews_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id);


--
-- Name: school_trip_details school_trip_details_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_details
    ADD CONSTRAINT school_trip_details_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: school_trip_inquiries school_trip_inquiries_user_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_inquiries
    ADD CONSTRAINT school_trip_inquiries_user_id_fkey FOREIGN KEY (user_id) REFERENCES dbo.users(user_id) ON DELETE SET NULL;


--
-- Name: school_trip_itineraries school_trip_itineraries_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itineraries
    ADD CONSTRAINT school_trip_itineraries_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: school_trip_itinerary_images school_trip_itinerary_images_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_images
    ADD CONSTRAINT school_trip_itinerary_images_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.school_trip_itineraries(id) ON DELETE CASCADE;


--
-- Name: school_trip_itinerary_images school_trip_itinerary_images_product_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_images
    ADD CONSTRAINT school_trip_itinerary_images_product_id_fkey FOREIGN KEY (product_id) REFERENCES dbo.products(product_id) ON DELETE CASCADE;


--
-- Name: school_trip_itinerary_images school_trip_itinerary_images_schedule_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_images
    ADD CONSTRAINT school_trip_itinerary_images_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES dbo.school_trip_itinerary_schedules(id) ON DELETE CASCADE;


--
-- Name: school_trip_itinerary_schedules school_trip_itinerary_schedules_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: dbo; Owner: -
--

ALTER TABLE ONLY dbo.school_trip_itinerary_schedules
    ADD CONSTRAINT school_trip_itinerary_schedules_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES dbo.school_trip_itineraries(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--


