/*
* File   : $Source: /alkacon/cvs/opencms/src/com/opencms/file/genericSql/Attic/CmsQueries.java,v $
* Date   : $Date: 2003/03/04 17:19:29 $
* Version: $Revision: 1.44 $
*
* This library is part of OpenCms -
* the Open Source Content Mananagement System
*
* Copyright (C) 2001  The OpenCms Group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* For further information about OpenCms, please see the
* OpenCms Website: http://www.opencms.org 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.opencms.file.genericSql;

import com.opencms.boot.I_CmsLogChannels;
import com.opencms.core.A_OpenCms;

import java.util.Properties;

/**
 * This interface is defines all queries used in the DB-Access class.
 * @author Michael Emmerich
 *
 * @version $Revision: 1.44 $ $Date: 2003/03/04 17:19:29 $
 */
public class CmsQueries
{
    private static Properties m_queries = null;
    /**
     * CmsQueries constructor comment.
     */
    public CmsQueries() {
        if(m_queries == null) {
            m_queries = new Properties();
            try {
                m_queries.load(getClass().getClassLoader().getResourceAsStream("com/opencms/file/genericSql/query.properties"));
            } catch(NullPointerException exc) {
                if(A_OpenCms.isLogging() && I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING) {
                    A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, "[CmsQueries] cannot get com/opencms/file/genericSql/query.properties");
                }
            } catch(java.io.IOException exc) {
                if(A_OpenCms.isLogging() && I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING) {
                    A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, "[CmsQueries] cannot get com/opencms/file/genericSql/query.properties");
                }
            }
        }
    }

    /**
     * Get the value for the query name
     *
     * @param queryName the name of the property
     * @return The value of the property
     */
    public String get(String queryName){
        if(m_queries == null) {
            m_queries = new Properties();
            try {
                m_queries.load(getClass().getClassLoader().getResourceAsStream("com/opencms/file/genericSql/query.properties"));
            } catch(NullPointerException exc) {
                if(A_OpenCms.isLogging() && I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING) {
                    A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, "[CmsQueries] cannot get com/opencms/file/genericSql/query.properties");
                }
            } catch(java.io.IOException exc) {
                if(A_OpenCms.isLogging() && I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING) {
                    A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, "[CmsQueries] cannot get com/opencms/file/genericSql/query.properties");
                }
            }
        }
        
        String value = null;
        
        if ((value=m_queries.getProperty(queryName))==null) {
            if(A_OpenCms.isLogging() && I_CmsLogChannels.C_PREPROCESSOR_IS_LOGGING) {
                A_OpenCms.log(I_CmsLogChannels.C_OPENCMS_CRITICAL, "[CmsQueries] query '" + queryName + "' not found in query.properties!" );
            }            
        }
        
        return value;
    }
}
