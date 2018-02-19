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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class DefaultSpringXmlParser
    implements SpringXmlParser
{
    private static final String SPRING_NAMESPACE = "http://www.springframework.org/schema/beans";

    private final XMLInputFactory inputFactory;
    
    public DefaultSpringXmlParser() {
        inputFactory = XMLInputFactory.newInstance();
    }
    
    @Override
    public void parse( InputStream springXml, SpringFileBeanVisitor visitor )
        throws Exception
    {
        final InputStream in = new BufferedInputStream( springXml );
        try
        {
            final XMLEventReader reader = inputFactory.createXMLEventReader( in );

            boolean isSpringXml = false;
            while ( reader.hasNext() )
            {
                final XMLEvent event = reader.nextEvent();

                if ( event.getEventType() != XMLEvent.START_ELEMENT )
                {
                    continue;
                }

                final StartElement startElement = event.asStartElement();
                final String tagName = startElement.getName().getLocalPart();
                final String namespaceURI = startElement.getName().getNamespaceURI();

                if ( !isSpringXml )
                {
                    if ( !"beans".equals( tagName ) || !SPRING_NAMESPACE.equals( namespaceURI ) )
                    {
                        throw new NoSpringXmlException( "Not a Spring XML file, expected <beans> root element",
                                                        event.getLocation().getCharacterOffset() );
                    }
                    isSpringXml = true;
                }
                else if ( "bean".equals( tagName ) )
                {
                    final String clasz = getClassNameFromBeanTag( startElement );
                    if ( clasz != null )
                    {
                        visitor.visitBeanDefinition( clasz );
                    }
                }
            }
        }
        catch ( javax.xml.stream.XMLStreamException e )
        {
            throw new NoSpringXmlException( "Exception while parsing XML file" );
        }
        finally
        {
            in.close();
        }
    }

    @SuppressWarnings( "unchecked" )
    protected String getClassNameFromBeanTag( StartElement startElement )
    {
        final Iterator<Attribute> it = startElement.getAttributes();
        while ( it.hasNext() )
        {
            final Attribute a = it.next();
            if ( "class".equals( a.getName().getLocalPart() ) )
            {
                return a.getValue();
            }
        }
        return null;
    }
}
