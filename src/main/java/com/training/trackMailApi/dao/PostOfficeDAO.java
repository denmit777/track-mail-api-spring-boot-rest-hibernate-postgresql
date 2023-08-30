package com.training.trackMailApi.dao;

import com.training.trackMailApi.model.PostOffice;

import java.util.List;

public interface PostOfficeDAO {

    void save(PostOffice postOffice);

    List<PostOffice> getAll();

    PostOffice getById(Long id);

    void update(PostOffice postOffice);
}
