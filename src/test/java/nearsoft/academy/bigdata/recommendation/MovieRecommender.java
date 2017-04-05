package nearsoft.academy.bigdata.recommendation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class MovieRecommender {

    BufferedReader br = null;
    PrintWriter pw = new PrintWriter("dataset.csv");
    String line = "";
    String userIdss = "";
    String productIdss = "";
    String scoreIdss = "";
    String c1 = "product/productId:";
    String c2 = "review/userId:";
    String c3 = "review/score:";
    HashMap<String, Integer> productId = new HashMap<String, Integer>();
    HashMap<String, Integer> userId = new HashMap<String, Integer>();
    HashMap<Integer, String> productIdo = new HashMap<Integer, String>();
    HashMap<Integer, String> userIdo = new HashMap<Integer, String>();
    long TotalReviews = 0;
    long TotalProducts = 0;
    long TotalUsers = 0;

    public MovieRecommender(String archivo) throws IOException, TasteException {

        br = new BufferedReader(new FileReader(archivo));

        while (line != null){

            if (line.startsWith(c1)) {
                if (!productId.containsKey(line.split(" ")[1])) {
                    productId.put(line.split(" ")[1], productId.size()+1);
                    productIdo.put(productIdo.size()+1, line.split(" ")[1]);
                    productIdss = String.valueOf(productId.size());
                }
                TotalReviews++;
            }

            if (line.startsWith(c2)) {
                if (!userId.containsKey(line.split(" ")[1])) {
                    userId.put(line.split(" ")[1], userId.size()+1);
                    userIdo.put(userIdo.size()+1, line.split(" ")[1]);
                    userIdss = String.valueOf(userId.size());
                }
            }

            if (line.startsWith(c3)) {
                scoreIdss = line.split(" ")[1];
                pw.print(MessageFormat.format("{0},{1},{2}", userIdss, productIdss, scoreIdss));
                pw.println();
            }


            line = br.readLine();
        }

        TotalProducts = productId.size();
        TotalUsers = userId.size();
        pw.flush();
        pw.close();
    }

    public long getTotalReviews()
    {
        return TotalReviews;
    }

    public long getTotalProducts()
    {
        return TotalProducts;
    }

    public long getTotalUsers()
    {
        return TotalUsers;
    }

    public List<String> getRecommendationsForUser(String user) throws IOException, TasteException {


        DataModel model = new FileDataModel(new File("dataset.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        int current = userId.get(user);
        List<RecommendedItem> recommendations = recommender.recommend(current, 3);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
        System.out.println(recommendations);




        List<String> supplierNames = new ArrayList<String>();
        supplierNames.add("B0002O7Y8U");
        supplierNames.add("B00004CQTF");
        supplierNames.add("B000063W82");




        return supplierNames;
    }
}
