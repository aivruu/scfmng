package io.github.aivruu.scfmng;

import io.github.aivruu.scfmng.controller.client.ClientController;
import io.github.aivruu.scfmng.controller.DatabaseController;
import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.controller.QuotesController;
import io.github.aivruu.scfmng.controller.client.ClientQuotesController;
import io.github.aivruu.scfmng.model.Client;

import java.util.logging.Logger;

public final class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static DatabaseController databaseController;
  private static EquipmentController equipmentController;
  private static ClientController clientController;
  private static QuotesController quotesController;
  private static ClientQuotesController clientQuotesController;

	public static void main(final String[] args) {
		databaseController = new DatabaseController();
    // here should go your database-parameters.
		if (!databaseController.connect("localhost", (short) 3306, "test", "root", "")) {
			LOGGER.severe("Failed to connect to the database, or already it is connected.");
			return;
		}
		LOGGER.info("Connected to the database successfully.");
    quotesController = new QuotesController(databaseController);
    if (!quotesController.start()) {
      LOGGER.severe("Failed to create quotes-table in the database.");
      return;
    }
    clientController = new ClientController(databaseController);
    if (!clientController.start()) {
      LOGGER.severe("Failed to create client-table in the database.");
      return;
    }
    equipmentController = new EquipmentController(databaseController);
    if (!equipmentController.start()) {
      LOGGER.severe("Failed to create equipment-table in the database.");
      return;
    }
    LOGGER.info("Required database-tables were created successfully.");
    clientQuotesController = new ClientQuotesController(clientController, quotesController);
		final Client client = clientController.findClientData("123").join(); // await for operation result.
		if (client == null) {
			return;
		}
		LOGGER.info("Client found: " + client.id());
	}
}
