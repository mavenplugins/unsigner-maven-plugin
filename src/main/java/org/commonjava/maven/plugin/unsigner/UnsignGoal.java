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
import java.io.IOException;
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
import org.apache.maven.shared.jarsigner.JarSignerUtil;

/**
 * Unsign goal
 */
@Mojo(name = "unsign", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class UnsignGoal extends AbstractMojo {
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
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (this.skip) {
      getLog().info("Skipping unsigner per configuration.");
      return;
    }

    for (String s : this.includes) {
      getLog().info("+++ " + s);
    }
    for (String s : this.excludes) {
      getLog().info("--- " + s);
    }

    int unsigned = 0;

    if (this.processAttachments) {
      unsigned += unsignArtifacts(this.project.getAttachedArtifacts());
    }

    if (this.processDependencies) {
      unsigned += unsignArtifacts(this.project.getArtifacts());
    }

    getLog().info("Unsigned " + unsigned + " jars");
  }

  /**
   * Unsign artifacts in given list.
   *
   * @param artifacts collection of artifacts
   * @return number of unsigned artifacts
   * @throws MojoExecutionException
   */
  private int unsignArtifacts(Collection<Artifact> artifacts) throws MojoExecutionException {
    if (artifacts == null) {
      return 0;
    }

    int unsigned = 0;
    for (final Artifact a : artifacts) {
      if (this.includes.length > 0) {
        boolean matched = false;
        for (String id : this.includes) {
          matched = Glob.match(id, a.getId());
          if (matched) {
            break;
          }
        }
        if (!matched) {
          continue;
        }
      }

      if (this.excludes.length > 0) {
        boolean matched = false;
        for (String id : this.excludes) {
          matched = Glob.match(id, a.getId());
          if (matched) {
            break;
          }
        }
        if (matched) {
          continue;
        }
      }

      final File artifactFile = a.getFile();
      try {
        if (artifactFile != null && artifactFile.exists() && !artifactFile.isDirectory()
            && JarSignerUtil.isZipFile(artifactFile) && JarSignerUtil.isArchiveSigned(artifactFile)) {
          getLog().info("Unsigning project artifact " + a.getId());
          JarSignerUtil.unsignArchive(artifactFile);
          unsigned++;
        }
      } catch (final IOException ioe) {
        throw new MojoExecutionException("Failed to unsign artifact file=" + artifactFile, ioe);
      }
    }
    return unsigned;
  }
}
