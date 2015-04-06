/**
 * Created by yiranfei on 2/9/15.
 */
import java.util.*;

public class CardsHand {
  public List<Card> mCards;

  public CardsHand() {
    mCards = null;
  }

  public CardsHand(Card c1, Card c2, Card c3, Card c4, Card c5) {
    mCards = new ArrayList<Card>();
    mCards.add(c1);
    mCards.add(c2);
    mCards.add(c3);
    mCards.add(c4);
    mCards.add(c5);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CardsHand))
      return false;
    if (obj == this)
      return true;

    CardsHand rhs = (CardsHand) obj;

    return (rhs.mCards.containsAll(this.mCards) && this.mCards.containsAll(rhs.mCards));
  }
}
