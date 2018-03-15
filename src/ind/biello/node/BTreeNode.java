package ind.biello.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:  bielu
 * Date:    2018/3/13 23:12
 * Desc:    M阶B-Tree的节点
 */
public class  BTreeNode {
    // B树的阶
    private int M = 3;
    // 关键字列表
    public LinkedList<Integer> values;
    // 父节点
    public BTreeNode parent;
    // 孩子列表
    public LinkedList<BTreeNode> children;

    private BTreeNode() {
        this.values = new LinkedList<Integer>();
        this.children = new LinkedList<BTreeNode>();
    }

    public BTreeNode(int m) {
        this();
        if(m < 3) {
            throw new RuntimeException("The order of B-Tree should be greater than 2.");
        }
        this.M = m;
    }

    public BTreeNode(BTreeNode parent) {
        this(parent.M);
        this.parent = parent;
    }




    /**
     * 从根节点开始往B树里插数据
     * @param node B树的任意节点
     * @param e 要插入的元素
     * @return 插入完成后的根节点
     */
    public static BTreeNode insert(BTreeNode node, int e) {
        if(node == null) {
            throw new RuntimeException("Cannot insert element into null.");
        }
        if(node.isEmpty()) {
            node.values.add(e);
            node.children.add(new BTreeNode(node));
            node.children.add(new BTreeNode(node));
            return node;
        }
        node = node.getRoot();
        BTreeNode p = node.search(e);
        if(p.isEmpty()) {
            node.insertNode(p.parent, e, new BTreeNode());
        }
        return node.getRoot();

    }

    /**
     * 插入关键字e 和 e 右侧的子树
     * @param node
     * @param e
     * @param eNode
     */
    public void insertNode(BTreeNode node, int e, BTreeNode eNode) {
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
            // 由于rNode.children是从node.children分离出来的,其parent仍指向node，所以需要将rNode.children的parent改为指向rNode
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

    public BTreeNode(int m, int[] values, List<BTreeNode> children) throws RuntimeException {
        if(m < 3) {
            throw new RuntimeException("BTreeNode init Exception: the order of B-Tree should be greater than 2.");
        }
        this.M = m;
        // 孩子节点个数必须为关键字个数+1
        if(children.size() != values.length + 1) {
            throw new RuntimeException("BTreeNode init Exception: the number of children is not compatible with values size.");
        }
        this.values = new LinkedList<Integer>();
        for(int i : values) {
            this.values.add(i);
        }
        if(children instanceof LinkedList) {
            this.children = (LinkedList<BTreeNode>) children;
        }else {
            this.children = new LinkedList<BTreeNode>();
            for (BTreeNode node : children) {
                this.children.add(node);
            }
        }
    }


    /**
     * 从当前节点往下查找目标值target
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
     * @return 是根节点返回true, 不是返回false
     */
    public boolean isRoot() {
        return parent == null;
    }


    /*
     * 清空当前节点
     */
    public void clear() {
        if(!isEmpty()) {
            values.clear();
            children.clear();
        }
    }
}
