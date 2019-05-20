/*******************************************************************************
 * Copyright (c) 2014-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs.exception;

/**
 * Excption class that is thrown if something bad happens inside the file system.
 * @author Matthew Tropiano
 */
public class FileSystemException extends RuntimeException
{
	private static final long serialVersionUID = 4441345821023367717L;

	public FileSystemException()
	{
		super("A file system exception has occurred.");
	}

	public FileSystemException(String message)
	{
		super(message);
	}
}
