import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.*;
import java.util.*;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        List<String[]> trainLabels = readAllLabels("question-4-train-labels.csv");
        List<String[]> allWords = readAllVocabulary("question-4-vocab.txt");

        int N = 0; //the total number of tweets in the training set

        for ( String[] row : trainLabels) {
            for (String cell : row) {
                N++;
            }
        }
        //System.out.println("N: " + N);

        int Npositive = 0; //the number of positive tweets in the training set

        for ( String[] row : trainLabels) {
            for (String cell : row) {
                if(cell.equals("positive")) Npositive++;
            }
        }
        System.out.println("Npositive: " + Npositive);

        int Nneutral = 0; //the number of neutral tweets in the training set

        for ( String[] row : trainLabels) {
            for (String cell : row) {
                if(cell.equals("neutral")) Nneutral++;
            }
        }
        System.out.println("Nneutral: " + Nneutral);

        int Nnegative = 0; //the number of negative tweets in the training set

        for ( String[] row : trainLabels) {
            for (String cell : row) {
                if(cell.equals("negative")) Nnegative++;
            }
        }
        System.out.println("Nnegative: " + Nnegative);

        double PIy_positive = (double) Npositive / (double) N; //estimates the probability that any particular tweet will be positive.
        System.out.println("PIy_positive: " + PIy_positive);

        double PIy_neutral = (double) Nneutral / (double) N; //estimates the probability that any particular tweet will be neutral.
        System.out.println("PIy_neutral: " + PIy_neutral);

        double PIy_negative = (double) Nnegative / (double) N; //estimates the probability that any particular tweet will be negative.
        System.out.println("PIy_negative: " + PIy_negative);

        //------------------------------------------------------------------------------------------

        int[] Tjpositive = new int[allWords.size()]; //the number of occurrences of the word j in positive tweets in the training set including the multiple occurrences
        int[] Tjneutral = new int[allWords.size()]; //the number of occurrences of the word j in neutral tweets in the training set including the multiple occurrences
        int[] Tjnegative = new int[allWords.size()]; //the number of occurrences of the word j in negative tweets in the training set including the multiple occurrences

        String train_feature_file_address = "question-4-train-features.csv";
        trainFeatureData(train_feature_file_address, trainLabels, Tjpositive, Tjneutral, Tjnegative); //multinomial model

        int SUMofAllWords_positive = 0;
        int SUMofAllWords_neutral = 0;
        int SUMofAllWords_negative = 0;

        //System.out.print("Tjpositive: ");
        for (int cell: Tjpositive) {
            SUMofAllWords_positive += cell;
            //System.out.print(cell + "\t");
        }
        //System.out.println();

        //System.out.print("Tjneutral: ");
        for (int cell: Tjneutral) {
            SUMofAllWords_neutral += cell;
            //System.out.print(cell + "\t");
        }
        //System.out.println();

        //System.out.print("Tjnegative: ");
        for (int cell: Tjnegative) {
            SUMofAllWords_negative += cell;
            //System.out.print(cell + "\t");
        }
        //System.out.println();

        //System.out.println("SUMofAllWords_positive: " + SUMofAllWords_positive);
        //System.out.println("SUMofAllWords_neutral: " + SUMofAllWords_neutral);
        //System.out.println("SUMofAllWords_negative: " + SUMofAllWords_negative);



        //------------------------------------------------------------------------------------------

        double[] THETAjy_positive = new double[allWords.size()];
        double[] THETAjy_neutral = new double[allWords.size()];
        double[] THETAjy_negative = new double[allWords.size()];

        int ii = 0;
        //System.out.print("THETAjy_positive: ");
        for (int i1 = 0; i1 < THETAjy_positive.length; i1++) {
            THETAjy_positive[i1] = (double) Tjpositive[ii] / SUMofAllWords_positive;
            //System.out.print(THETAjy_positive[i1] + "\t");
            ii++;
        }
        //System.out.println();
        ii = 0;
        //System.out.print("THETAjy_neutral: ");
        for (int i1 = 0; i1 < THETAjy_neutral.length; i1++) {
            THETAjy_neutral[i1] = (double) Tjneutral[ii] / SUMofAllWords_neutral;
            //System.out.print(THETAjy_neutral[i1] + "\t");
            ii++;
        }
        //System.out.println();
        ii = 0;
        //System.out.print("THETAjy_negative: ");
        for (int i1 = 0; i1 < THETAjy_negative.length; i1++) {
            THETAjy_negative[i1] = (double) Tjnegative[ii] / SUMofAllWords_negative;
            //System.out.print(THETAjy_negative[i1] + "\t");
            ii++;
        }
        //System.out.println();

        //------------------------------------------------------------------------------------------
        double[] THETAjy_positive_log = new double[allWords.size()];
        double[] THETAjy_neutral_log = new double[allWords.size()];
        double[] THETAjy_negative_log = new double[allWords.size()];

        ii = 0;
        //System.out.print("THETAjy_positive_log: ");
        for (int i1 = 0; i1 < THETAjy_positive_log.length; i1++) {
            if (THETAjy_positive[ii] != 0.0) {
                THETAjy_positive_log[i1] = Math.log(THETAjy_positive[ii]);
            } else {
                THETAjy_positive_log[i1] = 0.0;
            }
            //System.out.print(THETAjy_positive_log[i1] + "\t");
            ii++;
        }
        //System.out.println();
        ii = 0;
        //System.out.print("THETAjy_neutral_log: ");
        for (int i1 = 0; i1 < THETAjy_neutral_log.length; i1++) {
            if (THETAjy_neutral[ii] != 0.0) {
                THETAjy_neutral_log[i1] = Math.log(THETAjy_neutral[ii]);
            } else {
                THETAjy_neutral_log[i1] = 0.0;
            }
            //System.out.print(THETAjy_neutral_log[i1] + "\t");
            ii++;
        }
        //System.out.println();
        ii = 0;
        //System.out.print("THETAjy_negative_log: ");
        for (int i1 = 0; i1 < THETAjy_negative_log.length; i1++) {
            if (THETAjy_negative[ii] != 0.0) {
                THETAjy_negative_log[i1] = Math.log(THETAjy_negative[ii]);
            } else {
                THETAjy_negative_log[i1] = 0.0;
            }
            //System.out.print(THETAjy_negative_log[i1] + "\t");
            ii++;
        }
        //System.out.println();

        // END OF TRAIN

        String test_labels = "question-4-test-labels.csv";
        String test_features = "question-4-test-features.csv";

        System.out.println("\n    Naive Bayes MLE RESULTS: \n");
        NaiveBayesWithMLE(test_labels, test_features, THETAjy_positive_log, THETAjy_neutral_log, THETAjy_negative_log, PIy_positive, PIy_neutral, PIy_negative);

        //----------------------------------------------------------------------------------------------
        //PARAMETERS FOR MAP LOGIC

        double delta = 1.0;

        double[] THETAjy_positive_MAP = new double[allWords.size()];
        double[] THETAjy_neutral_MAP = new double[allWords.size()];
        double[] THETAjy_negative_MAP = new double[allWords.size()];

        int j = 0;
        //System.out.print("THETAjy_positive_MAP: ");
        for (int i1 = 0; i1 < THETAjy_positive_MAP.length; i1++) {
            THETAjy_positive_MAP[i1] = ((double) Tjpositive[j] + delta) / (SUMofAllWords_positive + (delta * (double)allWords.size()));
            //System.out.print(THETAjy_positive_MAP[i1] + "\t");
            j++;
        }
        //System.out.println();
        j=0;
        //System.out.print("THETAjy_neutral_MAP: ");
        for (int i1 = 0; i1 < THETAjy_neutral_MAP.length; i1++) {
            THETAjy_neutral_MAP[i1] = ((double) Tjneutral[j] + delta) / (SUMofAllWords_neutral + (delta * (double)allWords.size()));
            //System.out.print(THETAjy_neutral_MAP[i1] + "\t");
            j++;
        }
        //System.out.println();
        j=0;
        //System.out.print("THETAjy_negative_MAP: ");
        for (int i1 = 0; i1 < THETAjy_negative_MAP.length; i1++) {
            THETAjy_negative_MAP[i1] = ((double) Tjnegative[j] + delta) / (SUMofAllWords_negative + (delta * (double)allWords.size()));
            //System.out.print(THETAjy_negative_MAP[i1] + "\t");
            j++;
        }
        //System.out.println();

        double[] THETAjy_positive_MAP_log = new double[allWords.size()];
        double[] THETAjy_neutral_MAP_log = new double[allWords.size()];
        double[] THETAjy_negative_MAP_log = new double[allWords.size()];

        j=0;
        //System.out.print("THETAjy_positive_MAP_log: ");
        for (int i1 = 0; i1 < THETAjy_positive_MAP_log.length; i1++) {
            THETAjy_positive_MAP_log[i1] = Math.log(THETAjy_positive_MAP[j]);
            //System.out.print(THETAjy_positive_MAP_log[i1] + "\t");
            j++;
        }
        //System.out.println();
        j=0;
        //System.out.print("THETAjy_neutral_MAP_log: ");
        for (int i1 = 0; i1 < THETAjy_neutral_MAP_log.length; i1++) {
            THETAjy_neutral_MAP_log[i1] = Math.log(THETAjy_neutral_MAP[j]);
            //System.out.print(THETAjy_neutral_MAP_log[i1] + "\t");
            j++;
        }
        //System.out.println();
        j=0;
        //System.out.print("THETAjy_negative_MAP_log: ");
        for (int i1 = 0; i1 < THETAjy_negative_MAP_log.length; i1++) {
            THETAjy_negative_MAP_log[i1] = Math.log(THETAjy_negative_MAP[j]);
            //System.out.print(THETAjy_negative_MAP_log[i1] + "\t");
            j++;
        }
        //System.out.println();

        NaiveBayesWithMAP(test_labels, test_features, THETAjy_positive_MAP_log, THETAjy_neutral_MAP_log,THETAjy_negative_MAP_log, PIy_positive, PIy_neutral, PIy_negative);

        //----------------------------------------------------------------------------------------------
        //PARAMETERS FOR BAG OF WORDS LOGIC

        int[] Sjpositive = new int[allWords.size()]; //the number of occurrences of the word j in positive tweets in the training set NOT including the multiple occurrences
        int[] Sjneutral = new int[allWords.size()]; //the number of occurrences of the word j in neutral tweets in the training set NOT including the multiple occurrences
        int[] Sjnegative = new int[allWords.size()]; //the number of occurrences of the word j in negative tweets in the training set NOT including the multiple occurrences

        trainFeatureDataBagOfWords(train_feature_file_address, trainLabels, Sjpositive, Sjneutral, Sjnegative);

        double[] THETAjy_positive_BoW = new double[allWords.size()];
        double[] THETAjy_neutral_BoW = new double[allWords.size()];
        double[] THETAjy_negative_BoW = new double[allWords.size()];

        int k = 0;
        //System.out.print("THETAjy_positive_BoW: ");
        for (int i1 = 0; i1 < THETAjy_positive_BoW.length; i1++){
            THETAjy_positive_BoW[i1] = ((double) Sjpositive[k] / Npositive);
            //System.out.print(THETAjy_positive_BoW[i1] + "\t");
            k++;
        }
        //System.out.println();
        k = 0;
        //System.out.print("THETAjy_neutral_BoW: ");
        for (int i1 = 0; i1 < THETAjy_neutral_BoW.length; i1++){
            THETAjy_neutral_BoW[i1] = ((double) Sjneutral[k] / Nneutral);
            //System.out.print(THETAjy_neutral_BoW[i1] + "\t");
            k++;
        }
        //System.out.println();
        k = 0;
        //System.out.print("THETAjy_negative_BoW: ");
        for (int i1 = 0; i1 < THETAjy_negative_BoW.length; i1++){
            THETAjy_negative_BoW[i1] = ((double) Sjnegative[k] / Nnegative);
            //System.out.print(THETAjy_negative_BoW[i1] + "\t");
            k++;
        }
        //System.out.println();

        NaiveBayesWithBoW(test_labels, test_features, THETAjy_positive_BoW, THETAjy_neutral_BoW, THETAjy_negative_BoW, PIy_positive, PIy_neutral, PIy_negative);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //MOST COMMONLY USED 20 WORDS
        //

        Object[] mostpositive = new String[20];
        Object[] mostneutral = new String[20];
        Object[] mostnegative = new String[20];
        class ValueComparator implements Comparator<String> {

            Map<String, Integer> base;
            public ValueComparator(Map<String, Integer> base) {
                this.base = base;
            }

            public int compare(String a, String b) {
                if (base.get(a) >= base.get(b)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        //positive
        System.out.println("Most commonly used 20 words in Positive Class:");
        HashMap<String, Integer> positive_ratings = new HashMap<>();
        for (int i = 0; i < allWords.size(); i++) {
            positive_ratings.put(allWords.get(i)[0], Tjpositive[i]);
        }

        ValueComparator  bvc =  new ValueComparator(positive_ratings);
        TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);
        sorted_map.putAll(positive_ratings);

        mostpositive = sorted_map.keySet().toArray();
        for (int i = 1; i <= 20; i++) {
            Object s = mostpositive[i];
            System.out.println(i + ": " + s.toString().split("\t")[0]);
        }
        System.out.println();

        //neutral
        System.out.println("Most commonly used 20 words in Neutral Class:");
        HashMap<String, Integer> neutral_ratings = new HashMap<>();
        for (int i = 0; i < allWords.size(); i++) {
            neutral_ratings.put(allWords.get(i)[0], Tjneutral[i]);
        }

        ValueComparator  bvc1 =  new ValueComparator(neutral_ratings);
        TreeMap<String, Integer> sorted_map1 = new TreeMap<>(bvc1);
        sorted_map1.putAll(neutral_ratings);

        mostneutral = sorted_map1.keySet().toArray();
        for (int i = 1; i <= 20; i++) {
            Object s = mostneutral[i];
            System.out.println(i + ": " + s.toString().split("\t")[0]);
        }
        System.out.println();

        //negative
        System.out.println("Most commonly used 20 words in Negative Class:");
        HashMap<String, Integer> negative_ratings = new HashMap<>();
        for (int i = 0; i < allWords.size(); i++) {
            negative_ratings.put(allWords.get(i)[0], Tjnegative[i]);
        }

        ValueComparator  bvc2 =  new ValueComparator(negative_ratings);
        TreeMap<String, Integer> sorted_map2 = new TreeMap<>(bvc2);
        sorted_map2.putAll(negative_ratings);

        mostnegative = sorted_map2.keySet().toArray();
        for (int i = 1; i <= 20; i++) {
            Object s = mostnegative[i];
            System.out.println(i + ": " + s.toString().split("\t")[0]);
        }
        System.out.println();

        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private static void NaiveBayesWithBoW(String test_labels, String test_features, double[] thetAjy_positive_boW, double[] thetAjy_neutral_boW, double[] thetAjy_negative_boW, double PIy_positive, double PIy_neutral, double PIy_negative) {

        System.out.println("\n    Naive Bayes Bag of Words RESULTS: \n");
        List<String[]> testLabels = readAllLabels(test_labels);

        double[] positiveLikelihoodEstimator = new double[testLabels.size()];
        double[] neutralLikelihoodEstimator = new double[testLabels.size()];
        double[] negativeLikelihoodEstimator = new double[testLabels.size()];

        Arrays.fill(positiveLikelihoodEstimator, Math.log(PIy_positive));
        Arrays.fill(neutralLikelihoodEstimator, Math.log(PIy_neutral));
        Arrays.fill(negativeLikelihoodEstimator, Math.log(PIy_negative));

        try{
            FileReader fileReader = new FileReader(test_features);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            int column = 0;
            int row = 0;
            while((nextRecord = csvReader.readNext()) != null){
                for(String cell: nextRecord){
                    if(cell.equals("0")){
                        positiveLikelihoodEstimator[row] += (thetAjy_positive_boW[column] == 0.0) ? 0.0 : Math.log((1 - thetAjy_positive_boW[column]));
                        neutralLikelihoodEstimator[row] += (thetAjy_neutral_boW[column] == 0.0) ? 0.0 : Math.log((1 - thetAjy_neutral_boW[column]));
                        negativeLikelihoodEstimator[row] += (thetAjy_negative_boW[column] == 0.0) ? 0.0 : Math.log((1 - thetAjy_negative_boW[column]));
                    }else{
                        positiveLikelihoodEstimator[row] += (thetAjy_positive_boW[column] == 0.0) ? 0.0 : Math.log(thetAjy_positive_boW[column]);
                        neutralLikelihoodEstimator[row] += (thetAjy_neutral_boW[column] == 0.0) ? 0.0 : Math.log(thetAjy_neutral_boW[column]);
                        negativeLikelihoodEstimator[row] += (thetAjy_negative_boW[column] == 0.0) ? 0.0 : Math.log(thetAjy_negative_boW[column]);
                    }
                    column++;
                }
                column = 0;
                row++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.print("positiveLikelihoodEstimator: ");
        //for (double cell : positiveLikelihoodEstimator) {
            //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //System.out.print("neutralLikelihoodEstimator: ");
        //for (double cell : neutralLikelihoodEstimator) {
            //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //System.out.print("negativeLikelihoodEstimator: ");
        //for (double cell : negativeLikelihoodEstimator) {
            //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //----------------------------------------------------------------------------------------------
        //TEST RESULTS FOR BoW LOGIC

        int hit = 0;
        for (int i = 0; i < testLabels.size() ; i++) {
            String result ="";
            double max = Math.max(neutralLikelihoodEstimator[i], Math.max(positiveLikelihoodEstimator[i] , negativeLikelihoodEstimator[i]));
            if(max == neutralLikelihoodEstimator[i]){
                result = "neutral";
            }else{
                if(max == positiveLikelihoodEstimator[i]){
                    result = "positive";
                }else{
                    if(max == negativeLikelihoodEstimator[i]){
                        result = "negative";
                    }else{
                        System.out.println("Error - max value is corrupt");
                    }
                }
            }
            if(testLabels.get(i)[0].equals(result)){
                hit++;
            }
        }
        System.out.println("hits: " + hit);
        System.out.println("test size: " + testLabels.size());
        double Accuracy = (double) hit / (double) testLabels.size();
        System.out.println("ACCURACY: " + Accuracy);
        System.out.println();

    }

    private static void NaiveBayesWithMAP(String test_labels, String test_features, double[] thetAjy_positive_log, double[] thetAjy_neutral_log, double[] thetAjy_negative_log, double PIy_positive, double PIy_neutral, double PIy_negative) {
        System.out.println("\n    Naive Bayes MAP RESULTS: \n");
        NaiveBayesWithMLE(test_labels, test_features, thetAjy_positive_log, thetAjy_neutral_log, thetAjy_negative_log, PIy_positive, PIy_neutral, PIy_negative);
    }

    private static void NaiveBayesWithMLE(String test_labels, String test_features, double[] thetAjy_positive_log, double[] thetAjy_neutral_log, double[] thetAjy_negative_log, double PIy_positive, double PIy_neutral, double PIy_negative) {

        List<String[]> testLabels = readAllLabels(test_labels);

        double[] positiveLikelihoodEstimator = new double[testLabels.size()];
        double[] neutralLikelihoodEstimator = new double[testLabels.size()];
        double[] negativeLikelihoodEstimator = new double[testLabels.size()];

        Arrays.fill(positiveLikelihoodEstimator, Math.log(PIy_positive));
        Arrays.fill(neutralLikelihoodEstimator, Math.log(PIy_neutral));
        Arrays.fill(negativeLikelihoodEstimator, Math.log(PIy_negative));

        try{
            FileReader fileReader = new FileReader(test_features);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            int column = 0;
            int row = 0;
            while((nextRecord = csvReader.readNext()) != null){
                for(String cell :nextRecord){
                    if(cell.equals("0")){

                    }else {
                        positiveLikelihoodEstimator[row] += ((double)Integer.parseInt(cell) * thetAjy_positive_log[column]);
                        neutralLikelihoodEstimator[row] += ((double)Integer.parseInt(cell) * thetAjy_neutral_log[column]);
                        negativeLikelihoodEstimator[row] += ((double)Integer.parseInt(cell) * thetAjy_negative_log[column]);
                    }
                    column++;
                }
                column = 0;
                row++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.print("positiveLikelihoodEstimator: ");
        //for (double cell : positiveLikelihoodEstimator) {
                //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //System.out.print("neutralLikelihoodEstimator: ");
        //for (double cell : neutralLikelihoodEstimator) {
                //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //System.out.print("negativeLikelihoodEstimator: ");
        //for (double cell : negativeLikelihoodEstimator) {
                //System.out.print(cell + "\t");
        //}
        //System.out.println();

        //----------------------------------------------------------------------------------------------
        //TEST RESULTS FOR MLE LOGIC

        int hit = 0;
        for (int i = 0; i < testLabels.size() ; i++) {
            String result ="";
            double max = Math.max(neutralLikelihoodEstimator[i], Math.max(positiveLikelihoodEstimator[i] , negativeLikelihoodEstimator[i]));
            if(max == neutralLikelihoodEstimator[i]){
                result = "neutral";
            }else{
                if(max == positiveLikelihoodEstimator[i]){
                    result = "positive";
                }else{
                    if(max == negativeLikelihoodEstimator[i]){
                        result = "negative";
                    }
                }
            }
            if(testLabels.get(i)[0].equals(result)){
                hit++;
            }
        }
        System.out.println("hits: " + hit);
        System.out.println("test size: " + testLabels.size());
        double Accuracy = (double) hit / (double) testLabels.size();
        System.out.println("ACCURACY: " + Accuracy);

    }

    private static List<String[]> readAllVocabulary(String fileAdr) {

        LinkedList allWords = new LinkedList();
        try{

            File file = new File(fileAdr);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while((line = br.readLine())!= null){
                String[] nextLineAsTokens = new String[]{line};
                if (nextLineAsTokens != null) {
                    allWords.add(nextLineAsTokens);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return allWords;
    }

    public static List<String[]> readAllLabels(String file){

        List<String[]> allTestFeatures = null;

        try{
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReaderBuilder(fileReader).build();

            allTestFeatures = csvReader.readAll();

            /*for (String[] row : allTestFeatures) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

        return allTestFeatures;
    }

    public static void trainFeatureData(String file, List<String[]> trainLabels, int[] tjpositive, int[] tjneutral, int[] tjnegative){

        try{
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReader(fileReader);

            String[] nextRecord;
            int row = 0;
            int column = 0;
            while((nextRecord = csvReader.readNext()) != null){

                if(trainLabels.get(row)[0].equals("positive")){
                    //System.out.println("hit1");
                    for(String cell :nextRecord){
                        tjpositive[column] += Integer.parseInt(cell);
                        column++;
                    }
                }else{
                    if(trainLabels.get(row)[0].equals("neutral")){
                        //System.out.println("hit2");
                        for(String cell :nextRecord){
                            tjneutral[column] += Integer.parseInt(cell);
                            column++;
                        }
                    }else{
                        if(trainLabels.get(row)[0].equals("negative")){
                            //System.out.println("hit3");
                            for(String cell :nextRecord){
                                tjnegative[column] += Integer.parseInt(cell);
                                column++;
                            }
                        }
                        else{
                            System.out.println("Error - improper label");
                        }
                    }

                }

                row++;
                column = 0;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void trainFeatureDataBagOfWords(String file, List<String[]> trainLabels, int[] sjpositive, int[] sjneutral, int[] sjnegative) {

        try{
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReader(fileReader);

            String[] nextRecord;
            int row = 0;
            int column = 0;
            while((nextRecord = csvReader.readNext()) != null){
                if(trainLabels.get(row)[0].equals("positive")){
                    for (String cell: nextRecord){
                        sjpositive[column] += (cell.equals("0")) ? Integer.parseInt(cell): 1;
                        column++;
                    }
                }else{
                    if(trainLabels.get(row)[0].equals("neutral")){
                        for (String cell: nextRecord){
                            sjneutral[column] += (cell.equals("0")) ? Integer.parseInt(cell): 1;
                            column++;
                        }
                    }else{
                        if(trainLabels.get(row)[0].equals("negative")){
                            for (String cell: nextRecord){
                                sjnegative[column] += (cell.equals("0")) ? Integer.parseInt(cell): 1;
                                column++;
                            }
                        }else{
                            System.out.println("Error - improper label");
                        }
                    }
                }
                row++;
                column = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        /*System.out.print("Sjpositive: ");
        for (int cell: sjpositive
             ) {
            System.out.print(cell + "\t");
        }
        System.out.println();
        System.out.print("Sjneutral: ");
        for (int cell: sjneutral
                ) {
            System.out.print(cell + "\t");
        }
        System.out.println();
        System.out.print("Sjnegative: ");
        for (int cell: sjnegative
                ) {
            System.out.print(cell + "\t");
        }
        System.out.println();*/
    }
}
