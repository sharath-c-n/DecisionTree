import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class contains utility functions to parse data
 * @author Sharath
 */
public class Util {
    public static final String COMMA = ",";
    public static DataSet createDataSet(String csvFile) throws IOException {
        return createDataSet(csvFile,COMMA);
    }

    /**
     * REads the input file which is numerical and creates a data set out of it
     * @param csvFile : file to be parsed
     * @param splitBy : The separator.
     * @return : Data set.
     * @throws IOException
     */
    public static DataSet createDataSet(String csvFile, String splitBy) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line = br.readLine();
        DataSet DataSet = new DataSet();
        if(line !=null){
            DataSet.setHeader(Arrays.asList(line.split(splitBy)));
        }
        else
        {
            return null;
        }
        while ((line = br.readLine()) != null) {
            IntStream inputData = Arrays.stream(line.split(splitBy)).mapToInt(Integer::parseInt);
            List<Integer> row = inputData.mapToObj(i->i).collect(Collectors.toList());
            DataSet.addRow(row);
        }
        return DataSet;
    }
}
