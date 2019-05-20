/*******************************************************************************
 * Copyright (c) 2014-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs;

import java.io.*;

/**
 * Wrapper structure that is used to create a common interface with all files.
 * A wrapper class should be used to wrap an archive or directory type in order for the
 * file system to be able to read it without needing to know what kind of file it is looking
 * in.  
 * @author Matthew Tropiano
 */
public abstract class FSFileArchive
{
	/** A string that contains the name of this file.. */
	private String parentArchiveName;
	/** A string that contains the path of this file. */
	private String parentArchivePath;
	
	/**
	 * Returns an FSFile reference of a file within this wrapped file.
	 * @param path		the abstract path of a file inside this file.
	 * @return			a new reference to the file requested, or null if the path
	 * 					refers to a location that is not inside this file.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract FSFile getFile(String path) throws IOException;
	
	/**
	 * Retrieves all of the files within this object as FSFile objects.
	 * @return an array of FSFiles, each entry representing a particular file in this object.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract FSFile[] getAllFiles() throws IOException;

	/**
	 * Retrieves all of the files within a directory as FSFile objects.
	 * @param path		the abstract path of a directory inside this file.
	 * @return 		an array of FSFiles, each entry representing a particular file in this object.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract FSFile[] getAllFilesInDir(String path) throws IOException;

	/**
	 * Retrieves all of the files within this object as FSFile objects that pass the filter test.
	 * @param path		the abstract path of a directory inside this file.
	 * @param filter	the file filter to use.
	 * @return 		an array of FSFiles, each entry representing a particular file in this object.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract FSFile[] getAllFilesInDir(String path, FSFileFilter filter) throws IOException;

	/**
	 * Retrieves all of the files within this object that pass the filter test as FSFile objects.
	 * @param filter	the file filter to use.
	 * @return			an array of FSFiles, each entry representing a particular file in this object.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract FSFile[] getAllFiles(FSFileFilter filter) throws IOException;

	/**
	 * Can this archive create a new file within itself?
	 * @return	true if it can, false if it is read-only.
	 */
	public abstract boolean canCreateFiles();
	
	/**
	 * Creates a file in this archive using the name and path provided.
	 * @param path the path of the file to create.
	 * @return	an acceptable OutputStream for filling the file with data.
	 * @throws	UnsupportedOperationException if this is called on an archive that does not support writing.
	 * @throws IOException if a read error occurs during the fetch.
	 */
	public abstract OutputStream createFile(String path) throws IOException;

	/** 
	 * Sets the name of this archive.
	 * @param name the new archive name. 
	 */
	protected void setArchiveName(String name)
	{
		parentArchiveName = name;
	}
	
	/** 
	 * Sets the path of this archive. 
	 * @param path the new archive name. 
	 */
	protected void setPath(String path)
	{
		parentArchivePath = path;
	}

	/** 
	 * Gets the name of this archive. 
	 * @return the archive name. 
	 */
	public String getArchiveName()
	{
		return parentArchiveName;
	}
	
	/** 
	 * Gets the path of this archive.
	 * @return the path. 
	 */
	public String getPath()
	{
		return parentArchivePath;
	}
	
}
