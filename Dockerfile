FROM jboss/wildfly:20.0.1.Final

COPY jwtee-ear/target/jwtee.ear /opt/jboss/wildfly/standalone/deployments/
