package me.imgalvin.noelytra;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NoElytraServer implements DedicatedServerModInitializer {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private MinecraftServer server;

    @Override
    public void onInitializeServer() {
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
            server.getPlayerManager().getPlayerList().forEach(player -> {
                PlayerInventory inventory = player.getInventory();

                // Loop through the player's inventory
                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack stack = inventory.getStack(i);

                    // Check if the item is an Elytra
                    if (stack.getItem() == Items.ELYTRA) {
                        // Remove Elytra by setting the stack to empty
                        inventory.setStack(i, ItemStack.EMPTY);
                        System.out.println("[NoElytraServer] Removed Elytra from slot " + i + " in " + player.getName().getString() + "'s inventory.");
                    }
                }

                // Mark the inventory as dirty so the changes take effect
                inventory.markDirty();

                // Send updates to the player's screen
                player.currentScreenHandler.sendContentUpdates();
            });
        }
    }
}
