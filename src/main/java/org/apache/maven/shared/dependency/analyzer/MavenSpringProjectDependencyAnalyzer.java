/**
 * Copyright 2010 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.maven.shared.dependency.analyzer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.spring.ArtifactForClassResolver;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlFileLocator;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlParser;
import org.apache.maven.shared.dependency.analyzer.spring.SpringProjectDependencyAnalyzer;

/**
 * @author tobias.gierke@code-sourcery.de
 * @plexus.component role="org.apache.maven.shared.dependency.analyzer.ProjectDependencyAnalyzer" role-hint="spring"
 */
public class MavenSpringProjectDependencyAnalyzer
    extends DefaultProjectDependencyAnalyzer
{
    private static Logger log = Logger.getLogger(MavenSpringProjectDependencyAnalyzer.class.getCanonicalName());

    @SuppressWarnings( "unchecked" )
    @Override
    protected Set<String> buildDependencyClasses( MavenProject project )
        throws java.io.IOException
    {
        final Map<Artifact, Set<String>> typedMap = new LinkedHashMap<Artifact, Set<String>>();
        final Set<String> result = super.buildDependencyClasses( project );

        log.info( "Including dependencies from Spring XMLs in analysis" );

        final ArtifactForClassResolver resolver = new ArtifactForClassResolver()
        {
            @Override
            public Artifact findArtifactForClass( String className )
            {
                return findArtifactForClassName( typedMap, className );
            }
        };

        final SpringProjectDependencyAnalyzer analyzer = new SpringProjectDependencyAnalyzer();

        final DefaultSpringXmlFileLocator fileLocator = new DefaultSpringXmlFileLocator();

        analyzer.setFileLocator( fileLocator );
        analyzer.setFileParser( new DefaultSpringXmlParser() );
        analyzer.setResolver( resolver );

        try
        {
            analyzer.addSpringDependencyClasses( project, result );
        }
        catch ( RuntimeException e )
        {
            throw e;
        }
        catch ( IOException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            // TODO: Most likely not the right type of exception to throw
            throw new RuntimeException( e );
        }
        return result;
    }

    @Override
    protected org.apache.maven.artifact.Artifact findArtifactForClassName(Map<org.apache.maven.artifact.Artifact, Set<String>> artifactClassMap, String className) {
        return super.findArtifactForClassName(artifactClassMap, className);
    }
}
