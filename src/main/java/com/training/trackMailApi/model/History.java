package com.training.trackMailApi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.trackMailApi.model.enums.MailStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class History implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "historyIdSeq", sequenceName = "history_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historyIdSeq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "action")
    private String action;

    @ManyToOne
    @JoinColumn(name = "mailing_id")
    @ToString.Exclude
    @JsonIgnore
    private Mailing mailing;

    @Column(name = "mail_status")
    @Enumerated(EnumType.STRING)
    private MailStatus mailStatus;
}
