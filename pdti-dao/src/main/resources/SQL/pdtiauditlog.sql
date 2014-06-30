CREATE TABLE pdtiauditlog (
    id bigserial primary key,
    sourcedomain varchar(20) default NULL,
    baseDn varchar(20) default NULL,
	pdRequestType varchar(20) default NULL,
	status varchar(20) NOT NULL,
    creationDate timestamp default NULL
);