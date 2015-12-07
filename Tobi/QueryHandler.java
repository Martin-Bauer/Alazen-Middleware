import java.util.*;
import java.io.File;
import java.io.IOException;

public class QueryHandler {

  protected LinkedList<String> x_QueryList;
  protected Set<IntervalST.Node> x_AnswerSet;
  public static IntervalST x_ST;

  public QueryHandler() {
    x_QueryList = new LinkedList<String>();
    x_ST = null;
    x_AnswerSet = new HashSet<IntervalST.Node>();
  }

  //Take a list of queries, put them in a queue and send them to the index, one after another.
  //Safe the answer from the index in a set to remove duplicates
  public void answerQuery(String x_Queries[]){
    for (int i=0;i<x_Queries.length;i++) {
      x_QueryList.add(x_Queries[i]);
    }
    boolean b_IsEmpty = false;
    while (b_IsEmpty==false) {
      try {
        String[] x_Parts = x_QueryList.removeFirst().split(",");
        x_AnswerSet.addAll(x_ST.searchAll(Integer.parseInt(x_Parts[0]), Integer.parseInt(x_Parts[1])));
      } catch (NoSuchElementException e) {
        b_IsEmpty = true;
      }
    }
    return;
  }

  //prints the answers without source and metadata
  //only useful for 
  public void printAnswer(){
    for (IntervalST.Node x_Node : x_AnswerSet) {
      System.out.println("[" + x_Node.i_Low + ", " + x_Node.i_High + "]: " + x_Node.x_Mutation + " (ID: " + x_Node.i_MutationID + ")");
    }
    return;
  }

  //tests 5 simple random queries . format: start, end
  public static void main(String[] args) throws IOException {
    QueryHandler x_QueryHandler=new QueryHandler();
    File x_File = new File("./Testfile.txt");
    x_ST = IntervalST.buildIndex(x_File);
    String x_Queries[] = new String[5];
    int i_Low = 0;
    int i_High = 0;
    for (int i=0; i<5; i++) {
      i_Low  = (int) (Math.random() * 200000000);         //adjust these values for other testfiles
      i_High = (int) (Math.random() * 200) + i_Low;
      x_Queries[i] = "" + i_Low + "," + i_High;
    }
    x_QueryHandler.answerQuery(x_Queries);
    x_QueryHandler.printAnswer();
  }
}