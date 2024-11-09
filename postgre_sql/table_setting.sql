

-- init
-- ALTER TABLE p_order
-- DISABLE TRIGGER ALL;


-- close
delete from p_order;

ALTER TABLE p_order
ENABLE TRIGGER ALL;
