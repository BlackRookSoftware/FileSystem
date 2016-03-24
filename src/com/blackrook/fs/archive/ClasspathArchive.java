/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs.archive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;

import com.blackrook.commons.list.List;
import com.blackrook.fs.FSFile;
import com.blackrook.fs.FSFileArchive;
import com.blackrook.fs.FSFileFilter;


/**
 * This is an FS wrapping class for wrapping a classpath tree in the filesystem.
 * There only needs to be only one of these in the FS stack, as this refers to
 * the Java Environment classpath inclusion.
 * @author Matthew Tropiano
 */
public class ClasspathArchive extends FSFileArchive
{
	/** System ClassLoader reference for convenience. */
	private ClassLoader classLoader;
	
	/**
	 * Creates the entry point into the classpath.
	 */
	public ClasspathArchive()
	{
		classLoader = ClassLoader.getSystemClassLoader();
	}
	
	@Override
	public boolean canCreateFiles()
	{
		return false;
	}

	@Override
	public OutputStream createFile(String path) throws IOException
	{
		return null;
	}

	@Override
	/**
	 * This does not return any files.
	 */
	public FSFile[] getAllFiles() throws IOException
	{
		return new FSFile[0];
	}

	@Override
	/**
	 * This does not return any files.
	 */
	public FSFile[] getAllFiles(FSFileFilter filter) throws IOException
	{
		return new FSFile[0];
	}

	@Override
	/**
	 * This does not return any files.
	 */
	public FSFile[] getAllFilesInDir(String path) throws IOException
	{
		List<FSFile> flist = new List<FSFile>(25);
		
		Enumeration<URL> allFiles = classLoader.getResources(path);
		while (allFiles.hasMoreElements())
			flist.add(new ClasspathFile(allFiles.nextElement()));

		FSFile[] files = new FSFile[flist.size()];
		flist.toArray(files);
		return files;
	}

	@Override
	public FSFile[] getAllFilesInDir(String path, FSFileFilter filter) throws IOException
	{
		FSFile[] ff = getAllFilesInDir(path);
		List<FSFile> v = new List<FSFile>(ff.length);
		
		for (FSFile f : ff)
			if (filter.accept(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		return out;
	}

	@Override
	public FSFile getFile(String path)
	{
		URL furl = classLoader.getResource(path);
		if (furl == null)
			return null;
		return new ClasspathFile(furl);
	}
	
	
	protected class ClasspathFile extends FSFile
	{
		protected URL fileURL;
		
		public ClasspathFile(URL file)
		{
			fileURL = file;
		}

		public InputStream getInputStream() throws IOException
		{
			return fileURL.openStream();
		}

		public long getDate()
		{
			return 0;
		}

		public String getName()
		{
			String fname = fileURL.toString();
			return fname.indexOf("/") >= 0 ? fname.substring(fname.indexOf("/")+1) : fname;
		}

		public String getPath()
		{
			return fileURL.getPath();
		}

		public long length()
		{
			return -1;
		}

		public int compareTo(FSFile fsf)
		{
			return getName().compareTo(fsf.getName());
		}
		
	}
	
}
