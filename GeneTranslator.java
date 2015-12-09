import org.apache.commons.collections4.trie.*;
import java.util.*;

public class GeneTranslator{
	static PatriciaTrie<String> dictionary;

	public GeneTranslator(){
		//TreeMap map=new TreeMap();
		dictionary=new PatriciaTrie<String>();
	}

	public String[] translateToIntervall(String name){
		String intervall=new String();
		intervall=dictionary.get(name);
		String[] answers;
		answers=intervall.split(" ",2);
		return answers;
	}

	public void addGene(String name, int begin, int end){
		String coordinates=begin+" "+end;
		dictionary.put(name, coordinates);
		return;
	}

	public String[] completeGeneName(String prefix){
		SortedMap genenames=dictionary.prefixMap(prefix);
		Set temp_genenames=genenames.keySet();
		String[] answers = Arrays.copyOf(temp_genenames.toArray(), temp_genenames.toArray().length, String[].class);
		//String[] answers=(temp_genenames.toArray());
		return answers;

	}

	/*public static void main(String[] args) {
		GeneTranslator genetranslator=new GeneTranslator();
		genetranslator.addGene("Test1",10,20);
		genetranslator.addGene("Test2",20,30);			
		genetranslator.addGene("Test3",20,30);	
		genetranslator.addGene("Test4",20,30);	
		genetranslator.addGene("Test5",20,30);

		String[] intervall_answers;
		intervall_answers=genetranslator.translateToIntervall("Test1");
		System.out.println("coordinates for gene Test1: start:"+intervall_answers[0]+" end:"+intervall_answers[1]);

		String[] prefix_answers;
		prefix_answers=genetranslator.completeGeneName("Te");
		for(int i=0;i<prefix_answers.length;i++){
			System.out.println("Prefix found: "+prefix_answers[i]);
		}
	}
	*/
}
