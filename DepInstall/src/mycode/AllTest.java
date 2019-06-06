package mycode;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Vector;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.*;

public class AllTest {

	String PATH = "/home/madras/teaching/17comp225/ass/data/";
	// change to your own path
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	/*
	 * Sample PASS level tests
	 */
	
	@Test
	public void testP1_5() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_P1";
		Integer N = 5;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommands(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	@Test
	public void testP1_9() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_P1";
		Integer N = 9;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommands(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	@Test
	public void testP1_all() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_P1";
		Integer N = d.MAXCOMS;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommands(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	/*
	 * Sample CREDIT level tests 
	 */
	
	@Test
	public void testC1_all() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_C1";
		Integer N = d.MAXCOMS;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheck(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	/*
	 * Sample (HIGH) DISTINCTION level tests 
	 */
	
	@Test
	public void testD1_all() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_D1";
		Integer N = d.MAXCOMS;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheckRecLarge(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	@Test
	public void testD1a_8() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_D1a";
		Integer N = 8;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheckRecLarge(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	@Test
	public void testD2_8() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_D2";
		Integer N = 8;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheckRecSmall(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}

	@Test
	public void testD2a_8() {
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String soln = "";
		String datfile = "sample_D2a";
		Integer N = 8;

		try {
			inCommands = d.readCommandsFromFile(PATH+datfile+".in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheckRecSmall(inCommands, N);
		final String ans = outContent.toString();
		
		try {
			soln = d.readSoln(PATH+datfile+".out", N);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		assertEquals(soln, ans);
		
	}


}
