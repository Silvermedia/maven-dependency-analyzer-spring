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
package org.apache.maven.shared.dependency.analyzer.spring;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.TestCase;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlBeanVisitor;

public class DefaultSpringXmlBeanVisitorTest
    extends TestCase
{
    public void testVisit()
        throws Exception
    {

        final Class<SomeTestClass> TESTCLASS = SomeTestClass.class;

        final Set<String> result = new HashSet<String>();
        final DefaultSpringXmlBeanVisitor visitor = new DefaultSpringXmlBeanVisitor( result );

        visitor.visitBeanDefinition( TESTCLASS.getName() );

        assertNotNull( result );
        System.out.println("Got "+result);

        // the following assertEquals() will fail
        // when running this test with byte-code enhancing
        // plugins that dynamically introduce 
        // additional dependencies (eclEmma is one of those)
        assertEquals( 1, result.size() );
       // assertEquals( 3, result.size() );

        System.out.println("Got "+result);
        assertTrue( result.contains( TESTCLASS.getName() ) );
       // assertTrue( result.contains( "org.apache.maven.plugin.logging.Log" ) );
    }
}
