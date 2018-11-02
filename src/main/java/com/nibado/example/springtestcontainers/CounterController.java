package com.nibado.example.springtestcontainers;

import com.nibado.example.springtestcontainers.dto.Counter;
import com.nibado.example.springtestcontainers.dto.Counters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter")
public class CounterController {
    private final CounterService service;

    public CounterController(CounterService service) {
        this.service = service;
    }

    @GetMapping
    public Counters counters() {
        return new Counters(service.getAll());
    }

    @GetMapping("/{counter}")
    public ResponseEntity<Counter> get(@PathVariable String counter) {
        return ResponseEntity.of(service.get(counter));
    }

    @PostMapping("/{counter}")
    public Counter increment(@PathVariable String counter) {
        return service.increment(counter);
    }

    @DeleteMapping("/{counter}")
    public ResponseEntity<Void> delete(@PathVariable String counter) {
        service.delete(counter);
        return ResponseEntity.accepted().build();
    }
}
