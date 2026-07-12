package com.dam.kineo.domain.usecase;

import com.dam.kineo.domain.repository.StepRepository;
import javax.inject.Inject;

public class UpdateStepsUseCase {
    private final StepRepository repository;

    @Inject
    public UpdateStepsUseCase(StepRepository repository) {
        this.repository = repository;
    }

    public void execute(int steps) {
        repository.saveOrUpdateSteps(steps);
    }
}
