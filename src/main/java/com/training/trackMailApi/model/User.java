package com.training.trackMailApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "usersIdSeq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersIdSeq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "index", nullable = false)
    private String index;

    @Column(name = "address")
    private String address;

    @Column(name = "password", nullable = false, unique = true)
    @ToString.Exclude
    @JsonIgnore
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private Set<Mailing> mailings = new HashSet<>();
}
