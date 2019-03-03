# Neutron | An open-source essential suite for the Velocity proxy
[![Build Status](https://travis-ci.org/Crypnotic/Neutron.svg?branch=master)](https://travis-ci.org/Crypnotic/Neutron)

## What is Neutron?
Neutron is an open-source plugin written to try to provide the bare essentials for servers using Velocity.

## How do I get it?
You can download Neutron from the CodeMC Jenkins: 
https://ci.codemc.org/job/Crypnotic/job/Neutron/lastSuccessfulBuild/

_Neutron just entered its first release, there may still be lingering bugs_

## What does it offer?
Currently, Neutron offers 6 commands:
  * `alert` - broadcasts a message to everyone connected to the proxy
  * `find` - allows you to find what server another player is on
  * `info` - provides information regarding a player
  * `glist` - shows a list of all servers along with how many players are connected
  * `message` - sends a private message to another player
  * `send`- sends either a player, a server, or everyone to a specified server

Alongside the commands, Neutron offers 4 modules:
  * Announcements:
    * Announcements will be sent to all online players at set intervals
    * Multiple lists can be defined, which can run a different intervals with different messages
    * You can choose whether a list will maintain its order or be randomized
    * Each list can have a `prefix`, which is added to the beginning of every message in the list
  * Commands:
    * Allows the customization of all command aliases
    * Disable each command individually, or disable them all
  * Locales:
    * Translate all messages per player using pre-defined localizations
    * English(`en_US`) is default and currently the only locale shipped with the plugin
    * Adding locales:
      1. Copy the existing `en_US.conf` file and rename it to the target locale
      2. Change any messages to your liking
      3. Save the file, and run /velocity reload in-game
  * Server list:
    * Provide a custom MOTD for your server in the multiplayer server list
    * Change the max players online:
      * `Current`: player count matches the number of players online
      * `OneMore`: player count shows the number of players online plus 1 
      * `Ping`: ping all online backend servers and sum the max player counts
      * `Static`: player count will always be the number defined under `max-player-count` 
    * Change the message shown when the mouse hovers over the online player count:
      * `Message`: preview will show the messages defined under `preview-messages`
      * `Players`: preview matches the vanilla server preview of showing online players
      * `Empty`: preview is empty

_Modules can be `enabled/disabled` in the `config.conf`_
_All modules support reloading with the `/velocity reload` command_

## Permissions
Permissions are simply in the way that all command permissions are `neutron.command.{command}`

For example: the permission for alert is `neutron.command.alert`

## Issues
If you run into any issues, please report them on Github: https://github.com/Crypnotic/Neutron/issues

I'm usually lurking around the Velocity discord if you want to chat about anything regarding Neutron