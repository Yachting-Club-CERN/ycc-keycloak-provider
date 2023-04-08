# YCC Keycloak Provider

[![Release](https://img.shields.io/github/v/release/Yachting-Club-CERN/ycc-keycloak-provider?label=Release)](https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/releases/latest)
[![CI](https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/workflows/CI/badge.svg)](https://github.com/Yachting-Club-CERN/ycc-keycloak-provider/actions)

This Keycloak provider connects our Oracle DB users to Keycloak. Handy, since we can keep running
both old and new generation components.

For developers, start by reading
[this guide](https://www.baeldung.com/java-keycloak-custom-user-providers) and check out
[this demo](https://github.com/keycloak/keycloak-quickstarts/tree/latest/user-storage-jpa) and
[this one](https://github.com/dasniko/keycloak-user-spi-demo). Also, there is
an [official documentation](https://www.keycloak.org/docs/latest/server_development/index.html#_user-storage-spi).

## Features

- Federate YCC users from the Oracle database (with existing credentials)
- Auto-create group `ycc-members-all-past-and-present` (contains all members)
- Auto-create role `ycc-member-active` (contains active members)

## How to Extend Attributes, Groups and Roles

Recommendation:

- Use *attributes* for property-like information/extra metadata (e.g., birthday, nationality).
  Prefix attributes with `ycc.` (tip: it can be a nested object)
- Use *groups* for grouping users, without any relation to permissions. Prefix groups with `ycc-`,
  e.g., `ycc-members-all-past-and-present`
- Use *roles* for giving permissions to users, e.g., active, committee, boat licence. Prefix roles
  with `ycc-`, e.g., `ycc-member-active`

Attributes, groups and roles are available in the as user info on the client side if the
client is authorised to access this information.

## Configuration

See `conf/keycloak.conf`. For the deployed instance use the dedicated database.

Health and metrics endpoints should be enabled by default, but only accessible inside the cluster.

### JPA Configuration

Persistence units must be created in `persistence.xml` and Quarkus eagerly creates all connections
upon startup. It is possible to put part of the configuration into `conf/quarkus.properties`.

This gives that `ycc-keycloak-provider` comes with several JARs:

- `ycc-keycloak-provider-VERSION.jar`: provider implementation
- `ycc-keycloak-provider-VERSION-ycc-db-prod.jar`: `persistence.xml` for production database,
  persistence unit: `ycc-db-prod`
- `ycc-keycloak-provider-VERSION-ycc-db-test.jar`: `persistence.xml` for test database, persistence
  unit: `ycc-db-test`
- `ycc-keycloak-provider-VERSION-ycc-db-dev.jar`: `persistence.xml` for dev database, persistence
  unit: `ycc-db-dev`
- `ycc-keycloak-provider-VERSION-ycc-db-local.jar`: `persistence.xml` for local database,
  persistence unit: `ycc-db-local`

If any of these persistence JARs is present in Keycloak's `providers/` directory, the credentials
_must be specified_ in `conf/quarkus.properties` for all of them, otherwise Keycloak would not
start. (This can be done when the container is started.) If `local` is present, the local database
_must be running_, otherwise Keycloak would not start.

(However, this allows us to only have one Keycloak instance deployed which can serve several realms,
either from the production or the test database.)

## Development

Clone this repo and use your favourite editor (if in doubt, just use IntelliJ Community Edition).
This is a Gradle project.

For local development it is recommended to use `ycckeycloaklocal` (in `ycc-db-local`), since this
also allows to persist changes, test Keycloak upgrades, etc., while having test user federation.
See `ycc-infra/ycc-keycloak-local` for more details.

Prepare some time for adding new features - to test integration with Keycloak, the iteration is the
following:

1. Code
2. Assemble/build
3. Copy JARs to Keycloak instance
4. Restart Keycloak instance
5. Log in again and test

### Release Procedure

1. Release commit: fix version, finalise change log (don't forget about the links in the bottom of
   the change log)
2. Check CI for success. Check that the packages were published too.
3. Publish release on GitHub
4. Bump version

## Notes

### JPA Configuration Dead Ends (Lajos)

I have tried _many-many_ ways of facilitating the configuration. From the code point of view
using `EntityManager` (JPA/Hibernate) is beneficial. However, Keycloak+Quarkus complicates the view.

The root problem was that Quarkus eagerly reads `persistence.xml` (regardless the settings and
whether `beans.xml` is present). This file cannot be renamed and is essential to JPA `EntityManager`
creation.

What I did not manage to make work reliable under Keycloak 21 as of 2023-03:

- Making Quarkus to not read `persistence.xml` (the properties simply do not work at all in the
  setup)
- Complete dynamic configuration of JPA (bypassing `persistence.xml`): first of all needs a lot of
  code, and in the end it did not work as expected. Cannot recommend at all.
- "Fake placeholder" in `persistence.xml` and dynamic runtime reconfiguration: not Quarkus
  friendly:

  ```
  Failed to validate the database configuration: javax.persistence.PersistenceException:
  The FastbootHibernateProvider PersistenceProvider can not support runtime provided properties.
  Make sure you set all properties you need in the configuration resources before building the application.
  ```
