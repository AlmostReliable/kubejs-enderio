# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog],
and this project adheres to [Semantic Versioning].

## Unreleased
- /

## [0.7.0] - 2024-10-21

Initial 1.21.1 release!

- added support for new recipe types like Vat Fermenting
- added support for registering custom conduits, including modded variants
- added binding for Sag Mill outputs to fully support all properties
- added support for replacement block in Fire Crafting recipes
- changed minimum EnderIO version to 7.0.8-alpha
- reworked recipe schemas for internal recipe changes

## [0.6.0] - 2024-09-29

This update adjusts recipe schemas and introduces changes to the Alloy Smelter filtering.
Please read the [wiki](https://github.com/AlmostReliable/kubejs-enderio/wiki) for more information.

- added automatic smelting recipe inheritance
- changed minimum EnderIO version to 6.2.0-beta
- fixed custom energy conduits not connecting to some modded blocks ([#6](https://github.com/AlmostReliable/kubejs-enderio/issues/6))
- removed manual synchronization of filtered smelting recipes
- removed KubeJS binding for utility methods

## [0.5.0] - 2024-08-04

- changed minimum EnderIO version to 6.1.7-beta

## [0.4.1] - 2023-11-16

- fixed custom conduit registry not detecting IDs correctly

## [0.4.0] - 2023-11-10

- added `EnderIOEvents.conduits` event to allow adding custom energy conduits
  - read more about it in the [wiki](https://github.com/AlmostReliable/kubejs-enderio/wiki/Events#registering-custom-energy-conduits)

## [0.3.1] - 2023-09-22

- changed minimum EnderIO version to 6.0.20-alpha
- fixed a crash caused by the Alloy Smelter mixin ([enderio#520](https://github.com/Team-EnderIO/EnderIO/issues/520), [#1](https://github.com/AlmostReliable/kubejs-enderio/issues/1), [#2](https://github.com/AlmostReliable/kubejs-enderio/pull/2))

## [0.3.0] - 2023-09-20

- added `max_item_drops` property to fire crafting recipes to limit loot table drops
- changed minimum EnderIO version to 6.0.19-alpha
- changed name of exposed binding from `EIOBonusType` to `EnderIOBonusType`
- changed id of sag mill recipes from `sagmilling` to `sag_milling`
- changed grinding ball function name from `powerUse` to `powerUseMultiplier`
- changed enchanter recipes to use counted ingredient instead of an amount property

## [0.2.0] - 2023-09-13

- added utility method for fire crafting to add single dimensions
- added ability to remove vanilla smelting recipes from the alloy smelter
- fixed swallowed exception when slicer recipes have not exactly 6 inputs

## [0.1.0] - 2023-09-05

Initial 1.20.1 release!

<!-- Links -->
[keep a changelog]: https://keepachangelog.com/en/1.0.0/
[semantic versioning]: https://semver.org/spec/v2.0.0.html

<!-- Versions -->
[0.7.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.21.1-neoforge-0.7.0
[0.6.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.6.0
[0.5.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.5.0
[0.4.1]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.4.1-beta
[0.4.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.4.0-beta
[0.3.1]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.3.1-beta
[0.3.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-forge-0.3.0-beta
[0.2.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-0.2.0-forge-beta
[0.1.0]: https://github.com/AlmostReliable/kubejs-enderio/releases/tag/v1.20.1-0.1.0-forge-beta
