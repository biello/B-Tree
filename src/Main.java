import ind.biello.node.BTreeNode;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BTreeNode node = new BTreeNode(3);
        node = BTreeNode.insert(node, 35);
        node = BTreeNode.insert(node, 17);
        node = BTreeNode.insert(node, 18);
        node = BTreeNode.insert(node,19);
        printBTree(node, 0);
    }

    public static void printBTree(BTreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < depth; i++) {
            sb.append("|   ");
        }
        if(depth > 0) {
            sb.append("|---");
        }
        System.out.println(sb.toString() + node.values);
        for(BTreeNode child : node.children) {
            printBTree(child, depth+1);
        }
    }

    public static List<BTreeNode> getEmptyNodes(int num, int m) {
        List<BTreeNode> emptyNodes = new LinkedList<>();
        while(num-- != 0) {
            emptyNodes.add(new BTreeNode(m));
        }
        return  emptyNodes;
    }
}
