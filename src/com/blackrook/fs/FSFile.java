/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.blackrook.commons.Common;


/**
 * This describes metadata of a file within a FileSystem.
 * @author Matthew Tropiano
 */
public abstract class FSFile implements Comparable<FSFile>
{
	/**
	 * Returns an InputStream of a file within this wrapped file so that it may be read via a stream.
	 * @return			an InputStream that can read the file, or null if the path
	 * 					refers to a location that is not inside this file.
	 * @throws IOException if acquiring the stream throws an exception.
	 */
	public abstract InputStream getInputStream() throws IOException;

	/**
	 * Returns an BufferedReader of this file.
	 * @return			an InputStream that can read the file wrapped in a BufferedReader,
	 * 					or null if the path refers to a location that is not inside this file.
	 * @throws IOException if acquiring the stream throws an exception.
	 */
	public BufferedReader getBufferedReader() throws IOException
	{
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	/**
	 * Returns the ASCII contents of this file.
	 * @return		a contiguous string (including newline characters) of the file's contents.
	 * @throws IOException if acquiring the stream throws an exception.
	 */
	public String getASCIIContents() throws IOException
	{
		String out = "";
		BufferedReader br = getBufferedReader();
		String line;
		while ((line = br.readLine()) != null)
			out += line + "\n";
		br.close();
		return out;
	}

	/**
	 * Retrieves the binary contents of a file.
	 * @return an array of the bytes that make up the file.
	 * @throws FileNotFoundException	if the file cannot be found.
	 * @throws IOException				if the read cannot be done.
	 */
	public byte[] getBinaryContents() throws IOException
	{
		InputStream in = getInputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Common.relay(in, bos);
		Common.close(in);
		return bos.toByteArray();
	}

	@Override
	public String toString()
	{
		return getPath();
	}
	
	/**
	 * @return the name of this file.
	 */
	public abstract String getName();
	
	/**
	 * @return the path of this file.
	 */
	public abstract String getPath();

	/**
	 * @return the length of this file.
	 */
	public abstract long length();

	/**
	 * @return the length of this file.
	 */
	public abstract long getDate();
	
}
