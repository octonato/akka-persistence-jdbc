DROP TABLE IF EXISTS public.journal;

CREATE TABLE IF NOT EXISTS public.journal (
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  message TEXT NOT NULL,
  created BIGINT NOT NULL,
  manifest VARCHAR(255) not null,
  deleted BOOL not null,
  writer_uuid VARCHAR(255) not null,
  tags VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY(persistence_id, sequence_number)
);

DROP TABLE IF EXISTS public.deleted_to;

CREATE TABLE IF NOT EXISTS public.deleted_to (
  persistence_id VARCHAR(255) NOT NULL,
  deleted_to BIGINT NOT NULL
);

DROP TABLE IF EXISTS public.snapshot;

CREATE TABLE IF NOT EXISTS public.snapshot (
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  created BIGINT NOT NULL,
  snapshot TEXT NOT NULL,
  PRIMARY KEY(persistence_id, sequence_number)
);