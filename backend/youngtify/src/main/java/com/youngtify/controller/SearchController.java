package com.youngtify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngtify.config.ElasticsearchConfig;
import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.entity.UserRequestEntity;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.CustomUser;
import com.youngtify.model.ESUser;
import com.youngtify.model.ESUserRes;
import com.youngtify.model.UserRequest;
import com.youngtify.repository.IESUserRepository;
import com.youngtify.repository.UserRequestRepository;
import com.youngtify.specification.UserSpecification;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    @Autowired
    private IESUserRepository userRepository;

    @Autowired
    private UserRequestRepository userRequestRepository;

    @GetMapping(value = "")
    public ResponseEntity<?> search(@RequestParam String key) {
        try {
            RestHighLevelClient client = elasticsearchConfig.client();
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("fullname", key)
                    .fuzziness(Fuzziness.AUTO)
                    .maxExpansions(70);
            String[] includeFields = new String[]{"id", "fullname", "phonenumber", "email", "avatar"};
            String[] excludeFields = new String[]{"innerObject.*"};
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(queryBuilder)
                    .fetchSource(includeFields, excludeFields);
            SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<ESUserRes> responseFields = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<UUID> ids = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                String id = hit.getId();
                if (!customUser.getUserId().toString().equals(id)) {
                    ids.add(UUID.fromString(id));
                    responseFields.add(mapper.convertValue(hit.getSourceAsMap(), ESUserRes.class));
                }
            }
            if (ids.size() > 0) {
                Specification<UserRequestEntity> spec1 = (root, xq, cb) -> root.get("senderId").in(ids);
                Specification<UserRequestEntity> spec2 = (root, xq, cb) -> root.get("receiverId").in(ids);
                Specification<UserRequestEntity> spec3 = (root, xq, cb) -> cb.equal(root.get("senderId"), customUser.getUserId());
                Specification<UserRequestEntity> spec4 = (root, xq, cb) -> cb.equal(root.get("receiverId"), customUser.getUserId());
                Specification<UserRequestEntity> specification1 = spec1.and(spec4);
                Specification<UserRequestEntity> specification2 = spec2.and(spec3);
                Specification<UserRequestEntity> specification = specification1.or(specification2);
//                Specification specification1 = Specification.where(UserSpecification
//                        .in("senderId", ids).and(UserSpecification.equal("receiverId", customUser.getUserId()))
//                        .or(UserSpecification.in("receiverId", ids)).and(UserSpecification.equal("senderId", customUser.getUserId())));
                List<UserRequestEntity> entities = userRequestRepository.findAll(specification);
                for (UserRequestEntity item : entities) {
                    for (ESUserRes esUserRes : responseFields) {
                        if (item.getSenderId().toString().equals(esUserRes.getId())) {
                            if (item.getStatus() == 1)
                                esUserRes.setStatus(3);
                            else
                                esUserRes.setStatus(item.getStatus());
                            break;
                        } else if (item.getReceiverId().toString().equals(esUserRes.getId())) {
                            esUserRes.setStatus(item.getStatus());
                            break;
                        }
                    }
                }
            }
            return ResponseEntity.ok(responseFields);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addToES(@RequestBody ESUser user) {
        return ResponseEntity.ok(userRepository.save(user));
    }
}
