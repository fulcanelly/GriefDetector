# Grief Detector 

![GitHub Tag](https://img.shields.io/github/v/tag/fulcanelly/GriefDetector)
<a href="https://github.com/fulcanelly/mctg-bridge/releases/"><img src="https://img.shields.io/github/downloads/fulcanelly/GriefDetector/total.svg" alt="GitHub All Releases"/></a>
<img src="https://img.shields.io/github/stars/fulcanelly/GriefDetector"/>
![GitHub Build](https://img.shields.io/github/actions/workflow/status/fulcanelly/GriefDetector/main.yml?branch=master)


<a><img src="https://img.shields.io/badge/MC-1.17.*-brightgreen.svg" alt="Minecraft"/></a>
<img src="https://img.shields.io/badge/MC-1.18.*-brightgreen.svg" alt="Minecraft"/>
<img src="https://img.shields.io/badge/MC-1.19.*-brightgreen.svg" alt="Minecraft"/>
<img src="https://img.shields.io/badge/MC-1.20.*-brightgreen.svg" alt="Minecraft"/>
<img src="https://img.shields.io/badge/MC-1.21.*-brightgreen.svg" alt="Minecraft"/>


Monitors and notifies suspicious activity on the server.

### Purpose

Ideal for SMPs that prioritize freedom over restrictions (no private regions or adventure mode). 
Suitable for servers with a philosophy that assumes everyone is good until proven otherwise.

### Dependencies 

- [CoreProtect](https://github.com/PlayPro/CoreProtect) - for checking is player breaking someones building
- [tg-bridge](https://github.com/fulcanelly/mctg-bridge) - to notify if it actually does that

### Example of notification

```
Such a... it seems that the grifer started:
a player with the nickname sdfsdfsdf blew up TNT at coordinates
x: -35..-27 y: 61..66 z: 18..29 world: -nether-

At these coordinates were the buildings of such players - ki11mepls, fulcanelly, NXMAEDIC
-- the message may be erroneous
```  
