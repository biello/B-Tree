import ind.biello.node.BTreeNode;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BTreeNode b = new BTreeNode(new int[]{18}, getEmptyNodes(2));
        BTreeNode c = new BTreeNode(new int[]{43,78}, getEmptyNodes(3));
        List rootNodes = new LinkedList<BTreeNode>();
        rootNodes.add(b);
        rootNodes.add(c);
        BTreeNode root = new BTreeNode(new int[]{35}, rootNodes);
        System.out.println(root.search(78).getValues());
    }

    public static List<BTreeNode> getEmptyNodes(int num) {
        List<BTreeNode> emptyNodes = new LinkedList<>();
        while(num-- != 0) {
            emptyNodes.add(new BTreeNode());
        }
        return  emptyNodes;
    }
}
