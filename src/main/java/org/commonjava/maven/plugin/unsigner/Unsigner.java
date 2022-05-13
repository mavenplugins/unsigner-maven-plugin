/**
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.maven.plugin.logging.Log;

public class Unsigner
{
   public File unsign(final File src, Log log) throws RuntimeException
   {
      final File bak = new File(src.getParentFile(), src.getName() + ".bak");

      if (bak.exists())
      {
         bak.delete();
      }

      log.debug("Backing up original jar: " + src + "\n    to: " + bak);
      src.renameTo(bak);

      ZipFile in = null;
      ZipOutputStream out = null;
      try
      {
         in = new ZipFile(bak);
         out = new ZipOutputStream(new FileOutputStream(src));

         for(final Enumeration<? extends ZipEntry> entries = in.entries(); entries.hasMoreElements();)
         {
            final ZipEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".SF") || entry.getName().endsWith(".RSA") || entry.getName().endsWith(".LIST"))
            {
               continue;
            }

            out.putNextEntry(entry);
            if (!entry.isDirectory())
            {
               final InputStream stream = in.getInputStream(entry);
               final byte[] buf = new byte[16384];
               int read = -1;
               while((read = stream.read(buf)) > -1)
               {
                  out.write(buf, 0, read);
               }
            }
            out.closeEntry();
         }
      }
      catch(final IOException e)
      {
         throw new RuntimeException("Cannot read: " + bak, e);
      }
      finally
      {
         if (in != null)
         {
            try
            {
               in.close();
            }
            catch(final IOException e)
            {
            }
         }

         if (out != null)
         {
            try
            {
               out.close();
            }
            catch(final IOException e)
            {
               log.warn("Failed to close output jar: " + src + ". Error: " + e.getMessage(), e);
            }
         }
      }

      return bak;
   }
}
