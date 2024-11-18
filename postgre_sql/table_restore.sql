
-- close
delete from p_order;

ALTER TABLE p_order
ENABLE TRIGGER ALL;

delete from p_payment;

ALTER TABLE p_payment
ENABLE TRIGGER ALL;

delete from p_user;

ALTER TABLE p_user
ENABLE TRIGGER ALL;
