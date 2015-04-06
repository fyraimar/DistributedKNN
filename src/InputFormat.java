import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

/**
 * Created by yiranfei on 2/9/15.
 */
public class InputFormat extends FileInputFormat<LongWritable, CardsHand> {
  @Override
  public RecordReader<LongWritable, CardsHand> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
    return new Reader();
  }

  static class Reader extends RecordReader<LongWritable, CardsHand> {

    private long key;
    private CardsHand value;
    private final LineRecordReader r;

    public Reader() {
      r = new LineRecordReader();
    }

    @Override
    public void close() throws IOException {
      r.close();
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
      return new LongWritable(key);
    }

    @Override
    public CardsHand getCurrentValue() throws IOException,
            InterruptedException {
      return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
      return r.getProgress();
    }

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
      r.initialize(split, context);
      FileSplit fs = (FileSplit) split;
      key = 0;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
      if (r.nextKeyValue()) {
        Text line = r.getCurrentValue();

        key += 1;
        value = readLine(line.toString());
        return true;
      }
      return false;
    }
  }

  public static CardsHand readLine(String line) {
    String[] para = line.split(",");

    Card c1 = new Card(Integer.parseInt(para[0]), Integer.parseInt(para[1]));
    Card c2 = new Card(Integer.parseInt(para[2]), Integer.parseInt(para[3]));
    Card c3 = new Card(Integer.parseInt(para[4]), Integer.parseInt(para[5]));
    Card c4 = new Card(Integer.parseInt(para[6]), Integer.parseInt(para[7]));
    Card c5 = new Card(Integer.parseInt(para[8]), Integer.parseInt(para[9]));

    return new CardsHand(c1, c2, c3, c4, c5);
  }

  @Override
  protected boolean isSplitable(JobContext context, Path file) {
    CompressionCodec codec = new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
    return codec == null;
  }
}
