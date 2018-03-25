package main;

import ind.biello.node.BTreeNode;


public class Main {

    public static void main(String[] args) {
       testDeleteCase5();
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
        node = node.insert(25);
        node = node.insert(23);
        node = node.insert(35);
        node = node.insert(24);
        node = node.insert(4);
        node = node.insert(17);
//        node.print();
//        System.out.println("-----------------");
        node = node.delete(4);
//        node.print();
//        System.out.println("-----------------");
        node = node.delete(17);
//        node.print();
//        System.out.println("-----------------");
        node = node.delete(15);
        node.print();
        System.out.println("-----------------");
        node = node.delete(24);
        node.print();
        System.out.println("-----------------");
        node = node.delete(23);
        node.print();
        System.out.println("-----------------");
        node = node.delete(35);
        node.print();
        System.out.println("-----------------");
    }

    public static void testDeleteCase5() {
        BTreeNode node = new BTreeNode(3);
        node = node.insert(3);
        node = node.insert(24);
        node = node.insert(37);
        node = node.insert(45);
        node = node.insert(50);
        node = node.insert(53);
        node = node.insert(61);
        node = node.insert(90);
        node = node.insert(100);
        node = node.insert(70);
        System.out.println("--------原树：---------");
        node.print();
        System.out.println("--------删除50后：---------");
        node = node.delete(50);
        node.print();
        System.out.println("--------删除53后：---------");
        node = node.delete(53);
        node.print();
        System.out.println("--------删除37后：---------");
        node = node.delete(37);
        node.print();
    }

}
