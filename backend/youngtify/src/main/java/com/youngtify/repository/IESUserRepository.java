package com.youngtify.repository;

import com.youngtify.model.ESUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IESUserRepository extends ElasticsearchRepository<ESUser, String> {
}
