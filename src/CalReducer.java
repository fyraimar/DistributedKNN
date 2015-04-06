import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by yiranfei on 2/10/15.
 */
public class CalReducer extends Reducer<LongWritable, ScoredCardsHand, LongWritable, Result> {
  @Override
  protected void reduce(LongWritable key, java.lang.Iterable<ScoredCardsHand> vec, Context context) throws IOException, InterruptedException {

    for (ScoredCardsHand item : vec) {
      Vector<ScoreItem> vector = item.mScores;

      Collections.sort(vector);
      Map<Integer, Double> scores = new HashMap<Integer, Double>();
      Map<Integer, Integer> counters = new HashMap<Integer, Integer>();

      int K = Integer.parseInt(context.getConfiguration().get("K"));

      int count = 0;
      for (ScoreItem score : vector) {
        if (count > K) {
          break;
        }

        if (null == scores.get(score.mLabel)) {
          scores.put(score.mLabel, score.mScore);
          counters.put(score.mLabel, 1);
        } else {
          scores.put(score.mLabel, scores.get(score.mLabel) + score.mScore);
          counters.put(score.mLabel, counters.get(score.mLabel) + 1);
        }
      }

      double tmpScore = -1.0;
      int tmpName = -1;
      Iterator<Map.Entry<Integer, Double>> iter = scores.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry<Integer, Double> entry = iter.next();
        //System.out.print(entry.getKey() + ":" + entry.getValue() / counters.get(entry.getKey()) + ";");

        if (entry.getValue() / counters.get(entry.getKey()) > tmpScore) {
          tmpScore = entry.getValue() / counters.get(entry.getKey());
          tmpName = entry.getKey();
        }
      }
      System.out.println("\n");

      String para = "";
      for (Card c : item.mCards) {
        para += c.mSuit + ",";
        para += c.mRank + ";";
      }
      String val = Integer.toString(tmpName);

      context.write(key, new Result(para, val));
    }
  }
}
