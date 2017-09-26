1)To compile the code use the below command
javac *.java

2)To run the driver command use the below command
java DecisionTreeDriver <L> <K> <training-set> <validation-set> <test-set> <to-print : YES / NO>

Note:
1)The path to proper data files should be given for the dataset. If wrong files are given the output
is unpredictable.

2)If the to-print is given as yes, all four trees will be printed namely : decision tree using Entropy,
post pruned tree using entropy, decision tree using Impurity variance, post pruned tree using impurity
variance in order. The accuracy will be printed in the beginning followed by the constructed trees.