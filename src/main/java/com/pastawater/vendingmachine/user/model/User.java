package com.pastawater.vendingmachine.user.model;

import com.pastawater.vendingmachine.product.model.Product;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "vending_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String username;
    @Column(nullable = false)
    private String password;
    @ColumnDefault(value = "0")
    private Integer deposit;
    @Column(nullable = false)
    private Role role;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "sellerId", fetch = FetchType.LAZY)
    private transient Set<Product> productSet = new HashSet<>();
}
