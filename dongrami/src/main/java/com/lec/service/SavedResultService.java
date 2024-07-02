package com.lec.service;

import com.lec.entity.SavedResult;
import com.lec.repository.SavedResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavedResultService {

    @Autowired
    private SavedResultRepository savedResultRepository;

    public SavedResult saveResult(SavedResult savedResult) {
        return savedResultRepository.save(savedResult);
    }
}
