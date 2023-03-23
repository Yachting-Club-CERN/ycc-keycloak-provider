# YCC Keycloak Provider

This Keycloak provider connects our Oracle DB users to Keycloak. Handy, since we can keep running
both old and new generation components.

For developers, start by reading
[this guide](https://www.baeldung.com/java-keycloak-custom-user-providers) and check out
[this demo](https://github.com/keycloak/keycloak-quickstarts/tree/latest/user-storage-jpa) and
[this one](https://github.com/dasniko/keycloak-user-spi-demo). Also, there is
an [official documentation](https://www.keycloak.org/docs/latest/server_development/index.html#_user-storage-spi).

## TODO

* OKD4 deployment
* metrics and health endpoints only exposed inside the cluster
* dedicated oracle db for keycloak

## Configuration

See `conf/keycloak.conf`. For the deployed instance use the dedicated database (for local
development default H2 database is perfect, but you can also use `ycckeycloaklocal`
in `ycc-db-local`).

Health and metrics endpoints should be enabled by default, but only accessible inside the cluster.

### JPA Configuration

Persistence units must be created in `persistence.xml` and Quarkus eagerly creates all connections
upon startup. It is possible to put part of the configuration into `conf/quarkus.properties`.

This gives that `ycc-keycloak-provider` comes with several JARs:

* `ycc-keycloak-provider-VERSION.jar`: provider implementation
* `ycc-keycloak-provider-VERSION-ycc-db-prod.jar`: `persistence.xml` for production database,
  persistence unit: `ycc-db-prod`
* `ycc-keycloak-provider-VERSION-ycc-db-test.jar`: `persistence.xml` for test database, persistence
  unit: `ycc-db-test`
* `ycc-keycloak-provider-VERSION-ycc-db-local.jar`: `persistence.xml` for local database,
  persistence unit: `ycc-db-local`

If any of these persistence JARs is present in Keycloak's `providers/` directory, the credentials
*must be specified* in `conf/quarkus.properties` for all of them, otherwise Keycloak would not
start. (This can be done when the container is started.) If `local` is present, the local database
*must be running*, otherwise Keycloak would not start.

(However, this allows us to only have one Keycloak instance deployed which can serve several realms,
either from the production or the test database.)

## JPA Configuration Dead Ends

I (Lajos) have tried *many-many* ways of facilitating the configuration. From the code point of view
using `EntityManager` (JPA/Hibernate) is beneficial. However, Keycloak+Quarkus complicates the view.

The root problem was that Quarkus eagerly reads `persistence.xml` (regardless the settings and
whether `beans.xml` is present). This file cannot be renamed and is essential to JPA `EntityManager`
creation.

What I did NOT manage to make work reliable under Keycloak 21 as of 2023-03:

* Making Quarkus to NOT read `persistence.xml` (the properties simply do not work at all in the
  setup)
* Complete dynamic configuration of JPA (bypassing `persistence.xml`): first of all needs a lot of
  code, and in the end it did not work as expected. Cannot recommend at all.
* "Fake placeholder" in `persistence.xml` and dynamic runtime reconfiguration: not Quarkus
  friendly:

  ```
  Failed to validate the database configuration: javax.persistence.PersistenceException: 
  The FastbootHibernateProvider PersistenceProvider can not support runtime provided properties. 
  Make sure you set all properties you need in the configuration resources before building the application.
  ```
