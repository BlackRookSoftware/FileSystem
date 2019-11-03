/*******************************************************************************
 * Copyright (c) 2014-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs.archive;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.blackrook.fs.FSFile;
import com.blackrook.fs.FSFileArchive;
import com.blackrook.fs.FSFileFilter;
import com.blackrook.fs.FileSystemException;

/**
 * This is an FS wrapping class for wrapping a zip file on the native file system. 
 * @author Matthew Tropiano
 */
public class ZipArchive extends FSFileArchive
{
	/** Table for file lookup. Maps file path to File object. */
	protected Map<String, ZipEntry> fileLookupTable;
	
	/** Zip file reference. */
	protected ZipFile zipfileRef;
	
	/**
	 * Constructs a new zip archive from an abstract path. 
	 * @param path	a path to a zip file on the native file system.
	 * @throws FileSystemException if the path refers to a location that is not a folder.
	 * @throws ZipException if something is wrong with the Zip file.
	 * @throws IOException if something is wrong with the file.
	 * @throws NullPointerException if path is null.
	 */
	public ZipArchive(String path) throws ZipException, IOException
	{
		this(new File(path));
	}
	
	/**
	 * Constructs a new zip archive from an abstract file. 
	 * @param path	a path to a zip file on the native file system.
	 * @throws FileSystemException if the path refers to a location that is not a folder.
	 * @throws ZipException if something is wrong with the Zip file.
	 * @throws IOException if something is wrong with the file.
	 * @throws NullPointerException if path is null.
	 */
	public ZipArchive(File path) throws ZipException, IOException
	{
		this(new ZipFile(path));
	}
	
	/**
	 * Constructs a new zip archive from a file object.
	 * @param f	a file that is a zip file on the native file system.
	 * @throws FileSystemException if the file refers to a location that is not a folder.
	 * @throws ZipException if something is wrong with the Zip file.
	 * @throws IOException if something is wrong with the file.
	 * @throws NullPointerException if f is null.
	 */
	public ZipArchive(ZipFile f) throws ZipException, IOException
	{
		this.zipfileRef = f;
		
		String fname = f.getName();
		setArchiveName(fname.indexOf(File.separator) >= 0 ? fname.substring(fname.indexOf(File.separator)+1) : fname);
		setPath(fname);
				
		this.fileLookupTable = new HashMap<String,ZipEntry>(10);

		Enumeration<? extends ZipEntry> en = zipfileRef.entries(); 
		while (en.hasMoreElements())
		{
			ZipEntry entry = en.nextElement();
			String path = entry.getName();
			if (!entry.isDirectory())
				this.fileLookupTable.put(path,entry);
		}
	}
	
	@Override
	public FSFile getFile(String path)
	{
		ZipEntry f = fileLookupTable.get(path);
		if (f == null)
			return null;
		return new ZipEntryFile(f);
	}

	public FSFile[] getAllFiles()
	{
		Iterator<ZipEntry> it = fileLookupTable.values().iterator();
		List<ZipEntryFile> vect = new ArrayList<ZipEntryFile>(fileLookupTable.size());
		while (it.hasNext())
			vect.add(new ZipEntryFile(it.next()));
		
		ZipEntryFile[] out = new ZipEntryFile[vect.size()];
		vect.toArray(out);
		return out;
	}

	@Override
	public FSFile[] getAllFiles(FSFileFilter filter)
	{
		Iterator<ZipEntry> it = fileLookupTable.values().iterator();
		List<ZipEntryFile> vect = new ArrayList<ZipEntryFile>(fileLookupTable.size());
		while (it.hasNext())
		{
			ZipEntryFile f = new ZipEntryFile(it.next());
			if (filter.accept(f))
				vect.add(f);
		}
		
		ZipEntryFile[] out = new ZipEntryFile[vect.size()];
		vect.toArray(out);
		return out;
	}

	@Override
	public FSFile[] getAllFilesInDir(String path)
	{
		return getAllFiles(new StartingPathFilter(path));
	}

	@Override
	public FSFile[] getAllFilesInDir(String path, FSFileFilter filter)
	{
		FSFile[] ff = getAllFilesInDir(path);
		List<FSFile> v = new ArrayList<FSFile>(ff.length);
		
		for (FSFile f : ff)
			if (filter.accept(f)) v.add(f);
		
		FSFile[] out = new FSFile[v.size()];
		v.toArray(out);
		
		return out;
	}

	@Override
	public boolean canCreateFiles()
	{
		return false;
	}

	@Override
	public OutputStream createFile(String path) throws IOException
	{
		throw new UnsupportedOperationException("You can't create files in this zip archive.");
	}
	
	private class ZipEntryFile extends FSFile
	{
		protected ZipEntry zent;
		
		ZipEntryFile(ZipEntry ze)
		{
			zent = ze;
		}

		public InputStream getInputStream() throws IOException
		{
			return new BufferedInputStream(zipfileRef.getInputStream(zent));
		}

		public long getDate()
		{
			return zent.getTime();
		}

		public String getName()
		{
			String fname = getPath();
			return fname.indexOf("/") >= 0 ? fname.substring(fname.indexOf("/")+1) : fname;
		}

		public String getPath()
		{
			return zent.getName();
		}

		public long length()
		{
			return zent.getSize();
		}

		public int compareTo(FSFile fsf)
		{
			return getName().compareTo(fsf.getName());
		}
	}
	
	private static class StartingPathFilter implements FSFileFilter
	{
		String path;
		
		StartingPathFilter(String path)
		{
			this.path = path;
		}
		
		public boolean accept(FSFile pathname)
		{
			return pathname.getName().startsWith(path);
		}
	}

}
