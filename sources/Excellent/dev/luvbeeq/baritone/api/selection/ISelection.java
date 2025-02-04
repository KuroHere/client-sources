package dev.luvbeeq.baritone.api.selection;

import dev.luvbeeq.baritone.api.utils.BetterBlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;

/**
 * A selection is an immutable object representing the current selection. The selection is commonly used for certain
 * types of build commands, however it can be used for anything.
 */
public interface ISelection {

    /**
     * @return The first corner of this selection. This is meant to preserve the user's original first corner.
     */
    BetterBlockPos pos1();

    /**
     * @return The second corner of this selection. This is meant to preserve the user's original second corner.
     */
    BetterBlockPos pos2();

    /**
     * @return The {@link BetterBlockPos} with the lowest x, y, and z position in the selection.
     */
    BetterBlockPos min();

    /**
     * @return The opposite corner from the {@link #min()}.
     */
    BetterBlockPos max();

    /**
     * @return The size of this ISelection.
     */
    Vector3i size();

    /**
     * @return An {@link AxisAlignedBB} encompassing all blocks in this selection.
     */
    AxisAlignedBB aabb();

    /**
     * Returns a new {@link ISelection} expanded in the specified direction by the specified number of blocks.
     *
     * @param direction The direction to expand the selection.
     * @param blocks    How many blocks to expand it.
     * @return A new selection, expanded as specified.
     */
    ISelection expand(Direction direction, int blocks);

    /**
     * Returns a new {@link ISelection} contracted in the specified direction by the specified number of blocks.
     * <p>
     * Note that, for example, if the direction specified is UP, the bottom of the selection will be shifted up. If it
     * is DOWN, the top of the selection will be shifted down.
     *
     * @param direction The direction to contract the selection.
     * @param blocks    How many blocks to contract it.
     * @return A new selection, contracted as specified.
     */
    ISelection contract(Direction direction, int blocks);

    /**
     * Returns a new {@link ISelection} shifted in the specified direction by the specified number of blocks. This moves
     * the whole selection.
     *
     * @param direction The direction to shift the selection.
     * @param blocks    How many blocks to shift it.
     * @return A new selection, shifted as specified.
     */
    ISelection shift(Direction direction, int blocks);
}
