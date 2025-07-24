# Oreon Minecraft Mod

A comprehensive administration and moderation tool for Minecraft, ported from Forge 1.16.5 to Fabric 1.21.4.

## Features

### Core Functions
- **Fullbright**: Enhanced visibility in dark areas
- **Hide Players**: Option to hide other players for cleaner screenshots
- **Target System**: Advanced player targeting with distance calculation and health display

### Administration Tools  
- **Report System**: Comprehensive report management with templates and history
- **Banspec Manager**: Predefined ban reasons and specifications
- **Check Logic**: Automated detection of suspicious chat patterns and behavior
- **Clean Verdict Generator**: Generate professional verdicts for player checks

### GUI Systems
- **Modern Interface**: Updated GUI components using Fabric 1.21.4 APIs
- **Settings Management**: Comprehensive configuration through Cloth Config
- **Multi-line Text Editor**: Advanced text editing capabilities for reports

### Security Features
- **HWID Checking**: Hardware ID verification system
- **Version Checking**: Automatic version validation
- **Pattern Detection**: Intelligent detection of cheats, advertising, and spam

### Integration
- **Discord Rich Presence**: Show current activity and server information
- **Hitbox Visualization**: Advanced hitbox display and management
- **Template System**: Customizable templates for reports and verdicts

## Installation

1. Install Minecraft 1.21.4
2. Install Fabric Loader 0.16.9 or later
3. Install Fabric API 0.110.0+1.21.4 or later
4. Install Cloth Config API (optional but recommended)
5. Download and place the Oreon mod file in your mods folder

## Configuration

The mod creates configuration files in the `oreon/` directory:
- `config/oreon.json5` - Main configuration
- `reports/` - Report storage
- `templates/` - Report templates
- `banspecs/` - Ban specifications

## Key Bindings

Default key bindings (configurable):
- **Right Shift** - Open main Oreon GUI
- **O** - Open legacy GUI  
- **U** - Open settings
- **P** - Open profile manager

## Requirements

- **Java 21** or later
- **Minecraft 1.21.4**
- **Fabric Loader 0.16.9+**
- **Fabric API 0.110.0+1.21.4**

## License

This mod is provided as-is for administration and moderation purposes. Please ensure compliance with server rules and Minecraft's terms of service.

## Support

For issues, suggestions, or questions, please contact the development team.

---

**Oreon v2.0.0** - Complete Fabric 1.21.4 Port