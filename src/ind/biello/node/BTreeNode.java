package ind.biello.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:  bielu
 * Date:    2018/3/13 23:12
 * Desc:    M阶B-Tree的节点
 */
public class  BTreeNode {
    /**
     * B树的阶
     */
    int M;

    /**
     * 关键字列表
     */
    LinkedList<Integer> values;

    /**
     * 父节点
     */
    BTreeNode parent;

    /**
     * 孩子列表
     */
    LinkedList<BTreeNode> children;

    /**
     * 构造一棵空的B-树
     */
    private BTreeNode() {
        this.values = new LinkedList<Integer>();
        this.children = new LinkedList<BTreeNode>();
    }

    /**
     * 构造一棵空的m阶B-树
     *
     * @param m B-数的阶
     */
    public BTreeNode(int m) {
        this();
        if(m < 3) {
            throw new RuntimeException("The order of B-Tree should be greater than 2.");
        }
        this.M = m;
    }

    /**
     * 根据父节点构造一个孩子节点
     *
     * @param parent 父节点
     */
    public BTreeNode(BTreeNode parent) {
        this(parent.M);
        this.parent = parent;
    }

    /**
     * 往B-树里插数据，先找到根节点，从根节点往下找插入的位置，由于
     * B-树不允许有重复的数据，如果插入已有的值则抛出异常插入.
     * 插入可能会产生新的根节点，会导致当前节点不再是根节点
     *
     * @param e 要插入的元素
     * @return 插入完成后的根节点
     */
    public BTreeNode insert(int e) {
        if(isEmpty()) {
            values.add(e);
            children.add(new BTreeNode(this));
            children.add(new BTreeNode(this));
            return this;
        }
        BTreeNode p = getRoot().search(e);
        if(!p.isEmpty()) {
            throw new RuntimeException("cannot insert duplicate key to B-Tree, key: " + e);
        }
        insertNode(p.parent, e, new BTreeNode());
        return getRoot();
    }

    /**
     * 向指定节点内插入关键字和关键字右侧的孩子节点
     *
     * @param node 插入的节点
     * @param e 待插入关键字
     * @param eNode 待关键字右侧的孩子节点
     */
    private void insertNode(BTreeNode node, int e, BTreeNode eNode) {
        int valueIndex = 0;
        while(valueIndex < node.values.size() && node.values.get(valueIndex) < e) {
            valueIndex++;
        }
        node.values.add(valueIndex, e);
        eNode.parent = node;
        node.children.add(valueIndex+1, eNode);
        if(node.values.size() > M-1) {
            // 获取上升关键字
            int upIndex = M/2;
            int up = node.values.get(upIndex);
            // 当前节点分为左右两部分，左部的parent不变，右部的parent放在上升关键字右侧
            BTreeNode rNode = new BTreeNode(M);
            rNode.values = new LinkedList(node.values.subList(upIndex+1, M));
            rNode.children = new LinkedList(node.children.subList(upIndex+1, M+1));
            /*  由于rNode.children是从node.children分离出来的,其parent仍指向node，
                所以需要将rNode.children的parent改为指向rNode
             */
            for(BTreeNode rChild : rNode.children) {
                rChild.parent = rNode;
            }
            node.values = new LinkedList(node.values.subList(0, upIndex));
            node.children = new LinkedList(node.children.subList(0, upIndex+1));
            // 从根节点中上升，选取上升关键字作为新的根节点
            if(node.parent == null) {
                node.parent = new BTreeNode(M);
                node.parent.values.add(up);
                node.parent.children.add(node);
                node.parent.children.add(rNode);
                rNode.parent = node.parent;
                return;
            }
            // 父节点增加关键字，递归调用
            insertNode(node.parent, up, rNode);
        }
    }

    /**
     * 从当前节点往下查找目标值target
     *
     * @param target
     * @return 找到则返回找到的节点，不存在则返回叶子节点
     */
    public BTreeNode search(int target) {
        if(isEmpty()) {
            return this;
        }
        int valueIndex = 0;
        while(valueIndex < values.size() && values.get(valueIndex) <= target) {
            if(values.get(valueIndex) == target) {
                return this;
            }
            valueIndex++;
        }
        return children.get(valueIndex).search(target);
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public BTreeNode getRoot() {
        BTreeNode p = this;
        while(!p.isRoot()) {
            p = p.parent;
        }
        return p;
    }

    /**
     * 判断当前节点是否是空节点
     *
     * @return 空节点返回true, 非空节点返回false
     */
    public boolean isEmpty() {
        if(values == null || values.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前节点是否是根节点
     *
     * @return 是根节点返回true, 不是返回false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /*
     * 清空当前节点
     */
    public void clear() {
        parent = null;
        values.clear();
        children.clear();
    }

    /**
     * 以当前节点为根，在控制台打印B-树
     */
    public void print() {
        printNode(this, 0);
    }

    /**
     * 控制台打印节点的递归调用
     *
     * @param node 要打印节点
     * @param depth 递归深度
     */
    private void printNode(BTreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < depth; i++) {
            sb.append("|    ");
        }
        if(depth > 0) {
            sb.append("|----");
        }
        System.out.println(sb.toString() + node.values);
        for(BTreeNode child : node.children) {
            printNode(child, depth+1);
        }
    }
}
