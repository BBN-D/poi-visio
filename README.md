poi-visio
=========

poi-visio is a Java library that loads Visio OOXML (vsdx) files and creates
an in memory data structure that allows full access to the contents of the
document. There is built-in support for easily traversing the content of the
document in a structured way, and can render pages to simplified PNG files,
or other backends supported by Java AWT. It is built using the Apache POI
library which operates on other Microsoft OOXML documents (docx, pptx, xlsx).
Currently the library only operates in read-only mode, but its design does
not exclude being able to modify existing documents or creating new documents.

Currently, this library cannot handle older XML or proprietary formats (.vsd, .vsx).

Building
========

Currently, this library requires a simple patch to the POI library, so you need
to build our patched version of POI to use it. You can use the following steps
to build and install our patched version of POI:

    git clone https://github.com/BBN-D/poi.git
    cd poi
    git checkout bbn-fork
    ant mvn-install

Once this is done, you can build this project just like any other maven project:

    mvn install

Dealing with visio files
========================

The visio XML format (with an XSD 1.0 schema) is documented for SharePoint at:

* https://msdn.microsoft.com/en-us/library/hh645006(v=office.12).aspx

However, there's additional documentation and an updated XSD 1.1 schema at:

* https://msdn.microsoft.com/en-us/library/office/jj684209(v=office.15).aspx

Each provides different details, but the SharePoint reference has better
documentation and is more useful.

There is a visual basic project to explore visio files, that can be found at
http://pkgvisio.codeplex.com/


Developer mode
--------------

Examining documents with Visio is very helpful in developer mode.

Legal
=====

This code has been approved by DARPA's Public Release Center for
public release, with the following statement:

* Approved for Public Release, Distribution Unlimited

Copyright & License
-------------------

Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
