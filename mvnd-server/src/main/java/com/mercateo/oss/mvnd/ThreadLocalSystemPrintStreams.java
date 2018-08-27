/**
 * Copyright © 2018 Mercateo AG (http://www.mercateo.com)
 *
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
 */
package com.mercateo.oss.mvnd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.function.Supplier;

public final class ThreadLocalSystemPrintStreams implements AutoCloseable {

	public static synchronized void install() {
		if (System.out instanceof TLPrintStream) {
			System.err.println("Already installed. skipping.");
		}
		else {
			register(System.out, System.err);
			
			System.setOut(new TLPrintStream(outs::get));
	        System.setErr(new TLPrintStream(errs::get));
		}
	}

	private static InheritableThreadLocal<PrintStream> outs = new InheritableThreadLocal<>();

	private static InheritableThreadLocal<PrintStream> errs = new InheritableThreadLocal<>();

	private static final OutputStream nullStream = new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	};

	public synchronized static void register(PrintStream out, PrintStream err) {
		outs.set(out);
		errs.set(err);
	}

	public synchronized static void release() {
		outs.remove();
		errs.remove();
	}

	/**
	 * Decorator that redirects calls to the printstream provided by the supplier.
	 * @author usr
	 *
	 */
	static class TLPrintStream extends PrintStream {
		private Supplier<PrintStream> supplier;

		public TLPrintStream(Supplier<PrintStream> s) {
			super(nullStream);
			this.supplier = s;
		}

		public int hashCode() {
			return get().hashCode();
		}

		public void write(byte[] b) throws IOException {
			get().write(b);
		}

		public boolean equals(Object obj) {
			return get().equals(obj);
		}

		public String toString() {
			return get().toString();
		}

		public void flush() {
			get().flush();
		}

		public void close() {
			get().close();
		}

		public boolean checkError() {
			return get().checkError();
		}

		public void write(int b) {
			get().write(b);
		}

		public void write(byte[] buf, int off, int len) {
			get().write(buf, off, len);
		}

		public void print(boolean b) {
			get().print(b);
		}

		public void print(char c) {
			get().print(c);
		}

		public void print(int i) {
			get().print(i);
		}

		public void print(long l) {
			get().print(l);
		}

		public void print(float f) {
			get().print(f);
		}

		public void print(double d) {
			get().print(d);
		}

		public void print(char[] s) {
			get().print(s);
		}

		public void print(String s) {
			get().print(s);
		}

		public void print(Object obj) {
			get().print(obj);
		}

		public void println() {
			get().println();
		}

		public void println(boolean x) {
			get().println(x);
		}

		public void println(char x) {
			get().println(x);
		}

		public void println(int x) {
			get().println(x);
		}

		public void println(long x) {
			get().println(x);
		}

		public void println(float x) {
			get().println(x);
		}

		public void println(double x) {
			get().println(x);
		}

		public void println(char[] x) {
			get().println(x);
		}

		public void println(String x) {
			get().println(x);
		}

		public void println(Object x) {
			get().println(x);
		}

		public PrintStream printf(String format, Object... args) {
			return get().printf(format, args);
		}

		public PrintStream printf(Locale l, String format, Object... args) {
			return get().printf(l, format, args);
		}

		public PrintStream format(String format, Object... args) {
			return get().format(format, args);
		}

		public PrintStream format(Locale l, String format, Object... args) {
			return get().format(l, format, args);
		}

		public PrintStream append(CharSequence csq) {
			return get().append(csq);
		}

		public PrintStream append(CharSequence csq, int start, int end) {
			return get().append(csq, start, end);
		}

		public PrintStream append(char c) {
			return get().append(c);
		}

		private PrintStream get() {
			return supplier.get();
		}

	}

	@Override
	public void close() throws Exception {
		release();
	}

}
