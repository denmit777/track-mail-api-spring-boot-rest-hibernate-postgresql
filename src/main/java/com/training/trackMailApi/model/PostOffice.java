package com.training.trackMailApi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_office")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostOffice implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "postOfficeIdSeq", sequenceName = "post_office_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postOfficeIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "index", nullable = false)
    private String index;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_office_id")
    @ToString.Exclude
    private List<Mailing> mailings = new ArrayList<>();
}
