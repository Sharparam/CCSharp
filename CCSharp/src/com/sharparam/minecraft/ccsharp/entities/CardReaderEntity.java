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

import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

/**
 * User: Sharparam
 * Date: 2013-06-18
 * Time: 22:25
 */
public class CardReaderEntity extends BaseEntity implements IPeripheral {
    public static final String ID = "cardReader";

    public CardReaderEntity() {
        super(ID, "Card Reader");
    }

    public static void init() {
        GameRegistry.registerTileEntity(CardReaderEntity.class, ID);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public String getType() {
        return this.id;
    }

    @Override
    public String[] getMethodNames() {
        return new String[] {
            "test"
        };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
        Object[] result = new Object[0];

        switch (method) {
            case 0:
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("foo", "baz");
                result = new Object[] {map};
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
        System.out.println("attach called on CardReaderEntity!");
    }

    @Override
    public void detach(IComputerAccess computer) {

    }
}
