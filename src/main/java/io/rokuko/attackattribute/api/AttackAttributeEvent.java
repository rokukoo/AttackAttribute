package io.rokuko.attackattribute.api;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AttackAttributeEvent {

    @Getter
    public static class PreAttack extends Event implements Cancellable {

        private static final HandlerList handlers = new HandlerList();

        private Player player;
        private Entity entity;
        private double attackInterval;
        private double attackIntervalReduction;
        private double attackDistance;
        private boolean cancelled;

        public PreAttack(Player player, Entity entity, Double attackInterval, Double attackIntervalReduction, Double attackDistance) {
            this.player = player;
            this.entity = entity;
            this.attackInterval = attackInterval;
            this.attackIntervalReduction = attackIntervalReduction;
            this.attackDistance = attackDistance;
        }


        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }

        @Override
        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }

    }

}
