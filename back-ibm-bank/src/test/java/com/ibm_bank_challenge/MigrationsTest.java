package com.ibm_bank_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Runs the real Flyway migrations (V1 schema + V2 seed) and lets Hibernate validate the result. */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:migrations;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=validate"
})
class MigrationsTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void seedCreatesCustomersAndTransactions() {
        assertEquals(6, count("customers"));
        assertEquals(12, count("transactions"));
    }

    @Test
    void seededBalancesMatchTheirTransactions() {
        Integer mismatches = jdbc.queryForObject("""
                SELECT COUNT(*) FROM customers c
                WHERE c.balance <> COALESCE((SELECT SUM(amount) FROM transactions WHERE receiver_id = c.id), 0)
                                 - COALESCE((SELECT SUM(amount) FROM transactions WHERE sender_id = c.id), 0)
                """, Integer.class);
        assertEquals(0, mismatches);
    }

    @Test
    void totalMoneyInAccountsEqualsDepositsMinusWithdrawals() {
        BigDecimal balances = jdbc.queryForObject("SELECT SUM(balance) FROM customers", BigDecimal.class);
        BigDecimal net = jdbc.queryForObject("""
                SELECT SUM(CASE transaction_type WHEN 'DEPOSIT' THEN amount WHEN 'WITHDRAWAL' THEN -amount ELSE 0 END)
                FROM transactions
                """, BigDecimal.class);
        assertEquals(0, balances.compareTo(net));
    }

    private int count(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
    }
}
