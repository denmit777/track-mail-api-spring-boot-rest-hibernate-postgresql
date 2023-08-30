package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.converter.PostOfficeConverter;
import com.training.trackMailApi.dao.PostOfficeDAO;
import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.PostOffice;
import com.training.trackMailApi.service.PostOfficeService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PostOfficeServiceImpl implements PostOfficeService {

    private static final Logger LOGGER = LogManager.getLogger(PostOfficeServiceImpl.class.getName());

    private final PostOfficeDAO postOfficeDAO;
    private final PostOfficeConverter postOfficeConverter;

    @Override
    @Transactional
    public PostOffice save(PostOfficeDto postOfficeDto) {
        PostOffice postOffice = postOfficeConverter.fromPostOfficeDto(postOfficeDto);

        postOfficeDAO.save(postOffice);

        LOGGER.info("New Post office : {}", postOffice);

        return postOffice;
    }

    @Override
    @Transactional
    public List<PostOfficeDto> getAll() {
        return postOfficeConverter.convertToListPostOfficeDto(postOfficeDAO.getAll());
    }

    @Override
    @Transactional
    public PostOfficeDto getById(Long postOfficeId) {
        return postOfficeConverter.convertToPostOfficeDto(postOfficeDAO.getById(postOfficeId));
    }
}
