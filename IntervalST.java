/******************************************************************************
 *  Compilation:  javac IntervalST.java
 *  Execution:    java IntervalST
 *  
 *  Interval search tree implemented using a randomized BST.
 *
 *  Duplicate policy:  if an interval is inserted that already
 *                     exists, the new value overwrite the old one
 * 
 ******************************************************************************/

import java.util.LinkedList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class IntervalST {

  private Node x_Root;   // root of the BST

  // BST helper node data type
  public class Node {
    int i_Low;                    // start of interval
    int i_High;                   // end of interval
    int i_MutationID;             // ID of the mutation
    int i_RefID;                  // ID of the ref-genome
    short s_Source;               // number of source, can be translated into name
    String x_Mutation;            // mutated genome sequence
    String x_Metadata1;           // storing some metadata
    String x_Metadata2;           // storing some metadata
    String x_Metadata3;           // storing some metadata
    Node x_Left, x_Right;         // left and right subtrees
    int i_N;                      // size of subtree rooted at this node
    int i_Max;                    // max endpoint in subtree rooted at this node

    Node(int i_Low, int i_High, int i_MutationID, int i_RefID, short s_Source, String x_Mutation, String x_Metadata1, String x_Metadata2, String x_Metadata3) {
      this.i_Low            = i_Low;
      this.i_High           = i_High;
      this.i_MutationID     = i_MutationID;
      this.i_RefID          = i_RefID;
      this.s_Source         = s_Source;
      this.x_Mutation       = x_Mutation;
      this.x_Metadata1      = x_Metadata1;
      this.x_Metadata2      = x_Metadata2;
      this.x_Metadata3      = x_Metadata3;
      this.i_N              = 1;
      this.i_Max            = i_High;
    }
  }

  /***************************************************************************
  *  BST search
  ***************************************************************************/

  public int compareTo(int i_Low, int i_High, int i_Low2, int i_High2) {
    if (i_Low  < i_Low2) {
      return -1;
    } else if (i_Low  > i_Low2) {
      return +1;
    } else if (i_High < i_High2) {
      return -1;
    } else if (i_High > i_High2) {
      return +1;
    } else {
      return  0;
    }
  }

  public boolean contains(int i_Low, int i_High) {
    return (get(i_Low, i_High) != -1);
  }

  // return value associated with the given key
  // if no such value, return null
  public int get(int i_Low, int i_High) {
    return get(x_Root, i_Low, i_High);
  }

  private int get(Node x_Node, int i_Low, int i_High) {
    if (x_Node == null) {
      return -1;
    }
    int i_Cmp = compareTo(i_Low, i_High, x_Node.i_Low, x_Node.i_High);
    if (i_Cmp < 0) {
      return get(x_Node.x_Left, i_Low, i_High);
    } else if (i_Cmp > 0) {
      return get(x_Node.x_Right, i_Low, i_High);
    } else {
      return x_Node.i_MutationID;
    }
  }


  /***************************************************************************
  *  randomized insertion
  ***************************************************************************/

  public void put(int i_Low, int i_High, int i_MutationID, int i_RefID, short s_Source, String x_Mutation, String x_Metadata1, String x_Metadata2, String x_Metadata3) {
    if (contains(i_Low, i_High)) {
      //System.out.println("duplicate");
      remove(i_Low, i_High);
    }
    x_Root = randomizedInsert(x_Root, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
  }

  // make new node the root with uniform probability
  private Node randomizedInsert(Node x_Node, int i_Low, int i_High, int i_MutationID, int i_RefID, short s_Source, String x_Mutation, String x_Metadata1, String x_Metadata2, String x_Metadata3) {
    if (x_Node == null) {
      return new Node(i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
    }
    if (Math.random() * size(x_Node) < 1.0) {
      return rootInsert(x_Node, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
    }
    int i_Cmp = compareTo(i_Low, i_High, x_Node.i_Low, x_Node.i_High);
    if (i_Cmp < 0) {
      x_Node.x_Left  = randomizedInsert(x_Node.x_Left, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
    } else {
      x_Node.x_Right = randomizedInsert(x_Node.x_Right, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
    }
    fix(x_Node);
    return x_Node;
  }

  private Node rootInsert(Node x_Node, int i_Low, int i_High, int i_MutationID, int i_RefID, short s_Source, String x_Mutation, String x_Metadata1, String x_Metadata2, String x_Metadata3) {
    if (x_Node == null) {
      return new Node(i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
    }
    int i_Cmp = compareTo(i_Low, i_High, x_Node.i_Low, x_Node.i_High);
    if (i_Cmp < 0) {
      x_Node.x_Left  = rootInsert(x_Node.x_Left, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
      x_Node = rotR(x_Node);
    } else {
      x_Node.x_Right = rootInsert(x_Node.x_Right, i_Low, i_High, i_MutationID, i_RefID, s_Source, x_Mutation, x_Metadata1, x_Metadata2, x_Metadata3);
      x_Node = rotL(x_Node);
    }
    return x_Node;
  }

  /***************************************************************************
  *  deletion
  ***************************************************************************/

  private Node joinLR(Node x_A, Node x_B) {
    if (x_A == null) {
      return x_B;
    }
    if (x_B == null) {
      return x_A;
    }
    if (Math.random() * (size(x_A) + size(x_B)) < size(x_A)) {
      x_A.x_Right = joinLR(x_A.x_Right, x_B);
      fix(x_A);
      return x_A;
    } else {
      x_B.x_Left = joinLR(x_A, x_B.x_Left);
      fix(x_B);
      return x_B;
    }
  }

  // remove and return value associated with given interval;
  // if no such interval exists return null
  public int remove(int i_Low, int i_High) {
    int i_Value = get(i_Low, i_High);
    x_Root = remove(x_Root, i_Low, i_High);
    return i_Value;
  }

  private Node remove(Node x_Node, int i_Low, int i_High) {
    if (x_Node == null) {
      return null;
    }
    int i_Cmp = compareTo(i_Low, i_High, x_Node.i_Low, x_Node.i_High);
    if (i_Cmp < 0) {
      x_Node.x_Left  = remove(x_Node.x_Left, i_Low, i_High);
    } else if (i_Cmp > 0) {
      x_Node.x_Right = remove(x_Node.x_Right, i_Low, i_High);
    } else {
      x_Node = joinLR(x_Node.x_Left, x_Node.x_Right);
    }
    fix(x_Node);
    return x_Node;
  }

  /***************************************************************************
  *  Interval searching
  ***************************************************************************/

  // return an interval in data structure that intersects the given interval;
  // return null if no such interval exists
  // running time is proportional to log N

  public boolean intersects(int i_Low, int i_High, int i_Low2, int i_High2) {
    if (i_High2 < i_Low) {
      return false;
    }
    if (i_High < i_Low2) {
      return false;
    }
    return true;
  }

  // return *all* intervals in data structure that intersect the given interval
  // running time is proportional to R log N, where R is the number of intersections
  public LinkedList<Node> searchAll(int i_Low, int i_High) {
    LinkedList<Node> x_List = new LinkedList<Node>();
    searchAll(x_Root, i_Low, i_High, x_List);
    return x_List;
  }

  // look in subtree rooted at x
  public boolean searchAll(Node x_Node, int i_Low, int i_High, LinkedList<Node> x_List) {
    boolean b_Found1 = false;
    boolean b_Found2 = false;
    boolean b_Found3 = false;
    if (x_Node == null) {
      return false;
    }
    if (intersects(i_Low, i_High, x_Node.i_Low, x_Node.i_High)) {
      x_List.add(x_Node);
      b_Found1 = true;
    }
    if (x_Node.x_Left != null && x_Node.x_Left.i_Max >= i_Low) {
      b_Found2 = searchAll(x_Node.x_Left, i_Low, i_High, x_List);
    }
    if (b_Found2 || x_Node.x_Left == null || x_Node.x_Left.i_Max < i_Low) {
      b_Found3 = searchAll(x_Node.x_Right, i_Low, i_High, x_List);
    }
    return b_Found1 || b_Found2 || b_Found3;
  }

  /***************************************************************************
  *  useful binary tree functions
  ***************************************************************************/

  // return number of nodes in subtree rooted at x
  public int size() {
    return size(x_Root);
  }

  private int size(Node x_Node) { 
    if (x_Node == null) {
      return 0;
    } else {
      return x_Node.i_N;
    }
  }

  // height of tree (empty tree height = 0)
  public int height() {
    return height(x_Root);
  }

  private int height(Node x_Node) {
    if (x_Node == null) {
      return 0;
    }
    return 1 + Math.max(height(x_Node.x_Left), height(x_Node.x_Right));
  }

  /***************************************************************************
  *  helper BST functions
  ***************************************************************************/

  // fix auxilliar information (subtree count and max fields)
  private void fix(Node x_Node) {
    if (x_Node == null) {
      return;
    }
    x_Node.i_N = 1 + size(x_Node.x_Left) + size(x_Node.x_Right);
    x_Node.i_Max = max3(x_Node.i_High, max(x_Node.x_Left), max(x_Node.x_Right));
  }

  private int max(Node x_Node) {
    if (x_Node == null) {
      return Integer.MIN_VALUE;
    }
    return x_Node.i_Max;
  }

  // precondition: a is not null
  private int max3(int i_A, int i_B, int i_C) {
    return Math.max(i_A, Math.max(i_B, i_C));
  }

  // right rotate
  private Node rotR(Node x_Node) {
    Node x_Node2 = x_Node.x_Left;
    x_Node.x_Left = x_Node2.x_Right;
    x_Node2.x_Right = x_Node;
    fix(x_Node);
    fix(x_Node2);
    return x_Node2;
  }

  // left rotate
  private Node rotL(Node x_Node) {
    Node x_Node2 = x_Node.x_Right;
    x_Node.x_Right = x_Node2.x_Left;
    x_Node2.x_Left = x_Node;
    fix(x_Node);
    fix(x_Node2);
    return x_Node2;
  }

  /***************************************************************************
  *  Debugging functions that test the integrity of the tree
  ***************************************************************************/

  // check integrity of subtree count fields
  public boolean check() {
    return checkCount() && checkMax();
  }

  // check integrity of count fields
  private boolean checkCount() {
    return checkCount(x_Root);
  }

  private boolean checkCount(Node x_Node) {
    if (x_Node == null) {
      return true;
    }
    return checkCount(x_Node.x_Left) && checkCount(x_Node.x_Right) && (x_Node.i_N == 1 + size(x_Node.x_Left) + size(x_Node.x_Right));
  }

  private boolean checkMax() {
    return checkMax(x_Root);
  }

  private boolean checkMax(Node x_Node) {
    if (x_Node == null) {
      return true;
    }
    return x_Node.i_Max == max3(x_Node.i_High, max(x_Node.x_Left), max(x_Node.x_Right));
  }

  /***************************************************************************
  *  build Index
  ***************************************************************************/

  //builds an Index from the given file
  public static IntervalST buildIndex(File x_File) throws IOException {
    if (x_File.exists() && !x_File.isDirectory()) {
      String x_Line = "";
      IntervalST x_ST = new IntervalST();
      /*int i_MB = 1024*1024;
      Runtime x_Runtime = Runtime.getRuntime();
      long l_OldTime = System.currentTimeMillis();*/
      FileReader x_FR = new FileReader(x_File);
      BufferedReader x_BR = new BufferedReader(x_FR);
      short s_SourceNo = 0;
      while ((x_Line = x_BR.readLine()) != null) {
        String[] x_Parts = x_Line.split(";");
        if (x_Parts[5].equals("1000 Genomes Project")) {
          s_SourceNo = (short) 0;
        } else if (x_Parts[5].equals("The Cancer Genome Atlas")) {
          s_SourceNo = (short) 1;
        } else if (x_Parts[5].equals("dbSNP")) {
          s_SourceNo = (short) 2;
        }
        x_ST.put(Integer.parseInt(x_Parts[2]), Integer.parseInt(x_Parts[3]), Integer.parseInt(x_Parts[0]), Integer.parseInt(x_Parts[4]), s_SourceNo, x_Parts[1], x_Parts[6], x_Parts[7], x_Parts[8]);
      }
      x_BR.close();
      x_FR.close();
      /*System.out.println("Time to build Tree: "+(System.currentTimeMillis()-l_OldTime));
      int i_Size = x_ST.size();
      System.out.println("Number of Intervals: " + i_Size);
      long l_Memory = (x_Runtime.totalMemory() - x_Runtime.freeMemory()) / i_MB;
      System.out.println("Used Memory: " + l_Memory + "mb");
      System.out.println("approximately " + ((l_Memory*1024*1024)/i_Size) + " Bytes per Node");
      System.gc();
      l_Memory = (x_Runtime.totalMemory() - x_Runtime.freeMemory()) / i_MB;
      System.out.println("Used Memory after GC: " + l_Memory + "mb");
      System.out.println("approximately " + ((l_Memory*1024*1024)/i_Size) + " Bytes per Node after GC");*/
      return x_ST;
    } else {
      return null;
    }
  }
}
