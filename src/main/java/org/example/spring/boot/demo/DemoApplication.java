package org.example.spring.boot.demo;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

import za.globed.varsite.hebs.services.api.DeploymentServiceRemote;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws NamingException {
		ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		
		DemoApplication demo = ctx.getBean(DemoApplication.class);
		demo.showDeploymentEnvironment();

		System.out.println("Application exited");
	}

	public void showDeploymentEnvironment() throws NamingException {
		DeploymentServiceRemote remoteService = lookupRemoteService();
		int envId = remoteService.getEnvironmentId();
		System.out.println("EnvironmentId = " + envId);
	}

	private static DeploymentServiceRemote lookupRemoteService() throws NamingException {
		String jndiName = "ejb:apd-10.32.2/GENServices/DeploymentServiceBean!"
				+ DeploymentServiceRemote.class.getName();

		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		SimpleRemoteStatelessSessionProxyFactoryBean factory = new SimpleRemoteStatelessSessionProxyFactoryBean();
		factory.setJndiName(jndiName);
		factory.setBusinessInterface(DeploymentServiceRemote.class);
		factory.setJndiEnvironment(jndiProperties);
		factory.afterPropertiesSet();

		return (DeploymentServiceRemote) factory.getObject();
	}

}
