/**
 * Decision tree node
 * @author Sharath
 */
public class TreeNode {
    /**
     * Split Attribute at the current node
     */
    private String attribute;

    /**
     * Left child of the tree
     */
    private TreeNode left;
    /**
     * Right child of the tree
     */
    private TreeNode right;
    /**
     * Entropy of data at the current node
     */
    private double entropy;
    /**
     * indicates if the this is a internal node or a leaf
     */
    private boolean isLeaf;
    /**
     * Majority class at the current node
     */
    private int majorityClass;

    public TreeNode(String attribute, double entropy, boolean isLeaf, int majorityClass) {
        this.attribute = attribute;
        this.entropy = entropy;
        this.isLeaf = isLeaf;
        this.majorityClass = majorityClass;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public int getMajorityClass() {
        return majorityClass;
    }

    public String toString(){
        return attribute + " : " + entropy;
    }

    public TreeNode clone(){
        TreeNode newTreeNode = new TreeNode(attribute,entropy,isLeaf,majorityClass);
        newTreeNode.left = this.left !=null ? this.left.clone():null;
        newTreeNode.right = this.right != null ? this.right.clone():null;
        return newTreeNode;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
