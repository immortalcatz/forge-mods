/**
 * Copyright (C) Jon Rowlett. All rights reserved.
 */

package de.shittyco.morematerials;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Common Game related utility methods.
 * @author jrowlett
 *
 */
public final class GameUtility {

    /**
     * Helper to get an item from its block.
     * @param name the id of the block.
     * @return the matching item.
     */
    public static Item getItemFromBlock(final String name) {
        return GameRegistry.findItem("morematerials", name);
    }

    /**
     * Prevents an instance of the GameUtility class from
     * being instantiated.
     */
    private GameUtility() {
    }
}
