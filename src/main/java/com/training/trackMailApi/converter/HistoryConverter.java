package com.training.trackMailApi.converter;

import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.model.History;

public interface HistoryConverter {

    HistoryDto convertToHistoryDto(History history);
}
