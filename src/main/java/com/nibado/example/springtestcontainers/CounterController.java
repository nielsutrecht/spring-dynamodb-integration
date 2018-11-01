package com.nibado.example.springtestcontainers;

import com.nibado.example.springtestcontainers.dto.Counter;
import com.nibado.example.springtestcontainers.dto.Counters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter")
public class CounterController {
    private final CounterRepository repository;

    public CounterController(CounterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Counters counters() {
        return new Counters(repository.findAll());
    }

    @GetMapping("/{counter}")
    public ResponseEntity<Counter> get(@PathVariable String counter) {
        return ResponseEntity.of(repository.get(counter));
    }

    @PostMapping("/{counter}")
    public Counter increment(@PathVariable String counter) {
        var opt = repository.get(counter);

        var inc = opt.map(c -> new Counter(c.getName(), c.getValue() + 1)).orElse(new Counter(counter, 1));

        repository.put(inc.getName(), inc.getValue());

        return inc;
    }

    @DeleteMapping("/{counter}")
    public ResponseEntity<Void> delete(@PathVariable String counter) {
        repository.delete(counter);
        return ResponseEntity.accepted().build();
    }
}
