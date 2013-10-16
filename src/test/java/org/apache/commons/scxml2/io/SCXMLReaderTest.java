/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.scxml2.io;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml2.ErrorReporter;
import org.apache.commons.scxml2.EventDispatcher;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.SCXMLTestHelper;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.CustomAction;
import org.apache.commons.scxml2.model.ExternalContent;
import org.apache.commons.scxml2.model.Final;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import org.apache.commons.scxml2.model.Send;
import org.apache.commons.scxml2.model.State;
import org.apache.commons.scxml2.model.Transition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
/**
 * Unit tests {@link org.apache.commons.scxml2.io.SCXMLReader}.
 */
public class SCXMLReaderTest {

    // Test data
    private URL microwave01, microwave02, transitions01, prefix01, send01,
        microwave03, microwave04, scxmlinitialattr, action01;
    private SCXML scxml;
    private String scxmlAsString;

    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        microwave01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/env/jsp/microwave-01.xml");
        microwave02 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/env/jsp/microwave-02.xml");
        microwave03 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/env/jexl/microwave-03.xml");
        microwave04 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/env/jexl/microwave-04.xml");
        transitions01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/transitions-01.xml");
        send01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/send-01.xml");
        prefix01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/prefix-01.xml");
        scxmlinitialattr = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/io/scxml-initial-attr.xml");
        action01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml2/io/custom-action-body-test-1.xml");
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        microwave01 = microwave02 = microwave03 = microwave04 = transitions01 = prefix01 = send01 = action01 = null;
        scxml = null;
        scxmlAsString = null;
    }

    /**
     * Test the implementation
     */    
    @Test
    public void testSCXMLReaderMicrowave01Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(microwave01);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderMicrowave02Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(microwave02);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderMicrowave03Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(microwave03);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderMicrowave04Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(microwave04);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderTransitions01Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(transitions01);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderPrefix01Sample() throws Exception {
        scxml = SCXMLTestHelper.parse(prefix01);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
    }
    
    @Test
    public void testSCXMLReaderSend01Sample() throws Exception {
        // Digest
        scxml = SCXMLTestHelper.parse(send01);
        State ten = (State) scxml.getInitialTarget();
        Assert.assertEquals("ten", ten.getId());
        List<Transition> ten_done = ten.getTransitionsList("ten.done");
        Assert.assertEquals(1, ten_done.size());
        Transition ten2twenty = ten_done.get(0);
        List<Action> actions = ten2twenty.getActions();
        Assert.assertEquals(1, actions.size());
        Send send = (Send) actions.get(0);
        Assert.assertEquals("send1", send.getSendid());
        /* Serialize
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
        String expectedFoo2Serialization =
            "<foo xmlns=\"http://my.test.namespace\" id=\"foo2\">"
            + "<prompt xmlns=\"http://foo.bar.com/vxml3\">This is just"
            + " an example.</prompt></foo>";
        Assert.assertFalse(scxmlAsString.indexOf(expectedFoo2Serialization) == -1);
        */
    }
    
    @Test
    public void testSCXMLReaderInitialAttr() throws Exception {
        scxml = SCXMLTestHelper.parse(scxmlinitialattr);
        Assert.assertNotNull(scxml);
        scxmlAsString = serialize(scxml);
        Assert.assertNotNull(scxmlAsString);
        Final foo = (Final) scxml.getInitialTarget();
        Assert.assertEquals("foo", foo.getId());
    }
    
    @Test
    public void testSCXMLReaderCustomActionWithBodyTextSample() throws Exception {
        List<CustomAction> cas = new ArrayList<CustomAction>();
        CustomAction ca = new CustomAction("http://my.custom-actions.domain",
            "action", MyAction.class);
        cas.add(ca);
        scxml = SCXMLTestHelper.parse(action01, cas);
        State state = (State) scxml.getInitialTarget();
        Assert.assertEquals("actions", state.getId());
        List<Action> actions = state.getOnEntry().getActions();
        Assert.assertEquals(1, actions.size());
        MyAction my = (MyAction) actions.get(0);
        Assert.assertNotNull(my);
        Assert.assertTrue(my.getExternalNodes().size() > 0);
    }

    private String serialize(final SCXML scxml) throws IOException, XMLStreamException {
        scxmlAsString = SCXMLWriter.write(scxml);
        Assert.assertNotNull(scxmlAsString);
        return scxmlAsString;
    }

    public static class MyAction extends Action implements ExternalContent {
        private static final long serialVersionUID = 1L;

        private List<Node> nodes = new ArrayList<Node>();

        @Override
        public void execute(EventDispatcher evtDispatcher,
                ErrorReporter errRep, SCInstance scInstance, Log appLog,
                Collection<TriggerEvent> derivedEvents)
        throws ModelException, SCXMLExpressionException {
            // Not relevant to test
        }

        @Override
        public List<Node> getExternalNodes() {
            return nodes;
        }

    }

}

