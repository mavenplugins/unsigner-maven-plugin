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
import java.util.Collection;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Unsign goal
 */
@Mojo(name = "unsign", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class UnsignGoal extends AbstractMojo
{
   @Parameter(defaultValue = "${project}", readonly = true)
   private MavenProject project;

   @Parameter(defaultValue = "false", property = "unsigner.skip")
   private boolean skip;

   @Parameter(defaultValue = "true", property = "unsigner.processAttachments")
   private boolean processAttachments;

   @Parameter(defaultValue = "false", property = "unsigner.processDependencies")
   private boolean processDependencies;

   @Parameter(defaultValue = "", property = "unsigner.filter")
   private String[] includes;

   @Parameter(defaultValue = "", property = "unsigner.filter")
   private String[] excludes;

   /**
    * {@inheritDoc}
    * 
    * @see org.apache.maven.plugin.Mojo#execute()
    */
   public void execute() throws MojoExecutionException, MojoFailureException
   {
      if (skip)
      {
         getLog().info("Skipping unsigner per configuration.");
         return;
      }

      for(String s : includes)
         getLog().info("+++ " + s);
      for(String s : excludes)
         getLog().info("--- " + s);

      final Unsigner unsigner = new Unsigner();
      int unsigned = 0;

      if (processAttachments)
         unsigned += unsignArtifacts(project.getAttachedArtifacts(), unsigner);

      if (processDependencies)
         unsigned += unsignArtifacts(project.getArtifacts(), unsigner);

      getLog().info("Unsigned " + unsigned + " jars");
   }

   /**
    * Unsign artifacts in given list.
    *
    * @param artifacts collection of artifacts
    * @param unsigner unsigner
    * @return number of unsigned artifacts
    */
   private int unsignArtifacts(Collection<Artifact> artifacts, Unsigner unsigner)
   {
      if (artifacts == null)
         return 0;

      int unsigned = 0;
      for(final Artifact a : artifacts)
      {
         if (includes.length > 0)
         {
            boolean matched = false;
            for(String id : includes)
            {
               matched = Glob.match(id, a.getId());
               if (matched)
                  break;
            }
            if (!matched)
               continue;
         }

         if (excludes.length > 0)
         {
            boolean matched = false;
            for(String id : excludes)
            {
               matched = Glob.match(id, a.getId());
               if (matched)
                  break;
            }
            if (matched)
               continue;
         }

         if (a.getFile() != null && a.getFile().getName().endsWith(".jar"))
         {
            final File src = a.getFile();
            if (src.exists() && !src.isDirectory())
            {
               getLog().info("Unsigning project artifact " + a.getId());
               unsigner.unsign(src, getLog());
               unsigned++;
            }
         }
      }
      return unsigned;
   }
}
