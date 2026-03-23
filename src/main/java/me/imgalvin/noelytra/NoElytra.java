package me.imgalvin.noelytra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Inventory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is the client-side code
 * As you notice, this looks the exact same as the server-side code
 * This is because when I tried to join them both together into one class the code would not work
 * This is probably for a future TODO
  */
public class NoElytra implements ClientModInitializer {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private MinecraftServer server;

	@Override
	public void onInitializeClient() {
		// Register the server started event
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);

		// Schedule the task to run every second, but only after the server has started
		scheduler.scheduleAtFixedRate(this::checkInventories, 1, 1, TimeUnit.SECONDS);
	}

	// This method will be called when the server starts
	private void onServerStarted(MinecraftServer minecraftServer) {
		this.server = minecraftServer;
		System.out.println("Server started, NoElytra mod initialized!");
	}

	private void checkInventories() {
		if (server != null) {
			// Loop through all players and check their inventories
			server.getPlayerList().getPlayers().forEach(player -> {
				Inventory inventory = player.getInventory();

				// Loop through the player's inventory
				for (int i = 0; i < inventory.getContainerSize(); i++) {
					net.minecraft.world.item.ItemStack stack = inventory.getItem(i);

					// Check if the item is an Elytra
					if (stack.getItem() == net.minecraft.world.item.Items.ELYTRA) {
						// Remove Elytra by setting the stack to empty
						inventory.setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
						System.out.println("[NoElytraServer] Removed Elytra from slot " + i + " in " + player.getName().getString() + "'s inventory.");
					}
				}

				// Mark the inventory as dirty so the changes take effect
				inventory.setChanged();
			});
		}
	}
}