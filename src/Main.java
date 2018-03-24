import ind.biello.node.BTreeNode;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
       testDeleteCase4();
    }


    public static void testDeleteCase1() {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(35);
        node = node.insert(13);
        node = node.insert(17);
        node = node.insert(2);
        node.print();
        node = node.delete(2);
        node.print();
    }

    public static void testDeleteCase2() {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(35);
        node = node.insert(13);
        node = node.insert(17);
        node = node.insert(2);
        node.print();
        node = node.delete(35);
        node.print();
    }

    public static void testDeleteCase3() {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(35);
        node = node.insert(13);
        node = node.insert(17);
        node = node.insert(22);
        node.print();
        node = node.delete(13);
        node.print();
    }

    public static void testDeleteCase4() {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(2);
        node = node.insert(13);
        node = node.insert(15);
//        node.print();
        node = node.insert(25);
        node = node.insert(23);
        node = node.insert(35);
        node = node.insert(24);
        node = node.insert(4);
        node = node.insert(17);
        node.print();
        System.out.println("-----------------");
        node = node.delete(4);
        node.print();
        System.out.println("-----------------");
        node = node.delete(17);
        node.print();
        System.out.println("-----------------");
        node = node.delete(35);
        node.print();

//        node = node.delete(13);
//        node.print();
    }

}
