package elasticsearchdecryptor.logs.driver;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DriverLogAggregationProvider {

    private static final int MAX_SIZE = 100;

    List<AggregationBuilder> getAggregations() {
        TermsAggregationBuilder byNationality = AggregationBuilders.terms("byNationality").field("nationality")

        ;

        TermsAggregationBuilder byTitles = AggregationBuilders.terms("byTitles").field("statistics.titles");

        TermsAggregationBuilder byActivity = AggregationBuilders.terms("byActivity").field("active");

        return List.of(byNationality, byTitles, byActivity);
    }

    private TopHitsAggregationBuilder topHitsSubAggregation() {
        return AggregationBuilders.topHits("byHits").size(MAX_SIZE);
    }

}
