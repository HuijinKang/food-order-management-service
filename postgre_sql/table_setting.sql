-- delete from p_order


ALTER TABLE p_order
DISABLE TRIGGER ALL;

ALTER TABLE p_order
ENABLE TRIGGER ALL;