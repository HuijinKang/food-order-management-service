

-- search list, total_price로 구분
SELECT address,total_price, deleted_at ,created_at
FROM public.p_order
where user_name = 'bones'
ORDER BY created_at desc 

-- select * from p_order
-- select address, total_price from p_order where user_name='bones' order by created_at desc
