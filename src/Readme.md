<h1>DecisionTree and random pruning for data in domain {0,1}</h1>
<ul>
<li>To compile the code use the below command
javac *.java
</li>
<li>
To run the driver command use the below command
<pre>java DecisionTreeDriver &lt; L &gt; &lt; K &gt; &lt; training-set &gt; &lt; validation-set &gt; &lt; test-set &gt; &lt; to-print : YES / NO &gt;</pre>
</li>
</ul>
<h1>Note:</h1>
<ol>
<li>The path to proper data files should be given for the dataset. If wrong files are given the output
is unpredictable.
</li>
<li>
If the to-print is given as yes, all four trees will be printed namely : decision tree using Entropy,
Post pruned tree using entropy, decision tree using Impurity variance, post pruned tree using impurity
variance in order. The accuracy will be printed in the beginning followed by the constructed trees.
</li>
</ol>

