create table photos
(
    id         varchar(36)                              not null
        primary key,
    photo_url  varchar(200)                             not null comment '사진 URL',
    is_deleted tinyint(1)                               not null comment '삭제 여부',
    created_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '생성일시',
    updated_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '수정일시'
)
    comment '여행 사진 테이블';

create index photos_created_at_index
    on photos (created_at);

create table place
(
    id         varchar(36)                              not null
        primary key,
    place_name varchar(200)                             not null comment '여행 장소 명칭',
    is_deleted tinyint(1)                               not null comment '삭제 여부',
    created_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '생성일시',
    updated_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '수정일시'
)
    comment '여행 장소 테이블';

create index place_created_at_index
    on place (created_at);

create table reviews
(
    id         varchar(36)                              not null
        primary key,
    user_id    varchar(36)                              null comment '사용자 ID',
    contents   varchar(1000)                            not null comment '리뷰 내용',
    is_deleted tinyint(1)                               not null comment '삭제 여부',
    created_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '등록일시',
    updated_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '수정일시'
)
    comment '리뷰 테이블';

create table places_reviews_mapping
(
    id         varchar(36)                              not null
        primary key,
    place_id   varchar(36)                              not null comment '장소 ID',
    review_id  varchar(36)                              not null comment '리뷰 ID',
    is_deleted tinyint(1)                               null comment '삭제여부',
    created_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '생성일시',
    updated_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '수정일시',
    constraint places_reviews_mapping_place_id_fk
        foreign key (place_id) references place (id),
    constraint places_reviews_mapping_reviews_id_fk
        foreign key (review_id) references reviews (id)
)
    comment '여행 사진 리뷰 매핑 테이블';

create index places_reviews_mapping_created_at_index
    on places_reviews_mapping (created_at);

create index places_reviews_mapping_place_id_index
    on places_reviews_mapping (place_id);

create table points
(
    id          varchar(36)                              not null
        primary key,
    user_id     varchar(36)                              not null comment '유저 ID',
    point       tinyint                                  not null comment '포인트 점수',
    type        varchar(20)                              not null comment '포인트 입력 근거 타입',
    detail_type varchar(20)                              null comment '포인트 적립/회수 타입',
    place_id    varchar(36)                              not null comment '장소 ID',
    review_id   varchar(36)                              not null comment '리뷰 ID',
    created_at  datetime(3) default CURRENT_TIMESTAMP(3) null comment '생성일시',
    constraint points_place_id_fk
        foreign key (place_id) references place (id),
    constraint points_reviews_id_fk
        foreign key (review_id) references reviews (id)
)
    comment '포인트 테이블';

create index points_created_at_index
    on points (created_at);

create index points_place_id_index
    on points (place_id);

create index points_user_id_index
    on points (user_id);

create index reviews_created_at_index
    on reviews (created_at);

create table reviews_photots_mapping
(
    id         varchar(36)                              not null
        primary key,
    review_id  varchar(36)                              not null comment '리뷰 ID',
    photo_id   varchar(36)                              not null comment '사진 ID',
    is_deleted tinyint(1)                               null comment '삭제 여부',
    created_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '생성일시',
    updated_at datetime(3) default CURRENT_TIMESTAMP(3) null comment '수정일시',
    constraint reviews_photots_mapping_photos_id_fk
        foreign key (photo_id) references photos (id),
    constraint reviews_photots_mapping_reviews_id_fk
        foreign key (review_id) references reviews (id)
)
    comment '리뷰 사진 매핑 테이블';

create index reviews_photots_mapping_created_at_index
    on reviews_photots_mapping (created_at);

create index reviews_photots_mapping_review_id_id_index
    on reviews_photots_mapping (review_id);

