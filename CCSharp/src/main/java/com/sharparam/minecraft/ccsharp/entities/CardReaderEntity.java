/*
 * CardReaderEntity.java
 *
 * Copyright © 2013 by Adam Hellberg <adam.hellberg@sharparam.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sharparam.minecraft.ccsharp.entities;

import com.sharparam.minecraft.ccsharp.blocks.CardReaderBlock;
import com.sharparam.minecraft.ccsharp.utils.CardHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;

import java.util.HashMap;
import java.util.UUID;

/**
 * User: Sharparam
 * Date: 2013-06-18
 * Time: 22:25
 */
public class CardReaderEntity extends InventoryEntity implements IPeripheral {


    private static final int INVENTORY_COLS = 9;
    private static final int INVENTORY_ROWS = 3;
    // private static final int INVENTORY_SIZE = INVENTORY_COLS * INVENTORY_ROWS;
    private static final int INVENTORY_STACK_SIZE = 1;

    private static final int MAX_NUM_RANGED_UPGRADES = 1024;

    public CardReaderEntity() {
        super(CardReaderBlock.UID, CardReaderBlock.NAME, INVENTORY_COLS, INVENTORY_ROWS, INVENTORY_STACK_SIZE);
    }

    public static void init() {
        GameRegistry.registerTileEntity(CardReaderEntity.class, CardReaderBlock.UID);
    }

    @Override
    public String getType() {
        return this.id;
    }

    @Override
    public String[] getMethodNames() {
        return new String[] {
            "get",
            "getNumSlots"
        };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
        Object[] result = null;

        switch (method) {
            case 0: // get
                if (arguments.length != 1)
                    throw new Exception("get: expected 1 argument, got " + arguments.length);
                else if (!(arguments[0] instanceof Double))
                    throw new Exception("get: bad argument #1 (expected number, got " + arguments[0].getClass() + ")");
                int index = (int) Math.floor((Double) arguments[0]);
                if (index < 1 || index > getSizeInventory())
                    throw new Exception("get: index out of range (expected 1-" + getSizeInventory() + ")");
                index--;
                ItemStack stack = getStackInSlot(index);
                HashMap<String, Object> data = CardHelper.getCardInfo(this, stack);
                result = new Object[] {data != null, data};
                break;
            case 1: // getNumSlots
                result = new Object[] {getSizeInventory()};
                break;
        }

        return result;
    }

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }
}
