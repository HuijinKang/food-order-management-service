

begin;
update p_payment 
set receipt = 'aaadsf'
where id = '054d0dfe-ce8b-4097-8bf5-aee2f0f58c22';
commit;

SELECT id, order_id, receipt FROM public.p_payment
ORDER BY id ASC 
