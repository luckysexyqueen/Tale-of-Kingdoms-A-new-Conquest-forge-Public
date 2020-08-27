package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */

public class SchematicHandler {

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link OperationInstance}
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @param position the {@link BlockVector3} position to paste at
     * @return {@link CompletableFuture} containing the {@link OperationInstance}
     */
	public static CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockVector3 position) {
        CompletableFuture<OperationInstance> cf = new CompletableFuture<>();

        // WorldEdit requires actions to be done on the server thread.
        MinecraftClient.getInstance().getServer().execute(() -> {
            TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
            World adaptedWorld = FabricAdapter.adapt(player.getServerWorld());
            ClipboardFormat format = ClipboardFormats.findByFile(schematic.getFile());
            try {
                Clipboard clipboard = format.getReader(new FileInputStream(schematic.getFile())).read();
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);

                clipboard.setOrigin(position); // Set this so the region returned is correct.

                BlockVector3 centerY = clipboard.getRegion().getCenter().toBlockPoint();
                System.out.println(centerY); // Mainly debug, can be used to find the schematic in the world

                Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                        .to(position)
                        .ignoreAirBlocks(false)
                        .build();
                final UUID uuid = UUID.randomUUID();
                Operations.complete(operation);
                editSession.flushSession();
                cf.complete(new OperationInstance(uuid, clipboard.getRegion()));
            } catch (WorldEditException | IOException e) {
                e.printStackTrace();
            }
        });
		return cf;
	}

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link OperationInstance}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayerEntity, BlockVector3) 
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link OperationInstance}
     */
	public static CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, ServerPlayerEntity player) {
	    BlockVector3 position = BlockVector3.at(player.getBlockPos().getX(), player.getBlockPos().getY() + 1, player.getBlockPos().getZ());
	    return pasteSchematic(schematic, player, position);
    }
}
