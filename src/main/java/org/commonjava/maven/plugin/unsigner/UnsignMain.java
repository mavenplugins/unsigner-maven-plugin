/*
 * Copyright 2012 Red Hat, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.commonjava.maven.plugin.unsigner;

import java.io.File;
import java.util.logging.Logger;

public class UnsignMain
{

    public static void main( final String[] args )
    {
        final Unsigner unsigner = new Unsigner();
        final Logger log = Logger.getLogger( UnsignMain.class.getName() );
        for ( final String arg : args )
        {
            final File src = new File( arg );
            if ( src.exists() && src.isFile() )
            {
                log.info( "Processing: " + src );
                unsigner.unsign( src );
            }
            else
            {
                log.info( "Cannot read: " + src + "; skipping." );
            }
        }
    }

}
