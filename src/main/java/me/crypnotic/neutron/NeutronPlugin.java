package me.crypnotic.neutron;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import lombok.Getter;
import me.crypnotic.neutron.api.Neutron;
import me.crypnotic.neutron.command.AlertCommand;
import me.crypnotic.neutron.command.FindCommand;
import me.crypnotic.neutron.command.InfoCommand;
import me.crypnotic.neutron.command.ListCommand;
import me.crypnotic.neutron.command.MessageCommand;
import me.crypnotic.neutron.command.SendCommand;
import me.crypnotic.neutron.command.ServerCommand;
import me.crypnotic.neutron.manager.LocaleManager;
import me.crypnotic.neutron.manager.ModuleManager;

@Plugin(id = "@ID@", name = "@NAME@", version = "@VERSION@")
public class NeutronPlugin {

    @Inject
    @Getter
    private ProxyServer proxy;
    @Inject
    @Getter
    private Logger logger;
    @Inject
    @DataDirectory
    @Getter
    private Path dataFolderPath;

    @Getter
    private LocaleManager localeManager;
    @Getter
    private ModuleManager moduleManager;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        Neutron.setNeutron(this);

        this.localeManager = new LocaleManager();
        this.moduleManager = new ModuleManager();

        if (!moduleManager.init()) {
            logger.warn("Failed to initialize ModuleManager");
            return;
        }
        
        if (!localeManager.init()) {
            logger.warn("Failed to initialize LocaleManager. Default values will be used");
        }

        registerCommands();
    }

    private void registerCommands() {
        CommandManager commandManager = proxy.getCommandManager();

        commandManager.register(new AlertCommand(), "alert");
        commandManager.register(new FindCommand(), "find");
        commandManager.register(new InfoCommand(), "info");
        commandManager.register(new ListCommand(), "glist");
        commandManager.register(new MessageCommand(), "message", "msg", "tell", "whisper");
        commandManager.register(new SendCommand(), "send");
        commandManager.register(new ServerCommand(), "server");
    }
}
