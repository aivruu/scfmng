package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.database.StatementConstants;
import io.github.aivruu.scfmng.model.Equipment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@link Controller} implementation for general equipment-management operations.
 *
 * @since 0.0.1
 */
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

  /**
   * Retrieves all existing equipments from the database.
   *
   * @return A {@link CompletableFuture} that will complete with a {@link List} of {@link Equipment}
   * or empty if there are no-equipments created.
   * @since 0.0.1
   */
  public CompletableFuture<List<Equipment>> getAllExisting() {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.ALL_EQUIPMENTS_RETRIEVING)) {
        final List<Equipment> existingEquipments = new ArrayList<>();
        try (final ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            existingEquipments.add(new Equipment(resultSet.getString(1), resultSet.getString(2),
               resultSet.getString(5), resultSet.getFloat(4), resultSet.getInt(3)));
          }
          return existingEquipments;
        }
      } catch (final SQLException existing) {
        existing.printStackTrace();
        return null;
      }
    });
  }

  /**
   * Searches the information for the equipment with the provided id.
   *
   * @param equipmentId the equipment's id.
   * @return A {@link CompletableFuture} that will complete with the {@link Equipment} if found,
   * {@code null} otherwise.
   * @since 0.0.1
   */
  public CompletableFuture<Equipment> findEquipment(final String equipmentId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_RETRIEVE)) {
        statement.setString(1, equipmentId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          if (!resultSet.next()) {
            return null;
          }
          return new Equipment(equipmentId, resultSet.getString(1), resultSet.getString(4),
             resultSet.getFloat(2), resultSet.getInt(3));
        }
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return null;
      }
    });
  }

  /**
   * Saves the provided {@link Equipment}'s into the database.
   *
   * @param equipment the equipment to save.
   * @return A {@link CompletableFuture} that will complete with {@code true} if the operation was
   * succesful, {@code false} otherwise.
   * @since 0.0.1
   */
  public CompletableFuture<Boolean> saveEquipment(final Equipment equipment) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_SAVE)) {
        statement.setString(1, equipment.id());
        statement.setString(2, equipment.description());
        statement.setFloat(3, equipment.unitaryCost());
        statement.setInt(4, equipment.stock());
        statement.setString(5, equipment.manufacturer());
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  /**
   * Deletes the equipment with the provided id from the database.
   *
   * @param equipmentId the equipment's id.
   * @return A {@link CompletableFuture} that will complete with {@code true} if the operation was
   * succesful, {@code false} otherwise.
   * @since 0.0.1
   */
  public CompletableFuture<Boolean> deleteEquipment(final String equipmentId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.EQUIPMENT_INFORMATION_DELETE)) {
        statement.setString(1, equipmentId);
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }
}
