/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.services.impl.xoai;

import com.google.common.collect.Lists;
import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.services.api.SetRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.xoai.data.DSpaceSet;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.dspace.xoai.services.api.config.ConfigurationService;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DSpaceSetRepository implements SetRepository
{
    private static final Logger log = LogManager.getLogger(DSpaceSetRepository.class);

    private final ConfigurationService _context;

    public DSpaceSetRepository(ConfigurationService context)
    {
        _context = context;
    }

    private int getCommunityCount()
    {
      return Integer.parseInt(_context.getProperty("oai", "community.count"));
    }

    private int getCollectionCount()
    {
      return Integer.parseInt(_context.getProperty("oai", "collection.count"));
    }

    /**
     * Produce a list of DSpaceCommunitySet.  The list is a segment of the full
     * list of Community ordered by ID.
     *
     * @param offset start this far down the list of Community.
     * @param length return up to this many Sets.
     * @return some Sets representing the Community list segment.
     */
    private List<Set> community(int offset, int length)
    {
                List<Set> array = new ArrayList<Set>();
        List<String> newArrayList = Lists.newArrayList(_context.getProperty("oai", "community.list").split(";"));
        
        newArrayList= newArrayList.subList(
    Math.min(newArrayList.size(), offset),
    Math.min(newArrayList.size(), offset + length));
        
            for(String community : newArrayList)
            {
                array.add(DSpaceSet.newDSpaceCommunitySet(
                        "com_"+community,
                        community));
            }

        return array;
    }

    /**
     * Produce a list of DSpaceCollectionSet.  The list is a segment of the full
     * list of Collection ordered by ID.
     *
     * @param offset start this far down the list of Collection.
     * @param length return up to this many Sets.
     * @return some Sets representing the Collection list segment.
     */
    private List<Set> collection(int offset, int length)
    {
              List<Set> array = new ArrayList<Set>();
        List<String> newArrayList = Lists.newArrayList(_context.getProperty("oai", "collection.list").split(";"));
                newArrayList= newArrayList.subList(
              Math.min(newArrayList.size(), offset),
              Math.min(newArrayList.size(), offset + length));
            for(String community : newArrayList)
            {
                array.add(DSpaceSet.newDSpaceCollectionSet(
                        "col_"+community,
                        community));
            }

        return array;
    }

    @Override
    public ListSetsResult retrieveSets(int offset, int length)
    {
        // Only database sets (virtual sets are added by lyncode common library)
        log.debug("Querying sets. Offset: " + offset + " - Length: " + length);
        List<Set> array = new ArrayList<Set>();
        int communityCount = this.getCommunityCount();
        log.debug("Communities: " + communityCount);
        int collectionCount = this.getCollectionCount();
        log.debug("Collections: " + collectionCount);

        if (offset < communityCount)
        {
            if (offset + length > communityCount)
            {
                // Add some collections
                List<Set> tmp = community(offset, length);
                array.addAll(tmp);
                array.addAll(collection(0, length - tmp.size()));
            }
            else
                array.addAll(community(offset, length));
        }
        else if (offset < communityCount + collectionCount)
        {
            array.addAll(collection(offset - communityCount, length));
        }
        log.debug("Has More Results: "
                + ((offset + length < communityCount + collectionCount) ? "Yes"
                        : "No"));
        return new ListSetsResult(offset + length < communityCount
                + collectionCount, array, communityCount + collectionCount);
    }

    @Override
    public boolean supportSets()
    {
        return true;
    }

    @Override
    public boolean exists(String setSpec)
    {
      if (setSpec.startsWith("col_"))
        {
            return _context.getProperty("oai", "collection.list").contains(setSpec.replace("col_", "").replace("_", "/"));
        }
        else if (setSpec.startsWith("com_"))
        {
            return _context.getProperty("oai", "community.list").contains(setSpec.replace("com_", "").replace("_", "/"));
        }
        return false;
    }

}
