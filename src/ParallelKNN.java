import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by yiranfei on 2/9/15.
 */
public class ParallelKNN {
  public ParallelKNN() {
  }

  public static void main(String[] argv) throws Exception {
      Configuration conf = new Configuration();
      conf.set("K", argv[3]);
      conf.set("TRAINING_FILE", argv[0]);
      Job job = new Job(conf);
      job.setJarByClass(ParallelKNN.class);
      job.setMapperClass(CalMapper.class);
      job.setReducerClass(CalReducer.class);

      job.setMapOutputKeyClass(LongWritable.class);
      job.setMapOutputValueClass(ScoredCardsHand.class);

      job.setOutputKeyClass(LongWritable.class);
      job.setOutputValueClass(ScoredCardsHand.class);

      job.setInputFormatClass(InputFormat.class);
      job.setOutputFormatClass(OutputFormat.class);

      FileInputFormat.addInputPath(job, new Path(argv[1]));
      FileOutputFormat.setOutputPath(job, new Path(argv[2]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
