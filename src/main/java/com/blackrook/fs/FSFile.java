/*******************************************************************************
 * Copyright (c) 2014-2020 Black Rook Software
 * This program and the accompanying materials are made available under the 
 * terms of the GNU Lesser Public License v2.1 which accompanies this 
 * distribution, and is available at 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs;

import java.io.IOException;
import java.io.InputStream;

/**
 * This describes metadata of a file within a FileSystem.
 * @author Matthew Tropiano
 */
public abstract class FSFile implements Comparable<FSFile>
{
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

	/**
	 * Returns an InputStream of a file within this wrapped file so that it may be read via a stream.
	 * @return			an InputStream that can read the file, or null if the path
	 * 					refers to a location that is not inside this file.
	 * @throws IOException if acquiring the stream throws an exception.
	 */
	public abstract InputStream getInputStream() throws IOException;
	
}
