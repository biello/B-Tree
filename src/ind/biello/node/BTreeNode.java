package ind.biello.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:  bielu
 * Date:    2018/3/13 23:12
 * Desc:    B-Tree节点
 */
public class BTreeNode {

    private LinkedList<Integer> values;

    private BTreeNode parent;

    private LinkedList<BTreeNode> children;

    public BTreeNode() {}

    public BTreeNode(int[] values, List<BTreeNode> children) throws RuntimeException {
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
     * 获取当前节点的关键字list
     * @return
     */
    public List<Integer> getValues() {
        return this.values;
    }

    /**
     * 判断当前节点是否是空节点
     * @return 空节点返回true, 非空节点返回false
     */
    public boolean isEmpty() {
        if(this.values == null || this.values.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 清空当前节点
     */
    public void clear() {
        this.values = null;
        this.children = null;
    }
}
