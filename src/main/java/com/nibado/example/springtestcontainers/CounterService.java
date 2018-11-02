package com.nibado.example.springtestcontainers;

import com.nibado.example.springtestcontainers.dto.Counter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CounterService {
    private final CounterRepository repository;

    public CounterService(CounterRepository repository) {
        this.repository = repository;
    }

    public List<Counter> getAll() {
        return repository.findAll().stream().map(CounterService::map).collect(Collectors.toList());
    }

    public Optional<Counter> get(String counter) {
        return repository.get(counter).map(CounterService::map);
    }

    public Counter increment(String counter) {
        var opt = repository.get(counter);

        var inc = opt.map(c -> new Counter(c.getCounter(), c.getValue() + 1)).orElse(new Counter(counter, 1));

        repository.put(inc.getName(), inc.getValue());

        return inc;
    }

    public void delete(String counter) {
        repository.delete(counter);
    }

    private static Counter map(CounterRepository.CounterEntity entity) {
        return new Counter(entity.getCounter(), entity.getValue());
    }
}
