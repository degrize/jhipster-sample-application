package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.FrereQuiInvite;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FrereQuiInvite} entity.
 */
public interface FrereQuiInviteSearchRepository
    extends ElasticsearchRepository<FrereQuiInvite, Long>, FrereQuiInviteSearchRepositoryInternal {}

interface FrereQuiInviteSearchRepositoryInternal {
    Page<FrereQuiInvite> search(String query, Pageable pageable);
}

class FrereQuiInviteSearchRepositoryInternalImpl implements FrereQuiInviteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    FrereQuiInviteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<FrereQuiInvite> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<FrereQuiInvite> hits = elasticsearchTemplate
            .search(nativeSearchQuery, FrereQuiInvite.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
