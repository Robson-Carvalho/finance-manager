package com.oak.finance_manager.domain.transaction;

import com.oak.finance_manager.domain.account.Account;
import com.oak.finance_manager.domain.category.Category;
import com.oak.finance_manager.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "transaction")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
