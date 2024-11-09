-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- uuid_generate_v4()


insert into p_order (
			id, 
			address, 
			comment, 
			created_at, 
			created_by, 
			deleted_at, 
			deleted_by,
			total_price,
			type,
			updated_at,
			updated_by,
			store_id,
			user_id,
			status
			)
values(
	uuid_generate_v4(),
	'a',
	'aa',
	NOW(),
	'me',
	null,
	null,
	floor(random() * 100000 + 1)::int,
	'DELIVERY',
	null,
	null,
	'05bf78d1-1286-4b39-8188-9fa83b7aa458',
	1,
	'WAIT'
);

