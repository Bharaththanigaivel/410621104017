package affordmed.example.averagecalculator.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AverageCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(AverageCalculatorService.class);

    private static final int WINDOW_SIZE = 10;
    private final List<Integer> window = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    // URLs for the different number types
    private final String primeUrl = "http://20.244.56.144/test/primes";
    private final String fibonacciUrl = "http://20.244.56.144/test/fibo";
    private final String evenUrl = "http://20.244.56.144/test/evens"; // Assume this is the URL for even numbers
    // Add URLs for random numbers if available

    public Map<String, Object> processNumberId(String numberid) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> response = new HashMap<>();

        // Log the received number ID
        logger.info("Processing number ID: {}", numberid);

        // Fetch numbers from the test server
        List<Integer> numbers = fetchNumbersFromTestServer(numberid);

        // Ignore the response if it took too long
        if (System.currentTimeMillis() - startTime > 500) {
            logger.warn("Request processing took too long. Returning empty response.");
            response.put("numbers", Collections.emptyList());
            response.put("windowPrevState", new ArrayList<>(window));
            response.put("windowCurrState", new ArrayList<>(window));
            response.put("avg", calculateAverage(window));
            return response;
        }

        response.put("numbers", numbers);

        List<Integer> windowPrevState = new ArrayList<>(window);

        // Add unique numbers to the window and maintain window size
        for (Integer number : numbers) {
            if (!window.contains(number)) {
                if (window.size() >= WINDOW_SIZE) {
                    window.remove(0);
                }
                window.add(number);
            }
        }

        response.put("windowPrevState", windowPrevState);
        response.put("windowCurrState", new ArrayList<>(window));
        response.put("avg", calculateAverage(window));

        // Log the final response
        logger.info("Response: {}", response);

        return response;
    }

    private List<Integer> fetchNumbersFromTestServer(String numberid) {
        try {
            String url = getUrlForNumberId(numberid);
            if (url == null) {
                logger.error("Invalid number ID: {}", numberid);
                return Collections.emptyList();
            }
            logger.info("Fetching numbers from URL: {}", url);
            Map<String, List<Integer>> response = restTemplate.getForObject(url, Map.class);
            logger.info("Received response: {}", response);
            return response != null ? response.get("numbers") : Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error fetching numbers from test server: ", e);
            return Collections.emptyList();
        }
    }

    private String getUrlForNumberId(String numberid) {
        switch (numberid.toLowerCase()) {
            case "p":
                return primeUrl;
            case "f":
                return fibonacciUrl;
            case "e":
                return evenUrl;
            // Add case for "r" if you have a URL for random numbers
            default:
                return null;
        }
    }

    private double calculateAverage(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
