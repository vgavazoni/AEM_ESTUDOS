package com.adobe.aem.tutorial.core.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = PokemonService.class, immediate = true)
@Designate(ocd = PokemonConfig.class)
public class PokemonService {

    private PokemonConfig config;

    @Activate
    protected void activate(PokemonConfig config) {
        this.config = config;
    }

    public PokemonConfig getConfiguration() {
        return this.config;
    }
}