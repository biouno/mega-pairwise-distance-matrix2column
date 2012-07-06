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
package org.biouno.mega;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A simple parser for MEGA CC output files.
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class SimpleMegaParser {

	public enum States {
		STARTED, PROCESSING_IDS, PROCESSING_TABLE_HEADER, PROCESSING_TABLE, FINISHED
	};
	
	private Pattern SPECIES_ID_PATTERN = Pattern.compile("\\[\\s?(\\d*)\\]\\s+#(.*)");
	private Pattern TABLE_LINE_PATTERN = Pattern.compile("\\[\\s?(\\d+)\\](.*)");
	
	private States state;
	
	private Map<Integer, Species> species = new HashMap<Integer, Species>();
	private List<SpeciesDistance> distances = new ArrayList<SpeciesDistance>();
	
	public SimpleMegaParser() {
	}
	
	public void parse(String fileName) {
		this.parse(new File(fileName));
	}
	
	public void parse(File file) {
		state = States.STARTED;
		List<String> lines = new ArrayList<String>();
		try {
			lines = IOUtils.readLines(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String line : lines) {
			processLine(line);
		}
		state = States.FINISHED;
	}

	/**
	 * @param line
	 */
	private void processLine(String line) {
		Matcher matcher = SPECIES_ID_PATTERN.matcher(line);
		
		if(state == States.STARTED) {
			if(matcher.matches()) {
				state = States.PROCESSING_IDS;
			}
		}
		
		if(state == States.PROCESSING_IDS) {
			if(StringUtils.isNotBlank(line)) {
				if(matcher.matches()) {
					Integer id = Integer.parseInt(matcher.group(1));
					String name = matcher.group(2);
					Species s = new Species(id, name);
					this.species.put(s.getId(), s);
				}
			} else {
				state = States.PROCESSING_TABLE_HEADER;
			}
		}
		
		Matcher lineMatcher = TABLE_LINE_PATTERN.matcher(line);
		
		if(state == States.PROCESSING_TABLE_HEADER) {
			if(StringUtils.isNotBlank(line)) {
				if(lineMatcher.matches()) {
					state = States.PROCESSING_TABLE;
				}
			}
		}
		
		if(state == States.PROCESSING_TABLE) {
			if(StringUtils.isNotBlank(line)) {
				if(lineMatcher.matches()) {
					Integer id = Integer.parseInt(lineMatcher.group(1));
					String distances = lineMatcher.group(2);
					if(StringUtils.isNotBlank(distances)) {
						StringTokenizer st = new StringTokenizer(distances, " ");
						int processingId = 1;
						while(st.hasMoreTokens()) {
							Double distance = Double.parseDouble(st.nextToken().trim());
							
							Species from = this.species.get(processingId);
							Species to = this.species.get(id);
							
							SpeciesDistance sd = new SpeciesDistance();
							sd.setFrom(from);
							sd.setTo(to);
							sd.setDistance(distance);
							this.distances.add(sd);
							
							++processingId; // column ID, and also the species ID
						}
					}
				}
			}
		}
	}
	
	/**
	 * @return the state
	 */
	public States getState() {
		return state;
	}
	
	/**
	 * @return the species
	 */
	public Map<Integer, Species> getSpecies() {
		return this.species;
	}
	
	/**
	 * @return the distances
	 */
	public List<SpeciesDistance> getDistances() {
		return distances;
	}
	
	public static void main(String[] args) {
		String fileName = args[0];
		String outputFile = args[1];
		SimpleMegaParser p = new SimpleMegaParser();
		p.parse(SimpleMegaParser.class.getClassLoader().getResource(fileName).getFile());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("Species 1|Species 2|Dist");
		for(SpeciesDistance sd : p.getDistances()) {
			pw.println(sd.getFrom().getName() + "|" + 
					sd.getTo().getName() + "|" + 
					sd.getDistance());
		}
		try {
			FileUtils.write(new File(outputFile), sw.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
