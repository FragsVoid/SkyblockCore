package me.skyblockcore.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.util.io.Closer;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class WorldEditHook {


    public static void save(Location primary, Location sedcondary, File schematicFile) {
        Region region = new CuboidRegion(BukkitAdapter.asBlockVector(primary), BukkitAdapter.asBlockVector(sedcondary));
        EditSession editSession = createEditSession(primary.getWorld());

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());

        try {
            Operations.complete(copy);
        } catch (final WorldEditException e) {
            e.printStackTrace();
        }

        try (Closer closer = Closer.create()) {
            final FileOutputStream outputStream = closer.register(new FileOutputStream(schematicFile));
            final ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(outputStream));

            writer.write(clipboard);


        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    public static void paste(Location to, File schematicFile) {
        try (EditSession session = createEditSession(to.getWorld())) {

            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));

            Clipboard schematic = reader.read();

            Operation operation = new ClipboardHolder(schematic).createPaste(session).
                    to(BukkitAdapter.asBlockVector(to)).build();

            Operations.complete(operation);
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }


    private static EditSession createEditSession(World bukkitWorld) {
        final com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);
        final EditSession session = WorldEdit.getInstance().newEditSession(world);
        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }
}
