package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.ImageCulte;
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
 * Spring Data Elasticsearch repository for the {@link ImageCulte} entity.
 */
public interface ImageCulteSearchRepository extends ElasticsearchRepository<ImageCulte, Long>, ImageCulteSearchRepositoryInternal {}

interface ImageCulteSearchRepositoryInternal {
    Page<ImageCulte> search(String query, Pageable pageable);
}

class ImageCulteSearchRepositoryInternalImpl implements ImageCulteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ImageCulteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ImageCulte> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ImageCulte> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ImageCulte.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
