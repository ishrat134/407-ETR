import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TollCalculator {
    private static String jsonPath = "src/main/resources/interchanges.json";

    public Map<String, String> getLocationMap(JSONObject innerObj)  throws IOException, ParseException {
        Map<String,String> map = new HashMap<>();
        innerObj.keySet().forEach(keyStr->{
            JSONObject ob = (JSONObject) innerObj.get(keyStr);
            String name = (String) ob.get("name");
            String key = (String) keyStr;
            map.put(name,key);

        });
        return map;
    }

    public double calculateDistance(String start, String end, JSONObject jsonObject) throws NullPointerException{
        final double[] distance = new double[1];
        if(Integer.parseInt(start)<Integer.parseInt(end)) {
            jsonObject.keySet().forEach(keyStr -> {
                String key = (String) keyStr;
                if (Integer.parseInt(key) < Integer.parseInt(end)) {
                    JSONObject object = (JSONObject) jsonObject.get(keyStr);
                    JSONArray routes = (JSONArray) object.get("routes");
                    JSONObject toRoute = (JSONObject) routes.get(0);
                    distance[0] = distance[0] + (double) toRoute.get("distance");
                }

            });
        } else if (Integer.parseInt(start)>Integer.parseInt(end)) {
            jsonObject.keySet().forEach(keyStr -> {
                String key = (String) keyStr;
                if (Integer.parseInt(key) < Integer.parseInt(start)) {
                    JSONObject object = (JSONObject) jsonObject.get(keyStr);
                    JSONArray routes = (JSONArray) object.get("routes");
                    JSONObject toRoute=null;
                    if(routes.size()>1) {
                        toRoute = (JSONObject) routes.get(1);
                        distance[0] = distance[0] + (double) toRoute.get("distance");
                    }
                }
            });
        }

        return distance[0];
    }

    public double calculateCost(Double distance){
        return 0.25*distance;
    }

    public static void main(String[] args){

        if(args.length<2){
            System.out.println("please enter valid start and end points");
            System.exit(0);
        }

        String start = args[0].replaceAll("_"," ");
        String end = args[1].replaceAll("_"," ");

        try {
            TollCalculator tollCalculator = new TollCalculator();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonPath));
            JSONObject jsonObject = (JSONObject)obj;
            JSONObject innerObj = (JSONObject) jsonObject.get("locations");
            Map<String,String> mp = tollCalculator.getLocationMap(innerObj);
            String s = mp.get(start);
            String e = mp.get(end);
             if(s==null || e==null)
            {
                System.out.println("please enter valid start/end point");
                System.exit(0);
            }
            double dist = tollCalculator.calculateDistance(s,e, innerObj);
            double cost = tollCalculator.calculateCost(dist);
            System.out.println("distance: "+dist);
            System.out.println("cost: "+cost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
