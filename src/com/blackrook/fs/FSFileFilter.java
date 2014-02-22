/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.fs;

/**
 * File filter interface for FileSystem stuff.
 * @author Matthew Tropiano
 */
public interface FSFileFilter
{
	/** Tests if a file is accepted by this filter. */
	public boolean accept(FSFile f);
}
