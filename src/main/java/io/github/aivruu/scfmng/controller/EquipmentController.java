package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.abstraction.StatementConstants;
import io.github.aivruu.scfmng.model.Equipment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public final class EquipmentController extends Controller {
  public EquipmentController(final DatabaseController databaseController) {
    super(databaseController);
  }

  @Override
  public boolean start() {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = super.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_TABLE_CREATE)) {
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    }).join();
  }

  public CompletableFuture<Equipment> findEquipment(final String equipmentId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_RETRIEVE)) {
        statement.setString(1, equipmentId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          if (!resultSet.next()) {
            return null;
          }
          return new Equipment(equipmentId, resultSet.getString(2), resultSet.getString(5),
             resultSet.getFloat(3), resultSet.getInt(4));
        }
      } catch (final SQLException exception) {
        return null;
      }
    });
  }

  public CompletableFuture<Boolean> saveEquipment(final Equipment equipment) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_SAVE)) {
        statement.setString(1, equipment.id());
        statement.setString(2, equipment.description());
        statement.setFloat(3, equipment.unitaryCost());
        statement.setInt(4, equipment.stock());
        statement.setString(5, equipment.manufacturer());
        return statement.executeUpdate() > 0;
      } catch (final SQLException exception) {
        return false;
      }
    });
  }

  public CompletableFuture<Boolean> deleteEquipment(final String equipmentId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_DELETE)) {
        statement.setString(1, equipmentId);
        return statement.executeUpdate() > 0;
      } catch (final SQLException exception) {
        return false;
      }
    });
  }
}
