package elasticsearchdecryptor.logs.elastic;

public class ElasticsearchReadException extends RuntimeException {
  public ElasticsearchReadException(Exception e) {
    super(e);
  }
}
