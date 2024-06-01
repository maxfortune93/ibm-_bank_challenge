package com.ibm_bank_challenge.domain.customer;

import com.ibm_bank_challenge.domain.Transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(name="customers")
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Customer implements Serializable {

    private static final long serialVersionUID = -2622318059295526512L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, unique = true, length = 80)
    private String email;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String bankName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
