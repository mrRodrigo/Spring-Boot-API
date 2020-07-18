  
INSERT INTO `company` (`id`, `cnpj`, `update_date`, `create_date`, `name`) 
VALUES (NULL, '82198127000121', CURRENT_DATE(), CURRENT_DATE(), 'Kazale IT');

INSERT INTO `worker` (`id`, `cpf`, `update_date`, `create_date`, `email`, `name`, 
`role`, `qtd_hours_lunch`, `qtd_hours_day`, `password`, `hour_cost`, `company_id`) 
VALUES (NULL, '16248890935', CURRENT_DATE(), CURRENT_DATE(), 'admin@kazale.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL, 
'$2a$06$xIvBeNRfS65L1N17I7JzgefzxEuLAL0Xk0wFAgIkoNqu9WD6rmp4m', NULL, 
(SELECT `id` FROM `company` WHERE `cnpj` = '82198127000121'));