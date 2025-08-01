package com.oak.finance_manager.repositories;

import com.oak.finance_manager.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}