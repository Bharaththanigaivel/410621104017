package affordmed.example.averagecalculator.Restcontroller;


import affordmed.example.averagecalculator.Service.AverageCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/numbers")
public class AverageCalculatorController {

    @Autowired
    private AverageCalculatorService averageCalculatorService;

    @GetMapping("/{numberid}")
    public Map<String, Object> getNumbers(@PathVariable String numberid) {
        return averageCalculatorService.processNumberId(numberid);
    }
}
