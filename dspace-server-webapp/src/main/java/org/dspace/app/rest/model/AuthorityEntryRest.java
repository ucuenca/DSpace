/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dspace.app.rest.RestResourceController;

/**
 * The Authority Entry REST Resource
 *
 * @author Andrea Bollini (andrea.bollini at 4science.it)
 */
public class AuthorityEntryRest extends RestAddressableModel {
    public static final String NAME = "authorityEntry";
    private String id;
    private String display;
    private String value;
    private Boolean selectable;
    private Map<String, String> otherInformation;

    @JsonIgnore
    private String authorityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String value) {
        this.display = value;
    }

    public Map<String, String> getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(Map<String, String> otherInformation) {
        this.otherInformation = otherInformation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getName() {
        return NAME;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public Boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public String getCategory() {
        return AuthorityRest.CATEGORY;
    }

    @Override
    public String getType() {
        return AuthorityEntryRest.NAME;
    }

    @Override
    public Class getController() {
        return RestResourceController.class;
    }

}
