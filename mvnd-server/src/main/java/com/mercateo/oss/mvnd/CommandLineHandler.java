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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.unix4j.Unix4j;

import io.grpc.internal.IoUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandLineHandler implements Runnable {
	final String[] args;
	private File mvnDir=new File("/path/to/apache/maven");

	@Override
	public void run() {
		if (args.length != 2 || !args[0].equals("install")) {
			help(true);
			System.exit(2);
		}

		mvnDir = new File(args[1]);
		File mvn = new File(new File(mvnDir, "bin"), "mvn");
		File lib = new File(mvnDir, "lib");
		File bin = new File(mvnDir, "bin");
		File conf = new File(bin, "mvnd.conf");
		File mvnc = new File(bin, "mvnc");

		assume(mvnDir.isDirectory(), mvnDir.getAbsolutePath() + " does not exist, or is not a directory.");
		assume(mvn.exists(), mvn.getAbsolutePath() + " does not exist.");
		assume(!conf.exists(), "Skipping installation. (" + conf.getAbsolutePath() + " already there)");

		System.out.println();
		System.out.println("Patching apache maven in " + mvnDir);
		createBinMvnd(bin);
		createBinM2dConf(bin);
		createLibMvndJar(lib);
		createMvnc(mvnc);

		System.out.println("Patching done.");
		System.out.println();

		help(false);
		System.exit(0);

	}

	private void createMvnc(File mvnc) {
		String os_name = System.getProperty("os.name");
		String arch = System.getProperty ("os.arch");
		String os = os_name+"-"+arch;
		
		System.out.println("Detected Operating System: " + os);

		try (InputStream is = MVNDaemon.class.getResourceAsStream("/client/" + os+ "/mvn-go");) {
			if (is == null)
				noClientAvailableError();
			else
				try (FileOutputStream out = new FileOutputStream(mvnc)) {
					IoUtils.copy(is, out);
				} catch (IOException e) {
					error("While creating " + mvnc.getAbsolutePath(), e);
				}
			mvnc.setExecutable(true);
			System.out.println("Created " + mvnc.getAbsolutePath());
		} catch (IOException e) {
			noClientAvailableError();
		}
	}

	private void noClientAvailableError() {
		System.out.println();
		System.out.println("Sad News: you're out of luck.");
		System.out.println();
		System.out.println("There is no prebuilt client for you environment, so you should head over to");
		System.out.println("https://github.com/uweschaefer/mvn-go");
		System.out.println("and build it yourself.");
		System.out.println();
		System.out.println("If you have any problems, or (even better) want to volunteer, create an issue on github");
		System.out.println();

		System.exit(1);
	}

	private void createLibMvndJar(File lib) {
		File mvnd_jar = new File(lib, "mvnd.jar");
		URL url = MVNDaemon.class.getProtectionDomain().getCodeSource().getLocation();
		assume(url.toExternalForm().endsWith(".jar"), "Was assuming to be run from a jar... exiting.");

		try (InputStream is = url.openConnection().getInputStream();
				FileOutputStream os = new FileOutputStream(mvnd_jar);) {
			IoUtils.copy(is, os);
		} catch (IOException e) {
			error("while trying to create " + mvnd_jar.getAbsolutePath(), e);
		}

		System.out.println("Created " + mvnd_jar.getAbsolutePath());
	}

	private void error(String msg) {
		error(msg, null);
	}

	private void error(String msg, Throwable e) {
		System.err.println(msg);
		if (e != null)
			e.printStackTrace(System.err);
		System.exit(1);
	}

	private void createBinM2dConf(File bin) {
		File m2d = new File(bin, "m2d.conf");
		Unix4j.fromFile(new File(bin, "m2.conf"))
				.sed("s/org.apache.maven.cli.MavenCli/com.mercateo.oss.mvnd.MVNDaemon/g").toFile(m2d);

		System.out.println("Created " + m2d.getAbsolutePath());
	}

	private void createBinMvnd(File bin) {
		File mvnd = new File(bin, "mvnd");
		Unix4j.fromFile(new File(bin, "mvn")).sed("s/m2.conf/m2d.conf/g").toFile(mvnd);
		mvnd.setExecutable(true);

		System.out.println("Created " + mvnd.getAbsolutePath());
	}

	void help(boolean full) {
		System.out.println("Usage: ");

		if (full) {
			System.out.println("");
			System.out.println(
					" java -jar " + MVNDaemon.class.getProtectionDomain().getCodeSource().getLocation().getFile()
							+ " install "+mvnDir.getAbsolutePath());
			System.out.println("    Patch a fresh maven installation to use MVND");
			System.out.println("");
			System.out.println("and after installing use");
		}
		System.out.println(" ");
		System.out.println(" "+mvnDir.getAbsolutePath()+"/bin/mvnd");
		System.out.println("    to start the daemon and");
		System.out.println(" ");
		System.out.println(" "+mvnDir.getAbsolutePath()+"/bin/mvnc");
		System.out.println("    as a mvn replacement, to run mavenCli from within the daemon");
		System.out.println("");

	}

	private static void assume(boolean eval, String msg) {
		if (!eval) {
			System.out.println(msg);
			System.exit(1);
		}
	}

}
