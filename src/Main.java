import ind.biello.node.BTreeNode;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BTreeNode node = new BTreeNode(3);
        node.insert(35);
        node.insert(17);
        node.insert(18);

        while(node.parent != null) {
            node = node.parent;
        }
        System.out.println(node.values);
    }

    public static List<BTreeNode> getEmptyNodes(int num) {
        List<BTreeNode> emptyNodes = new LinkedList<>();
        while(num-- != 0) {
            emptyNodes.add(new BTreeNode());
        }
        return  emptyNodes;
    }
}
