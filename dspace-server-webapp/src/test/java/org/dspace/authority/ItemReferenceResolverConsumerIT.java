/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.authority;

import static org.dspace.app.matcher.MetadataValueMatcher.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.dspace.app.rest.test.AbstractControllerIntegrationTest;
import org.dspace.authority.service.AuthorityValueService;
import org.dspace.builder.CollectionBuilder;
import org.dspace.builder.CommunityBuilder;
import org.dspace.builder.EPersonBuilder;
import org.dspace.builder.ItemBuilder;
import org.dspace.builder.WorkspaceItemBuilder;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.WorkspaceItem;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.InstallItemService;
import org.dspace.eperson.EPerson;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test to verify the references resolving of
 * {@link ItemReferenceResolverConsumer}.
 * @author Luca Giamminonni (luca.giamminonni at 4science.it)
 *
 */
public class ItemReferenceResolverConsumerIT extends AbstractControllerIntegrationTest {

    private EPerson submitter;

    private Collection publicationCollection;

    private Collection personCollection;

    private InstallItemService installItemService;

    @Before
    public void setup() throws Exception {

        installItemService = ContentServiceFactory.getInstance().getInstallItemService();

        context.turnOffAuthorisationSystem();

        submitter = EPersonBuilder.createEPerson(context)
            .withEmail("submitter@example.com")
            .withPassword(password)
            .build();

        parentCommunity = CommunityBuilder.createCommunity(context)
            .withName("Parent Community")
            .build();

        publicationCollection = createCollection("Collection of publications", "Publication");
        personCollection = createCollection("Collection of persons", "Person");

        context.setCurrentUser(submitter);
        context.restoreAuthSystemState();

    }

    @Test
    public void testItemReferenceResolverConsumerOrcid() throws SQLException {

        context.turnOffAuthorisationSystem();

        String orcidAuthority = formatWillBeReferencedAuthority("ORCID", "0000-0002-1825-0097");

        Item firstItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", orcidAuthority)
            .build();

        Item secondItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Second Item")
            .withAuthor("Author", orcidAuthority)
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            orcidAuthority, 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            orcidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item itemWithOrcid = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withOrcidIdentifier("0000-0002-1825-0097")
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithOrcid.getID().toString(), 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithOrcid.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerWithRid() throws SQLException {

        context.turnOffAuthorisationSystem();

        String ridAuthority = formatWillBeReferencedAuthority("RID", "0000-1111");

        Item firstItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", ridAuthority)
            .build();

        Item secondItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Second Item")
            .withAuthor("Author", ridAuthority)
            .build();

        context.commit();
        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            ridAuthority, 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            ridAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item itemWithRid = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withResearcherIdentifier("0000-1111")
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerWithIsni() throws SQLException {

        context.turnOffAuthorisationSystem();

        String ridAuthority = formatWillBeReferencedAuthority("ISNI", "AAA-BBB");

        Item firstItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", ridAuthority)
            .build();

        Item secondItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Second Item")
            .withAuthor("Author", ridAuthority)
            .build();

        context.commit();
        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            ridAuthority, 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            ridAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item itemWithIsni = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withIsniIdentifier("AAA-BBB")
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithIsni.getID().toString(), 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithIsni.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerWithManyReferences() throws SQLException {

        context.turnOffAuthorisationSystem();

        String ridAuthority = formatWillBeReferencedAuthority("RID", "0000-1111");
        String orcidAuthority = formatWillBeReferencedAuthority("ORCID", "0000-0002-1825-0097");

        Item firstItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", ridAuthority)
            .build();

        Item secondItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Second Item")
            .withAuthor("Author", orcidAuthority)
            .build();

        context.commit();
        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            ridAuthority, 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            orcidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item itemWithRid = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withOrcidIdentifier("0000-0002-1825-0097")
            .withResearcherIdentifier("0000-1111")
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerWithManyMetadata() throws SQLException {

        context.turnOffAuthorisationSystem();

        String firstRidAuthority = formatWillBeReferencedAuthority("RID", "0000-1111");
        String secondRidAuthority = formatWillBeReferencedAuthority("RID", "2222-3333");

        Item firstItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", firstRidAuthority)
            .build();

        Item secondItem = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Second Item")
            .withAuthor("Author", secondRidAuthority)
            .build();

        context.commit();
        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            firstRidAuthority, 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            secondRidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item itemWithRid = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withResearcherIdentifier("0000-1111")
            .withResearcherIdentifier("2222-3333")
            .build();

        context.restoreAuthSystemState();

        firstItem = context.reloadEntity(firstItem);
        assertThat(firstItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));

        secondItem = context.reloadEntity(secondItem);
        assertThat(secondItem.getMetadata(), hasItem(with("dc.contributor.author", "Author", null,
            itemWithRid.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerWorksOnlyAfterArchiving() throws Exception {

        context.turnOffAuthorisationSystem();

        String orcidAuthority = formatWillBeReferencedAuthority("ORCID", "0000-0002-1825-0097");

        Item item = ItemBuilder.createItem(context, publicationCollection)
            .withTitle("Publication")
            .withAuthor("Author", orcidAuthority)
            .build();

        context.restoreAuthSystemState();

        item = context.reloadEntity(item);
        assertThat(item.getMetadata(), hasItem(with("dc.contributor.author", "Author", null, orcidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        WorkspaceItem author = WorkspaceItemBuilder.createWorkspaceItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withOrcidIdentifier("0000-0002-1825-0097")
            .build();

        String authorId = author.getItem().getID().toString();

        context.restoreAuthSystemState();

        item = context.reloadEntity(item);
        assertThat(item.getMetadata(), hasItem(with("dc.contributor.author", "Author", null, orcidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();
        installItemService.installItem(context, author);
        context.commit();
        context.restoreAuthSystemState();

        item = context.reloadEntity(item);
        assertThat(item.getMetadata(), hasItem(with("dc.contributor.author", "Author", null, authorId, 0, 600)));

    }

    @Test
    public void testItemReferenceResolverConsumerUpdateAlsoInProgressItems() throws SQLException {

        context.turnOffAuthorisationSystem();

        String orcidAuthority = formatWillBeReferencedAuthority("ORCID", "0000-0002-1825-0097");

        WorkspaceItem workspaceItem = WorkspaceItemBuilder.createWorkspaceItem(context, publicationCollection)
            .withTitle("First Item")
            .withAuthor("Author", orcidAuthority)
            .build();

        Item item = workspaceItem.getItem();

        context.restoreAuthSystemState();

        item = context.reloadEntity(item);
        assertThat(item.getMetadata(), hasItem(with("dc.contributor.author", "Author", null, orcidAuthority, 0, 600)));

        context.turnOffAuthorisationSystem();

        Item author = ItemBuilder.createItem(context, personCollection)
            .withTitle("Author")
            .withRelationshipType("Person")
            .withOrcidIdentifier("0000-0002-1825-0097")
            .build();

        context.restoreAuthSystemState();

        item = context.reloadEntity(item);
        assertThat(item.getMetadata(),
            hasItem(with("dc.contributor.author", "Author", null, author.getID().toString(), 0, 600)));
    }

    @Test
    public void testItemReferenceResolverConsumerViaRest() throws Exception {

        context.turnOffAuthorisationSystem();

        String orcidAuthority = "will be referenced::ORCID::0000-0002-1825-0097";

        WorkspaceItem firstWsItem = WorkspaceItemBuilder.createWorkspaceItem(context, publicationCollection)
            .withTitle("Submission Item")
            .withIssueDate("2017-10-17")
            .withFulltext("article.pdf", null, "test".getBytes())
            .withAuthor("Mario Rossi", orcidAuthority)
            .withAuthorAffilitation("4Science")
            .withType("Article")
            .withSubmitter(submitter)
            .grantLicense()
            .build();

        WorkspaceItem secondWsItem = WorkspaceItemBuilder.createWorkspaceItem(context, publicationCollection)
            .withTitle("Another submission Item")
            .withIssueDate("2020-10-17")
            .withFulltext("another-article.pdf", null, "test".getBytes())
            .withAuthor("Mario Rossi", orcidAuthority)
            .withType("Article")
            .withSubmitter(submitter)
            .grantLicense()
            .build();

        context.restoreAuthSystemState();

        String authToken = getAuthToken(submitter.getEmail(), password);

        submitItemViaRest(authToken, firstWsItem.getID());

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", firstWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(orcidAuthority)));

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", secondWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(orcidAuthority)));

        context.turnOffAuthorisationSystem();

        WorkspaceItem authorItem = WorkspaceItemBuilder.createWorkspaceItem(context, personCollection)
            .withTitle("Mario Rossi")
            .withFulltext("cv.pdf", null, "test".getBytes())
            .withOrcidIdentifier("0000-0002-1825-0097")
            .withSubmitter(submitter)
            .grantLicense()
            .build();

        context.restoreAuthSystemState();

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", firstWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(orcidAuthority)));

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", secondWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(orcidAuthority)));

        submitItemViaRest(authToken, authorItem.getID());

        String authorUUID = authorItem.getItem().getID().toString();

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", firstWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(authorUUID)));

        getClient(authToken).perform(get(BASE_REST_SERVER_URL + "/api/core/items/{id}", secondWsItem.getItem().getID()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata['dc.contributor.author'][0].authority", is(authorUUID)));
    }

    private Collection createCollection(String name, String relationshipType) throws Exception {
        return CollectionBuilder.createCollection(context, parentCommunity)
            .withName(name)
            .withRelationshipType(relationshipType)
            .withSubmitterGroup(submitter)
            .build();
    }

    private void submitItemViaRest(String authToken, Integer wsId) throws Exception, SQLException {
        getClient(authToken).perform(post(BASE_REST_SERVER_URL + "/api/workflow/workflowitems")
            .content("/api/submission/workspaceitems/" + wsId).contentType(textUriContentType))
            .andExpect(status().isCreated());
    }

    private String formatWillBeReferencedAuthority(String authorityPrefix, String value) {
        return AuthorityValueService.REFERENCE + authorityPrefix + "::" + value;
    }

}
