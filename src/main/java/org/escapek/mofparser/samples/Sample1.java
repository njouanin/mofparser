package org.escapek.mofparser.samples;

import java.io.IOException;
import java.io.InputStream;

import org.escapek.mofparser.MOFParser;
import org.escapek.mofparser.exceptions.MOFParserException;
import org.escapek.mofparser.helpers.LoggingHandler;

public class Sample1 {
	public static void main(String[] args) {

		//Create parser instance
		MOFParser parser = new MOFParser();

		try {
			//Open an input stream from a file content
			InputStream is = Sample1.class.getResourceAsStream("sample.mof");

			//Run parse
			parser.parse(is, new LoggingHandler());
		}
		catch (IOException e) {
			//Exception while reading input stream
			System.err.println(e.getMessage());
		}
		catch (MOFParserException mpe) {
			//Parse error
			System.err.println(mpe.getMessage());
		}
	}
}
