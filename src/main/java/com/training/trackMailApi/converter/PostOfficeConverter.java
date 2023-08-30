package com.training.trackMailApi.converter;

import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.PostOffice;

import java.util.List;

public interface PostOfficeConverter {

    PostOfficeDto convertToPostOfficeDto(PostOffice postOffice);

    PostOffice fromPostOfficeDto(PostOfficeDto postOfficeDto);

    List<PostOfficeDto> convertToListPostOfficeDto(List<PostOffice> postOffices);
}
