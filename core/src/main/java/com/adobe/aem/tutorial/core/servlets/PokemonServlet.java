package com.adobe.aem.tutorial.core.servlets;


import com.adobe.aem.tutorial.core.service.PokemonService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;


@Component(
        service = Servlet.class,
        property = {
                SLING_SERVLET_PATHS + "=/bin/pokemon"
        }
)
public class PokemonServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final String POKEMON_API_URL = "https://pokeapi.co/api/v2/pokemon?limit=1000";

    @Reference
    protected transient PokemonService pokemonService;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        logger.info("Received GET request at /bin/pokemon from {}", request.getRemoteAddr());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(getURL());
            logger.debug("Constructed PokeAPI request URL: {}", get);
            try (CloseableHttpResponse apiResponse = httpClient.execute(get)) {
                int statusCode = apiResponse.getStatusLine().getStatusCode();
                logger.info("PokeAPI responded with HTTP status code {}", statusCode);
                HttpEntity entity = apiResponse.getEntity();

                if (statusCode == 200 && entity != null) {
                    try (InputStream is = entity.getContent()) {
                        String result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        response.getWriter().write(result);

                        logger.warn("Successfully fetched and returned data from PokeAPI");
                    }
                } else {
                    logger.warn("Failed to fetch data from PokeAPI. Status: {}, Entity: {}", statusCode, entity);
                    response.setStatus(SlingHttpServletResponse.SC_BAD_GATEWAY);
                    response.getWriter().write("{\"error\":\"Failed to fetch data from PokeAPI\"}");
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching data from PokeAPI", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    private String getURL(){
        return MessageFormat.format(pokemonService.getConfiguration().getAll(), pokemonService.getConfiguration().baseURL());
    }
}