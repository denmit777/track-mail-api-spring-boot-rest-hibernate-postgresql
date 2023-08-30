package com.training.trackMailApi.converter.impl;

import com.training.trackMailApi.converter.HistoryConverter;
import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.model.History;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HistoryConverterImpl implements HistoryConverter {

    @Override
    public HistoryDto convertToHistoryDto(History history) {
        HistoryDto historyDto = new HistoryDto();

        historyDto.setDate(history.getDate());
        historyDto.setMailStatus(history.getMailStatus());
        historyDto.setRecipientAddress(history.getMailing().getUser().getAddress());
        historyDto.setRecipientIndex(history.getMailing().getUser().getIndex());
        historyDto.setRecipientName(history.getMailing().getUser().getName());
        historyDto.setMailType(history.getMailing().getMailType());
        historyDto.setAction(history.getAction());

        return historyDto;
    }
}
