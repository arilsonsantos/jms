# JMS e HornetQ
### Controle de fila com Wildfly

<p> Para o funcionamento da verificação da fila, é necessário instalar algumas dependências no pom.xml 
e algumas alterações no arquivo de configuração standalone-full.xml do wildfly.
</p>

### POM.XML

		<dependency>
			<groupId>org.hornetq</groupId>
			<artifactId>hornetq-jms-client</artifactId>
			<version>2.4.5.Final</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.hornetq</groupId>
			<artifactId>hornetq-core-client</artifactId>
			<version>2.4.5.Final</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.hornetq</groupId>
			<artifactId>hornetq-server</artifactId>
			<version>2.4.5.Final</version>
			<scope>provided</scope>
		</dependency>
    
    
### STANDALONE-FULL.XML

        <subsystem xmlns="urn:jboss:domain:messaging:2.0">
            <hornetq-server>
                <jmx-management-enabled>true</jmx-management-enabled>
                <statistics-enabled>true</statistics-enabled>
                
         ...
         
         <subsystem xmlns="urn:jboss:domain:ee:2.0">
            <global-modules>
                <module name="org.hornetq"/>
                <module name="org.jboss.remoting-jmx"/>
            </global-modules>
