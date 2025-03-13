# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Add support for new password hashing

## [1.0.1] - 2024-03-09

### Changed

- Inconsistent data resilience

## [1.0.0] - 2024-01-07

### Changed

- Upgrade to Keycloak 22.0.4 (also brings in Hibernate 6.2)
- Upgrade to Ruthless 0.8.0 & Gradle 8.4
- Transform JPA queries to never return open result streams to avoid cursor leak. This was triggered by the Hibernate 6.2 upgrade

## [0.4.0] - 2023-04-20

### Added

- Add support for Helpers App permissions

## [0.3.2] - 2023-04-08

### Fixed

- Create groups and roles by name only to support multiple YCC realms in the same Keycloak instance (and let Keycloak generate IDs)

## [0.3.1] - 2023-04-08

### Added

- Add DEV environment support

## [0.3.0] - 2023-04-05

### Added

- Add `ycc-licence-*` roles based on members' active keys

## [0.2.1] - 2023-04-03

### Changed

- More robust password hashing

## [0.2.0] - 2023-04-02

### Added

- Auto-create group `ycc-members-all-past-and-present` (contains all members)
- Auto-create role `ycc-member-active` (contains active members)

## [0.1.0] - 2023-03-23

### Added

- Initial version for federating users from YCC Oracle Database to Keycloak

[Unreleased]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v1.0.1
[1.0.0]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v1.0.0
[0.4.0]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.4.0
[0.3.2]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.3.2
[0.3.1]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.3.1
[0.3.0]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.3.0
[0.2.1]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.2.1
[0.2.0]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.2.0
[0.1.0]: https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/tag/v0.1.0
