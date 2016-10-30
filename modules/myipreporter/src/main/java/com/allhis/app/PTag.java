package com.allhis.app;

public class PTag {
	public static final String OK = "+OK ";
	public static final String ERR = "-ERR ";
	public static final String USER = "USER ";
	public static final String PASS = "PASS ";
	public static final String STAT = "STAT";
	public static final String LIST = "LIST";
	public static final String RETR = "RETR ";
	public static final String DELE = "DELE ";
	public static final String NOOP = "NOOP";
	public static final String RSET = "RSET";
	public static final String QUIT = "QUIT";
	public static final String UIDL = "UIDL";
	public static final String TOP = "TOP";
	public static final String ONLINE = "ONLINE";
	public static final String DENY = "DENY";
	public static final String CAPA = "CAPA";
	public static final String ATTACK = "-ATTACK";
	
	public static final byte[] LINE_SEPARATOR = { '\r', '\n'};
	public static final String NEWLINE = "\r\n";

	public static final int TERMINATOR_C = '.';  
	public static final String TERMINATOR = ".";
	public static final byte[] TERMINATOR_BYTE = { '.' };
}
