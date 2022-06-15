package io.rokuko.attackattribute.handler;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor(staticName = "of")
public class UUidItemStackHolder extends Object{

    private UUID uuid;
    private ItemStack itemStack;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        result = prime * result + ((itemStack == null) ? 0 : itemStack.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null && getClass() != obj.getClass())
            return false;
        UUidItemStackHolder other = (UUidItemStackHolder) obj;
        if (this.uuid.equals(other.uuid) && this.itemStack.equals(other.itemStack)) return true;
        return false;
    }

}
