/* Copyright (c) 2016 Christopher Simons
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import java.io.PrintStream;

/**
 * Filters Xerces warnings from standard output and standard error streams.
 *
 * @author Christopher Simons
 */
public class XercesWarningFilter extends PrintStream {
	private static final String BEGIN_SIG
			= "Warning:  org.apache.xerces.parsers.SAXParser:";
	private static final String END_SIG = "is not recognized.";

	private static PrintStream STDOUT;
	private static PrintStream STDERR;

	private XercesWarningFilter(PrintStream printStream) {
		super(printStream);
	}

	private static boolean initialized = false;
	public static synchronized void start() {
		if (!initialized) {
			STDOUT = System.out;
			STDERR = System.err;
			initialized = true;
		}
		System.setOut(new XercesWarningFilter(STDOUT));
		System.setErr(new XercesWarningFilter(STDERR));
	}

	public static synchronized void stop() {
		if (initialized) {
			System.setOut(STDOUT);
			System.setErr(STDERR);
		}
	}

	@Override
	public void println(String s) {
		if (!(s.startsWith(BEGIN_SIG) && s.endsWith(END_SIG))) {
			super.print(s);
		}
	}
}
