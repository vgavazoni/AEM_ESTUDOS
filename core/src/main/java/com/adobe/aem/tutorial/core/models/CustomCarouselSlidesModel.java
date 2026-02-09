package com.adobe.aem.tutorial.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomCarouselSlidesModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String buttonLabel;

    @ValueMapValue
    private String pagePath;

    @ValueMapValue
    private String imagePath;

    // Getters simples para o HTL acessar
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getButtonLabel() { return buttonLabel; }
    public String getPagePath() { return pagePath; }
    public String getImagePath() { return imagePath; }
}