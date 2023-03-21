package ch.cern.ycc.keycloakprovider;

import lombok.experimental.UtilityClass;

/**
 * Constants.
 */
@UtilityClass
 class YccKeycloakProviderConstants {

// quarkus.datasource.ycc-db.db-kind=jdbc-oracle
// quarkus.datasource.ycc-db.jdbc.driver=oracle.jdbc.OracleDriver
// quarkus.datasource.ycc-db.jdbc.url=jdbc:oracle:thin:@localhost:1521:XE
// quarkus.datasource.ycc-db.username=ycclocal
// quarkus.datasource.ycc-db.password=changeit

//     <property name="javax.persistence.jdbc.driver" value="oracle.jdbc.OracleDriver"/>
//      <property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:XE"/>
//      <property name="javax.persistence.jdbc.user" value="ycclocal"/>
//      <property name="javax.persistence.jdbc.password" value="changeit"/>



 // TODO

//      <property name="javax.persistence.jdbc.driver" value="oracle.jdbc.OracleDriver"/>
//      <property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:XE"/>
//      <property name="javax.persistence.jdbc.user" value="ycclocal"/>
//      <property name="javax.persistence.jdbc.password" value="changeit"/>
//      <property name="javax.persistence.transactionType" value="RESOURCE_LOCAL" />
//
//      <property name="hibernate.dialect" value="org.hibernate.dialect.Oracle12cDialect"/>
//      <property name="hibernate.show_sql" value="true"/>
//      <property name="hibernate.format_sql" value="true"/>


 // TODO

//      <!--  TODO This probs not needed-->
//      <property name="hibernate.connection.datasource" value="ycc-db"/>

 // But this is
//      <property name="javax.persistence.transactionType" value="JTA"/>
//
//      <property name="hibernate.dialect" value="org.hibernate.dialect.Oracle12cDialect"/>
//      <property name="hibernate.show_sql" value="false"/>
//      <property name="hibernate.format_sql" value="true"/>

 // TODO needed?
 public final String JDBC_DRIVER_PROPERTY_NAME = "jdbcDriver";
 public final String JDBC_URL_PROPERTY_NAME = "jdbcUrl";
 public final String JDBC_USERNAME_PROPERTY_NAME = "jdbcUsername";
 public final String JDBC_PASSWORD_PROPERTY_NAME = "jdbcPassword";

}
