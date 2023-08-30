package com.training.trackMailApi.service;

import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.PostOffice;

import java.util.List;

public interface PostOfficeService {

    PostOffice save(PostOfficeDto postOfficeDto);

    List<PostOfficeDto> getAll();

    PostOfficeDto getById(Long id);
}
