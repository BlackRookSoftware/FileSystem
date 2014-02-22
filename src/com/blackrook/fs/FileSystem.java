/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs;

import java.io.IOException;
import java.io.OutputStream;

import com.blackrook.commons.linkedlist.Stack;
import com.blackrook.commons.list.List;
import com.blackrook.commons.list.SortedList;

/**
 * This is a virtual file system layer for applications and 
 * other projects that resolve resources from virtual and nonvirtual 
 * file systems and archives.
 * @author Matthew Tropiano
 */
public class FileSystem
{
	/** Lookup stack for file system. */
	protected Stack<FSFileArchive> fileStack;
	
	/**
	 * Creates and initializes a new FileSystem.
	 */
	public FileSystem()
	{
		fileStack = new Stack<FSFileArchive>();
	}
	
	/**
	 * Adds an archive to the bottom of the search stack.
	 * @param fsfa	the archive to add.
	 */
	public void addArchive(FSFileArchive fsfa)
	{
		fileStack.add(fsfa);
	}
	
	/**
	 * Removes an archive from the search stack.
	 * @param fsfa	the archive to remove.
	 */
	public boolean removeArchive(FSFileArchive fsfa)
	{
		return fileStack.remove(fsfa);
	}
	
	/**
	 * Pushes an archive onto the search stack.
	 * @param fsfa	the archive to push.
	 */
	public void pushArchive(FSFileArchive fsfa)
	{
		fileStack.push(fsfa);
	}
	
	/**
	 * Pops an archive off of the search stack.
	 * @return	what used to be the topmost archive on the stack.
	 */
	public FSFileArchive popArchive()
	{
		return fileStack.pop();
	}
	
	/**
	 * Retrieves a file from the system. Searches down the stack.
	 * @param path	the file path.
	 * @return		A reference to the file as an FSFile object, null if not found.
	 */
	public FSFile getFile(String path) throws IOException
	{
		for (FSFileArchive a : fileStack)
		{
			FSFile f = a.getFile(path);
			if (f != null)
				return f;
		}
		
		return null;
	}

	/**
	 * Retrieves all of the instances of a file from the system. Searches down the stack.
	 * @param path	the file path.
	 * @return	A reference to the files as an FSFile array object. 
	 * 			An empty array implies that no files were found.
	 */
	public FSFile[] getAllFileInstances(String path) throws IOException
	{
		List<FSFile> v = new List<FSFile>(10);
		for (FSFileArchive a : fileStack)
		{
			FSFile f = a.getFile(path);
			if (f != null)
				v.add(f);
		}
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}

	/**
	 * Retrieves all of the recent instances of a file from the system. Searches down the stack.
	 * @return		A reference to the files as an FSFile array object.
	 */
	public FSFile[] getAllFiles() throws IOException
	{
		SortedList<FSFile> v = new SortedList<FSFile>(50);
		for (FSFileArchive a : fileStack)
			for (FSFile f : a.getAllFiles())
				if (!v.contains(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}
	
	/**
	 * Retrieves all of the recent instances of the files within this system that pass the filter test as FSFile objects.
	 * @param filter	the file filter to use.
	 * @return		A reference to the files as an FSFile array object.
	 */
	public FSFile[] getAllFiles(FSFileFilter filter) throws IOException
	{
		SortedList<FSFile> v = new SortedList<FSFile>(50);
		for (FSFileArchive a : fileStack)
			for (FSFile f : a.getAllFiles(filter))
				if (!v.contains(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}
	
	/**
	 * Retrieves all of the recent instances of the files within this system that pass the filter test as FSFile objects.
	 * @param path	the file path. Must be a directory.
	 * @return		A reference to the files as an FSFile array object.
	 */
	public FSFile[] getAllFilesInDir(String path) throws IOException
	{
		SortedList<FSFile> v = new SortedList<FSFile>(50);
		for (FSFileArchive a : fileStack)
			for (FSFile f : a.getAllFilesInDir(path))
				if (!v.contains(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}
	
	/**
	 * Retrieves all of the recent instances of the files within this system that pass the filter test as FSFile objects.
	 * @param path		the file path. Must be a directory.
	 * @param filter	the file filter to use.
	 * @return			A reference to the files as an FSFile array object.
	 */
	public FSFile[] getAllFilesInDir(String path, FSFileFilter filter) throws IOException
	{
		SortedList<FSFile> v = new SortedList<FSFile>(50);
		for (FSFileArchive a : fileStack)
			for (FSFile f : a.getAllFilesInDir(path,filter))
				if (!v.contains(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}
	
	/**
	 * Creates a file in this system using the name and path provided.
	 * @return	an acceptable OutputStream for filling the file with data, or null if no stream can be made.
	 */
	public OutputStream createFile(String path) throws IOException
	{
		for (FSFileArchive a : fileStack)
			if (a.canCreateFiles())
				return a.createFile(path);
		return null;
	}

}
