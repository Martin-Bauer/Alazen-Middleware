import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class Genomindex {
	public class Mutation {
		String data;

		public Mutation(String string) {
			data = string;
		}
		
		public String toString() { return data; }
	}

	static HashMap<String, TreeMap<Integer, Mutation>> index = new HashMap<String, TreeMap<Integer, Mutation>>();

	public Genomindex(Iterable<String> data) {
		for (String line : data) {
			String[] words = line.split(" ", 3);
			getMap(index, words[0]).put(Integer.parseInt(words[1]), new Mutation(words[2]));
		}
	}

	private TreeMap<Integer, Mutation> getMap(HashMap<String, TreeMap<Integer, Mutation>> index, String source) {
		if (!index.containsKey(source))
			index.put(source, new TreeMap<Integer, Mutation>());
		return index.get(source);
	}

	public Collection<Mutation> search(int min, int max, Iterable<String> sources) {
		Collection<Mutation> result = new ArrayList<Mutation>();
		for (String source : sources)
			result.addAll(index.get(source).subMap(min, true, max, true).values());
		return result;
	}
	
  /**
   * Some example command line arguments:
   * Genomtestdaten.txt 50 70               []
   * Genomtestdaten.txt 1 6 Quelle1         [Mut1, Mut2, Mut3]
   * Genomtestdaten.txt 2 4 Quelle1 Quelle2 [Mut1, Mut2, Mut3, Mut5]
   */
	public static void main(String[] args) throws IOException {
		Genomindex index = new Genomindex(Files.readAllLines(FileSystems.getDefault().getPath(args[0]), StandardCharsets.UTF_8));
		System.out.println(index.search(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Arrays.asList(args).subList(3, args.length)));
	}
}
