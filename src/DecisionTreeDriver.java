import java.io.IOException;

/**
 * Main driver program to run decision tree algorithm
 */
public class DecisionTreeDriver {
    public static void main(String args[]) throws IOException {
        if (args.length < 6) {
            System.out.println("Needs 6 parameters");
            System.out.println("<L> <K> <training-set> <validation-set> <test-set> <to-print : YES / NO>");
            System.exit(0);
        }
        int L = Integer.parseInt(args[0]);
        int K = Integer.parseInt(args[1]);
        DataSet trainingData = Util.createDataSet(args[2]);
        DataSet validationData = Util.createDataSet(args[3]);
        DataSet testData = Util.createDataSet(args[4]);
        boolean shouldPrint = "YES".equalsIgnoreCase(args[5]);

        DecisionTree entropyTree = new DecisionTree();
        entropyTree.constructTree(trainingData);

        DecisionTree purityTree = new DecisionTree(false);
        purityTree.constructTree(trainingData);

        DecisionTree prunedEntropyTree = entropyTree.prune(L, K, validationData);
        DecisionTree prunedPurityTree = purityTree.prune(L, K, validationData);

        System.out.println("Accuracy of Entropy based tree: " + entropyTree.testAccuracy(testData) + " %");
        System.out.println("Post pruning Accuracy of Entropy based tree: " + prunedEntropyTree.testAccuracy(testData) + " %");

        System.out.println("Accuracy of Purity based tree: " + purityTree.testAccuracy(testData) + " %");
        System.out.println("Post pruning Accuracy of purity based tree: " + prunedPurityTree.testAccuracy(testData) + " %");

        if (shouldPrint) {
            System.out.println("Entropy based tree" + entropyTree);
            System.out.println("Pruned Entropy based tree" + prunedEntropyTree);
            System.out.println("Purity based tree" + purityTree);
            System.out.println("Pruned Purity based tree" + prunedPurityTree);
        }
    }
}

