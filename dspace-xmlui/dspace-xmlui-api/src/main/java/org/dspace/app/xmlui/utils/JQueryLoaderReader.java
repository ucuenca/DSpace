/*
 * JQueryLoaderReader.java
 *
 * Version: $Revision: 5497 $
 *
 * Date: $Date: 2010-10-20 23:06:10 +0200 (wo, 20 okt 2010) $
 *
 * Copyright (c) 2002, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.dspace.app.xmlui.utils;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.reading.AbstractReader;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;

import java.io.IOException;

/**
 * Loads in the jquery javascript library if it hasn't been loaded in already
 *
 * @author Kevin Van de Velde (kevin at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 */
public class JQueryLoaderReader extends AbstractReader {

    @Override
    public void generate() throws IOException, SAXException, ProcessingException {
        String contextPath = ObjectModelHelper.getRequest(objectModel).getContextPath();

        String script = "!window.jQuery && document.write('<script type=\"text/javascript\" src=\"" + contextPath + "/static/js/jquery-1.4.2.min.js\">&nbsp;</script>');";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(script.getBytes("UTF-8"));

        byte[] buffer = new byte[8192];

        ObjectModelHelper.getResponse(objectModel).setHeader("Content-Length", String.valueOf(script.length()));
        int length;
        while ((length = inputStream.read(buffer)) > -1)
        {
            out.write(buffer, 0, length);
        }
        out.flush();

    }
}
