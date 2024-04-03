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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
  /**
   * Current Maven project or project module.
   */
  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  /**
   * Skip execution of this plugin.
   */
  @Parameter(defaultValue = "false", property = "unsigner.skip")
  private boolean skip;

  /**
   * Unsign this projects main artifact if true.
   */
  @Parameter(defaultValue = "true", property = "unsigner.processMainArtifact")
  private boolean processMainArtifact;

  /**
   * Unsign this projects attached artifacts if true.
   */
  @Parameter(defaultValue = "true", property = "unsigner.processAttachments")
  private boolean processAttachments;

  /**
   * Unsign this projects dependency artifacts if true.
   */
  @Parameter(defaultValue = "false", property = "unsigner.processDependencies")
  private boolean processDependencies;

  /**
   * Comma separated pattern(s) to filter artifact IDs to get included. All included, if not set. Wildcard characters
   * '*' and '?' are supported. Patterns are NOT trimmed for white spaces.
   */
  @Parameter(defaultValue = "", property = "unsigner.filter.includes")
  private String[] includes;

  /**
   * Comma separated pattern(s) to filter artifact IDs for exclusion. No excluded, if not set. Wildcard characters
   * '*' and '?' are supported. Exclusion wins over inclusion. Patterns are NOT trimmed for white spaces.
   */
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
      getLog().info("+++ \"" + s + "\"");
    }
    for (String s : this.excludes) {
      getLog().info("--- \"" + s + "\"");
    }

    final List<Artifact> artifacts = new ArrayList<Artifact>();

    if (this.processMainArtifact) {
      artifacts.add(this.project.getArtifact());
    }

    if (this.processAttachments) {
      artifacts.addAll(this.project.getAttachedArtifacts());
    }

    if (this.processDependencies) {
      artifacts.addAll(this.project.getArtifacts());
    }

    final int unsignCount = unsignArtifacts(artifacts);

    getLog().info("Unsigned " + unsignCount + (unsignCount == 1 ? " jar" : " jars"));
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

    int unsignCount = 0;
    for (final Artifact artifact : artifacts) {
      final String artifactId = artifact.getId();
      boolean isIncluded = true;
      if (this.includes.length > 0) {
        isIncluded = false;
        for (String includePattern : this.includes) {
          isIncluded = Glob.match(includePattern, artifactId);
          if (isIncluded) {
            break;
          }
        }
      }

      if (isIncluded && this.excludes.length > 0) {
        for (String excludePattern : this.excludes) {
          if (Glob.match(excludePattern, artifactId)) {
            isIncluded = false;
            break;
          }
        }
      }

      if (!isIncluded) {
        getLog().info("Filter artifact with ID=" + artifactId);
        continue;
      }

      final File artifactFile = artifact.getFile();
      try {
        if (artifactFile != null && artifactFile.exists() && !artifactFile.isDirectory()
            && JarSignerUtil.isZipFile(artifactFile) && JarSignerUtil.isArchiveSigned(artifactFile)) {
          getLog().info("Unsigning artifact with ID=" + artifactId);
          JarSignerUtil.unsignArchive(artifactFile);
          unsignCount++;
        } else {
          getLog().info("Artifact with ID=" + artifactId + " is no signed JAR");
        }
      } catch (final IOException ioe) {
        throw new MojoExecutionException("Failed to unsign artifact file=" + artifactFile, ioe);
      }
    }
    return unsignCount;
  }
}
