package com.epsm.gwtHibernateHello.server.configuration;

import org.junit.Assert;
import org.junit.Test;

import com.epsm.gwtHibernateHello.server.configuration.PropertiesExtractor;


public class PropertiesExtractorTest{
	private final String EXISTS_PROPERTY = "test.property";
	private final String NOT_EXISTS_PROPERTY = "notExistProperty";
	private final String EXPECTED_RESULT = "someTestProperty";
	
	@Test
	public void returnsExpectedResultOnExistsProperty(){
		String actual = PropertiesExtractor.getProperty(EXISTS_PROPERTY);
		
		Assert.assertEquals(EXPECTED_RESULT, actual);
	}
	
	@Test(expected=RuntimeException.class)
	public void throwsRuntimeExceptionIfPropertyDoesNotExist(){
		PropertiesExtractor.getProperty(NOT_EXISTS_PROPERTY);
	}
}
