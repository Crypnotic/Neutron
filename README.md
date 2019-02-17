# Neutron | An open-source essential suite for the Velocity proxy
[![Build Status](https://travis-ci.org/Crypnotic/Neutron.svg?branch=master)](https://travis-ci.org/Crypnotic/Neutron)

## What is Neutron?
Neutron is an open-source plugin written to try to provide the bare essentials for servers using Velocity.

## How do I get it?
You can download Neutron from my GitHub: 
https://github.com/Crypnotic/Neutron/releases/tag/1.0.1-ALPHA

_Neutron just entered its first alpha, expect issues when using it for now_

## What does it offer?
Currently, Neutron offers 6 commands:
  * `alert` - broadcasts a message to everyone connected to the proxy
  * `find` - allows you to find what server another player is on
  * `info` - provides information regarding a player
  * `glist` - shows a list of all servers along with how many players are connected
  * `message` - sends a private message to another player
  * `send`- sends either a player/ a server/ or everyone to a specified server

Alongside the commands, Neutron offers 2 modules:
  * Server list:
    * Provide a custom MOTD for your server in the multiplayer server list
    * Change the max players online:
      * `Static`: player count will always be the number defined under `max-player-count` 
      * `Current`: player count matches the number of players online
      * `OneMore`: player count shows the number of players online plus 1 
    * Change the message shown when the mouse hovers over the online player count:
      * `Message`: preview will show the messages defined under `preview-messages`
      * `Players`: preview matches the vanilla server preview of showing online players
      * `Empty`: preview is empty
  * Announcements:
    * Announcements will be sent to all online players at set intervals
    * Multiple lists can be defined, which can run a different intervals with different messages
    * You can choose whether a list will maintain its order or be randomized
    * Each list can have a `prefix`, which will be added to the beginning of every message in the list

_Modules can be `enabled/disabled` in the `config.conf`_
_All modules support reloading with the `/velocity reload` command_

## Permissions
Permissions are simply in the way that all command permissions are `neutron.command.{command}`

For example: the permission for alert is `neutron.command.alert`

## Lets talk about Locales
Neutron has a built in Localization manager that allows all messages to be translated per player**

\** Messages can only be translated if the player's locale has been added to `neutron/locales`
The English(`en_US`) localization is default and provided with the plugin.

To add another localization, copy the existing `en_US.conf` file and rename it to the name of the locale you would like to support. Note: it is easier to use the `/info` command on a player to figure out the country tag to use for the name of the file

## Issues
If you run into any issues, please report them on Github: https://github.com/Crypnotic/Neutron/issues

I'm usually lurking around the Velocity discord if you want to chat about anything regarding Neutron