import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by yiranfei on 2/9/15.
 */
public class ScoreItem implements Comparable<ScoreItem>, Writable {
  public int mId;
  public int mLabel;
  public double mScore;

  public ScoreItem() {
  }

  public ScoreItem(int id, int name, double score) {
    this.mId = id;
    this.mLabel = name;
    this.mScore = score;
  }

  @Override
  public int compareTo(ScoreItem othItem) {
    if (this.mScore <= othItem.mScore)
      return -1;

    return 1;
  }

  @Override
  public void write(DataOutput d) throws IOException {
    d.writeInt(mId);
    d.writeInt(mLabel);
    d.writeDouble(mScore);
  }

  @Override
  public void readFields(DataInput di) throws IOException {
    mId = di.readInt();
    mLabel = di.readInt();
    mScore = di.readDouble();
  }
}
