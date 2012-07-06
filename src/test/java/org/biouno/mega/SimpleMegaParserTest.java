package org.biouno.mega;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
/*
 * The MIT License
 *
 * Copyright (c) <2012> <Bruno P. Kinoshita>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Tests SimpleMegaParser.
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 * @see SimpleMegaParser
 */
public class SimpleMegaParserTest {
	
	private static final String TEST_INPUT = "Step1_MegaInput_COI-2840";
	private static final Integer EXPECTED_SPECIES = 53;
	private static final Integer EXPECTED_DISTANCES = 1378;
	private SimpleMegaParser parser = null;
	
	@Before
	public void setUp() {
		parser = new SimpleMegaParser();
	}
	
	@Test
	public void testSimpleMegaParser() {
		parser.parse(SimpleMegaParserTest.class.getClassLoader().getResource(TEST_INPUT).getFile());
		Assert.assertEquals("Wrong number of species found", EXPECTED_SPECIES.intValue(), parser.getSpecies().size());
		Assert.assertEquals("Wrong number of distances found", EXPECTED_DISTANCES.intValue(), parser.getDistances().size());
	}
	
}
