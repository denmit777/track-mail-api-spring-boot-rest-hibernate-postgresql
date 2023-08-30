package com.training.trackMailApi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.model.enums.MailType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mailing")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Mailing implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @Id
    @SequenceGenerator(name = "mailingIdSeq", sequenceName = "mailing_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mailingIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mail_type")
    @Enumerated(EnumType.STRING)
    private MailType mailType;

    @Column(name = "mail_status")
    @Enumerated(EnumType.STRING)
    private MailStatus mailStatus;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_office_id")
    @ToString.Exclude
    @JsonIgnore
    private PostOffice postOffice;
}
