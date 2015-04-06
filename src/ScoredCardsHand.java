import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by yiranfei on 4/5/15.
 */
public class ScoredCardsHand extends CardsHand implements Writable {
  public Vector<ScoreItem> mScores;

  public ScoredCardsHand() {
  }

  public ScoredCardsHand(Card c1, Card c2, Card c3, Card c4, Card c5, Vector<ScoreItem> score) {
    mCards = new ArrayList<Card>();
    mCards.add(c1);
    mCards.add(c2);
    mCards.add(c3);
    mCards.add(c4);
    mCards.add(c5);

    mScores = score;
  }

  public ScoredCardsHand(CardsHand ch, Vector<ScoreItem> score) {
    mCards = new ArrayList<Card>();
    mCards = ch.mCards;
    mScores = score;
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    for (Card c : mCards) {
      dataOutput.writeInt(c.mSuit);
      dataOutput.writeInt(c.mRank);
    }
    dataOutput.writeInt(mScores.size());
    for (ScoreItem si : mScores) {
      si.write(dataOutput);
    }
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    mCards = new ArrayList<Card>();
    for (int i = 0; i < 5; i++) {
      int tmpSuit = dataInput.readInt();
      int tmpRand = dataInput.readInt();
      Card toAdd = new Card(tmpSuit, tmpRand);
      mCards.add(toAdd);
    }

    int k = dataInput.readInt();
    mScores = new Vector<ScoreItem>();
    for (int i = 0; i < k; i++) {
      ScoreItem si = new ScoreItem();
      si.readFields(dataInput);
      mScores.add(si);
    }
  }
}
