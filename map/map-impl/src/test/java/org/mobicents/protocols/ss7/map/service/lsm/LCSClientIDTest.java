/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * @author amit bhayani
 * 
 */
public class LCSClientIDTest {
	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecode() throws Exception {
		byte[] data = new byte[] { (byte)0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LCSClientID lcsClientID = new LCSClientIDImpl();
		lcsClientID.decodeAll(asn);

		assertNotNull(lcsClientID.getLCSClientType());
		assertEquals(LCSClientType.plmnOperatorServices, lcsClientID.getLCSClientType());

		assertNotNull(lcsClientID.getLCSClientInternalID());
		assertEquals(LCSClientInternalID.broadcastService, lcsClientID.getLCSClientInternalID());

		LCSClientName lcsClientName = lcsClientID.getLCSClientName();
		assertNotNull(lcsClientName);
		assertEquals((byte) 0x0f, lcsClientName.getDataCodingScheme());
		USSDString nameString = lcsClientName.getNameString();
		assertEquals("ndmgapp2ndmgapp2", nameString.getString());

	}

	@Test
	public void testEncode() throws Exception {

		byte[] data = new byte[] { (byte)0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		USSDString nameString = mapServiceFactory.createUSSDString("ndmgapp2ndmgapp2");
		LCSClientName lcsClientName = new LCSClientNameImpl((byte) 0x0f, nameString, null);

		LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, LCSClientInternalID.broadcastService, lcsClientName, null,
				null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		lcsClientID.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));

	}
}