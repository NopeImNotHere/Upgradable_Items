***
# EFFICTIVE IMMEDIATELTY THIS REPO IS ARCHIVED, NO FURTHER UPDATES, NO FURTHER SUPPORT
***
## Upgradable_Items - Plugin to replace the Smithing Table with Customizable upgrade Paths

[![GitHub release](https://img.shields.io/github/release/NopeImNotHere/Upgradable_Items?include_prereleases=&sort=semver&color=green)](https://github.com/NopeImNotHere/Upgradable_Items/releases/)
[![License](https://img.shields.io/badge/License-MIT-blue)](#license)

By using the Spigot Framework, Citizens Library, and Redempts Library, Upgradable Items provides a Plugin which gives the ability to open a GUI and spawn NPCs
that have that GUI. This GUI acts like a Smithing Table in Minecraft but with greater Customizability. This is achieved by mimicking the Smithing Table in a
custom GUI from the Redempts Library and using Configuration files for the Recipes.

## Installation

0. Preconditions: You need a 1.19.2 Server and the Citizens2 Plugin.

1. Start by downloading the [latest release](https://github.com/NopeImNotHere/Upgradable_Items/releases) from this repository.

2. Get the plugin from where ever you have installed it and copy or cut it.

3. Connect to your Server and open the plugins folder. This can also be locally on a your PC or NAS.

4. Now start the Server and look into the Console.

5. Check if the plugin has Started correctly and nothing has errored through the Console.

  i. If nothing has errored and everything has enabled go to the [Usage](#Usage)   
  
  ii. If something has broken please create an [Issue](https://github.com/NopeImNotHere/Upgradable_Items/issues)
  
## Usage

Now that you have your own instance of the plugin running, there are multiple Commands in your toolset.
Firstly all Commands start with a superset Command:


From here on out you have 3 different options:
```
/uai
```
To only open the GUI for Upgrading your Items. This one is good to test your new Recipies with:
```
/uai GUI-Open
```
To open the GUI for Removing NPCs in a 5 block radius of the Player invoking the Command:
```
/uai Remove
```
This Command is another superset for 2 different types of spawnable NPCs:
```
/uai Spawn
```
For a Mob NPC with the possibilty for a custom name.
For Example:
```
/uai Spawn NPC
```
```
/uai Spawn NPC VILLAGER Upgrade Items
```
For a Player NPC with the possibilty for a custom name.
For Example:
```
/uai Spawn PlayerNPC
```
```
/uai Spawn PlayerNPC Technoblade Upgrade your Items.
```


## License

Released under [MIT](/LICENSE) by [@NopeImNotHere](https://github.com/NopeImNotHere).
