import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class createTestfile {
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.out.println("Argument 1: No. of Intervals, Argument 2: Range of Intervals, Argument 3: Size of Intervals");
      System.exit(-1);
    }
    int i_N1 = Integer.parseInt(args[0]);
    int i_N2 = Integer.parseInt(args[1]);
    int i_N3 = Integer.parseInt(args[2]);
    File x_File = new File("./Testfile.txt");
    x_File.delete();
    x_File.createNewFile();
    FileWriter x_FW = new FileWriter(x_File);
    int i_Low = 0;
    int i_High = 0;
    String x_Mutation = "";
    int i_RandomChar = 0;
    int i_RandomString = 0;
    char[] c_Chars = {'A', 'C', 'G', 'T'};
    String[] x_Sources = {"1000 Genomes Project", "The Cancer Genome Atlas", "dbSNP"};
    for (int i=0; i<n1; i++) {
      x_Mutation = "";
      i_Low  = (int) (Math.random() * i_N2);
      i_High = (int) (Math.random() * i_N3) + i_Low;
      for (int j=0; j<i_High-i_Low+1; j++) {
        i_RandomChar = (int) (Math.random() * 4);
        x_Mutation = x_Mutation + c_Chars[i_RandomChar];
      }
      i_RandomString = (int) (Math.random() * 3);
      x_FW.write(i+";"+x_Mutation+";"+i_Low+";"+i_High+";1337;"+x_Sources[i_RandomString]+";metadata1_"+i+";metadata2_"+i+";metadata3_"+i);
      x_FW.write("\n");
    }
    x_FW.close();
  }
}