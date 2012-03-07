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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * @goal unsign
 * @phase package
 */
public class UnsignGoal
    implements Mojo
{

    private Log log;

    /**
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * {@inheritDoc}
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        final Artifact artifact = project.getArtifact();
        final File src = artifact.getFile();
        new Unsigner().unsign( src );
    }

    /**
     * {@inheritDoc}
     * @see org.apache.maven.plugin.Mojo#setLog(org.apache.maven.plugin.logging.Log)
     */
    public void setLog( final Log log )
    {
        this.log = log;
    }

    /**
     * {@inheritDoc}
     * @see org.apache.maven.plugin.Mojo#getLog()
     */
    public synchronized Log getLog()
    {
        if ( log == null )
        {
            log = new SystemStreamLog();
        }

        return log;
    }
}
