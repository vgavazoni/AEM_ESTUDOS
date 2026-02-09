package com.adobe.aem.tutorial.core.service;


import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Pokemon :: API Config")
public @interface PokemonConfig {

    @AttributeDefinition(name = "Base URL", type = AttributeType.STRING)
    String baseURL() default StringUtils.EMPTY;

    @AttributeDefinition(name = "Handle Get", description = "Endpoint used to get pokemon list", type = AttributeType.STRING)
    String getAll() default StringUtils.EMPTY;

}