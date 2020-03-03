/*******************************************************************************
 * Copyright (c) 2014-2020 Black Rook Software
 * This program and the accompanying materials are made available under the 
 * terms of the GNU Lesser Public License v2.1 which accompanies this 
 * distribution, and is available at 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs.archive;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.blackrook.fs.FSFile;
import com.blackrook.fs.FSFileArchive;
import com.blackrook.fs.FSFileFilter;
import com.blackrook.fs.FileSystemException;
import com.blackrook.fs.struct.Utils;

/**
 * This is an FS wrapping class for wrapping a folder on the native file system.
 * NOTE: This will not map hidden files/directories into its lookup table. 
 * @author Matthew Tropiano
 */
public class FolderArchive extends FSFileArchive
{
	/** Table for file lookup. Maps file path to File object. */
	protected Map<String, File> fileLookupTable;
	
	/**
	 * Constructs a new folder archive from an abstract path. 
	 * @param path	a path to a folder on the native file system.
	 * @throws FileSystemException if the path refers to a location that is not a folder.
	 * @throws NullPointerException if path is null.
	 */
	public FolderArchive(String path)
	{
		this(new File(path));
	}
	
	/**
	 * Constructs a new folder archive from a file object.
	 * @param f	a file that is a folder on the native file system.
	 * @throws FileSystemException if the file refers to a location that is not a folder.
	 * @throws NullPointerException if f is null.
	 */
	public FolderArchive(File f)
	{
		if (!f.isDirectory())
			throw new FileSystemException("File argument does not refer to a folder.");

		setArchiveName(f.getName());
		setPath(f.getPath());
		
		this.fileLookupTable = new HashMap<String, File>(10);
		
		String path = getPath().replaceAll("\\\\", "/");
		Queue<File[]> fileQueue = new LinkedList<File[]>();
		fileQueue.add(f.listFiles());
		while (!fileQueue.isEmpty())
		{
			File[] files = fileQueue.poll();
			for (File file : files)
			{
				if (file.isHidden())
					continue;
				String fpath = file.getPath().replaceAll("\\\\", "/");
				if (file.isDirectory())
					fileQueue.add(file.listFiles());
				else
					fileLookupTable.put(fpath.replace(path+"/", ""),file);
			}
		}
	}
	
	@Override
	public FSFile getFile(String path)
	{
		File f = fileLookupTable.get(path);
		if (f == null)
			return null;
		return new FolderFile(f);
	}

	@Override
	public FSFile[] getAllFiles()
	{
		Iterator<File> it = fileLookupTable.values().iterator();
		List<FolderFile> vect = new ArrayList<FolderFile>(fileLookupTable.size());
		while (it.hasNext())
			vect.add(new FolderFile(it.next()));
		
		FolderFile[] out = new FolderFile[vect.size()];
		vect.toArray(out);
		return out;
	}

	@Override
	public FSFile[] getAllFiles(FSFileFilter filter)
	{
		Iterator<File> it = fileLookupTable.values().iterator();
		List<FolderFile> vect = new ArrayList<FolderFile>(fileLookupTable.size());
		while (it.hasNext())
		{
			FolderFile f = new FolderFile(it.next());
			if (filter.accept(f))
				vect.add(f);
		}
		
		FolderFile[] out = new FolderFile[vect.size()];
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
		return true;
	}

	@Override
	public OutputStream createFile(String path) throws IOException
	{
		String dest = getPath()+'/'+path;
		if (!Utils.createPathForFile(dest))
			return null;
		File d = new File(dest);
		OutputStream osout = new FileOutputStream(d);
		fileLookupTable.put(path.replaceAll("\\\\", "/"),d);
		return osout;
	}

	protected class FolderFile extends FSFile
	{
		protected File fileRef;
		
		public FolderFile(File f)
		{
			fileRef = f;
		}

		public InputStream getInputStream() throws IOException
		{
			return new BufferedInputStream(new FileInputStream(fileRef));
		}

		public long getDate()
		{
			return fileRef.lastModified();
		}

		public String getName()
		{
			return fileRef.getName();
		}

		public String getPath()
		{
			return fileRef.getPath().substring(getArchiveName().length()+1);
		}

		public long length()
		{
			return fileRef.length();
		}

		public int compareTo(FSFile fsf)
		{
			return getPath().compareTo(fsf.getPath());
		}
		
		public String toString()
		{
			return getPath();
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
			String s = pathname.getPath();
			return s.startsWith(path);
		}
	}

}
