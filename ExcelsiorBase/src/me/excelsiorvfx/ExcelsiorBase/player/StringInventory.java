package me.excelsiorvfx.ExcelsiorBase.player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;

import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class StringInventory {

	public static String invToString(Inventory inv) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        NBTTagList itemList = new NBTTagList();
        
        for (int i = 0; i < inv.getSize(); i++) {
        	NBTTagCompound outputObject = new NBTTagCompound();
        	CraftItemStack craft = ((CraftItemStack) inv.getContents()[i]);
        	
        	if (craft != null) {
        		CraftItemStack.asNMSCopy(craft).save(outputObject);
        	} itemList.add(outputObject);
        }
        
        
        
        return Base64Coder.encodeLines(outputStream.toByteArray());
	}

}
