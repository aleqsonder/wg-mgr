CREATE TABLE vpn_users (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE contact_types (
	id BIGSERIAL PRIMARY KEY,
	typename VARCHAR(32) NOT NULL UNIQUE,
	notes TEXT
);

CREATE TABLE vpn_user_contacts (
	vpn_user_id BIGINT REFERENCES vpn_users(id),
	contact_type_id BIGINT REFERENCES contact_types(id),
	content TEXT NOT NULL,
	PRIMARY KEY (vpn_user_id, contact_type_id)
);

CREATE TABLE vpn_user_contacts (
                                   vpn_user_id BIGINT REFERENCES vpn_users(id),
                                   contact_type_id VARCHAR,
                                   content TEXT NOT NULL,
                                   PRIMARY KEY (vpn_user_id, contact_type_id)
);

CREATE TABLE vpn_servers (
	id BIGSERIAL PRIMARY KEY,
	server_name VARCHAR(32) NOT NULL UNIQUE,
	ipv4 VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE devices (
	id BIGSERIAL PRIMARY KEY,
	vpn_server_id BIGINT REFERENCES vpn_servers(id) NOT NULL,
	vpn_user_id BIGINT REFERENCES vpn_users(id) NOT NULL,
	device_name VARCHAR(64) NOT NULL,
	notes TEXT,
	ipv4 VARCHAR(15) NOT NULL,
	private_key TEXT NOT NULL, /* TODO: Use VARCHAR */
	public_key TEXT NOT NULL, /* TODO: Use VARCHAR */
	preshared_key TEXT NOT NULL, /* TODO: Use VARCHAR */
	UNIQUE (vpn_user_id, device_name),
	UNIQUE (vpn_server_id, ipv4)
	/* TODO: Add unique modifiers for keys */
);
