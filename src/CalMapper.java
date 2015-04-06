import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

/**
 * Created by yiranfei on 2/10/15.
 */
public class CalMapper extends Mapper<LongWritable, CardsHand, LongWritable, ScoredCardsHand> {
  private Vector<LabeledCardsHand> mTrainData;

  @Override
  protected void setup(Context context) throws IOException, IllegalArgumentException {
    FileSystem fs = FileSystem.get(context.getConfiguration());
    BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(context.getConfiguration().get("TRAINING_FILE")))));
    mTrainData = new Vector<LabeledCardsHand>();

    String line = br.readLine();
    int count = 0;
    while (line != null) {
      String[] para = line.split(",");
      if (para.length != 11)
        throw new IllegalArgumentException();

      Card c1 = new Card(Integer.parseInt(para[0]), Integer.parseInt(para[1]));
      Card c2 = new Card(Integer.parseInt(para[2]), Integer.parseInt(para[3]));
      Card c3 = new Card(Integer.parseInt(para[4]), Integer.parseInt(para[5]));
      Card c4 = new Card(Integer.parseInt(para[6]), Integer.parseInt(para[7]));
      Card c5 = new Card(Integer.parseInt(para[8]), Integer.parseInt(para[9]));
      int label = Integer.parseInt(para[10]);

      LabeledCardsHand tHand =  new LabeledCardsHand(c1, c2, c3, c4, c5, label);

      mTrainData.add(tHand);
      count++;
      line = br.readLine();
    }
  }

  @Override
  protected void map(LongWritable key, CardsHand is, Context context) throws IOException, InterruptedException {
    Vector<ScoreItem> vector = new Vector<ScoreItem>();

    List<Card> toCal = is.mCards;
    int[] testSuits = new int[4];
    double testSqrLength = 0.0;
    for (Card c : toCal) {
      testSuits[c.mSuit - 1] += c.mRank;
      testSqrLength += Math.pow(c.mRank, 2);
    }

    int count = 0;
    for (LabeledCardsHand iter : mTrainData) {
      int[] trainSuits = new int[4];
      double trainSqrLength = 0.0;
      double tmpScore = 0.0;

      for (Card c : iter.mCards) {
        trainSuits[c.mSuit - 1] += c.mRank;
        trainSqrLength += Math.pow(c.mRank, 2);
      }

      for (int i = 0; i < 4; i++) {
        tmpScore += trainSuits[i] * testSuits[i];
      }
      tmpScore /= Math.sqrt(trainSqrLength * testSqrLength);

      vector.add(new ScoreItem(count, iter.mLabel, tmpScore));
      count++;
    }

    ScoredCardsHand si = new ScoredCardsHand(is, vector);
    context.write(key, si);
  }
}
