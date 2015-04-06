import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.mockito.asm.Label;


public class OutputFormat extends TextOutputFormat<LongWritable, Result> {

  protected static class Writer extends RecordWriter<LongWritable, Result> {

    private LineRecordWriter<Text, Text> w;

    public Writer(DataOutputStream dos, String codec) {
      w = new LineRecordWriter<Text, Text>(dos, codec);
    }

    public Writer(DataOutputStream dos) {
      w = new LineRecordWriter<Text, Text>(dos);
    }

    public synchronized void write(LongWritable key, Result value)
            throws IOException {
      w.write(new Text(value.para), new Text(value.label));
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException,
            InterruptedException {
      w.close(context);
    }
  }

  @Override
  public RecordWriter<LongWritable, Result> getRecordWriter(
          TaskAttemptContext job) throws IOException, InterruptedException {
    Configuration conf = job.getConfiguration();

    String keyValueSeparator = conf.get(
            "mapred.textoutputformat.separator", "\t");
    CompressionCodec codec = null;
    String extension = "";

    Path file = getDefaultWorkFile(job, extension);
    FileSystem fs = file.getFileSystem(conf);

    FSDataOutputStream fileOut = fs.create(file, false);
    return new Writer(fileOut, keyValueSeparator);
  }
}