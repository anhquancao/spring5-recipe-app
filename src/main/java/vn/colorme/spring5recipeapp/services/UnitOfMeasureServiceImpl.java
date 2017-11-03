package vn.colorme.spring5recipeapp.services;

import org.springframework.stereotype.Service;
import vn.colorme.spring5recipeapp.commands.UnitOfMeasureCommand;
import vn.colorme.spring5recipeapp.converter.UnitOfMeasureToUnitOfMeasureCommand;
import vn.colorme.spring5recipeapp.domain.UnitOfMeasure;
import vn.colorme.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }


    @Override
    public Set<UnitOfMeasureCommand> listAllCommands() {
        Spliterator<UnitOfMeasure> unitOfMeasureSpliterator = unitOfMeasureRepository.findAll().spliterator();
        Stream<UnitOfMeasure> unitOfMeasureStream = StreamSupport.stream(unitOfMeasureSpliterator, false);
        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureStream.map(unitOfMeasureToUnitOfMeasureCommand::convert).collect(Collectors.toSet());
        return unitOfMeasureCommands;
    }
}
