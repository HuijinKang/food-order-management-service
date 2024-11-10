CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- uuid_generate_v4()

do $$
begin
	for i in 1..100 loop
		insert into p_payment (
					id, 
					payed_price, 
					receipt, 
					created_at, 
					created_by, 
					deleted_at, 
					deleted_by,
					status,
					updated_at,
					updated_by,
					order_id,
					user_id
					)
		values(
			uuid_generate_v4(),
			1000*i::int,
			'receipt',
			NOW(),
			'me',
			null,
			null,
			'CANCEL_PG_DENIED',
			null,
			null,
			uuid_generate_v4(),
			1
		);
	end loop;
end $$;

