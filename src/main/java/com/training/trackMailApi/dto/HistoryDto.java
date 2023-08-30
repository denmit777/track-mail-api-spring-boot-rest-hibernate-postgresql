package com.training.trackMailApi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.model.enums.MailType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private MailType mailType;

    private String recipientIndex;

    private String recipientAddress;

    private String recipientName;

    private MailStatus mailStatus;

    private String action;
}
