import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Beyza_Basaran_2019510021 {
    /*The main idea of DP function is to start from node n and work back to the root, each time basing the
    current optimal sum of on previously computed sub-problems that have been stored in some memoization table.
    memorizationTable[] contains max ability sum from this node down in the tree and HashMap DP contains node
    data and its max ability sum.If node has no grandchildren or children, than childrenSum helper function and
    grandchildrenSum  helper function returns 0*/
    public static int DP(Tree.Node root, ArrayList<Tree.Node>nodes,int numberOfNodes, HashMap<String,Integer>selectedNodes){//=>3O(n^2)=>O(n^2)
        HashMap<String,Integer>DP = new HashMap<>();//max sub-problem results stored with their lion data => O(n) space
        int[] memorizationTable= new int[numberOfNodes+1];//max sub-problem results stored. => O(n) space linearly increases
        memorizationTable[0]=0;
        int i;
        for(int x=memorizationTable.length-1 ;x>=1;x--) { //Starting from leaf nodes walking up to the root node =>total 2O(n^2)
            i = x-1;
            int current = nodes.get(i).ability;//current node ability
            //System.out.println(nodes.get(i).data + ": " + current + " cSum=" + childrenSum(root, nodes.get(i),DP)
            //         + " gcSum=" + grandchildrenSum(root, nodes.get(i),DP));
            /*childrenSum for loop =>O(n) grandchildrenSum for loop =>O(n) which makes 2O(n) time
            We can either take children or parent and grandchildren since lions are not allowed to go hunting with his/her immediate parent*/
            memorizationTable[x]=Math.max(childrenSum(root, nodes.get(i),DP), current + grandchildrenSum(root, nodes.get(i),DP));
            DP.put(nodes.get(i).data,memorizationTable[x]); //putting max sub problem results from memorization table and the node's data to DP hash map
            int sum=0;
            Tree.Node temp2=nodes.get(i); //keeping the Root
            Tree.Node temp3; //declaring temp node
            if(temp2.child!= null && temp2.child.child==null){ //if node has child but does not have grandchild
                if(temp2.ability>temp2.child.ability){ //if parent> child ability
                    selectedNodes.put(temp2.data,temp2.ability);// add parent to the selected nodes
                }
                if(temp2.child.sibling!=null){//if parent has a child and child has no child it has sibling
                    if(temp2.ability==temp2.child.ability){//if parent ability equals child's ability then take child because child sum will be grater
                        selectedNodes.put(temp2.child.data,temp2.child.ability);
                    }
                    Tree.Node temp4=temp2.child.sibling;
                    selectedNodes.put(temp2.child.data,temp2.child.ability);
                    while(temp4 !=null){//=>O(n) worst case
                        if(temp2.ability<temp4.ability){ // if parent's left child's siblings abilities are grater than the parent
                            if(temp4.child==null){
                                selectedNodes.put(temp4.data,temp4.ability);
                            }
                        }
                        temp4= temp4.sibling;// go to next right sibling
                    }
                    selectedNodes.remove(temp2.data,temp2.ability);//at the end remove parent since it will be less than the child sum
                }
                else{
                    if(temp2.ability<temp2.child.ability){//if parent ability less than child's ability put child to the selected nodes
                        selectedNodes.put(temp2.child.data,temp2.child.ability);// add child to the selected nodes
                    }
                }
            }
            else {
                if(temp2!=null&& temp2.child!=null && temp2.child.child!=null){//if node has child and also have grandchild
                    temp3=temp2.child; // Node's left child
                    while(temp3!=null){//while takes O(n), containsKey takes O(n) time at worst case => total O(n^2)
                        if(selectedNodes.containsKey(temp3.data)){//=>O(n)
                            sum=sum+DP.get(temp3.data); //add optimal sub problem result to sum
                        }
                        if(temp3.sibling!=null &&temp3.child== null){//if node has sibling and at the same time does not have child
                            selectedNodes.put(temp3.data,temp3.ability);
                            sum=sum+temp3.ability; //add node's ability to sum
                        }
                        else if(temp3.sibling==null &&temp3.child== null){//if node has no sibling and at the same time does not have child
                            selectedNodes.put(temp3.data,temp3.ability);// add to the selected nodes sub problem sum comes from this nodes
                            sum=sum+temp3.ability;//add node's ability to sum
                            //System.out.println("selected:"+temp3.data);
                        }
                        temp3= temp3.sibling;// go to next right sibling
                    }
                    if(temp2.ability> sum){// parent>children //while O(n) containsKey O(n) at worst case => total O(n^2)
                        temp3= temp2.child;// assigning the nodes left child in order to walk the children of the node
                        while(temp3!=null){//=>O(n)
                            if(selectedNodes.containsKey(temp3.data)){//if selected nodes contains children of the parent
                                selectedNodes.remove(temp3.data,temp3.ability);//then remove all the parent's children
                                //System.out.println("removed:"+temp3.data);
                            }
                            temp3=temp3.sibling;// go to next right sibling
                        }
                        selectedNodes.put(temp2.data,temp2.ability); // finally add the parent node to the selected nodes
                    }
                }
            }
        }
        return memorizationTable[1]; //return root node's optimal maximum sum
    }
    public  static int childrenSum(Tree.Node root, Tree.Node node, HashMap<String,Integer> DP){
        ArrayList<Tree.Node> children = new ArrayList<>();
        /*childList function starts from the root node, walks the tree and returns the given node's children list*/
        ArrayList<Tree.Node> childrenList= Tree.childList(root,node,children);
        int childrenSum=0;
        Tree.Node temp;
        if(childrenList.size()>0) {// if node does not have child function will return 0
            for (int a = 0; a < childrenList.size(); a++) {//=>O(n)
                temp= childrenList.get(a);
                childrenSum += DP.get(temp.data); //adding child's sub problems max sum from DP Hash Map
            }
        }
        return childrenSum;
    }

    public  static int grandchildrenSum(Tree.Node root, Tree.Node node, HashMap<String,Integer> DP){
        ArrayList<Tree.Node> grandchildren = new ArrayList<>();
        /*grandchildList function starts from the root node, walks the tree and returns the given node's grandchildren list*/
        ArrayList<Tree.Node> grandChildrenList= Tree.grandchildList(root,node,grandchildren);
        int grandChildrenSum=0;
        Tree.Node temp;
        if(grandChildrenList.size()>0) {// if node does not have grandchild function will return 0
            for (int a = 0; a < grandChildrenList.size(); a++) {//=>O(n)
                temp= grandChildrenList.get(a);
                grandChildrenSum += DP.get(temp.data);//adding grandchild's sub problems max sum from DP Hash Map
            }
        }
        return grandChildrenSum;
    }
    /*Unlike dynamic programming, greedy approach does not use previously calculated sub-problems.
    Instead, Greedy approach recursively calculates the sub-problems again for each node.*/
    public static int Greedy(Tree.Node node, HashMap<String,Integer>selectedNodes){ //keeping selected lions in a Hash Map structure //=>total 2O(n)=>O(n)
        int greedy_result=0;
        int result=0;
        boolean isChildContained= false;
        if(node==null){//if there is no tree it wil return 0"
            return result;
        }
        Tree.Node temp= node;//keep the node
        Tree.Node temp2= node.child; //keep the node's left child to walk the tree
        Tree.Node temp3;
        while(temp2!=null){//add sum of the children to the result //=>O(n)
            result=result+Greedy(temp2, selectedNodes); //recursively calling greedy while walking on the tree
            temp2= temp2.sibling;// go to next right sibling
        }
        if(result>temp.ability){//sum of the children > parent
            temp3 = temp.child;
            while(temp3!=null){//=>O(n)
                if(selectedNodes.containsKey(temp3.data)){//check  if it is contained
                    isChildContained=true; //if any child contained break
                    break;
                }
                temp3= temp3.sibling;// go to next right sibling
            }
            if(!isChildContained){ //if the child not contained add to result
                result=result+temp.ability;
                selectedNodes.put(temp.data,temp.ability);//if child not taken parent can be taken
                //System.out.println("selected:"+temp.data);
            }
        }
        else{//if children are not greater than parent remove
            temp3= node.child; //Back to node's left child then delete children
            while(temp3 !=null){//=>O(n)
                if(selectedNodes.containsKey(temp3.data)){
                    selectedNodes.remove(temp3.data);
                    //System.out.println("removed:"+temp3.data);
                }
                temp3 = temp3.sibling; // go to next right sibling
            }
            selectedNodes.put(temp.data,temp.ability); //after deleting children put the current node to the selected lions
            //System.out.println("selected:"+temp.data);
        }
        greedy_result= Math.max(temp.ability,result); // return max
        return greedy_result;
    }
    public static void main(String[] args) {

        try {
            /*Read File & Create Left-Child Right-Sibling Representation Tree*/
            File file1 = new File("hunting_abilities.txt");
            File file2 = new File("lions_hierarchy.txt");
            File file_deneme = new File("deneme.txt");
            HashMap<String,Integer> hm_ability= new HashMap();
            boolean flag_line1 = true;
            Scanner scn = new Scanner(file1);
            String[] line;
            int idx=-1;
            ArrayList<Tree.Node>nodes= new ArrayList<>();
            while (scn.hasNext()) {
                String data = scn.nextLine();
                if (!flag_line1) {
                    line = data.split("\t");
                    if (!hm_ability.containsKey(line[0])) {
                        hm_ability.put(line[0], Integer.valueOf(line[1]));
                    }
                }
                else {
                    flag_line1 = false;
                }
            }
            scn.close();
            scn = new Scanner(file2);
            flag_line1 = true; // check if its the first line,if it is skip
            String data;
            Tree.Node root = null; //keeping the root
            Tree.Node parentOfSibling; //keeping sibling parent
            while (scn.hasNext()) {
                data= scn.nextLine();
                if (!flag_line1) {
                    line= data.split("\t");
                    if(line[2].equalsIgnoreCase("Left-Child")){//if its left child and if its not previously added,create the parent node then add the newNode
                        for (Tree.Node n: nodes) {//Checking if the node added to nodes list before
                            Tree.Node temp =n;
                            if(temp.data.equalsIgnoreCase(line[0])) {//if added then add child to that node instead of making new node
                                Tree.Node newNode = Tree.addChild(temp, line[1],hm_ability.get(line[1]));
                                nodes.add(++idx,newNode);
                                break;
                            }
                        }
                    }
                    else if(line[2].equalsIgnoreCase("Right-Sibling")){//if its right sibling then find the parent then add
                        parentOfSibling= Tree.searchParent(nodes, line[0]);// searching parent node from tree
                        Tree.Node newNode = Tree.addChild(parentOfSibling, line[1],hm_ability.get(line[1]));//then adding new node to the parent node
                        nodes.add(++idx,newNode);
                    }
                }
                else {
                    data= scn.nextLine();// first line is passed
                    flag_line1 = false;
                    line= data.split("\t");
                    root = new Tree.Node(line[0],hm_ability.get(line[0]));//assigning root node
                    nodes.add(++idx,root);
                    Tree.Node node = Tree.addChild(root,line[1],hm_ability.get(line[1]));//assigning root node's left child
                    nodes.add(++idx,node);

                }
            }
            scn.close();

            long startTime;
            long DP_Time;
            long Greedy_Time;
            HashMap<String,Integer> DP_selectedNodes= new HashMap();
            int numberOfNodes=nodes.size();
            startTime=System.nanoTime();
            int DP_result =Beyza_Basaran_2019510021.DP(root,nodes,numberOfNodes,DP_selectedNodes);
            DP_Time = System.nanoTime() - startTime;
            System.out.println("DP Results: "+DP_result);
            System.out.println("DP Results- Selected Lions:");
            int dp_selected_lion_sum=0;
            for (HashMap.Entry<String,Integer> entry : DP_selectedNodes.entrySet()) {
                System.out.println(entry.getKey()+":"+ entry.getValue());
                dp_selected_lion_sum+=entry.getValue();
            }
            HashMap<String,Integer> Greedy_selectedNodes= new HashMap();
            startTime=System.nanoTime();
            int Greedy_result=Beyza_Basaran_2019510021.Greedy(root,Greedy_selectedNodes);
            Greedy_Time = System.nanoTime() - startTime;
            System.out.println("Greedy Results: "+Greedy_result);
            System.out.println("Greedy Results- Selected Lions:" );
            int greedy_selected_lion_sum=0;
            for (HashMap.Entry<String,Integer> entry : Greedy_selectedNodes.entrySet()) {
                System.out.println(entry.getKey()+":"+ entry.getValue());
                greedy_selected_lion_sum+=entry.getValue();
            }

            System.out.println("***");
            System.out.println("Dp  selected  lion  sum :"+dp_selected_lion_sum);
            System.out.println("Greedy selected lion sum:"+greedy_selected_lion_sum);
            System.out.println("DP  Time   :"+DP_Time+"ns");
            System.out.println("Greedy Time:"+Greedy_Time+"ns");

        } catch (FileNotFoundException e) {
            System.err.println("Error: File Not Found!");
            e.printStackTrace();
        }
    }
}
class Tree {
    static class Node {
        String data;
        Node parent;
        Node child;
        Node sibling;
        int ability;
        public Node(String data,int ability) {
            this.data = data;
            this.parent=null;
            this.child=null;
            this.sibling =null;
            this.ability= ability;
        }
    }
    static public Node addSibling(Node node, String data,int ability) {
        if(node == null) {
            return null;
        }
        while(node.sibling != null) {
            node = node.sibling;
        }
        node.sibling = new Node(data,ability);
        node.sibling.parent=node.parent;
        return(node.sibling);
    }
    static public Node addChild(Node node, String data,int ability) {
        if(node == null) {
            return null;
        }
        if(node.child != null) {// Control if child is empty or not
            return (addSibling(node.child, data,ability));
        }
        else {
            node.child = new Node(data,ability);
            node.child.parent= node; //assigning parent
            return (node.child);
        }
    }
    /*method for getting childList of the given node*/
    static public ArrayList<Node> childList(Node root, Node parent, ArrayList<Node> childList) {
        if(root == null) {
            return null;
        }
        while(root != null) {
            if(root.parent!= null && root.parent.data.equalsIgnoreCase(parent.data)){
                if(!childList.contains(root)){
                    childList.add(root);
                }
            }
            if(root.child != null) {
                childList= childList(root.child,parent,childList);
            }
            root = root.sibling;
        }
        return   childList;

    }
    /*method for getting grandChildList of the given node*/
    static public ArrayList<Node> grandchildList(Node root, Node grandParent, ArrayList<Node> grandChildList) {
        if(root == null) {
            return null;
        }
        while(root != null) {
            if(root.parent!= null && root.parent.parent!= null && root.parent.parent.data.equalsIgnoreCase(grandParent.data)){
                if(!grandChildList.contains(root)&& root!=null){
                    grandChildList.add(root);
                }
            }
            if(root.child != null) {
                grandChildList= grandchildList(root.child,grandParent,grandChildList);
            }
            root = root.sibling;
        }
        return   grandChildList;

    }
    /*method for searching parent of the given node and returning the parent*/
    static public Node searchParent(ArrayList<Node> nodes, String data) {
        for(int i=0; i< nodes.size();i++){
            if(nodes.get(i).data.equalsIgnoreCase(data)){
                return  nodes.get(i).parent;
            }
        }
        return null;
    }
}






