CREATE TABLE `company` (
  `id` bigint(20) NOT NULL,
  `cnpj` varchar(255) NOT NULL,
  `update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `worker` (
  `id` bigint(20) NOT NULL,
  `cpf` varchar(255) NOT NULL,
  `update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `qtd_hours_lunch` float DEFAULT NULL,
  `qtd_hours_day` float DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `hour_cost` decimal(19,2) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `punch` (
  `id` bigint(20) NOT NULL,
  `data` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `locale` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `worker_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `worker`
--
ALTER TABLE `worker`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4cm1kg523jlopyexjbmi6y54j` (`company_id`);

--
-- Indexes for table `punch`
--
ALTER TABLE `punch`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK46i4k5vl8wah7feutye9kbpi4` (`worker_id`);

--
-- AUTO_INCREMENT for table `company`
--
ALTER TABLE `company`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `worker`
--
ALTER TABLE `worker`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `punch`
--
ALTER TABLE `punch`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `worker`
--
ALTER TABLE `worker`
  ADD CONSTRAINT `FK4cm1kg523jlopyexjbmi6y54j` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`);

--
-- Constraints for table `punch`
--
ALTER TABLE `punch`
  ADD CONSTRAINT `FK46i4k5vl8wah7feutye9kbpi4` FOREIGN KEY (`worker_id`) REFERENCES `worker` (`id`);