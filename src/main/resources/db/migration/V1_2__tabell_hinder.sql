CREATE SEQUENCE HELSEHINDER_SEQ;
CREATE SEQUENCE ANDREHINDER_SEQ;

CREATE TABLE HELSEHINDER (
  HELSEHINDER_ID  NUMBER,
  AKTOR_ID       NVARCHAR2(20) NOT NULL,
  SVAR NUMBER(1)  NOT NULL,
  DATO          TIMESTAMP(6) NOT NULL,

  CONSTRAINT "HELSEHINDER_ID_PK" PRIMARY KEY ("HELSEHINDER_ID")
);

CREATE TABLE ANDREHINDER (
  ANDREHINDER_ID  NUMBER,
  AKTOR_ID       NVARCHAR2(20) NOT NULL,
  SVAR NUMBER(1)  NOT NULL,
  DATO          TIMESTAMP(6) NOT NULL,

  CONSTRAINT "ANDREHINDER_ID_PK" PRIMARY KEY ("ANDREHINDER_ID")
);
