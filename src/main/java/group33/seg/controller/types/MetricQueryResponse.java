package group33.seg.controller.types;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Pair;

//FIXME Cannot assume that the answer is going to be Integer
public class MetricQueryResponse {
  private MetricQuery query;
  private Future<List<Pair<String, Integer>>> result;

  public MetricQueryResponse(MetricQuery query, Future<List<Pair<String, Integer>>> result) {
    this.query = query;
    this.result = result;
  }

  public MetricQuery getQuery() {
    return query;
  }

  public List<Pair<String, Integer>> getResult() {
    try {
      return result.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return null;
    }
  }
}
