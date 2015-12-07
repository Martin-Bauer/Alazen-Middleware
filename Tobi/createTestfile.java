import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class createTestfile {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Argument 1: No. of Intervals, Argument 2: Range of Intervals, Argument 3: Size of Intervals");
			System.exit(-1);
		}
		int n1 = Integer.parseInt(args[0]);
		int n2 = Integer.parseInt(args[1]);
		int n3 = Integer.parseInt(args[2]);
		File f = new File("./Testfile.txt");
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		int low = 0;
		int high = 0;
		String mutation = "";
		int randomChar = 0;
		int randomString = 0;
		char[] chars = {'A', 'C', 'G', 'T'};
		String[] sources = {"1000 Genomes Project", "The Cancer Genome Atlas", "dbSNP"};
		for (int i=0; i<n1; i++) {
			mutation = "";
			low  = (int) (Math.random() * n2);
            high = (int) (Math.random() * n3) + low;
            for (int j=0; j<high-low+1; j++) {
            	randomChar = (int) (Math.random() * 4);
            	mutation = mutation + chars[randomChar];
            }
            randomString = (int) (Math.random() * 3);
			fw.write(i+";"+mutation+";"+low+";"+high+";1337;"+sources[randomString]+";metadata1_"+i+";metadata2_"+i+";metadata3_"+i);
			fw.write("\n");
		}
		fw.close();
	}
}