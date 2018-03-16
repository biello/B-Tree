import ind.biello.node.BTreeNode;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(35);
        node.print();
        node = node.insert(13);
        node.print();
        node = node.insert(18);
        node.print();
        node = node.insert(22);
        node.print();
    }

}
