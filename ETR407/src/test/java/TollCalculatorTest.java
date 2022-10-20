import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.FileReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TollCalculatorTest {

    TollCalculator tollCalculator;
    private static final String jsonPath = "src/main/resources/interchanges.json";
    JSONObject innerObject;

    private String start;
    private String end;

    @BeforeEach
    void setUp(){
        try {
            tollCalculator = new TollCalculator();
            innerObject = new JSONObject();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonPath));
            JSONObject jsonObject = (JSONObject) obj;
            innerObject = (JSONObject) jsonObject.get("locations");
            Map<String,String> mp = tollCalculator.getLocationMap(innerObject);
            start = mp.get("QEW");
            end = mp.get("Dundas Street");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void testCalculateDistance(){

        assertEquals(6.062, tollCalculator.calculateDistance(start, end ,innerObject), 0);
    }

    @Test
    void testCalculateCost(){
        assertEquals(1.5155, tollCalculator.calculateCost(6.062), 0);
    }

}
