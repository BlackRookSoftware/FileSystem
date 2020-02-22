# Black Rook FileSystem

Copyright (c) 2014-2019 Black Rook Software. All rights reserved.  
[https://github.com/BlackRookSoftware/FileSystem](https://github.com/BlackRookSoftware/FileSystem)

[Latest Release](https://github.com/BlackRookSoftware/FileSystem/releases/latest)
[Online Javadoc](https://blackrooksoftware.github.io/FileSystem/javadoc/)  


### NOTICE

This library is currently in **EXPERIMENTAL** status. This library's API
may change many times in different ways over the course of its development!

### Required Libraries

NONE

### Required Java Modules

[java.base](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/module-summary.html)  

### Introduction

This library simulates a Quake-like filesystem for loading resources via a 
virtual file stack.

### Library

Contained in this release is a set of classes for the creation of a virtual file system.

### Compiling with Ant

To compile this library with Apache Ant, type:

	ant compile

To make Maven-compatible JARs of this library (placed in the *build/jar* directory), type:

	ant jar

To make Javadocs (placed in the *build/docs* directory):

	ant javadoc

To compile main and test code and run tests (if any):

	ant test

To make Zip archives of everything (main src/resources, bin, javadocs, placed in the *build/zip* directory):

	ant zip

To compile, JAR, test, and Zip up everything:

	ant release

To clean up everything:

	ant clean
	
### Javadocs

Online Javadocs can be found at: [https://blackrooksoftware.github.io/FileSystem/javadoc/](https://blackrooksoftware.github.io/FileSystem/javadoc/)

### Other

This program and the accompanying materials
are made available under the terms of the GNU Lesser Public License v2.1
which accompanies this distribution, and is available at
http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html

A copy of the LGPL should have been included in this release (LICENSE.txt).
If it was not, please contact us for a copy, or to notify us of a distribution
that has not included it. 

This contains code copied from Black Rook Base, under the terms of the MIT License (docs/LICENSE-BlackRookBase.txt).
