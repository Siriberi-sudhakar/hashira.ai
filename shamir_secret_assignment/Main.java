import java.io.*;
import java.util.*;
import java.math.BigInteger; 
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {

    public static void main(String[] args) {
        String[] files = { "testcase1.json", "testcase2.json" };

        for (String file : files) {
            try {
                // Parse JSON
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(file));
                JSONObject keys = (JSONObject) jsonObject.get("keys");
                int k = Integer.parseInt(keys.get("k").toString());

                List<Integer> xList = new ArrayList<>();
                List<Double> yList = new ArrayList<>();

                for (Object keyObj : jsonObject.keySet()) {
                    String key = keyObj.toString();
                    if (!key.equals("keys")) {
                        int x = Integer.parseInt(key);
                        JSONObject point = (JSONObject) jsonObject.get(key);
                        int base = Integer.parseInt(point.get("base").toString());
                        String value = point.get("value").toString();
                        // Use BigInteger to parse large base numbers
                        BigInteger bigY = new BigInteger(value, base);
                        double y = bigY.doubleValue(); 
                        xList.add(x);
                        yList.add(y);
                    }
                }

                // Take first k points
                double[][] points = new double[k][2];
                for (int i = 0; i < k; i++) {
                    points[i][0] = xList.get(i);
                    points[i][1] = yList.get(i);
                }

                // Calculate constant term using Lagrange Interpolation
                double secret = lagrangeInterpolation(points);
                System.out.println("Secret from " + file + " = " + Math.round(secret));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static double lagrangeInterpolation(double[][] points) {
        double result = 0.0;

        for (int i = 0; i < points.length; i++) {
            double xi = points[i][0], yi = points[i][1];
            double term = yi;

            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    double xj = points[j][0];
                    term *= (0 - xj) / (xi - xj); // Since we want f(0), plug x=0
                }
            }

            result += term;
        }

        return result;
    }
}
