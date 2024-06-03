package com.ibm_bank_challenge.domain.Transaction;

import com.ibm_bank_challenge.domain.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Transaction implements Serializable {

    private static final long serialVersionUID  = -8364830285756522002L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = true)
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = true)
    private Customer receiver;

}

