package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.ConnectType;

public class XDGFConnection {

	// comments on frompart/topart taken from pkgVisio 
	
	// https://msdn.microsoft.com/en-us/library/ms367611(v=office.12).aspx
	
    //    The following constants declared by the Microsoft Office Visio type 
    //       library show return values for the FromPart property.
    // Constant                  Value  
    // visConnectFromError         -1
    // visFromNone                 0
    // visLeftEdge                 1
    // visCenterEdge               2
    // visRightEdge                3
    // visBottomEdge               4
    // visMiddleEdge               5
    // visTopEdge                  6
    // visBeginX                   7
    // visBeginY                   8
    // visBegin                    9
    // visEndX                     10
    // visEndY                     11
    // visEnd                      12
    // visFromAngle                13
    // visFromPin                  14
    // visControlPoint             100 + zero-based row index (for example, visControlPoint = 100 if the control point is in row 0; visControlPoint = 101 if the control point is in row 1)
	
	public static final int visConnectFromError        = -1;
	public static final int visFromNone                = 0;
	public static final int visLeftEdge                = 1;
	public static final int visCenterEdge              = 2;
	public static final int visRightEdge               = 3;
	public static final int visBottomEdge              = 4;
	public static final int visMiddleEdge              = 5;
	public static final int visTopEdge                 = 6;
	public static final int visBeginX                  = 7;
	public static final int visBeginY                  = 8;
	public static final int visBegin                   = 9;
	public static final int visEndX                    = 10;
	public static final int visEndY                    = 11;
	public static final int visEnd                     = 12;
	public static final int visFromAngle               = 13;
	public static final int visFromPin                 = 14;
	
	
	//  The ToPart property identifies the part of a shape to which another 
	//    shape is glued, such as its begin point or endpoint, one of its edges, 
	//    or a connection point. The following constants declared by the Visio type library in member VisToParts show possible return values for the ToPart property.
	// Constant               Value  
	// visConnectToError       -1
	// visToNone               0
	// visGuideX               1
	// visGuideY               2
	// visWholeShape           3
	// visGuideIntersect       4
	// visToAngle              7
	// visConnectionPoint      100 + row index of connection point
	
	public static final int visConnectToError          = -1;
	public static final int visToNone                  = 0;
	public static final int visGuideX                  = 1;
	public static final int visGuideY                  = 2;
	public static final int visWholeShape              = 3;
	public static final int visGuideIntersect          = 4;
	public static final int visToAngle                 = 7;
	
	private ConnectType _connect;
	private XDGFShape _from;
	private XDGFShape _to;
	
	
	public XDGFConnection(ConnectType connect, XDGFShape from, XDGFShape to) {
		_connect = connect;
		_from = from;
		_to = to;
	}
	
	public XDGFShape getFromShape() {
		return _from;
	}
	
	public XDGFCell getFromCell() {
		return _from.getCell(_connect.getFromCell());
	}
	
	public String getFromCellName() {
		return _connect.getFromCell();
	}
	
	public XDGFShape getToShape() {
		return _to;
	}
	
	public String getToCellName() {
		return _connect.getToCell();
	}
	
	// see constants above
	public Integer getFromPart() {
		if (_connect.isSetFromPart())
			return _connect.getFromPart();
		else
			return null;
	}
	
	// see constants above
	public Integer getToPart() {
		if (_connect.isSetToPart())
			return _connect.getToPart();
		else
			return null;
	}
}
