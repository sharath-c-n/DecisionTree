import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is the decision tree implementation, where all the data attributes belong to
 * {0,1} domain.
 */
public class DecisionTree {
    private TreeNode root;
    private Map<String,Integer> header;
    private boolean isID3 = true;

    public DecisionTree() {
    }


    public DecisionTree(boolean isID3) {
        this.isID3 = isID3;
    }

    /**
     * Constructs the decision tree for the given data set.
     * @param dataSet :
     */
    public void constructTree(DataSet dataSet) {
        root = buildTree(dataSet);
        header = new HashMap<>();
        int count = 0;
        for(String attr : dataSet.getHeader()){
            header.put(attr,count++);
        }
    }

    /**
     * Recursive function which build the decision tree
     * @param dataSet : data set for the current iteration
     * @return root of the tree .
     */
    private TreeNode buildTree(DataSet dataSet) {
        double entropy = isID3 ? dataSet.getEntropy():dataSet.getImpurity();
        int majorityClass = dataSet.getMajorityClass();
        if (entropy == 0 || dataSet.getAttributes().size() == 0) {
            return new TreeNode(null, entropy, true, majorityClass);
        }
        double gain = 0;
        String splitAttribute = null;
        for (String attribute : dataSet.getAttributes()) {
            double currentGain = dataSet.calculateGain(attribute,isID3);
            if (gain < currentGain) {
                splitAttribute = attribute;
                gain = currentGain;
            }
        }
        if(splitAttribute == null){
            return new TreeNode(null, entropy, true, majorityClass);
        }
        TreeNode node = new TreeNode(splitAttribute, entropy, false, majorityClass);
        node.setLeft(buildTree(dataSet.splitData(splitAttribute, 0)));
        node.setRight(buildTree(dataSet.splitData(splitAttribute, 1)));
        return node;
    }

    /**
     * Given a data vector, predicts the class it belongs to
     * @param row : input data vector
     * @return : class 0/1. Since this is a binary classifier
     */
    public int predictClass(List<Integer> row) {
        if(root!=null){
            TreeNode node = root;
            while(!node.isLeaf()){
                int attrIdx = header.get(node.getAttribute());
                node = row.get(attrIdx) == 0 ? node.getLeft() : node.getRight();
            }
            return node.getMajorityClass();
        }
        return -1;
    }

    /**
     * Calculates the accuracy of the current tree, for given test data.
     * @param testSet :
     * @return
     */
    public double testAccuracy(DataSet testSet){
        int classIdx = testSet.getRowSize() - 1;
        double accuracy = 0;
        for(List<Integer> row : testSet){
            if(predictClass(row) == row.get(classIdx))
                accuracy++;
        }
        return accuracy/testSet.getRowCount() * 100;
    }

    public DecisionTree clone(){
        DecisionTree newTree = new DecisionTree();
        newTree.root = root.clone();
        newTree.isID3 = isID3;
        newTree.header = new HashMap<>();
        for(String attribute : header.keySet()){
            newTree.header.put(attribute,header.get(attribute));
        }
        return newTree;
    }

    /**
     * Helper function for pruning the tree, this will just collect all the internal nodes in the
     * current tree and provide them in a list. Does a pre-order traversal of tree and collects all internal
     * node.
     * @param node : root node of the current tree.
     * @param nodeList :
     */
    private void orderNodes(TreeNode node,List<TreeNode> nodeList){
        if(node.isLeaf()){
            return;
        }
        nodeList.add(node);
        orderNodes(node.getLeft(),nodeList);
        orderNodes(node.getRight(),nodeList);
    }


    /**
     * Does a random pruning of the current tree and uses validation set to determine the best tree,
     * build L new trees by pruning K internal nodes and returns one tree out of L
     * which has the highest accuracy.
     * @param L : No of trees to create
     * @param K : number of nodes to prune
     * @param validationSet : data set to find accuracy of each tree which was built.
     * @return : best tree which has highest accuracy
     */
    public DecisionTree prune(int L, int K, DataSet validationSet){
        DecisionTree bestTree = this;
        double maxAccuracy = testAccuracy(validationSet);
        for(int i = 0 ;i < L; i++){
            DecisionTree currentTree = this.clone();
            int m = new Random().nextInt(K-1)+1;
            for(int j=0;j<m && !currentTree.root.isLeaf();j++){
                List<TreeNode> orderedList = new ArrayList<>();
                orderNodes(currentTree.root,orderedList);
                if(orderedList.size() <= 1){
                    break;
                }
                int p = new Random().nextInt(orderedList.size()-1)+1;
                orderedList.get(p).setLeaf(true);
            }
            double accuracy = currentTree.testAccuracy(validationSet);
            if(accuracy > maxAccuracy){
                maxAccuracy = accuracy;
                bestTree = currentTree;
            }
        }
        return bestTree;
    }

    /**
     * Prints the tree in a readable format
     * @return
     */
    public String toString() {
        StringBuilder tree = new StringBuilder();
        if (root != null) {
            preOrder(root, 0, tree);
        }
        return tree.toString();
    }

    /**
     * Helper function to print the tree
     * @param node
     * @param level
     * @param tree
     */
    private void preOrder(TreeNode node, int level, StringBuilder tree) {
        if(node.isLeaf()){
            tree.append(node.getMajorityClass());
            return;
        }
        tree.append("\n").append(format(node,level)).append("0 : ");
        preOrder(node.getLeft(),level+1,tree);
        tree.append("\n").append(format(node,level)).append("1 : ");
        preOrder(node.getRight(),level+1,tree);
    }

    /**
     * Helper function to print the tree
     * @param node
     * @param level
     */
    private StringBuilder format(TreeNode node, int level) {
        StringBuilder line = new StringBuilder();
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                line.append("| ");
            }
        }
        return line.append(node.getAttribute()).append(" = ");
    }


}
