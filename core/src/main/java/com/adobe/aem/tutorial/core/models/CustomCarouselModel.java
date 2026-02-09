package com.adobe.aem.tutorial.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import java.util.List;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomCarouselModel {

    // Este nome "slides" deve ser exatamente o mesmo que colocaste no 'name' do multifield no Dialog
    @ChildResource(name = "slides")
    private List<CustomCarouselSlidesModel> slides;

    public List<CustomCarouselSlidesModel> getSlides() {
        return slides;
    }
}