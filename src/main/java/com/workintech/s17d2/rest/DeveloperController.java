package com.workintech.s17d2.rest;


import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final Taxable taxable;
    public Map<Integer, Developer> developers = new HashMap<>();

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers.put(1, new JuniorDeveloper(1, "John", 5000));
        developers.put(2, new MidDeveloper(2, "Jane", 8000));
        developers.put(3, new SeniorDeveloper(3, "Doe", 12000));
    }

    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    // Get all developers
    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developers.values().stream().toList();
    }

    // Get developer by ID
    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable Integer id) {
        return developers.get(id);
    }

    // Add a new developer
    @PostMapping
    public ResponseEntity<Developer> addDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();
        Experience experience = developer.getExperience();

        switch (experience) {
            case JUNIOR:
                salary -= salary * (taxable.getSimpleTaxRate() / 100);
                developer = new JuniorDeveloper(developer.getId(), developer.getName(), salary);
                break;
            case MID:
                salary -= salary * (taxable.getMiddleTaxRate() / 100);
                developer = new MidDeveloper(developer.getId(), developer.getName(), salary);
                break;
            case SENIOR:
                salary -= salary * (taxable.getUpperTaxRate() / 100);
                developer = new SeniorDeveloper(developer.getId(), developer.getName(), salary);
                break;
        }

        developers.put(developer.getId(), developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(developers.get(developer.getId()));
    }


    // Update a developer by ID
    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        developers.put(id, developer);
        return developers.get(id);
    }

    // Delete a developer by ID
    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable Integer id) {
        return developers.remove(id);
    }

    // Additional endpoint to check course name and developer name
    @GetMapping("/info")
    public String getInfo() {
        return "Course Name: Spring Boot Developer API, Developer: Your Name";
    }
}
