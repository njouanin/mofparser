/**
 * Copyright 2008 EscapeK
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */
package org.escapek.mofparser.samples;

import java.io.BufferedReader;
import java.io.FileReader;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.escapek.mofparser.internal.cim23Lexer;
import org.escapek.mofparser.internal.cim23Parser;

/**
 * Snippet class which shows the AST tree representation of a MOF persed file 
 * into a SWT tree.
 * @author nico
 *
 */
public class MOFViewer {

	static class CaseInsensitiveStringStream extends ANTLRStringStream {

		public CaseInsensitiveStringStream(String arg0) {
			super(arg0);
		}

		@Override
		public int LA(int i) {
			if (i == 0) {
	            return 0;
	        }

	        if (i < 0) {
	            i++;
	        }

	        if (((p + i) - 1) >= n) {
	            return (int) CharStream.EOF;
	        }

	        return Character.toLowerCase(data[(p + i) - 1]);
		}
	}
		
		
	static String fileName=null;
	static Tree tree;
	static Text output;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GridData data;
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		shell.setLayout(layout);
		
		final Text text = new Text(shell, SWT.READ_ONLY|SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.horizontalSpan = 1;
		text.setLayoutData(data);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("...");
		Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		button.setSize(size);
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setFilterExtensions(new String[] {"*.mof", "*.*"});
				fileName = dialog.open();
				if (fileName == null) return;
				text.setText(fileName);
			}
		});
		
		output = new Text(shell, SWT.READ_ONLY|SWT.BORDER|SWT.V_SCROLL | SWT.H_SCROLL);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumHeight = 100;
		data.heightHint = 100;
		data.verticalSpan = 3;
		output.setLayoutData(data);

		final Button parseBtn= new Button(shell, SWT.PUSH);
		parseBtn.setText("Parse !!");
		data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.horizontalSpan = 2;
		parseBtn.setLayoutData(data);
		parseBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (fileName == null) return;
				fillTree();
			}
		});

		tree = new Tree (shell, SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumHeight = 200;
		data.horizontalSpan = 2;
		tree.setLayoutData(data);

		shell.setSize (250, 400);
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static void fillTree() {
		try
		{
			String content = readFileAsString(fileName);
			cim23Lexer lex = new cim23Lexer(new MOFViewer.CaseInsensitiveStringStream(content));
		   	CommonTokenStream tokens = new CommonTokenStream(lex);

		    cim23Parser parser = new cim23Parser(tokens);
		    
		    CommonTree t = (CommonTree)(parser.mofSpecification().getTree());
		  
		    tree.removeAll();
		    printTree(t, null);
		    output.setText(content);
			}
		catch(Exception e) {
			output.setText(e.getMessage());
		}
	}
	
	private static void printTree(CommonTree t, TreeItem parent) {
	    if ( t != null ) {
			for ( int i = 0; i < t.getChildCount(); i++ ) {
				TreeItem item;
				if(parent == null)
					item = new TreeItem (tree, 0);
				else
					item = new TreeItem (parent, 0);
				item.setText(t.getChild(i).toString());
				printTree((CommonTree)t.getChild(i), item);
			}
		}
	}

	private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

}
