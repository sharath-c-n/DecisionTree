import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains all the data that needs to be evaluated.
 * @author Sharath
 */
public class DataSet implements Iterable<List<Integer>> {
    /**
     * Attribute headers in the current data set
     */
    private List<String> header;
    /**
     * Current attributes which are available for selection for the next step in decision tree
     */
    private Set<String> attributes;
    /**
     * The data set
     */
    private List<List<Integer>> data;
    /**
     * Entropy of the current data set
     */
    private double entropy;
    /**
     * Impurity of the current data set
     */
    private double impurity;
    /**
     * Size of each row in the data set
     */
    private int rowSize;


    public DataSet() {
        header = new ArrayList<>();
        data = new ArrayList<>();
        attributes = null;
        entropy = -1;
        impurity = -1;
    }

    private DataSet(List<String> header, List<List<Integer>> data, Set<String> attributes) {
        this.header = header;
        this.rowSize = header.size();
        this.data = data;
        this.attributes = attributes;
        entropy = -1;
        impurity = -1;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
        rowSize = header.size();
        if (attributes == null) {
            attributes = new HashSet<>(header);
            attributes.remove(header.get(rowSize - 1));
        }
    }

    public Set<String> getAttributes() {
        return attributes;
    }

    public void addRow(List<Integer> row) {
        data.add(row);
    }

    public double getImpurity(){
        if (impurity == -1 && data.size() > 0) {
            int positive = 0;
            int negative = 0;
            for (List<Integer> row : data) {
                if (row.get(rowSize - 1) == 0) {
                    negative++;
                } else {
                    positive++;
                }
            }
            impurity = calculatePurity(positive, negative);
        }
        return impurity;
    }

    private double calculatePurity(int positive, int negative) {
        long total = positive+negative;
        return (double)(positive*negative)/Math.pow(total,2);
    }

    public double getEntropy() {
        if (entropy == -1 && data.size() > 0) {
            int positive = 0;
            int negative = 0;
            for (List<Integer> row : data) {
                if (row.get(row.size() - 1) == 0) {
                    negative++;
                } else {
                    positive++;
                }
            }
            entropy = calculateEntropy(positive, negative);
        }
        return entropy;
    }

    private double calculateEntropy(long positive, long negative) {
        long total = positive + negative;
        double posProb = (double) positive / total;
        double negProb = (double) negative / total;
        if (positive == negative)
            return 1;
        if (negative == 0 || positive == 0)
            return 0;
        return -1 * (posProb * Math.log(posProb) + negProb * Math.log(negProb)) / Math.log(2);
    }

    /**
     * Calculates the gain for the given attribute based on the requested parametric metric to use
     * if isID3 this uses entropy based parameter, else the function uses impurity based calculation.
     * @param attribute : attribute on which the gain is to be calculated
     * @param isID3 : Should use entropy based calculation or not
     * @return : gain for the attribute passed.
     */
    public double calculateGain(String attribute,boolean isID3) {
        if (header.size() > 0 && attributes.contains(attribute)) {
            int index = header.indexOf(attribute), lastIndex = header.size() - 1;
            int positive0, negative0, positive1, negative1;
            positive0 = positive1 = negative1 = negative0 = 0;
            for (List<Integer> row : data) {
                if (row.get(index) == 0) {
                    if (row.get(lastIndex) == 0) {
                        negative0++;
                    } else {
                        positive0++;
                    }
                } else {
                    if (row.get(lastIndex) == 0) {
                        negative1++;
                    } else {
                        positive1++;
                    }
                }
            }
            double size = (double) data.size();
            if(isID3){
                return getEntropy() - ((positive0 + negative0) / size * calculateEntropy(positive0, negative0) +
                        (positive1 + negative1) / size * calculateEntropy(positive1, negative1));
            }
            else
            {
                return getImpurity() - ((positive0 + negative0) / size * calculatePurity(positive0, negative0) +
                        (positive1 + negative1) / size * calculatePurity(positive1, negative1));
            }


        }
        return -1;
    }

    /**
     * Splits the data into a new data set, based on the attribute passed and the value which it should take.
     * @param attribute : attribute based on which splitting should happen.
     * @param value : the value of the attribute which should be used for this split.
     * @return : returns the data set which contains only those rows where the selected attribute value is same as the
     * passed value
     */
    public DataSet splitData(String attribute, int value) {
        if (header.size() > 0 && attributes.contains(attribute)) {
            int index = header.indexOf(attribute);
            List<List<Integer>> newData = data.stream().filter(row -> row.get(index) == value).
                    collect(Collectors.toList());
            Set<String> newAttributes = new HashSet<>(attributes);
            newAttributes.remove(attribute);
            return new DataSet(header, newData, newAttributes);
        }
        return null;
    }

    public int getMajorityClass() {
        int positive = 0,negative = 0;
        for(List<Integer> row : data){
            if(row.get(rowSize-1) == 0)
                negative++;
            else
                positive++;
        }
        return positive > negative ? 1 : 0;
    }

    @Override
    public Iterator<List<Integer>> iterator() {
        return data.iterator();
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getRowCount() {
        return data.size();
    }

    public List<Integer> get(int index) {
        if (data != null)
            return data.get(index);
        return null;
    }
}
