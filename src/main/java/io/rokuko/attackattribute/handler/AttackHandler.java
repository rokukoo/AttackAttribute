package io.rokuko.attackattribute.handler;

import io.rokuko.attackattribute.AttackAttribute;
import io.rokuko.attackattribute.utils.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class AttackHandler implements Listener {

    private HashMap<UUID, Long> lastAttackTimeHashMap = new HashMap<>();

    private AttackAttribute attackAttribute;

    public AttackHandler(AttackAttribute attackAttribute) {
        this.attackAttribute = attackAttribute;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            String attributeLore = hasAttribute(itemInMainHand, attackAttribute.config.attackIntervalName());
            if(!attributeLore.isEmpty()){
                double attackInterval = StringUtils.extractDouble(attributeLore);
                if (lastAttackTimeHashMap.containsKey(player.getUniqueId())){
                    long lastAttackTime = this.lastAttackTimeHashMap.get(player.getUniqueId());
                    double tickInterval = (System.currentTimeMillis() - lastAttackTime) / 50f;
                    // 如果攻击间隔小于攻击间隔，则不会受到伤害
                    if (tickInterval < attackInterval){
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 1, 1);
                    } else {
                        player.setCooldown(itemInMainHand.getType(), (int) tickInterval);
                        lastAttackTimeHashMap.put(player.getUniqueId(), System.currentTimeMillis());
                    }
                } else {
                    lastAttackTimeHashMap.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    public String hasAttribute(ItemStack itemStack, String attributeName) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.contains(attributeName)) return lore;
            }
        }
        return "";
    }

    @EventHandler
    public void onMove(PlayerInteractEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.setLore(Arrays.asList("§f攻击冷却: 60", "§f攻击冷却缩减: 10%", "§f攻击距离: 10"));
        itemInHand.setItemMeta(itemMeta);
    }

}
