poi-visio
=========

This implementation of a visio parser is based on the POI java library. It can
*only* handle the XML version of visio files (.vsdx), and not older XML or 
proprietary formats (.vsd, .vsx).

Currently, this parser is read only, and cannot write new visio documents or
modify existing documents.

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


License
=======

License is currently Raytheon BBN Proprietary, TBD
