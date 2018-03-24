package ind.biello.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:  bielu
 * Date:    2018/3/13 23:12
 * Desc:    M阶B-Tree的节点
 */
public class BTreeNode {
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
     * @param m B-树的阶
     */
    public BTreeNode(int m) {
        this();
        if(m < 3) {
            throw new RuntimeException("The order of B-Tree should be greater than 2.");
        }
        this.M = m;
    }

    /**
     * 根据父节点构造一个空的孩子节点
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
     * 插入可能会产生新的根节点，会导致当前节点不再是根节点，返回新的根节点
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
        insertNode(p.parent, e, new BTreeNode(p.M));
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
     * 从B-树里删除关键字，先找到根节点，从根节点往下找要删除的关键字，如果
     * 关键字不存在，抛出删除异常；如果存在，若关键字不是最下层非终端节点（叶子节点的
     * 上一层），此时只需要关键字和关键字节点紧邻的右子树中的最小值N互换，然后删除N，
     * 至此已转化为待删除关键字在最下层非终端节点的情况，所以只需讨论关键字在最下层
     * 非终端节点的情况。此时分为三种情况：
     * 1. 被删除关键字所在的节点关键字数大于等于 ceil(M/2)
     * 2. 被删除关键字所在的节点关键字数等于 ceil(M/2)-1，且该节点相邻右兄弟（或左兄弟）
     * 中的关键字数大于 ceil(M/2)-1，只需将该兄弟节点中的最小（或最大）关键字上移到
     * 双亲节点中，而将双亲节点中小于（或大于）该上移动关键字的紧邻关键字下移到被删
     * 关键字所在的节点中。
     * 3. 被删除关键字所在的节点关键字数等于 ceil(M/2)-1，且左右兄弟节点的关键字数都等于
     * ceil(M/2)-1，假设该节点有右兄弟A，则在删除关键字之后，它所在的节点中剩余的关键字和
     * 孩子引用，加上双亲节点中的指向A的左侧关键字一起，合并到A中去（若没有右兄弟则合并到
     * 左兄弟中）。此时双亲节点的关键字数减少了一个，若因此导致其关键字数小于ceil(M/2)-1，
     * 则对双亲节点做递归处理。
     *
     * 删除可能会产生新的根节点，会导致当前节点不再是根节点
     *
     * @param e 要删除的元素
     * @return 删除完成后的根节点
     */
    public BTreeNode delete(int e) {
        if(isEmpty()) {
            return this;
        }
        BTreeNode p = getRoot().search(e);
        if(p.isEmpty()) {
            throw new RuntimeException("the key to be deleted is not exist, key: " + e);
        }
        int valueIndex = 0;
        while(valueIndex < p.values.size() && p.values.get(valueIndex) < e) {
            valueIndex++;
        }
        // 如果p不是最下层非终端节点
        if(!p.children.get(0).isEmpty()) {
            BTreeNode rMin = p.children.get(valueIndex);
            while(!rMin.children.get(0).isEmpty()) {
                rMin = rMin.children.get(0);
            }
            p.values.set(valueIndex, rMin.values.get(0));
            return delete(rMin, valueIndex, 0);
        }
        return delete(p, valueIndex, 0);
    }

    /**
     * 删除指定节点中的关键字和孩子，并处理删除后打破B-树规则的情况
     * @param target 目标节点
     * @param valueIndex 关键字索引
     * @param childIndex 孩子索引
     * @return 删除完成后的根节点
     */
    private BTreeNode delete(BTreeNode target, int valueIndex, int childIndex) {
        target.values.remove(valueIndex);
        target.children.remove(childIndex);
        if(target.children.size() >= Math.ceil(M/2.0)) {
            return target.getRoot();
        }
        if(target.isRoot()) {
            if(target.children.size() > 1) {
                return target;
            }else {
                BTreeNode newRoot = target.children.get(0);
                newRoot.parent = null;
                return newRoot;
            }
        }
        int parentChildIndex = 0;
        while(parentChildIndex < target.parent.children.size() && target.parent.children.get(parentChildIndex) != target) {
            parentChildIndex++;
        }
        if(parentChildIndex > 0 && target.parent.children.get(parentChildIndex-1).values.size() >= Math.ceil(M/2.0)) {
            // 左兄弟关键字数大于 ceil(M/2)-1
            int downKey = target.parent.values.get(parentChildIndex-1);
            BTreeNode leftSibling = target.parent.children.get(parentChildIndex-1);
            int upKey = leftSibling.values.remove(leftSibling.values.size()-1);
            leftSibling.children.remove(leftSibling.children.size()-1);
            target.values.remove(valueIndex);
            target.values.add(0, downKey);
            target.parent.values.set(parentChildIndex-1, upKey);
            return target.getRoot();
        }else if(parentChildIndex < target.parent.children.size()-1 &&
                target.parent.children.get(parentChildIndex+1).values.size() >= Math.ceil(M/2.0)) {
            // 右兄弟关键字数大于 ceil(M/2)-1
            int downKey = target.parent.values.get(parentChildIndex);
            BTreeNode rightSibling = target.parent.children.get(parentChildIndex+1);
            int upKey = rightSibling.values.remove(0);
            rightSibling.children.remove(0);
            target.values.remove(valueIndex);
            target.values.add(downKey);
            target.parent.values.set(parentChildIndex, upKey);
            return target.getRoot();
        }else {
            // 左右兄弟关键字数都不大于 ceil(M/2)-1
            if(parentChildIndex > 0) {
                // 如果有左兄弟
                BTreeNode leftSibling = target.parent.children.get(parentChildIndex-1);
                int downKey = target.parent.values.get(parentChildIndex-1);
                // 删除目标关键字和关键字紧邻孩子的一个空孩子
//                target.values.remove(valueIndex);
//                if(target.children.get(valueIndex).isEmpty()) {
//                    target.children.remove(valueIndex);
//                }else {
//                    target.children.remove(valueIndex+1);
//                }
                // 加上父节点关键字
                leftSibling.values.add(downKey);
                // 加上目标节点的剩余信息
                leftSibling.values.addAll(target.values);
                target.children.forEach(c -> c.parent=leftSibling);
                leftSibling.children.addAll(target.children);
//                target.parent.children.remove(parentChildIndex);
                // 如果删除后父节点的关键字数不小于  ceil(M/2)-1
//                if(target.parent.children.size() >= Math.ceil(M/2.0)) {
//                    return leftSibling.getRoot();
//                }else {
                    return delete(target.parent, parentChildIndex-1, parentChildIndex);
//                }
            }else {
                // TODO: 右兄弟
                return null;
            }
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
        sb.append(node.values);
        System.out.println(sb.toString());
        for(BTreeNode child : node.children) {
            printNode(child, depth+1);
        }
    }
}
