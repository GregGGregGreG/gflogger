package org.gflogger.config.xml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.gflogger.GFLog;
import org.gflogger.GFLogFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlConfiguration {
	private final StringBuffer buffer = new StringBuffer();

	@Before
	public void setUp(){
		buffer.setLength(0);
		System.setProperty("TZ", "GMT");
		StreamAppenderFactory.outputStream = buffer;
	}

	@Test
	public void testIS() throws Throwable {
		final InputStream is = XmlConfiguration.class.getResourceAsStream("/gflogger1.xml");
		assertNotNull(is);
		XmlLogFactoryConfigurator.configure(is);

		test();
	}
	@Test
	public void testAbsolutePath() throws Throwable {
		final URL resource = XmlConfiguration.class.getResource("/gflogger1.xml");
		final String fileName = resource.getFile();

		final File file = new File(fileName);
		assertTrue(file.exists());
		assertTrue(file.isAbsolute());

		XmlLogFactoryConfigurator.configure(fileName);

		test();
	}

	@Test
	public void testClasspath() throws Throwable {
		XmlLogFactoryConfigurator.configure("classpath:/gflogger1.xml");

		test();
	}

	@Test
	public void testFile() throws Throwable {
		XmlLogFactoryConfigurator.configure("file:/gflogger1.xml");

		test();
	}

	private void test() {
		final GFLog log = GFLogFactory.getLog(XmlConfiguration.class);
		log.error("error");

		GFLogFactory.stop();

		assertTrue(buffer.toString().matches("\\w+ \\d+ \\d{2}:\\d{2}:\\d{2},\\d{3} GMT ERROR - error \\[xml.XmlConfiguration\\] \\[main\\]\n"));
	}

}