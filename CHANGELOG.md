# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog],
and this project adheres to [Semantic Versioning].

## Unreleased

### Changed
- updated to new EnderIO version
  - new minimum version is 6.1.7-beta

## [0.4.1] - 2023-11-16

### Fixed
- fixed custom conduit registry not detecting IDs correctly

## [0.4.0] - 2023-11-10

### Added
- added `EnderIOEvents.conduits` event to allow adding custom energy conduits
  - read more about it in the [wiki][energy-conduit]

<!-- Links -->
[energy-conduit]: https://github.com/AlmostReliable/kubejs-enderio/wiki/Events#registering-custom-energy-conduits

## [0.3.1] - 2023-09-22

### Changed
- changed minimum EnderIO version to 6.0.20-alpha

### Fixed
- fixed a crash caused by the alloy smelter mixin ([enderio#520], [#1], [#2])

<!-- Links -->
[enderio#520]: https://github.com/Team-EnderIO/EnderIO/issues/520
[#1]: https://github.com/AlmostReliable/kubejs-enderio/issues/1
[#2]: https://github.com/AlmostReliable/kubejs-enderio/pull/2

## [0.3.0] - 2023-09-20

### Added
- added `max_item_drops` property to fire crafting recipes to limit loot table drops

### Changed
- changed minimum EnderIO version to 6.0.19-alpha
- changed name of exposed binding from `EIOBonusType` to `EnderIOBonusType`
- changed id of sag mill recipes from `sagmilling` to `sag_milling`
- changed grinding ball function name from `powerUse` to `powerUseMultiplier`
- changed enchanter recipes to use counted ingredient instead of an amount property

## [0.2.0] - 2023-09-13

### Added
- added utility method for fire crafting to add single dimensions
- added ability to remove vanilla smelting recipes from the alloy smelter

### Fixed
- fixed swallowed exception when slicer recipes have not exactly 6 inputs

## [0.1.0] - 2023-09-05

Initial 1.20.1 release!

<!-- Links -->
[keep a changelog]: https://keepachangelog.com/en/1.0.0/
[semantic versioning]: https://semver.org/spec/v2.0.0.html

<!-- Versions -->
[0.4.1]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.4.1-beta
[0.4.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.4.0-beta
[0.3.1]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.3.1-beta
[0.3.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.3.0-beta
[0.2.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-0.2.0-forge-beta
[0.1.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-0.1.0-forge-beta
