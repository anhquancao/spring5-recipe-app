package vn.colorme.spring5recipeapp.services;

import vn.colorme.spring5recipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService  {
    Set<UnitOfMeasureCommand> listAllCommands();
}
