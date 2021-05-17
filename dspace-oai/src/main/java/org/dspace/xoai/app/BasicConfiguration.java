/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.app;

import org.apache.log4j.Logger;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.database.CollectionsService;
import org.dspace.xoai.services.api.database.EarliestDateResolver;
import org.dspace.xoai.services.api.database.FieldResolver;
import org.dspace.xoai.services.api.database.HandleResolver;
import org.dspace.xoai.services.api.solr.SolrQueryResolver;
import org.dspace.xoai.services.api.solr.SolrServerResolver;
import org.dspace.xoai.services.api.xoai.DSpaceFilterResolver;
import org.dspace.xoai.services.api.xoai.IdentifyResolver;
import org.dspace.xoai.services.api.xoai.ItemRepositoryResolver;
import org.dspace.xoai.services.api.xoai.SetRepositoryResolver;
import org.dspace.xoai.services.impl.config.DSpaceConfigurationService;
import org.dspace.xoai.services.impl.context.DSpaceContextService;
import org.dspace.xoai.services.impl.context.DSpaceXOAIManagerResolver;
import org.dspace.xoai.services.impl.database.DSpaceCollectionsService;
import org.dspace.xoai.services.impl.database.DSpaceEarliestDateResolver;
import org.dspace.xoai.services.impl.database.DSpaceFieldResolver;
import org.dspace.xoai.services.impl.database.DSpaceHandlerResolver;
import org.dspace.xoai.services.impl.resources.DSpaceResourceResolver;
import org.dspace.xoai.services.impl.solr.DSpaceSolrQueryResolver;
import org.dspace.xoai.services.impl.solr.DSpaceSolrServerResolver;
import org.dspace.xoai.services.impl.xoai.BaseDSpaceFilterResolver;
import org.dspace.xoai.services.impl.xoai.DSpaceIdentifyResolver;
import org.dspace.xoai.services.impl.xoai.DSpaceItemRepositoryResolver;
import org.dspace.xoai.services.impl.xoai.DSpaceSetRepositoryResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.dspace.utils.MockContextImpl;

@Configuration
public class BasicConfiguration {
    private static final Logger log = Logger.getLogger(BasicConfiguration.class);

    @Bean
    public ConfigurationService configurationService() {
        return new DSpaceConfigurationService();
    }

    @Bean
    public ContextService contextService() {
        return new MockContextImpl();
    }


    @Bean
    public SolrServerResolver solrServerResolver () {
        return new DSpaceSolrServerResolver();
    }


    @Bean
    public XOAIManagerResolver xoaiManagerResolver() {
        return new DSpaceXOAIManagerResolver();
    }

    @Bean
    public ResourceResolver resourceResolver() {
        return new DSpaceResourceResolver();
    }

    @Bean
    public FieldResolver databaseService () {
        return new DSpaceFieldResolver();
    }

    @Bean
    public EarliestDateResolver earliestDateResolver () {
        return new DSpaceEarliestDateResolver();
    }

    @Bean
    public ItemRepositoryResolver itemRepositoryResolver () {
        return new DSpaceItemRepositoryResolver();
    }
    @Bean
    public SetRepositoryResolver setRepositoryResolver () {
        return new DSpaceSetRepositoryResolver();
    }
    @Bean
    public IdentifyResolver identifyResolver () {
        return new DSpaceIdentifyResolver();
    }

    @Bean
    public DSpaceFilterResolver dSpaceFilterResolver () {
        return new BaseDSpaceFilterResolver();
    }

    @Bean
    public HandleResolver handleResolver () {
        return new DSpaceHandlerResolver();
    }

    @Bean
    public CollectionsService collectionsService () {
        return new DSpaceCollectionsService();
    }

    @Bean
    public SolrQueryResolver solrQueryResolver () {
        return new DSpaceSolrQueryResolver();
    }
}
