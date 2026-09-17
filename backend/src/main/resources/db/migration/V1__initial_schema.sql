CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE SEQUENCE request_no_seq START 1;
CREATE SEQUENCE project_code_seq START 1;
CREATE SEQUENCE activity_no_seq START 1;

CREATE TABLE org_unit (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), code varchar(40) NOT NULL UNIQUE, name varchar(200) NOT NULL,
  type varchar(30) NOT NULL, parent_id uuid REFERENCES org_unit(id), active boolean NOT NULL DEFAULT true,
  version bigint NOT NULL DEFAULT 0
);
CREATE TABLE org_unit_closure (
  ancestor_id uuid NOT NULL REFERENCES org_unit(id), descendant_id uuid NOT NULL REFERENCES org_unit(id), depth int NOT NULL,
  PRIMARY KEY (ancestor_id, descendant_id)
);
CREATE TABLE app_user (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), username varchar(100) NOT NULL UNIQUE, full_name varchar(200) NOT NULL,
  email varchar(200), unit_id uuid NOT NULL REFERENCES org_unit(id), active boolean NOT NULL DEFAULT true,
  roles text[] NOT NULL DEFAULT '{}', version bigint NOT NULL DEFAULT 0
);
CREATE TABLE personnel_history (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), user_id uuid NOT NULL REFERENCES app_user(id), unit_id uuid NOT NULL REFERENCES org_unit(id),
  title varchar(200), starts_at date NOT NULL, ends_at date
);
CREATE TABLE catalog_item (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), category varchar(40) NOT NULL, code varchar(50) NOT NULL,
  name varchar(160) NOT NULL, description text, sort_order int NOT NULL DEFAULT 0, active boolean NOT NULL DEFAULT true,
  UNIQUE(category, code)
);
CREATE TABLE district (id uuid PRIMARY KEY DEFAULT gen_random_uuid(), code varchar(20) UNIQUE NOT NULL, name varchar(100) NOT NULL);
CREATE TABLE neighborhood (id uuid PRIMARY KEY DEFAULT gen_random_uuid(), district_id uuid NOT NULL REFERENCES district(id), code varchar(20) UNIQUE NOT NULL, name varchar(120) NOT NULL);

CREATE TABLE city_request (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), request_no varchar(30) NOT NULL UNIQUE, title varchar(250) NOT NULL, description text NOT NULL,
  request_type varchar(50) NOT NULL, channel varchar(50) NOT NULL, priority varchar(30) NOT NULL,
  applicant_name varchar(160), applicant_contact varchar(200), responsible_unit_id uuid NOT NULL REFERENCES org_unit(id),
  assignee_id uuid REFERENCES app_user(id), district_id uuid NOT NULL REFERENCES district(id), neighborhood_id uuid NOT NULL REFERENCES neighborhood(id),
  address varchar(500), geometry geometry(Geometry,4326) NOT NULL, external_reference varchar(100), status varchar(40) NOT NULL,
  created_by uuid NOT NULL REFERENCES app_user(id), updated_by uuid NOT NULL REFERENCES app_user(id), created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(), deleted_at timestamptz, version bigint NOT NULL DEFAULT 0
);
CREATE INDEX idx_request_geometry ON city_request USING gist(geometry);
CREATE INDEX idx_request_unit_status ON city_request(responsible_unit_id, status) WHERE deleted_at IS NULL;
CREATE INDEX idx_request_title_trgm ON city_request USING gin(title gin_trgm_ops);
CREATE TABLE request_transition (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), request_id uuid NOT NULL REFERENCES city_request(id), from_status varchar(40), to_status varchar(40) NOT NULL,
  note text, changed_by uuid NOT NULL REFERENCES app_user(id), changed_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE request_assignment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), request_id uuid NOT NULL REFERENCES city_request(id), unit_id uuid NOT NULL REFERENCES org_unit(id),
  assignee_id uuid REFERENCES app_user(id), assigned_by uuid NOT NULL REFERENCES app_user(id), assigned_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE request_note (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), request_id uuid NOT NULL REFERENCES city_request(id), body text NOT NULL,
  author_id uuid NOT NULL REFERENCES app_user(id), created_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE request_duplicate (
  request_id uuid NOT NULL REFERENCES city_request(id), master_request_id uuid NOT NULL REFERENCES city_request(id), linked_by uuid NOT NULL REFERENCES app_user(id),
  linked_at timestamptz NOT NULL DEFAULT now(), PRIMARY KEY(request_id, master_request_id), CHECK(request_id <> master_request_id)
);

CREATE TABLE inspection (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), request_id uuid NOT NULL REFERENCES city_request(id), responsible_user_id uuid REFERENCES app_user(id),
  team_name varchar(160), planned_at timestamptz, occurred_at timestamptz, technical_description text, measurements text,
  estimated_cost numeric(18,2), geometry geometry(Geometry,4326), result varchar(50), photo_count int NOT NULL DEFAULT 0,
  status varchar(30) NOT NULL, created_at timestamptz NOT NULL DEFAULT now(), updated_at timestamptz NOT NULL DEFAULT now(), version bigint NOT NULL DEFAULT 0
);
CREATE INDEX idx_inspection_geometry ON inspection USING gist(geometry);
CREATE TABLE project_candidate (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), project_code varchar(30) NOT NULL UNIQUE, name varchar(250) NOT NULL, project_type varchar(50) NOT NULL,
  description text, responsible_unit_id uuid NOT NULL REFERENCES org_unit(id), responsible_user_id uuid REFERENCES app_user(id), priority varchar(30),
  estimated_start date, estimated_end date, estimated_cost numeric(18,2), geometry geometry(Geometry,4326), status varchar(30) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(), version bigint NOT NULL DEFAULT 0
);
CREATE INDEX idx_project_geometry ON project_candidate USING gist(geometry);
CREATE TABLE project_request (project_id uuid NOT NULL REFERENCES project_candidate(id), request_id uuid NOT NULL REFERENCES city_request(id), PRIMARY KEY(project_id,request_id));
CREATE TABLE activity (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), activity_no varchar(30) NOT NULL UNIQUE, activity_type varchar(50) NOT NULL, title varchar(250) NOT NULL,
  description text, request_id uuid REFERENCES city_request(id), responsible_unit_id uuid NOT NULL REFERENCES org_unit(id), assignee_id uuid REFERENCES app_user(id),
  geometry geometry(Geometry,4326), planned_start date, planned_end date, actual_start date, actual_end date, materials text,
  result_description text, result_photo_count int NOT NULL DEFAULT 0, status varchar(30) NOT NULL, created_at timestamptz NOT NULL DEFAULT now(), version bigint NOT NULL DEFAULT 0
);
CREATE INDEX idx_activity_geometry ON activity USING gist(geometry);
CREATE TABLE approval_instance (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), subject_type varchar(30) NOT NULL, subject_id uuid NOT NULL, level int NOT NULL CHECK(level BETWEEN 1 AND 3),
  status varchar(40) NOT NULL, requested_by uuid NOT NULL REFERENCES app_user(id), current_approver_id uuid REFERENCES app_user(id),
  created_at timestamptz NOT NULL DEFAULT now(), completed_at timestamptz
);
CREATE TABLE approval_decision (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), approval_id uuid NOT NULL REFERENCES approval_instance(id), decision varchar(40) NOT NULL,
  decided_by uuid NOT NULL REFERENCES app_user(id), explanation text NOT NULL, previous_status varchar(40) NOT NULL, next_status varchar(40) NOT NULL,
  decided_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE document_metadata (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), subject_type varchar(30) NOT NULL, subject_id uuid NOT NULL, original_name varchar(255) NOT NULL,
  object_key varchar(500) NOT NULL UNIQUE, mime_type varchar(150) NOT NULL, size_bytes bigint NOT NULL, sha256 varchar(64) NOT NULL,
  document_type varchar(50) NOT NULL, description text, uploaded_by uuid NOT NULL REFERENCES app_user(id), uploaded_at timestamptz NOT NULL DEFAULT now(),
  photo_location geometry(Point,4326), captured_at timestamptz, version_no int NOT NULL DEFAULT 1, previous_version_id uuid REFERENCES document_metadata(id)
);
CREATE INDEX idx_document_location ON document_metadata USING gist(photo_location);
CREATE TABLE notification (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), user_id uuid NOT NULL REFERENCES app_user(id), type varchar(50) NOT NULL, title varchar(200) NOT NULL,
  body text NOT NULL, link varchar(500), read_at timestamptz, created_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE audit_log (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(), actor_id uuid REFERENCES app_user(id), action varchar(50) NOT NULL, entity_type varchar(50) NOT NULL,
  entity_id uuid, before_value jsonb, after_value jsonb, ip_address inet, trace_id varchar(64), occurred_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_audit_entity ON audit_log(entity_type, entity_id, occurred_at DESC);
