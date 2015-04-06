import java.util.ArrayList;

/**
 * Created by yiranfei on 2/9/15.
 */
public class LabeledCardsHand extends CardsHand {
  public int mLabel;

  public LabeledCardsHand() {
  }

  public LabeledCardsHand(Card c1, Card c2, Card c3, Card c4, Card c5, int label) {
    mCards = new ArrayList<Card>();
    mCards.add(c1);
    mCards.add(c2);
    mCards.add(c3);
    mCards.add(c4);
    mCards.add(c5);

    mLabel = label;
  }
/*
  public LabeledCardsHand(ScoredCardsHand ir, String name) {
    mSL = ir.mSL;
    mSW = ir.mSW;
    mPL = ir.mPL;
    mPW = ir.mPW;

    mName = name;
  }
*/
}
