package vn.colorme.spring5recipeapp.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import vn.colorme.spring5recipeapp.commands.UnitOfMeasureCommand;
import vn.colorme.spring5recipeapp.domain.UnitOfMeasure;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure == null)
            return null;
        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription(unitOfMeasure.getDescription());
        unitOfMeasureCommand.setId(unitOfMeasure.getId());
        return unitOfMeasureCommand;
    }
}
