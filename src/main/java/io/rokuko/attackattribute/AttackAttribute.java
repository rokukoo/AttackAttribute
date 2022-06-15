package io.rokuko.attackattribute;

import io.rokuko.attackattribute.config.Config;
import io.rokuko.attackattribute.handler.AttackHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.function.Consumer;

public class AttackAttribute extends JavaPlugin implements CommandExecutor {

    @Getter
    private static AttackAttribute instance;
    public Config config = new Config();

    public AttackAttribute() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config.refresh(this.getConfig());
        AttackHandler attackHandler = new AttackHandler(this);
        Bukkit.getServer().getPluginManager().registerEvents(attackHandler, this);
        Bukkit.getServer().getPluginManager().registerEvents(attackHandler.new PostHandler(), this);
        Bukkit.getServer().getPluginCommand("aa").setExecutor(this);
        getLogger().info("AttackAttribute has been enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            if(args.length > 0 && args[0].equals("reload")){
                this.reloadConfig();
                this.config.refresh(this.getConfig());
            } else {
                sender.sendMessage(new String[]{
                        "",
                        "§f§lAttackAttribute§7@Rokuko v1.0-SNAPSHOT",
                        "",
                        " §7命令: §f/aa §8[...]",
                        " §7参数:",
                        "   §8- §freload",
                        "     §7重载插件配置",
                        ""});
            }
        }
        return true;
    }
}
