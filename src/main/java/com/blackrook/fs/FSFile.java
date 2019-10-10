/*******************************************************************************
 * Copyright (c) 2014-2019 Black Rook Software
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

import com.blackrook.fs.struct.Utils;

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
		Utils.relay(in, bos);
		Utils.close(in);
		return bos.toByteArray();
	}

	@Override
	public String toString()
	{
		return getPath();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof FSFile)
			return equals((FSFile)obj);
		return super.equals(obj);
	}
	
	/**
	 * Checks if this file equals another file.
	 * Compares using path.
	 * @param file the other file.
	 * @return true if equal, false if not.
	 */
	public boolean equals(FSFile file)
	{
		return compareTo(file) == 0;
	}
	
	@Override
	public int compareTo(FSFile file)
	{
		return getPath().compareTo(file.toString());
	}
	
	/**
	 * @return the name of this file in the file system.
	 */
	public abstract String getName();
	
	/**
	 * @return the path of this file from the root of the system.
	 */
	public abstract String getPath();

	/**
	 * @return the length of this file in bytes.
	 */
	public abstract long length();

	/**
	 * @return the date that this file was created/modified.
	 */
	public abstract long getDate();
	
}
